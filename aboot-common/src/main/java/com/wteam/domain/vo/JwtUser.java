/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 当前登录用户
 * @author mission
 * @since 2020/07/24 14:51
 */
@Getter
public class JwtUser implements UserDetails {

	private final Long id;

	private final String username;

	private  String nickname;

	@JsonIgnore
	private  String password;

	private  Integer sex;

	private  String avatar;

	private  String email;

	private  String phone;

	private  Integer loginType;


	private  boolean enabled;

	@JsonIgnore
	private Timestamp lastPasswordResetDate;

	private  String dept;

	private  String job;

	@JsonIgnore
	private final Collection<GrantedAuthority> authorities;



	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public Collection<?> getRoles() {
		return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
	}


	public JwtUser(Long id, String username, Collection<GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.authorities = authorities;
	}

	public JwtUser(Long id, String username, String nickname, String password, Integer sex, String avatar, String email, String phone, Integer loginType, boolean enabled, Timestamp lastPasswordResetDate, String dept, String job, Collection<GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.sex = sex;
		this.avatar = avatar;
		this.email = email;
		this.phone = phone;
		this.loginType = loginType;
		this.enabled = enabled;
		this.lastPasswordResetDate = lastPasswordResetDate;
		this.dept = dept;
		this.job = job;
		this.authorities = authorities;
	}
}
