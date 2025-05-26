package com.br.movement_stock.infrastructure.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovementStockRequest {
    @NotNull
    private Long productId;

    @NotNull
    private String movementType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal value;

    @NotNull
    @Min(1)
    private Integer quantity;
}
