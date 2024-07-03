package com.sparta.cafe.dto;

import com.sparta.cafe.entity.Weather;

import lombok.Getter;

@Getter
public class WeatherResponseDto {

	private int time;
	private int temp;
	private String sky;

	public WeatherResponseDto(Weather weather) {
		this.time = weather.getTime();
		this.temp = weather.getTemp();
		this.sky = weather.getSky();
	}
}
