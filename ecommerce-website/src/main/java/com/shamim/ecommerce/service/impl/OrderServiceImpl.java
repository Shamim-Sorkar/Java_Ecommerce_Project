package com.shamim.ecommerce.service.impl;

import com.shamim.ecommerce.constant.OrderStatus;
import com.shamim.ecommerce.constant.PaymentStatus;
import com.shamim.ecommerce.model.*;
import com.shamim.ecommerce.repository.AddressRepository;
import com.shamim.ecommerce.repository.OrderRepository;
import com.shamim.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if (!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(shippingAddress);
        }
        Address address = addressRepository.save(shippingAddress);

        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct()
                        .getSeller().getId()));
        Set<Order> orders = new HashSet<>();
        for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            int totalOrderPrice = items.stream().mapToInt(item -> item.getSellPrice()).sum();
            int totalItem = items.stream().mapToInt(item -> item.getQuantity()).sum();
            Order createOrder = new Order();
            createOrder.setUser(user);
            createOrder.setSellerId(sellerId);
            createOrder.setTotalMrpPrice(totalOrderPrice);
            createOrder.setTotalSellingPrice(totalOrderPrice);
            createOrder.setTotalItem(totalItem);
            createOrder.setShippingAddress(address);
            createOrder.setOrderStatus(OrderStatus.PENDING);
            createOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order savedOrder= orderRepository.save(createOrder);
            orders.add(savedOrder);
        }

        return Set.of();
    }

    @Override
    public Order findOrderById(Long id) {
        return null;
    }

    @Override
    public List<Order> userOrdersHistory(Long userId) {
        return List.of();
    }

    @Override
    public List<Order> sellerOrders(Long sellerId) {
        return List.of();
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        return null;
    }

    @Override
    public Order cancelOrder(Long orderId, User user) {
        return null;
    }
}
