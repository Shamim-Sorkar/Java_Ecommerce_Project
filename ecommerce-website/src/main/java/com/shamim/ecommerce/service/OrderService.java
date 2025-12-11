package com.shamim.ecommerce.service;

import com.shamim.ecommerce.constant.OrderStatus;
import com.shamim.ecommerce.model.Address;
import com.shamim.ecommerce.model.Cart;
import com.shamim.ecommerce.model.Order;
import com.shamim.ecommerce.model.User;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    Order findOrderById(Long id);
    List<Order> userOrdersHistory(Long userId);
    List<Order> sellerOrders(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus);
    Order cancelOrder(Long orderId, User user);
}
