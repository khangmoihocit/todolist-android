package com.khangmoihocit.shopapp.services;

import com.khangmoihocit.shopapp.dtos.ProductDTO;
import com.khangmoihocit.shopapp.dtos.ProductImageDTO;
import com.khangmoihocit.shopapp.models.Product;
import com.khangmoihocit.shopapp.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    Product getProductById(long id) throws Exception;
    Page<Product> getAllProducts(PageRequest pageRequest);
    Product updateProduct(long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(long id);
    boolean existsByName(String name);
    ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception;
}
