/**
 * 
 */
package com.myretail.service;

import com.myretail.dao.ProductDao;
import com.myretail.dao.ProductData;
import com.myretail.service.exception.ProductNotFoundException;

/**
 * @author sdevana
 *
 */
public interface ProductService {

	
	ProductData getProduct(int id) throws ProductNotFoundException;

	ProductData updateProductPrice(ProductDao product) throws ProductNotFoundException;


}
