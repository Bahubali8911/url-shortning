package com.daimler.urlshortner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daimler.urlshortner.model.UrlShortner;

@Repository
public interface UrlShortnerRepository extends JpaRepository<UrlShortner, Long>{
	
	Optional<UrlShortner> findByShortUrl(String shortUrl);

}
