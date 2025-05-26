package com.br.movement_stock.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.br.movement_stock.application.service.impl.ProductServiceImpl;
import com.br.movement_stock.domain.enums.MovementType;
import com.br.movement_stock.domain.enums.ProductType;
import com.br.movement_stock.domain.model.MovementStock;
import com.br.movement_stock.domain.model.Product;
import com.br.movement_stock.domain.repository.ProductRepository;
import com.br.movement_stock.infrastructure.request.ProductRequest;
import com.br.movement_stock.infrastructure.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setup() {
        product = Product.builder()
                .id(1L)
                .description("Produto Teste")
                .productType(ProductType.ELECTRONIC)
                .quantityStock(15)
                .valueSupplier(BigDecimal.valueOf(10))
                .movementStocks(List.of(
                        MovementStock.builder()
                                .movementType(MovementType.OUTBOUND)
                                .quantity(5)
                                .value(BigDecimal.valueOf(100))
                                .date(LocalDateTime.now())
                                .build(),
                        MovementStock.builder()
                                .movementType(MovementType.INBOUND)
                                .quantity(10)
                                .value(BigDecimal.valueOf(200))
                                .date(LocalDateTime.now())
                                .build()
                ))
                .build();
    }

    @Test
    void findAll_ShouldReturnList() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponse> responses = productService.findAll();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(product.getId(), responses.get(0).getId());
        assertEquals(product.getDescription(), responses.get(0).getDescription());
        assertEquals(product.getQuantityStock(), responses.get(0).getQuantityStock());
        assertEquals(product.getProductType().getType(), responses.get(0).getProductType());
    }

    @Test
    void findById_ShouldReturnProductResponseWithTotals() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.findById(1L);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getQuantityStock(), response.getQuantityStock());
        assertEquals(product.getProductType().getType(), response.getProductType());

        assertEquals(5, response.getTotalQuantityOut());

        assertTrue(response.getTotalProfit().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void save_ShouldPersistAndReturnResponse() {
        ProductRequest request = ProductRequest.builder()
                .description("Novo Produto")
                .productType(product.getProductType().getType())
                .quantityStock(20)
                .valueSupplier(BigDecimal.valueOf(30))
                .build();

        Product savedProduct = Product.builder()
                .id(2L)
                .description(request.getDescription())
                .productType(product.getProductType())
                .quantityStock(request.getQuantityStock())
                .valueSupplier(request.getValueSupplier())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponse response = productService.save(request);

        assertNotNull(response);
        assertEquals(savedProduct.getId(), response.getId());
        assertEquals(savedProduct.getDescription(), response.getDescription());
    }

    @Test
    void update_ShouldModifyAndReturnUpdatedResponse() {
        ProductRequest request = ProductRequest.builder()
                .description("Produto Atualizado")
                .productType(product.getProductType().getType())
                .quantityStock(25)
                .valueSupplier(BigDecimal.valueOf(60))
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response = productService.update(1L, request);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(request.getQuantityStock(), response.getQuantityStock());
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void findById_NotFound_ShouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.findById(99L);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
    }

    @Test
    void findById_ShouldReturnProductResponseWithTotals_WithZeroQuantityMovement() {
        MovementStock zeroQuantityMovement = MovementStock.builder()
                .movementType(MovementType.OUTBOUND)
                .quantity(0)
                .value(BigDecimal.ZERO)
                .date(LocalDateTime.now())
                .build();

        product.setMovementStocks(List.of(
                MovementStock.builder()
                        .movementType(MovementType.OUTBOUND)
                        .quantity(5)
                        .value(BigDecimal.valueOf(100))
                        .date(LocalDateTime.now())
                        .build(),
                zeroQuantityMovement,
                MovementStock.builder()
                        .movementType(MovementType.INBOUND)
                        .quantity(10)
                        .value(BigDecimal.valueOf(200))
                        .date(LocalDateTime.now())
                        .build()
        ));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.findById(1L);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getQuantityStock(), response.getQuantityStock());
        assertEquals(product.getProductType().getType(), response.getProductType());

        assertEquals(5, response.getTotalQuantityOut());

        assertTrue(response.getTotalProfit().compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void findEntityById_NotFound_ShouldThrowException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.findById(999L);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
    }

}
