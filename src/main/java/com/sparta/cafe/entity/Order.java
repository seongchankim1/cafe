package com.sparta.cafe.entity;

import com.sparta.cafe.dto.OrderRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coffee_id", nullable = false)
	private Coffee coffee;

	@Column(nullable = false)
	private int price;

	public Order(OrderRequestDto orderRequestDto) {
		this.price = orderRequestDto.getPrice();
	}

}
