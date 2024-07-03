package com.sparta.cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cafe.dto.WeatherResponseDto;
import com.sparta.cafe.entity.Weather;
import com.sparta.cafe.service.WeatherDataService;

@RestController
@RequestMapping("/api")
public class WeatherController {

	@Autowired
	private WeatherDataService weatherDataService;

	@GetMapping("/weather")
	public WeatherResponseDto getWeather() {
		try {
			Weather weather = weatherDataService.lookUpWeather();
			return new WeatherResponseDto(weather);
		} catch (Exception e) {
			return null;
		}
	}
}
