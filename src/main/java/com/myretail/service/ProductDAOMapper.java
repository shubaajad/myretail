package com.myretail.service;

import org.springframework.stereotype.Component;

import com.myretail.dao.PriceData;
import com.myretail.dao.ProductDao;
import com.myretail.dao.ProductData;

@Component
public class ProductDAOMapper {
	
	public ProductData convertToBean(ProductDao product){
		
		ProductData productData = null;
		if(product!=null){
			productData = new ProductData();
			
			productData.setPid(product.getPid());
			productData.setTitle(product.getTitle());
			
			PriceData priceData = new PriceData();
			priceData.setCurrencyCode(product.getCurrency_code());
			priceData.setPrice(String.valueOf(product.getCurrentPrice()));
			productData.setPriceData(priceData);
		}
		return productData;
	}
	
	public ProductDao convertToDao(ProductData productData){
		
		ProductDao productDao = new ProductDao(productData.getPid());
		productDao.setCurrentPrice(Double.valueOf(productData.getPriceData().getPrice()));
		return productDao;
	}

}
