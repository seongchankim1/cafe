package com.sparta.cafe.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.cafe.dto.MoneyRequestDto;
import com.sparta.cafe.dto.SignupRequestDto;
import com.sparta.cafe.dto.UserDto;
import com.sparta.cafe.dto.UserResponseDto;
import com.sparta.cafe.dto.UserSearchRequestDto;
import com.sparta.cafe.entity.User;
import com.sparta.cafe.entity.UserRoleEnum;
import com.sparta.cafe.jwt.JwtUtil;
import com.sparta.cafe.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
	}

	// 로그인은 security로 넘어감
	// public boolean login(LoginRequestDto requestDto, HttpServletResponse response) {
	//
	// 	String username = requestDto.getUsername();
	// 	String password = requestDto.getPassword();
	// 	User user = userRepository.findByUsername(username);
	// 	UserRoleEnum role = user.getRole();
	// 	if (user != null) {
	//
	// 		System.out.println("로그인 시도중. 이름 : " + user.getUsername());
	//
	// 		if (!passwordEncoder.matches(password, user.getPassword())) {
	// 			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
	// 		}
	//
	// 		String accessToken = jwtUtil.createAccessToken(username, role);
	// 		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
	//
	// 		String refreshToken = jwtUtil.createRefreshToken(username, role); // 리프레시 토큰 생성
	// 		user.setRefreshToken(refreshToken);
	// 		userRepository.save(user);
	// 		return true;
	// 	} else {
	// 		throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
	// 	}
	//
	// }

	public boolean signup(SignupRequestDto requestDto) {
		// 회원 중복 확인
		User checkUsername = userRepository.findByUsername(requestDto.getUsername());
		if (checkUsername != null) {
			throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
		}

		// 전화번호 중복확인
		User checkPhoneNumber = userRepository.findByPhoneNumber(requestDto.getPhone());
		if (checkPhoneNumber != null) {
			throw new IllegalArgumentException("중복된 휴대폰 번호입니다.");
		}

		// 사용자 등록
		User user = new User(requestDto.getUsername(), requestDto.getPassword(), requestDto.getPhone());
		user.setRole(UserRoleEnum.USER);
		user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		user.updateMoney(0);
		userRepository.save(user);

		return true;
	}

	public UserDto getUserInfo(String username) {
		User user = userRepository.findByUsername(username);
		UserDto userDto = new UserDto(user);
		return userDto;
	}

	public String getUsernameFromToken(HttpServletRequest request) {
		String token = jwtUtil.resolveTokenFromCookies(request);
		String username = jwtUtil.getUserInfoFromToken(token).getSubject();
		return username;
	}

	public List<UserResponseDto> getUserDB() {
		return userRepository.findAllByOrderByIdDesc().stream()
			.map(UserResponseDto::new)
			.collect(Collectors.toList());
	}

	public UserResponseDto addMoney(MoneyRequestDto request) {
		User user = userRepository.findById(request.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
		int newMoney = user.getMoney() + request.getAmount();
		user.updateMoney(newMoney);
		userRepository.save(user);
		return new UserResponseDto(user);
	}

	public List<UserResponseDto> getSearchDB(String username) {
		return userRepository.findAllByUsernameOrderById(username).stream()
			.map(UserResponseDto::new)
			.collect(Collectors.toList());
	}
}
