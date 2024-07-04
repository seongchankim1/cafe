package com.sparta.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cafe.dto.CompleteOrderResponseDto;
import com.sparta.cafe.entity.CompleteOrder;

public interface CompleteOrderRepository extends JpaRepository<CompleteOrder, Long> {

	List<CompleteOrder> findAllByOrderByCompleteIdDesc();
}
