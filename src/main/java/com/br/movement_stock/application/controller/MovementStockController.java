package com.br.movement_stock.application.controller;

import com.br.movement_stock.application.service.MovementStockService;
import com.br.movement_stock.infrastructure.request.MovementStockRequest;
import com.br.movement_stock.infrastructure.response.MovementStockResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/movements")
public class MovementStockController {
    private final MovementStockService movementStockService;

    @GetMapping
    public ResponseEntity<List<MovementStockResponse>> findAll() {
        return ResponseEntity.ok(movementStockService.findAll());
    }

    @PostMapping
    public ResponseEntity<MovementStockResponse> move(
            @RequestBody MovementStockRequest request
    ) {
        MovementStockResponse movement = movementStockService.registerMovement(request);
        return ResponseEntity.ok(movement);
    }
}