package com.shamim.ecommerce.controller;

import com.shamim.ecommerce.dto.request.AddItemRequest;
import com.shamim.ecommerce.dto.response.ApiResponse;
import com.shamim.ecommerce.model.Cart;
import com.shamim.ecommerce.model.CartItem;
import com.shamim.ecommerce.model.Product;
import com.shamim.ecommerce.model.User;
import com.shamim.ecommerce.repository.CartItemRepository;
import com.shamim.ecommerce.service.CartItemService;
import com.shamim.ecommerce.service.CartService;
import com.shamim.ecommerce.service.ProductService;
import com.shamim.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductService productService;
    private final CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(
            @RequestBody AddItemRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(req.getProductId());
        CartItem item = cartService.addCartItem(user, product, req.getSize(),  req.getQuantity());
        ApiResponse res = new ApiResponse("Item added to cart successfully");
        return new ResponseEntity<>(item, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse res = new ApiResponse("Item deleted from cart successfully");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        CartItem updatedCartItem = null;
        if (cartItem.getQuantity() > 0) {
            updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }
        return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);
    }
}
