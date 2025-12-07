package com.shamim.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private com.shamim.ecommerce.model.Order order;

    @ManyToOne
    private com.shamim.ecommerce.model.Product product;

    private String size;

    private Integer quantity;

    private Integer mrpPrice;

    private Integer sellingPrice;

    private Long userId;
}
