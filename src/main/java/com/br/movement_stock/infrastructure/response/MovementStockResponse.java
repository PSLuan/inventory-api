package com.br.movement_stock.infrastructure.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovementStockResponse {
    private Long id;
    private Long productId;
    private String productDescription;
    private String movementType;
    private BigDecimal value;
    private LocalDateTime date;
    private Integer quantity;
}
