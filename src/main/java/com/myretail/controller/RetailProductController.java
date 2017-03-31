/**
 * 
 */
package com.myretail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myretail.dao.ProductDao;
import com.myretail.dao.ProductData;
import com.myretail.service.ProductDAOMapper;
import com.myretail.service.ProductService;
import com.myretail.service.exception.ProductNotFoundException;

/**
 * @author sdevana
 *
 */
@RestController
@RequestMapping("/myretail")
public class RetailProductController {
	
		@Autowired
	    private ProductService productService;
		
		@Autowired
	    private ProductDAOMapper prdDaoMapper;
		
		/**
		 * Returns JSON representation of Product resource based on Id specified in URL
		 * @param productId
		 * @return
		 * @throws ProductNotFoundException 
		 */
		@RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
	    public ResponseEntity<ProductData> getProductDetails(@PathVariable int productId){					
	        ProductData product = productService.getProduct(productId);
	        return new ResponseEntity<>(product, HttpStatus.OK);
	    }
		
		/**
		 * Update existing Product Price from the Http Body Resource
		 * @param productRequestDTO
		 * @param productId
		 * @throws ProductNotFoundException 
		 */
		@RequestMapping(value = "/products/{productId}", method = RequestMethod.PUT)
	    public void updateProductPrice(@RequestBody ProductData productRequest,                                        
	                                   @PathVariable int productId) {
			ProductDao product = prdDaoMapper.convertToDao(productRequest);
			productService.updateProductPrice(product);	
	    }

}
