package com.br.movement_stock.domain.repository;

import com.br.movement_stock.domain.model.MovementStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementStockRepository extends JpaRepository<MovementStock, Long> {
}
