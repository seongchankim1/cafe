package com.sparta.cafe.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class MoneyRequestDto {

	private Long userId;
	private int amount;
}
