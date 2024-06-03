package com.sparta.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cafe.entity.CompleteOrder;

public interface CompleteOrderRepository extends JpaRepository<CompleteOrder, Long> {

}
