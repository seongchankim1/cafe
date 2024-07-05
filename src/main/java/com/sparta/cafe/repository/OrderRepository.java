package com.sparta.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cafe.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findAllByOrderByCreatedAt();

	List<Order> findAllByUserIdOrderByOrderIdDesc(Long id);
}
