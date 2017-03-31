/**
 * 
 */
package com.myretail.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.dao.ProductDao;
import com.myretail.dao.ProductData;
import com.myretail.dao.ProductRepository;
import com.myretail.service.exception.ProductNotFoundException;

/**
 * @author sdevana
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

	public static final String PRODUCT_PATH = "v1/pdp/tcin/";
	
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Value("${myretail.product.hostname}")
	private String hostName;
	
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ProductDAOMapper prdDaoMapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public String getURIPath(int productId){
		return hostName+PRODUCT_PATH+productId;
	}

	public void setProductMapper(ProductDAOMapper prdDaoMapper) {
		this.prdDaoMapper = prdDaoMapper;
	}
	
	private boolean isValid(int productId) {
		if (productId==0 && String.valueOf(productId).length()>7) {
			return false;
		}
		return true;
	}

	
	@Override
	public ProductData getProduct(int pid) throws ProductNotFoundException {

		//check if pid is valid
		if(!isValid(pid)){
			throw new ProductNotFoundException("Invalid pid");
		}
		
		ProductDao product = productRepository.findByPid(pid);
		
		String productTitle = getProductTitle(pid);
		if (product == null) {
			product = new ProductDao(pid);
		}
		product.setTitle(productTitle);
		
		return prdDaoMapper.convertToBean(product);
	}

	
	private String getProductTitle(int pid) throws ProductNotFoundException {
		
		String responseBody = null;
		ResponseEntity<String> response = null;
		String productTitle = null;
		
		List<String> excludes = new ArrayList<>();
		excludes.add("taxonomy");
		excludes.add("price");
		excludes.add("promotion");
		excludes.add("bulk_ship");
		excludes.add("rating_and_review_reviews");
		excludes.add("rating_and_review_statistics");
		excludes.add("question_answer_statistics");
		
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(getURIPath(pid)).queryParam("excludes",excludes).build();

		try {
			response = restTemplate.getForEntity(uriComponents.encode().toUri(), String.class);
			responseBody = response.getBody();
			
			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode prdRootNode = mapper.readTree(responseBody);
			
			if(prdRootNode!=null)
				productTitle = prdRootNode.get("product").get("item").get("product_description").get("title").asText();
			
			logger.debug("Product title is "+productTitle);
			
		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new ProductNotFoundException(HttpStatus.NOT_FOUND,pid+" not found");
			} if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
				throw new ProductNotFoundException(HttpStatus.FORBIDDEN,"product service forbid  the "+pid);
			} 
			else {
				throw new RuntimeException("product id : " + pid, ex);
			}
		
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Problem parsing JSON String" + responseBody, e);
		
		} catch (IOException e) {
			throw new RuntimeException(
					"Unknown exception while trying to fetch from Database for " + pid, e);
		}
		
		if (!response.getStatusCode().equals(HttpStatus.OK))
			logger.error("Error ocurred while retrieving product title, status code: " + response.getStatusCode().value());
		
		return productTitle;
	}
	
		@Override
		public ProductData updateProductPrice(ProductDao product) throws ProductNotFoundException {
			
			int pid = product.getPid();
			
			//check if pid is valid
			if(!isValid(pid)){
				throw new ProductNotFoundException("Invalid pid");
			}
		
			//retrieve product from database
			ProductDao existingProduct = productRepository.findByPid(pid);
			
			if (existingProduct == null) {
				logger.info("Product -" + pid + " is not found in database");
				throw new ProductNotFoundException(String.valueOf(pid)+"is not found in database");
			}
			
			logger.info("Original Price for "+ pid+ "-" + existingProduct.getCurrentPrice() );
			
			existingProduct.setCurrentPrice(product.getCurrentPrice());		
			
			//updating the price in database
			ProductDao updatedProduct = productRepository.save(existingProduct);
			logger.info("Updated Price for "+ pid+ "-" + updatedProduct.getCurrentPrice() );
				
			return prdDaoMapper.convertToBean(product);
		}
}
