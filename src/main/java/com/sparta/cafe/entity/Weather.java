package com.sparta.cafe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "weathers")
public class Weather {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int time;
	private String sky;
	private int temp;

	/*
	생성자
	 */
	public Weather(int time, String sky, int temp) {
		this.time = time;
		this.sky = sky;
		this.temp = temp;
	}
}
