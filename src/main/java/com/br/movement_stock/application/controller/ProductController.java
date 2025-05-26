package com.br.movement_stock.application.controller;

import com.br.movement_stock.application.service.ProductService;
import com.br.movement_stock.infrastructure.request.ProductRequest;
import com.br.movement_stock.infrastructure.response.ProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> save(
            @RequestBody ProductRequest productRequest
    ) {
        return ResponseEntity.ok(productService.save(productRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @RequestBody ProductRequest productRequest
    ) {
        return ResponseEntity.ok(productService.update(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}