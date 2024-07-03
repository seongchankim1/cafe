package com.sparta.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cafe.entity.Weather;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
}
