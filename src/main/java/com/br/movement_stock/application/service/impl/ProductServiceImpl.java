package com.br.movement_stock.application.service.impl;

import com.br.movement_stock.application.service.ProductService;
import com.br.movement_stock.domain.enums.MovementType;
import com.br.movement_stock.domain.enums.ProductType;
import com.br.movement_stock.domain.model.MovementStock;
import com.br.movement_stock.domain.model.Product;
import com.br.movement_stock.domain.repository.ProductRepository;
import com.br.movement_stock.infrastructure.request.ProductRequest;
import com.br.movement_stock.infrastructure.response.ProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .description(product.getDescription())
                        .quantityStock(product.getQuantityStock())
                        .productType(product.getProductType().getType())
                        .build()).toList();
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado."));
        int totalOut = product.getMovementStocks().stream()
                .filter(m -> m.getMovementType() == MovementType.OUTBOUND)
                .mapToInt(MovementStock::getQuantity)
                .sum();

        BigDecimal totalProfit = product.getMovementStocks().stream()
                .filter(m -> m.getMovementType() == MovementType.OUTBOUND)
                .map(m -> {
                    BigDecimal unitSalePrice = BigDecimal.ZERO;
                    if (m.getQuantity() > 0) {
                        unitSalePrice = m.getValue().divide(BigDecimal.valueOf(m.getQuantity()), RoundingMode.HALF_UP);
                    }

                    BigDecimal unitProfit = unitSalePrice.subtract(product.getValueSupplier());
                    return unitProfit.multiply(BigDecimal.valueOf(m.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ProductResponse.builder()
                .id(product.getId())
                .description(product.getDescription())
                .quantityStock(product.getQuantityStock())
                .productType(product.getProductType().getType())
                .valueSupplier(product.getValueSupplier())
                .totalQuantityOut(totalOut)
                .totalProfit(totalProfit)
                .build();
    }

    @Override
    public ProductResponse save(ProductRequest request) {
        Product product = Product.builder()
                .description(request.getDescription())
                .productType(ProductType.fromType(request.getProductType()))
                .valueSupplier(request.getValueSupplier())
                .quantityStock(request.getQuantityStock())
                .build();
        return toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findEntityById(id);
        product.setDescription(request.getDescription());
        product.setProductType(ProductType.valueOf(request.getProductType()));
        product.setValueSupplier(request.getValueSupplier());
        product.setQuantityStock(request.getQuantityStock());
        return toResponse(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        Product product = findEntityById(id);
        productRepository.delete(product);
    }

    private Product findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .description(product.getDescription())
                .productType(product.getProductType().getType())
                .quantityStock(product.getQuantityStock())
                .build();
    }
}
