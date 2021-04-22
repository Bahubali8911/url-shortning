package com.daimler.urlshortner.service;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

@Service
public class UrlShortnerService {
	
	private static final String SHORT_URL_PREFIX = "http://bitly.xz/";

	public String convertToShortUrl(String longUrl) throws IllegalArgumentException {

		UrlValidator validator = new UrlValidator(new String[] { "http", "https" });
		
		if (!validator.isValid(longUrl)) {
			throw new IllegalArgumentException("Invalid URL!!!");
		}

		return SHORT_URL_PREFIX + Hashing.murmur3_32().hashString(longUrl, Charsets.UTF_8).toString().substring(0, 6);

	}
}
