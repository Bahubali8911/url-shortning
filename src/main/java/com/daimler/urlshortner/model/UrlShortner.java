package com.daimler.urlshortner.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "urlShortner")
public class UrlShortner {

	@ApiModelProperty(required = false, hidden = true)
	private long id;
	
	private String longUrl;
	private String shortUrl;
	
	@ApiModelProperty(required = false, hidden = true)
	private LocalDateTime creationDate;
	
	public UrlShortner() {
		
	}
	
	public UrlShortner(String longUrl, String shortUrl, LocalDateTime creationDate) {
		this.longUrl = longUrl;
		this.shortUrl = shortUrl;
		this.creationDate = creationDate;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	
	@JsonIgnore
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "long_url", nullable = false, length = 2048)
	public String getLongUrl() {
		return longUrl;
	}
	public void setLongUrl(String firstName) {
		this.longUrl = firstName;
	}
	
	@Column(name = "short_Url", nullable = false)
	public String getShortUrl() {
		return shortUrl;
	}
	public void setShortUrl(String lastName) {
		this.shortUrl = lastName;
	}
	
	@Column(name = "creation_date", nullable = false)
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	@JsonIgnore
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "UrlShortner [id=" + id + ", longUrl=" + longUrl + ", shortUrl=" + shortUrl + ", creationDate=" + creationDate.toString()
				+ "]";
	}
	
}
