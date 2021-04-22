package com.daimler.urlshortner;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.daimler.urlshortner.model.UrlShortner;
import com.daimler.urlshortner.repository.UrlShortnerRepository;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlShortnerControllerIntegrationTest {

	@MockBean
	private UrlShortnerRepository urlShortnerRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		RestAssuredMockMvc.mockMvc(mockMvc);
	}

	@Test
	public void testCreateShortUrl_emptyLongUrl() {

		RestAssuredMockMvc.given()
			.contentType("application/json")
			.body("{}").when()
			.post("/api/v1/urlShortner")
			.then()
			.statusCode(400)
			.body(Matchers.equalTo("Invalid URL!!!"));
	}

	@Test
	public void testCreateShortUrl_invalidLongUrl() {

		RestAssuredMockMvc.given()
			.contentType("application/json")
			.body("htp://sdsd.cros/asd")
			.when()
			.post("/api/v1/urlShortner")
			.then()
			.statusCode(400)
			.body(Matchers.equalTo("Invalid URL!!!"));
	}

	@Test
	public void testCreateShortUrl_httpStatusOk() {

		Mockito.when(urlShortnerRepository.save(Mockito.any(UrlShortner.class)))
				.thenReturn((new UrlShortner("http://www.testtest/test", "http://xyz.by/teSt1", LocalDateTime.now())));

		RestAssuredMockMvc.given()
			.contentType("application/json")
			.body("http://www.asdasda.com/asd")
			.when()
			.post("/api/v1/urlShortner")
			.then()
			.statusCode(200)
			.body("statusMessage", Matchers.equalTo("URL Shortned Sucessfully!!!"));
	}
	
	@Test
	public void testCreateCustomShortUrl_emptyLongUrl() {
		
		UrlShortner shortner = new  UrlShortner("", "http://test.com/sss", LocalDateTime.now());

		RestAssuredMockMvc.given()
			.contentType("application/json")
			.body(shortner)
			.when()
			.post("/api/v1/customUrlShortner")
			.then()
			.statusCode(400)
			.body(Matchers.equalTo("Invalid Input!!!"));
	}	

	
	@Test
	public void testCreateCustomShortUrl_emptyShortUrl() {
		
		UrlShortner shortner = new  UrlShortner("http://test.com/sss", "", LocalDateTime.now());

		RestAssuredMockMvc.given()
		.contentType("application/json")
		.body(shortner)
		.when()
		.post("/api/v1/customUrlShortner")
		.then()
		.statusCode(400)
		.body(Matchers.equalTo("Invalid Input!!!"));
	}	

	
	@Test
	public void testCreateCustomShortUrl_HttpStatusOk() {
		
		UrlShortner shortner = new  UrlShortner("http://test.com/sss", "http://www.ded/ded", LocalDateTime.now());

		RestAssuredMockMvc.given()
		.contentType("application/json")
		.body(shortner)
		.when()
		.post("/api/v1/customUrlShortner")
		.then()
		.statusCode(200)
		.body(Matchers.equalTo("Custom URL Shortner saved successfully!!!"));
	}
	
	
}
