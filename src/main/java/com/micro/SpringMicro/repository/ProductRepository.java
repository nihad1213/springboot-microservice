package com.micro.SpringMicro.repository;

import com.micro.SpringMicro.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
