package com.shamim.ecommerce.service.impl;

import com.shamim.ecommerce.model.Cart;
import com.shamim.ecommerce.model.CartItem;
import com.shamim.ecommerce.model.Product;
import com.shamim.ecommerce.model.User;
import com.shamim.ecommerce.repository.CartItemRepository;
import com.shamim.ecommerce.repository.CartRepository;
import com.shamim.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);
        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart,product,size);
        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(cart.getId());
            cartItem.setSize(size);
            int totalPrice = quantity * product.getSellingPrice();
            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);
            cartItem.setMrpPrice(quantity * product.getMrpPrice());
            return  cartItemRepository.save(cartItem);
        }

        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        int totalPrice = 0;
        double totalDiscountedPrice = 0;
        int totalItem = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountedPrice += cartItem.getProduct().getSellingPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, (int) totalDiscountedPrice));
        cart.setTotalItem(totalItem);

        return cart;
    }

    private int calculateDiscountPercentage(Integer mrpPrice, Integer sellingPrice) {
        if (mrpPrice <= 0 || sellingPrice <= 0) {
            mrpPrice = 1;
        }
        double discount = (mrpPrice - sellingPrice);
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }
}
