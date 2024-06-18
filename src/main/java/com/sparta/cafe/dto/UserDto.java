package com.sparta.cafe.dto;

import com.sparta.cafe.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
	private String username;
	private int money;

	public UserDto(User user) {
		this.username = user.getUsername();
		this.money = user.getMoney();
	}
}
