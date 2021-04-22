package com.daimler.urlshortner.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.daimler.urlshortner.exception.ResourceNotFoundException;
import com.daimler.urlshortner.model.UrlShortner;
import com.daimler.urlshortner.repository.UrlShortnerRepository;
import com.daimler.urlshortner.service.UrlShortnerService;

@RestController
@RequestMapping("/api/v1")
public class UrlShortnerController {
	@Autowired
	private UrlShortnerRepository urlShortnerRepository;

	private final UrlShortnerService urlShortnerService;
	private AtomicInteger count = new AtomicInteger();

	public UrlShortnerController(UrlShortnerService urlShortnerService) {
		this.urlShortnerService = urlShortnerService;
	}

	@PostMapping("/urlShortner")
	public ResponseEntity<Object> createShortUrl(@RequestBody final String longUrl) {
		try {
			UrlShortner urlShortner = new UrlShortner();
			urlShortner.setLongUrl(longUrl);
			urlShortner.setShortUrl(urlShortnerService.convertToShortUrl(longUrl));
			urlShortner.setCreationDate(LocalDateTime.now());
			urlShortnerRepository.save(urlShortner);

			HashMap<String, String> map = new HashMap<>();
			map.put("statusMessage", "URL Shortned Sucessfully!!!");
			map.put("shortUrl", urlShortner.getShortUrl());

			return ResponseEntity.ok().body(map);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body("Error with DB while shortening URL !!!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Unknown Error !!!");
		}
	}

	@GetMapping("/redirect")
	public RedirectView redirect(@RequestParam(value = "shortUrl") String shortUrl) throws ResourceNotFoundException {
		UrlShortner urlShortner = urlShortnerRepository.findByShortUrl(shortUrl)
				.orElseThrow(() -> new ResourceNotFoundException("No long URL found for :: " + shortUrl));
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(urlShortner.getLongUrl());
		count.incrementAndGet();
		return redirectView;
	}

	@PostMapping("/customUrlShortner")
	public ResponseEntity<Object> createCustomShortUrl(@RequestBody UrlShortner urlShortner) {
		try {
			if (StringUtils.isEmpty(urlShortner.getLongUrl()) || StringUtils.isEmpty(urlShortner.getShortUrl())) {
				return ResponseEntity.badRequest().body("Invalid Input!!!");
			}
			urlShortner.setCreationDate(LocalDateTime.now());
			urlShortnerRepository.save(urlShortner);

			return new ResponseEntity<>("Custom URL Shortner saved successfully!!!", HttpStatus.OK);
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body("Error with DB while shortening URL !!!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Unknown Error !!!");
		}
	}

	@GetMapping("/shortApiCallCounter")
	public HashMap<String, Integer> shortUrlCallCounter() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("numberOfCalls", count.get());
		return map;
	}
}
