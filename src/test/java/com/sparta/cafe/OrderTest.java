package com.sparta.cafe;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cafe.dto.OrderRequestDto;
import com.sparta.cafe.entity.Coffee;
import com.sparta.cafe.entity.Order;
import com.sparta.cafe.entity.User;
import com.sparta.cafe.repository.CoffeeRepository;
import com.sparta.cafe.repository.OrderRepository;
import com.sparta.cafe.repository.UserRepository;
import com.sparta.cafe.service.OrderService;

@Transactional
@SpringBootTest
public class OrderTest {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderService orderService;

	@Autowired
	UserRepository userRepository;
	@Autowired
	CoffeeRepository coffeeRepository;

	@Test
	@Rollback(false)
	@DisplayName("유저 임의 추가")
	void test1() {
		User user = new User();
		user.setId(1L);
		user.setOrderList(new ArrayList<Order>());
		user.setUsername("김성찬");
		userRepository.save(user);
	}

	@Test
	@Rollback(false)
	@DisplayName("커피 임의 추가")
	void test2() {
		User user = new User();
		user.setUsername("김정재");
		user = userRepository.save(user);

		Coffee coffee = new Coffee();
		coffee.setCoffeeName("카페라떼");
		coffee.setOrderList(new ArrayList<>());
		coffee.setPrice(2000);

		coffee = coffeeRepository.save(coffee);
		assertNotNull(coffee.getId());
	}
}
