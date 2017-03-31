/**
 * 
 */
package com.myretail.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author sdevana
 *
 */
@Repository("ProductRepository")
public interface ProductRepository extends MongoRepository<ProductDao, String> {
	
	public ProductDao findByPid(@Param("id") int productId);
	
	@SuppressWarnings("unchecked")
	public ProductDao save(ProductDao productDao);

}