package com.br.movement_stock.application.service.impl;

import com.br.movement_stock.application.service.MovementStockService;
import com.br.movement_stock.domain.enums.MovementType;
import com.br.movement_stock.domain.model.MovementStock;
import com.br.movement_stock.domain.model.Product;
import com.br.movement_stock.domain.repository.MovementStockRepository;
import com.br.movement_stock.domain.repository.ProductRepository;
import com.br.movement_stock.infrastructure.request.MovementStockRequest;
import com.br.movement_stock.infrastructure.response.MovementStockResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MovementStockServiceImpl implements MovementStockService {

    private final MovementStockRepository movementStockRepository;
    private final ProductRepository productRepository;

    public List<MovementStockResponse> findAll() {
        List<MovementStock> movements = movementStockRepository.findAll();
        return movements.stream()
                .map(movement -> MovementStockResponse.builder()
                        .id(movement.getId())
                        .productId(movement.getProduct().getId())
                        .productDescription(movement.getProduct().getDescription())
                        .movementType(movement.getMovementType().getMovement())
                        .value(movement.getValue())
                        .date(movement.getDate())
                        .quantity(movement.getQuantity())
                        .build()).toList();
    }

    @Override
    @Transactional
    public MovementStockResponse registerMovement(MovementStockRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        validateStockForMovement(product, request);

        updateProductStock(product, request);

        MovementStock movement = MovementStock.builder()
                .product(product)
                .movementType(MovementType.valueOf(request.getMovementType()))
                .quantity(request.getQuantity())
                .value(request.getValue())
                .date(LocalDateTime.now())
                .build();

        MovementStock saved = movementStockRepository.save(movement);

        return toResponse(saved);
    }

    private void validateStockForMovement(Product product, MovementStockRequest request) {
        if (request.getMovementType().equals(MovementType.OUTBOUND.toString()) && product.getQuantityStock() < request.getQuantity()) {
            throw new IllegalStateException("Quantidade insuficiente!");
        }
    }

    private void updateProductStock(Product product, MovementStockRequest request) {
        int updatedStock = request.getMovementType().equals(MovementType.INBOUND.toString())
                ? product.getQuantityStock() + request.getQuantity()
                : product.getQuantityStock() - request.getQuantity();

        product.setQuantityStock(updatedStock);
        productRepository.save(product);
    }

    private MovementStockResponse toResponse(MovementStock movement) {
        return MovementStockResponse.builder()
                .id(movement.getId())
                .productId(movement.getProduct().getId())
                .productDescription(movement.getProduct().getDescription())
                .movementType(movement.getMovementType().getMovement())
                .quantity(movement.getQuantity())
                .value(movement.getValue())
                .date(movement.getDate())
                .build();
    }
}
