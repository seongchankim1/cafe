package com.sparta.cafe.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "coffees")
public class Coffee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String coffeeName;

	@OneToMany(mappedBy = "coffee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> orderList = new ArrayList<>();

	@OneToMany(mappedBy = "coffee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> completeOrderList = new ArrayList<>();

	@Column(nullable = false)
	private int price;

	@Column
	private String imageUrl;

	public Coffee(String coffeeName, List<Order> orderList, List<Order> completeOrderList, String imageUrl) {
		this.coffeeName = coffeeName;
		this.orderList = orderList;
		this.completeOrderList = completeOrderList;
		this.imageUrl = imageUrl;
	}
}
