package com.br.movement_stock.application.service;

import com.br.movement_stock.infrastructure.request.MovementStockRequest;
import com.br.movement_stock.infrastructure.response.MovementStockResponse;

import java.util.List;

public interface MovementStockService {
    List<MovementStockResponse> findAll();
    MovementStockResponse registerMovement(MovementStockRequest request);
}