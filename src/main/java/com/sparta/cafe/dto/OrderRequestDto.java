package com.sparta.cafe.dto;

import lombok.Getter;

@Getter
public class OrderRequestDto {
	private Long orderId;
	private String username;
	private Long coffeeId;
	private int price;
	private String strength;
}
