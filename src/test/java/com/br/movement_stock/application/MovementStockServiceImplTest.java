package com.br.movement_stock.application;

import com.br.movement_stock.application.service.impl.MovementStockServiceImpl;
import com.br.movement_stock.domain.enums.MovementType;
import com.br.movement_stock.domain.model.MovementStock;
import com.br.movement_stock.domain.model.Product;
import com.br.movement_stock.domain.repository.MovementStockRepository;
import com.br.movement_stock.domain.repository.ProductRepository;
import com.br.movement_stock.infrastructure.request.MovementStockRequest;
import com.br.movement_stock.infrastructure.response.MovementStockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementStockServiceImplTest {

    @Mock
    private MovementStockRepository movementStockRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MovementStockServiceImpl movementStockService;

    private Product product;

    @BeforeEach
    void setup() {
        product = Product.builder()
                .id(1L)
                .description("Produto Teste")
                .quantityStock(10)
                .build();
    }

    @Test
    void findAll_ShouldReturnMovementStockResponseList() {
        MovementStock movement = MovementStock.builder()
                .id(1L)
                .product(product)
                .movementType(MovementType.INBOUND)
                .quantity(5)
                .value(BigDecimal.valueOf(100.0))
                .date(LocalDateTime.now())
                .build();

        when(movementStockRepository.findAll()).thenReturn(List.of(movement));

        List<MovementStockResponse> responses = movementStockService.findAll();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(movement.getId(), responses.get(0).getId());
        assertEquals(product.getId(), responses.get(0).getProductId());
        assertEquals("Produto Teste", responses.get(0).getProductDescription());
    }

    @Test
    void registerMovement_ShouldThrowException_WhenProductNotFound() {
        MovementStockRequest request = MovementStockRequest.builder()
                .productId(99L)
                .movementType(MovementType.INBOUND.toString())
                .quantity(5)
                .value(BigDecimal.valueOf(50.0))
                .build();

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> movementStockService.registerMovement(request));

        assertEquals("Produto não encontrado.", ex.getMessage());
    }

    @Test
    void registerMovement_ShouldThrowException_WhenInsufficientStock() {
        MovementStockRequest request = MovementStockRequest.builder()
                .productId(product.getId())
                .movementType(MovementType.OUTBOUND.toString())
                .quantity(20)
                .value(BigDecimal.valueOf(50.0))
                .build();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> movementStockService.registerMovement(request));

        assertEquals("Quantidade insuficiente!", ex.getMessage());
    }

    @Test
    void registerMovement_ShouldRegisterInboundMovement() {
        MovementStockRequest request = MovementStockRequest.builder()
                .productId(product.getId())
                .movementType(MovementType.INBOUND.toString())
                .quantity(5)
                .value(BigDecimal.valueOf(50.0))
                .build();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        MovementStock savedMovement = MovementStock.builder()
                .id(1L)
                .product(product)
                .movementType(MovementType.INBOUND)
                .quantity(5)
                .value(BigDecimal.valueOf(50.0))
                .date(LocalDateTime.now())
                .build();

        when(movementStockRepository.save(any(MovementStock.class))).thenReturn(savedMovement);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        MovementStockResponse response = movementStockService.registerMovement(request);

        assertNotNull(response);
        assertEquals(savedMovement.getId(), response.getId());
        assertEquals(product.getId(), response.getProductId());
        assertEquals(MovementType.INBOUND.getMovement(), response.getMovementType());
        assertEquals(15, product.getQuantityStock());
    }

    @Test
    void registerMovement_ShouldRegisterOutboundMovement() {
        MovementStockRequest request = MovementStockRequest.builder()
                .productId(product.getId())
                .movementType(MovementType.OUTBOUND.toString())
                .quantity(5)
                .value(BigDecimal.valueOf(50.0))
                .build();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        MovementStock savedMovement = MovementStock.builder()
                .id(1L)
                .product(product)
                .movementType(MovementType.OUTBOUND)
                .quantity(5)
                .value(BigDecimal.valueOf(50.0))
                .date(LocalDateTime.now())
                .build();

        when(movementStockRepository.save(any(MovementStock.class))).thenReturn(savedMovement);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        MovementStockResponse response = movementStockService.registerMovement(request);

        assertNotNull(response);
        assertEquals(savedMovement.getId(), response.getId());
        assertEquals(product.getId(), response.getProductId());
        assertEquals(MovementType.OUTBOUND.getMovement(), response.getMovementType());
        assertEquals(5, product.getQuantityStock());
    }
}
