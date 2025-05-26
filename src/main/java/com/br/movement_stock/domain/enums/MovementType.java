package com.br.movement_stock.domain.enums;

import lombok.Getter;

@Getter
public enum MovementType {
    INBOUND("Entrada"),
    OUTBOUND("Saída");

    private final String movement;

    MovementType(String movement) {this.movement = movement;}
}
