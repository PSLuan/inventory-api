package com.br.movement_stock.infrastructure.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank
    private String description;

    @NotNull
    private String productType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal valueSupplier;

    @NotNull
    @Min(0)
    private Integer quantityStock;
}
