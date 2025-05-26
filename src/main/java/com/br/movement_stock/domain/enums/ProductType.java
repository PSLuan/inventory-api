package com.br.movement_stock.domain.enums;

import lombok.Getter;

@Getter
public enum ProductType {
    ELECTRONIC("Eletrônicos"),
    HOME_APPLIANCE("Eletrodomésticos"),
    FURNITURE("Movéis");

    private final String type;

    ProductType(String type) {this.type = type;}

    public static ProductType fromType(String type) {
        for (ProductType typeOption : ProductType.values()) {
            if (typeOption.type.equals(type)) {
                return typeOption;
            }
        }
        throw new IllegalArgumentException("No enum constant with type " + type);
    }
}
