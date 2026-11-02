package com.micro.product.repository;

import com.micro.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String keyword);

    @Modifying(clearAutomatically = true)
    @Query("update Product p set p.quantity = p.quantity - :qty where p.id = :id and p.quantity >= :qty")
    int decrementStock(@Param("id") Long id, @Param("qty") int qty);

    @Modifying(clearAutomatically = true)
    @Query("update Product p set p.quantity = p.quantity + :qty where p.id = :id")
    int incrementStock(@Param("id") Long id, @Param("qty") int qty);
}