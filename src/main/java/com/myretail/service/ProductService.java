/**
 * 
 */
package com.myretail.service;

import com.myretail.dao.ProductDao;
import com.myretail.dao.ProductData;

/**
 * @author sdevana
 *
 */
public interface ProductService {

	
	ProductData getProduct(int id) ;

	ProductData updateProductPrice(ProductDao product) ;


}
