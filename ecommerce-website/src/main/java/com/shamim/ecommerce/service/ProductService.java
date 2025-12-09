package com.shamim.ecommerce.service;

import com.shamim.ecommerce.dto.request.CreateProductRequest;
import com.shamim.ecommerce.exceptions.ProductException;
import com.shamim.ecommerce.model.Product;
import com.shamim.ecommerce.model.Seller;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Product createProduct(CreateProductRequest request, Seller seller);
    void deleteProduct(Long productId);
    Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> searchProduct(String query);
    Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );

    List<Product> getProductsBySellerId(Long sellerId);
}
