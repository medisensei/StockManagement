package com.StockManagement.repository;

import com.StockManagement.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Sale,Long> {
}
