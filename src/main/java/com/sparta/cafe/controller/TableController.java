package com.sparta.cafe.controller;

import com.sparta.cafe.entity.Coffee;
import com.sparta.cafe.entity.CompleteOrder;
import com.sparta.cafe.entity.Order;
import com.sparta.cafe.entity.User;
import com.sparta.cafe.repository.CoffeeRepository;
import com.sparta.cafe.repository.CompleteOrderRepository;
import com.sparta.cafe.repository.OrderRepository;
import com.sparta.cafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {

	@Autowired
	private CoffeeRepository coffeeRepository;

	@Autowired
	private CompleteOrderRepository completeOrderRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/coffees")
	public List<Coffee> getAllCoffees() {
		return coffeeRepository.findAll();
	}

	@GetMapping("/complete-orders")
	public List<CompleteOrder> getAllCompleteOrders() {
		return completeOrderRepository.findAll();
	}

	@GetMapping("/orders")
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
}
