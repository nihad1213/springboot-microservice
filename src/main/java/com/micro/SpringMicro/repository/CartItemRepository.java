package com.micro.SpringMicro.repository;

import com.micro.SpringMicro.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
