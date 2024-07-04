package com.sparta.cafe.dto;

import java.time.LocalDateTime;

import com.sparta.cafe.entity.User;
import com.sparta.cafe.entity.UserRoleEnum;

import lombok.Getter;

@Getter
public class UserResponseDto {

	private Long id;
	private String username;
	private int money;
	private String phoneNumber;
	private UserRoleEnum role;
	private LocalDateTime createdAt;

	public UserResponseDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.money = user.getMoney();
		this.phoneNumber = user.getPhoneNumber();
		this.role = user.getRole();
		this.createdAt = user.getCreatedAt();
	}
}
