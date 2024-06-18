package com.sparta.cafe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.cafe.jwt.JwtUtil;
import com.sparta.cafe.repository.UserRepository;
import com.sparta.cafe.security.JwtAuthenticationFilter;
import com.sparta.cafe.security.JwtAuthorizationFilter;
import com.sparta.cafe.security.UserDetailsServiceImpl;
import com.sparta.cafe.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final UserDetailsServiceImpl userDetailsService;
	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;
	private final UserService userService;
	private final UserRepository userRepository;

	// WebSecurityConfig 초기화
	public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil, ObjectMapper objectMapper, UserService userService, UserRepository userRepository) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
		this.objectMapper = objectMapper;
		this.userService = userService;
		this.userRepository = userRepository;
	}

	// SecurityFilterChain 빈 정의
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
		// AuthenticationManager 설정
		AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();

		http.csrf(csrf -> csrf.disable()) // CSRF 비활성화
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 안씀
			.authorizeHttpRequests(authorize -> authorize
				// 특정 경로에 대한 접근 권한 설정
				.requestMatchers("/user/signup", "/user/login", "/user/login-page", "/home").permitAll()
				.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
				.anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
			)
			// JWT 인증 필터 추가
			.addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtUtil, objectMapper, userService, userRepository), UsernamePasswordAuthenticationFilter.class)
			// JWT 인가 필터 추가
			.addFilterBefore(new JwtAuthorizationFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// AuthenticationManager 빈 정의
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}