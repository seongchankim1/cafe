package com.sparta.cafe.dto;

import java.time.LocalDateTime;

import com.sparta.cafe.entity.CompleteOrder;
import com.sparta.cafe.entity.Order;

import lombok.Getter;

@Getter
public class CompleteOrderResponseDto {

	private Long completeOrderId;
	private Long orderId;
	private int price;
	private String username;
	private String coffeeName;
	private String strength;
	private LocalDateTime createdAt;

	public CompleteOrderResponseDto(CompleteOrder order) {
		this.completeOrderId = order.getCompleteId();
		this.orderId = order.getOrderId();
		this.price = order.getPrice();
		this.username = order.getUser().getUsername(); // 사용자 이름 추가
		this.coffeeName = order.getCoffee().getCoffeeName(); // 음료 이름 추가
		this.strength = order.getStrength();
		this.createdAt = order.getCreatedAt();
	}
}
