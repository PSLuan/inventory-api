package com.br.movement_stock.application.service;

import com.br.movement_stock.infrastructure.request.ProductRequest;
import com.br.movement_stock.infrastructure.response.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> findAll();
    ProductResponse findById(Long id);
    ProductResponse save(ProductRequest request);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);
}
