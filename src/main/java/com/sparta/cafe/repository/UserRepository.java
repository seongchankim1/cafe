package com.sparta.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cafe.dto.CompleteOrderResponseDto;
import com.sparta.cafe.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	boolean existsByUsername(String username);

	User findByPhoneNumber(String phoneNumber);

	List<User> findAllByOrderByIdDesc();
}
