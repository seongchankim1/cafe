package com.sparta.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cafe.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	boolean existsByUsername(String username);
}
