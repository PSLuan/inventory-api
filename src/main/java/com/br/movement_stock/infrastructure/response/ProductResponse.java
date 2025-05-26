package com.br.movement_stock.infrastructure.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String description;
    private String productType;
    private BigDecimal valueSupplier;
    private Integer quantityStock;
    private Integer totalQuantityOut;
    private BigDecimal totalProfit;
}
