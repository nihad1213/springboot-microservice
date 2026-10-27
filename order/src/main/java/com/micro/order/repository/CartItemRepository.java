package com.micro.order.repository;

import com.micro.order.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
//    List<CartItem> findByUserId(Long userId);
//    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
}
