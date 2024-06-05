package com.sparta.cafe.dto;

import com.sparta.cafe.entity.Coffee;

import lombok.Getter;

@Getter
public class CoffeeResponseDto {

	private Long id;
	private String coffeeName;
	private int price;
	private String imageUrl;


	public CoffeeResponseDto(Coffee coffee) {
		this.id = coffee.getId();
		this.coffeeName = coffee.getCoffeeName();
		this.price = coffee.getPrice();
		this.imageUrl = coffee.getImageUrl();
	}
}
