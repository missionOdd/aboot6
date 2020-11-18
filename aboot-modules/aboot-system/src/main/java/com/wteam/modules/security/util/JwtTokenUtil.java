/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.modules.security.util;

import cn.hutool.core.date.DateUtil;
import com.wteam.domain.vo.JwtUser;
import com.wteam.modules.security.config.JwtProperties;
import com.wteam.utils.RedisUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * JwtTokenUtil
 * @author mission
 * @since 2019/07/10 11:54
 */
@Slf4j
@Component
public class JwtTokenUtil implements InitializingBean {

	private final JwtProperties jwtProperties;
	private final RedisUtils redisUtils;
	private static final String AUTHORITIES_KEY = "auth";
	private JwtParser jwtParser;
	private JwtBuilder jwtBuilder;

	public JwtTokenUtil(JwtProperties jwtProperties, RedisUtils redisUtils) {
		this.jwtProperties = jwtProperties;
		this.redisUtils = redisUtils;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
		Key key = Keys.hmacShaKeyFor(keyBytes);
		jwtParser = Jwts.parserBuilder()
				.setSigningKey(key)
				.build();
		jwtBuilder = Jwts.builder()
				.signWith(key, SignatureAlgorithm.HS512);

	}

	public String getIdFromToken(String token){
		return getClaimFromToken(token, Claims::getId);
	}

	public String getUsernameFromToken(String token){
		return getClaimFromToken(token,Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token){
		return getClaimFromToken(token,Claims::getIssuedAt);
	}


	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims getClaims(String token) {
		return jwtParser
				.parseClaimsJws(token)
				.getBody();
	}

	public String createToken(JwtUser jwtUser){
		String authorities =jwtUser.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		return jwtBuilder
				.setId(jwtUser.getId().toString())
				.setSubject(jwtUser.getUsername())
				.setIssuedAt(new Date())
				.claim(AUTHORITIES_KEY,authorities)
				.compact();
	}

	/**
	 *
	 * @param token 通过token获取Authentication
	 * @return
	 */
	public Authentication getAuthentication(String token){
		Claims claims =getClaims(token);
		/*从token获取权限*/
		Collection<GrantedAuthority> authorities =
				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
						.filter(StringUtils::hasText)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());
		/*创建登录用户,存入关键信息*/
		JwtUser principal = new JwtUser(Long.valueOf(claims.getId()),claims.getSubject(), authorities);
		return new UsernamePasswordAuthenticationToken(principal,token,authorities);
	}


	/**
	 * 验证签发的token
	 * @param token
	 * @return
	 */
	public boolean validateToken(String token){
		try {
			jwtParser.parseClaimsJws(token);
			return true;
		}catch (SecurityException e){
			log.info("Invalid JWT signature. ");
			e.printStackTrace();
		}catch (ExpiredJwtException e){
			log.info("Expired JWT token.");
			e.printStackTrace();
		}catch (UnsupportedJwtException e){
			log.info("Unsupported JWT token.");
			e.printStackTrace();
		}catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid");
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * @param token 需要检查的token
	 */
	public void checkRenewal(String token) {
		// 判断是否续期token,计算token的过期时间
		long time = redisUtils.getExpire(jwtProperties.getOnlineKey() + token) * 1000;
		Date expireDate = DateUtil.offsetMillisecond(new Date(), (int)time);
		// 判断当前时间与过期时间的时间差
		long differ = expireDate.getTime() - System.currentTimeMillis();
		// 如果在续期检查的范围内，则续期
		if (differ <= jwtProperties.getDetect()) {
			long renew = time + jwtProperties.getRenew();
			redisUtils.expire(jwtProperties.getOnlineKey() + token, renew, TimeUnit.MILLISECONDS);
		}
	}

	public String getToken(HttpServletRequest request){
		final String requestHeader =request.getHeader(jwtProperties.getHeader());
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			return requestHeader.substring(7);
		}
		return null;
	}
}
