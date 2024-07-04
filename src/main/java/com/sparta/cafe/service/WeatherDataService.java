package com.sparta.cafe.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.sparta.cafe.entity.Weather;
import com.sparta.cafe.repository.WeatherRepository;

@Service
public class WeatherDataService {

	private final WeatherRepository weatherRepository;
	private String serviceKey = "+bK//GEks5VWnzhSM+OWFa77qkVHq2FXiObP8LGtLy1lwk35l5TdHIo3iMnyyx+wEoCwMbKe1uC7H8lyYQkE+g==";

	private static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
	private static final String NX = "61"; // 고정된 nx 값
	private static final String NY = "127"; // 고정된 ny 값
	private int nowTime = 0;

	public WeatherDataService(WeatherRepository weatherRepository) {
		this.weatherRepository = weatherRepository;
	}

	public Weather lookUpWeather() throws IOException, JSONException {
		// 현재 날짜와 시간
		ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
		LocalDateTime now = LocalDateTime.now(koreaZoneId);
		String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String baseTime = now.format(DateTimeFormatter.ofPattern("HH00"));

		// baseTime을 3시간 단위로 맞추기
		int hour = now.getHour();
		if (hour % 3 < 2) {
		nowTime = (hour / 3) * 3 - 1;
		} else {
			nowTime = (hour / 3) * 3 + 2;
		}
		baseTime = String.format("%02d00", nowTime);

		StringBuilder urlBuilder = new StringBuilder(BASE_URL);
		urlBuilder.append("?");
		urlBuilder.append("serviceKey").append("=").append(URLEncoder.encode(serviceKey, "UTF-8")).append("&");
		urlBuilder.append("numOfRows").append("=").append(12).append("&");
		urlBuilder.append("pageNo").append("=").append(1).append("&");
		urlBuilder.append("dataType").append("=").append("JSON").append("&");
		urlBuilder.append("base_date").append("=").append(baseDate).append("&");
		urlBuilder.append("base_time").append("=").append(baseTime).append("&");
		urlBuilder.append("nx").append("=").append(NX).append("&");
		urlBuilder.append("ny").append("=").append(NY);

		URL url = new URL(urlBuilder.toString());

		System.out.println("url = " + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("Response code: " + conn.getResponseCode());
		System.out.println("conn = " + conn);
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}

		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}

		rd.close();
		conn.disconnect();
		String result = sb.toString();

		// 로깅: 응답 결과 출력
		System.out.println("API Response: " + result);

		// 서비스 오류 확인 및 예외 처리
		if (result.contains("<errMsg>")) {
			// XML 응답에서 오류 메시지 파싱
			String errMsg = result.substring(result.indexOf("<errMsg>") + 8, result.indexOf("</errMsg>"));
			throw new IOException("API Error: " + errMsg);
		}

		// JSON 응답 형식 확인 및 오류 메시지 처리
		if (!result.trim().startsWith("{")) {
			throw new JSONException("Invalid JSON response: " + result);
		}

		return parseWeatherData(result);
	}

	private Weather parseWeatherData(String result) throws JSONException {
		JSONObject jsonObj_1 = new JSONObject(result);
		JSONObject response = jsonObj_1.getJSONObject("response");
		JSONObject body = response.getJSONObject("body");
		JSONObject items = body.getJSONObject("items");
		JSONArray jsonArray = items.getJSONArray("item");

		String sky = "";
		int temperature = 0;

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObj_4 = jsonArray.getJSONObject(i);
			String fcstValue = jsonObj_4.getString("fcstValue");
			String category = jsonObj_4.getString("category");

			if (category.equals("SKY")) {
				switch (fcstValue) {
					case "1":
						sky = "맑음";
						break;
					case "2":
						sky += "비";
						break;
					case "3":
						sky += "구름";
						break;
					case "4":
						sky += "흐림";
						break;
				}
			}

			if (category.equals("TMP")) {
				temperature = Integer.parseInt(fcstValue);
			}
		}

		Weather weather = new Weather(nowTime, sky, temperature);
		weatherRepository.save(weather);
		return weather;
	}
}
