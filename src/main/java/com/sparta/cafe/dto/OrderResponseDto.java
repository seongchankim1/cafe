package com.sparta.cafe.dto;

import com.sparta.cafe.entity.Order;
import lombok.Getter;

@Getter
public class OrderResponseDto {

	private Long orderId;
	private Long userId;
	private Long coffeeId;
	private int price;

	public OrderResponseDto(Order order) {
		this.orderId = order.getOrderId();
		this.userId = order.getUser().getId();
		this.coffeeId = order.getCoffee().getId();
		this.price = order.getPrice();
	}
}
