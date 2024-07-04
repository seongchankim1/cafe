package com.sparta.cafe.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.sparta.cafe.dto.OrderRequestDto;
import com.sparta.cafe.dto.OrderResponseDto;
import com.sparta.cafe.entity.Coffee;
import com.sparta.cafe.entity.CompleteOrder;
import com.sparta.cafe.entity.Order;
import com.sparta.cafe.entity.User;
import com.sparta.cafe.repository.CoffeeRepository;
import com.sparta.cafe.repository.CompleteOrderRepository;
import com.sparta.cafe.repository.OrderRepository;
import com.sparta.cafe.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final CoffeeRepository coffeeRepository;
	private final UserRepository userRepository;
	private final CompleteOrderRepository completeOrderRepository;
	private List<CompleteOrder> completeList = new ArrayList<>();
	Long orderId = 1L;

	public OrderService(OrderRepository orderRepository, CoffeeRepository coffeeRepository, UserRepository userRepository, CompleteOrderRepository completeOrderRepository) {
		this.orderRepository = orderRepository;
		this.coffeeRepository = coffeeRepository;
		this.userRepository = userRepository;
		this.completeOrderRepository = completeOrderRepository;
	}

	@Transactional
	public OrderResponseDto addOrder(OrderRequestDto orderRequestDto) {
		Coffee coffee = coffeeRepository.findById(orderRequestDto.getCoffeeId())
			.orElseThrow(() -> new IllegalArgumentException("커피를 찾을 수 없습니다."));

		if (!userRepository.existsByUsername(orderRequestDto.getUsername())) {
			User user = new User();
			user.setUsername(orderRequestDto.getUsername());
			userRepository.save(user);
		}
		User user = userRepository.findByUsername(orderRequestDto.getUsername());

		Order order = new Order(orderRequestDto, user, coffee);
		order.setCoffee(coffee);
		order.setUser(user);
		order.setPrice(coffee.getPrice());
		if (orderRequestDto.getStrength().equals("샷추가")) {
			order.setPrice(order.getPrice() + 1000);
		}
		order.setOrderId(generateNewOrderId());
		System.out.println(order.getOrderId());
		// 키오스크는 돈이 차감되지 않음
		if ((user.getMoney() - coffee.getPrice()) < 0 && !user.getUsername().equals("kiosk")) {
			throw new IllegalArgumentException("잔액이 부족합니다.");
		} else if (user.getUsername().equals("kiosk")) {
		} else {
			user.setMoney(user.getMoney() - coffee.getPrice());
		}

		orderRepository.save(order);

		return new OrderResponseDto(order);
	}

	// 빈 번호 중에 가장 작은 번호를 부여
	private Long generateNewOrderId() {
		if (orderId > 20) {
			orderId = 1L;
		}
		while (orderRepository.existsById(orderId)) {
			orderId++;
		}
		return orderId++;
	}

	public List<OrderResponseDto> getAllOrders() {
		return orderRepository.findAllByOrderByCreatedAt().stream()
			.map(OrderResponseDto::new)
			.collect(Collectors.toList());
	}

	public int deleteOrder(Long id) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));
		completeOrder(order);
		orderRepository.deleteById(id);

		CompleteOrder completeOrder = new CompleteOrder();
		completeOrder.setOrderId(id);
		completeOrder.setCoffeeName(order.getCoffee().getCoffeeName());
		completeList.add(completeOrder);

		return id.intValue();
	}

	public List<CompleteOrder> getCompleteOrders() {
		List<CompleteOrder> reversedList = new ArrayList<>(completeList);
		Collections.reverse(reversedList);
		return reversedList;
	}

	public void completeOrder(Order order) {
		CompleteOrder completeOrder = new CompleteOrder();
		completeOrder.setOrderId(order.getOrderId()); // orderId 설정
		completeOrder.setUser(order.getUser());
		completeOrder.setCoffee(order.getCoffee());
		completeOrder.setPrice(order.getPrice());
		completeOrder.setCoffeeName(order.getCoffeeName());
		completeOrderRepository.save(completeOrder);
	}
}
