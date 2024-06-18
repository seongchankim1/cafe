package com.sparta.cafe.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.cafe.dto.LoginRequestDto;
import com.sparta.cafe.entity.User;
import com.sparta.cafe.entity.UserRoleEnum;
import com.sparta.cafe.jwt.JwtUtil;
import com.sparta.cafe.repository.UserRepository;
import com.sparta.cafe.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// JWT 기반 인증 처리를 위한 필터
@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;
	private final UserService userService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, ObjectMapper objectMapper, UserService userService, UserRepository userRepository) {
		super(new AntPathRequestMatcher("/user/login"));
		setAuthenticationManager(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.objectMapper = objectMapper;
		this.userService = userService;
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					requestDto.getUsername(),
					requestDto.getPassword(),
					null
				)
			);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	// 인증 성공 처리
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
		UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

		String accessToken = jwtUtil.createAccessToken(username, role);
		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
		jwtUtil.addJwtToCookie(accessToken, response);
		String refreshToken = jwtUtil.createRefreshToken(accessToken, role);
		User user = userRepository.findByUsername(username);
		user.setRefreshToken(refreshToken);
		userRepository.save(user);
	}

	// 인증 실패 처리
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		// 상태 코드 401
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write("Login failed");
	}
}