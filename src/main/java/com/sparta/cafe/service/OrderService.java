package com.sparta.cafe.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.sparta.cafe.dto.OrderRequestDto;
import com.sparta.cafe.dto.OrderResponseDto;
import com.sparta.cafe.entity.Coffee;
import com.sparta.cafe.entity.Order;
import com.sparta.cafe.entity.User;
import com.sparta.cafe.repository.CoffeeRepository;
import com.sparta.cafe.repository.OrderRepository;
import com.sparta.cafe.repository.UserRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final CoffeeRepository coffeeRepository;
	private final UserRepository userRepository;
	private List<Long> completeList = new ArrayList<>();

	public OrderService(OrderRepository orderRepository, CoffeeRepository coffeeRepository, UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.coffeeRepository = coffeeRepository;
		this.userRepository = userRepository;
	}

	public OrderResponseDto addOrder(OrderRequestDto orderRequestDto) {
		// 커피 정보를 불러와서 설정합니다.
		Coffee coffee = coffeeRepository.findById(orderRequestDto.getCoffeeId())
			.orElseThrow(() -> new IllegalArgumentException("커피를 찾을 수 없습니다."));

		// 유저 정보를 불러와서 설정합니다.
		if (!userRepository.existsByUsername(orderRequestDto.getUsername())) {
			User user = new User();
			user.setUsername(orderRequestDto.getUsername());
			userRepository.save(user);
		}
		User user = userRepository.findByUsername(orderRequestDto.getUsername());

		// Order Entity 생성
		Order order = new Order(orderRequestDto);
		order.setCoffee(coffee);
		order.setUser(user);
		order.setPrice(coffee.getPrice());
		order.setOrderId(generateNewOrderId());
		System.out.println(order.getOrderId());

		// 저장
		orderRepository.save(order);

		// Return
		return new OrderResponseDto(order);
	}

	private Long generateNewOrderId() {
		List<Long> orderIds = orderRepository.findAll().stream()
			.map(Order::getOrderId)
			.sorted()
			.toList();

		Long newOrderId = 1L;
		for (Long id : orderIds) {
			if (!id.equals(newOrderId)) {
				break;
			}
			newOrderId++;
		}
		return newOrderId;
	}

	public List<OrderResponseDto> getAllOrders() {
		return orderRepository.findAllByOrderByCreatedAt().stream().map(OrderResponseDto::new).toList();
	}

	public int deleteOrder(Long id) {
		orderRepository.deleteById(id);
		completeList.add(id);
		completeList.sort(Collections.reverseOrder());
		return id.intValue();
	}

	public List<Long> getCompletedOrders() {
		return completeList;
	}
}
