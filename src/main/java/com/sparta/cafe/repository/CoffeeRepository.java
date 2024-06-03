package com.sparta.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cafe.entity.Coffee;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
	Coffee findByCoffeeName(String coffeeName);
}
