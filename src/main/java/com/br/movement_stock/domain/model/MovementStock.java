package com.br.movement_stock.domain.model;

import com.br.movement_stock.domain.enums.MovementType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "movement_stock")
public class MovementStock implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    private BigDecimal value;

    private LocalDateTime date;

    private Integer quantity;
}