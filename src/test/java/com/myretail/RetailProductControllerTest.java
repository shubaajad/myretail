package com.myretail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.myretail.controller.RetailProductController;
import com.myretail.dao.ProductDao;
import com.myretail.dao.ProductData;
import com.myretail.service.ProductDAOMapper;
import com.myretail.service.ProductService;
import com.myretail.service.exception.ProductNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class RetailProductControllerTest {
	
	@InjectMocks
	private RetailProductController productController = new RetailProductController();
    
    @Mock
    private ProductService mockProductService;
    
    @Mock
    private ProductDAOMapper mockProductMapper; 

	@Test
	public void getProduct_Success() throws ProductNotFoundException {		
		when(mockProductService.getProduct(anyInt()))
	    .thenReturn(any(ProductData.class));
		ResponseEntity<ProductData> productEntity = productController.getProductDetails(123);
		assertEquals(HttpStatus.OK, productEntity.getStatusCode());
	}
	
	@Test(expected=ProductNotFoundException.class)
	public void getProduct_NotFound() throws ProductNotFoundException {		
		when(mockProductService.getProduct(anyInt()))
	    .thenThrow(new ProductNotFoundException());
		productController.getProductDetails(15117729);
	}

	@Test
	public void testUpdateProductPrice() throws ProductNotFoundException {	
		when(mockProductMapper.convertToDao(any(ProductData.class))).thenReturn(any(ProductDao.class));
		productController.updateProductPrice(new ProductData(), 15117729);
		verify(mockProductMapper).convertToDao(any(ProductData.class));
	}

}