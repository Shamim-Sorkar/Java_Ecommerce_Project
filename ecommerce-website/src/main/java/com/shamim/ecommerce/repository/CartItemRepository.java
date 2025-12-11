package com.shamim.ecommerce.repository;

import com.shamim.ecommerce.model.Cart;
import com.shamim.ecommerce.model.CartItem;
import com.shamim.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
