package com.br.movement_stock.domain.repository;

import com.br.movement_stock.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
