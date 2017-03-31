package com.myretail.test.apptest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.myretail.dao.PriceData;
import com.myretail.dao.ProductData;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class RetailIntegrationtest {

		@LocalServerPort
		private int port;

		private TestRestTemplate template = new TestRestTemplate();

		@Test
		public void testGetProduct() {
			ResponseEntity<ProductData> productEntity = this.template.getForEntity("http://localhost:"
					+ port +"/myretail/products/{productId}", ProductData.class, 13860428);
			assertEquals("The Big Lebowski (Blu-ray)", productEntity.getBody().getTitle());
			assertEquals(HttpStatus.OK, productEntity.getStatusCode());
		}
		
		@Test
		public void testGetProductNotFound() {
			ResponseEntity<ProductData> productEntity = this.template.getForEntity("http://localhost:"
					+ port +"/myretail/products/{productId}", ProductData.class, 138604281);
			assertEquals(HttpStatus.NOT_FOUND, productEntity.getStatusCode());
		}
		
		@Test
		public void testGetProductForbidden() {
			ResponseEntity<ProductData> productEntity = this.template.getForEntity("http://localhost:"
					+ port +"/myretail/products/{productId}", ProductData.class, 16483589);
			assertEquals(HttpStatus.FORBIDDEN, productEntity.getStatusCode());
		}

		@Test
		public void testUpdateProductPrice_ProductNotFound() {
			ProductData product = new ProductData();
			PriceData PriceData = new PriceData();
			PriceData.setPrice("99.99");
			product.setId(1234);
			product.setPriceData(PriceData);
			
			
		    HttpEntity<ProductData> requestEntity = new HttpEntity<ProductData>(product);
		    ResponseEntity<String> responseEntity = 
		    		this.template.exchange("http://localhost:"
		    				+ port +"/myretail/products/{productId}", HttpMethod.PUT, requestEntity, String.class, 1234);
		    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		}
		
		@Test
		public void testUpdateProductPrice() {
			ProductData product = new ProductData();
			PriceData PriceData = new PriceData();
			PriceData.setPrice("10.99");
			product.setId(13860428);
			product.setPriceData(PriceData);
			
			
		    HttpEntity<ProductData> requestEntity = new HttpEntity<ProductData>(product);
		    ResponseEntity<String> responseEntity = 
		    		this.template.exchange("http://localhost:"
		    				+ port +"/myretail/products/{productId}", HttpMethod.PUT, requestEntity, String.class, 13860428);
		    assertEquals(HttpStatus.RESET_CONTENT, responseEntity.getStatusCode());
		}
		
		@Test
		public void testUpdateProductPrice_BadRequest() {
			ProductData product = new ProductData();
			PriceData PriceData = new PriceData();
			PriceData.setPrice("0.99");
			product.setId(0);
			product.setPriceData(PriceData);
			
			
		    HttpEntity<ProductData> requestEntity = new HttpEntity<ProductData>(product);
		    ResponseEntity<String> responseEntity = 
		    		this.template.exchange("http://localhost:"
		    				+ port +"/myretail/products/{productId}", HttpMethod.PUT, requestEntity, String.class, 0);
		    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		}

}
