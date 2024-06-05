package com.sparta.cafe.entity;

import com.sparta.cafe.dto.OrderRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends Timestamped {

	@Id
	@Column(name = "order_id") // orderId 대신에 order_id로 컬럼명 변경
	private Long orderId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "coffee_id", nullable = false)
	private Coffee coffee;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String coffeeName;

	// 생성자 추가
	public Order(OrderRequestDto orderRequestDto, User user, Coffee coffee) {
		this.price = orderRequestDto.getPrice();
		this.username = user.getUsername();
		this.coffeeName = coffee.getCoffeeName();
	}

}

