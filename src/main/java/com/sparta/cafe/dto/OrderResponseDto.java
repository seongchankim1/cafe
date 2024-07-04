package com.sparta.cafe.dto;

import com.sparta.cafe.entity.Order;
import lombok.Getter;

@Getter
public class OrderResponseDto {

	private Long orderId;

	private Long userId;
	private Long coffeeId;
	private int price;
	private String username;
	private String coffeeName;
	private String strength;

	public OrderResponseDto(Order order) {
		this.orderId = order.getOrderId();
		this.userId = order.getUser().getId();
		this.coffeeId = order.getCoffee().getId();
		this.price = order.getPrice();
		this.username = order.getUser().getUsername(); // 사용자 이름 추가
		this.coffeeName = order.getCoffee().getCoffeeName(); // 음료 이름 추가
		this.strength = order.getStrength();
	}

}
