package com.shamim.ecommerce.service;

import com.shamim.ecommerce.model.Cart;
import com.shamim.ecommerce.model.CartItem;
import com.shamim.ecommerce.model.Product;
import com.shamim.ecommerce.model.User;

public interface CartService {

    public CartItem addCartItem(User user, Product product, String size, int quantity);
    public Cart findUserCart(User user);
}
