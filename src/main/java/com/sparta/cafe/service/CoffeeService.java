package com.sparta.cafe.service;

import com.sparta.cafe.dto.CoffeeResponseDto;
import com.sparta.cafe.dto.OrderResponseDto;
import com.sparta.cafe.entity.Coffee;
import com.sparta.cafe.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoffeeService {

	private CoffeeRepository coffeeRepository;

	public CoffeeService(CoffeeRepository coffeeRepository) {
		this.coffeeRepository = coffeeRepository;
	}

	public List<CoffeeResponseDto> getAllCoffees() {
		return coffeeRepository.findAll().stream().map(CoffeeResponseDto::new).collect(Collectors.toList());
	}
}
