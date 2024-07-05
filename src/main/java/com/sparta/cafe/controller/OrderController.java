package com.sparta.cafe.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cafe.dto.CoffeeResponseDto;
import com.sparta.cafe.dto.CompleteOrderResponseDto;
import com.sparta.cafe.dto.OrderRequestDto;
import com.sparta.cafe.dto.OrderResponseDto;
import com.sparta.cafe.entity.CompleteOrder;
import com.sparta.cafe.security.UserDetailsImpl;
import com.sparta.cafe.service.CoffeeService;
import com.sparta.cafe.service.OrderService;

@RestController
@RequestMapping("/api/cafe")
public class OrderController {

	private final OrderService orderService;
	private final CoffeeService coffeeService;

	public OrderController(OrderService orderService, CoffeeService coffeeService) {
		this.orderService = orderService;
		this.coffeeService = coffeeService;
	}

	@PostMapping
	public OrderResponseDto newOrder(@RequestBody OrderRequestDto orderRequestDto) {
		return orderService.addOrder(orderRequestDto);
	}

	@GetMapping("/orders")
	public List<OrderResponseDto> getAllOrders() {
		return orderService.getAllOrders();
	}

	@DeleteMapping("/orders/{id}")
	public int deleteOrder(@PathVariable Long id) {
	return orderService.deleteOrder(id);
	}

	@GetMapping("/completed-orders")
	public List<CompleteOrder> getCompleteOrders() {
		return orderService.getCompleteOrders();
	}

	@GetMapping("/coffees")
	public List<CoffeeResponseDto> getAllCoffees() {
		return coffeeService.getAllCoffees();
	}

	@GetMapping("/myorder")
	public List<OrderResponseDto> myOrder(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return orderService.getMyOrders(userDetails.getUser());
	}

	@GetMapping("/mycompleteorder")
	public List<CompleteOrderResponseDto> myCompleteOrder(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return orderService.getMyCompleteOrders(userDetails.getUser());
	}

}
