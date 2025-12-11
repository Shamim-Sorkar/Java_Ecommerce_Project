package com.shamim.ecommerce.controller;

import com.shamim.ecommerce.exceptions.ProductException;
import com.shamim.ecommerce.model.Product;
import com.shamim.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam (required = false) String query) {
        List<Product> products = productService.searchProduct(query);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam (required = false) String category,
            @RequestParam (required = false) String brand,
            @RequestParam (required = false) String color,
            @RequestParam (required = false) String size,
            @RequestParam (required = false) Integer minPrice,
            @RequestParam (required = false) Integer maxPrice,
            @RequestParam (required = false) Integer minDiscount,
            @RequestParam (required = false) String sort,
            @RequestParam (required = false) String stock,
            @RequestParam (defaultValue = "0") Integer pageNumber) {

        Page<Product> products = productService.getAllProducts(category, brand,  color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber);
        return ResponseEntity.ok().body(products);
    }

}
