package com.sparta.cafe.security;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.sparta.cafe.jwt.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final JwtUtil jwtUtil;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
	}

	// 필터 실행
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request.getCookies() != null) {
			Arrays.stream(request.getCookies())
				.forEach(cookie -> System.out.println(cookie.getName() + ": " + cookie.getValue()));
		}
		String token = resolveTokenFromCookies(request);

		// 토큰 유효성 검사
		if (token != null && jwtUtil.validateToken(token)) {
			Authentication authentication = jwtUtil.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {
			System.out.println("토큰이 없습니다. 로그인 해주세요.");
		}

		chain.doFilter(request, response);
	}


	private String resolveTokenFromCookies(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (JwtUtil.AUTHORIZATION_HEADER.equals(cookie.getName())) {
					String token = cookie.getValue();
					// %20을 공백으로 변환
					token = token.replace("%20", " ");
					return jwtUtil.substringToken(token);
				}
			}
		}
		return null;
	}
}
