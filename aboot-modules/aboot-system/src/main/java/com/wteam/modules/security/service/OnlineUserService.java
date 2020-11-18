/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.security.service;

import com.wteam.domain.vo.JwtUser;
import com.wteam.exception.BadRequestException;
import com.wteam.modules.security.domain.vo.OnlineUser;
import com.wteam.utils.*;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author mission
 * @since 2019/11/02 16:20
 */
@SuppressWarnings({"unchecked", "all"})
@Slf4j
@Service
@RequiredArgsConstructor
public class OnlineUserService {

	@Value("${jwt.expiration}")
	private Long expiration;

	@Value("${jwt.online-key}")
	private String onlineKey;

	private final RedisUtils redisUtils;

	/**
	 * 保存在线用户信息
	 * @param jwtUser /
	 * @param token /
	 * @param request /
	 */
	public void save(JwtUser jwtUser, String token, HttpServletRequest request) {
		String job = jwtUser.getDept() + "/" + jwtUser.getJob();
		String ip = StringUtils.getIP(request);
		String address = StringUtils.getCityInfo(ip);
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		String browser =userAgent.getBrowser().getName();
		String os = userAgent.getOperatingSystem().getName();
		OnlineUser onlineUser = null;
		try {
			onlineUser = new OnlineUser(jwtUser.getUsername(),jwtUser.getNickname(), job, browser, os, ip, address, EncryptUtils.desEncrypt(token), Timestamp.valueOf(LocalDateTime.now()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BadRequestException("登录失败");
		}
		redisUtils.set(onlineKey + token,onlineUser, expiration, TimeUnit.MILLISECONDS);
	}

	/**
	 * 查询全部数据
	 * @param filter
	 * @param pageable
	 * @return
	 */
	public Map getAll(String filter, Pageable pageable) {
		List<OnlineUser> onlineUsers = getAll(filter);
		return PageUtil.toPage(
				PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), onlineUsers),
				onlineUsers.size());
	}

	/**
	 * 查询全部数据,不分页
	 * @param filter
	 * @return
	 */
	public List<OnlineUser> getAll(String filter) {
		List<String> keys = redisUtils.scan(onlineKey + "*");
		Collections.reverse(keys);
		List<OnlineUser> onlineUsers = new ArrayList<>();
		for (String key : keys) {
			OnlineUser onlineUser = (OnlineUser) redisUtils.get(key);
			if (StringUtils.isNotBlank(filter)) {
				if (onlineUser.toString().contains(filter)) {
					onlineUsers.add(onlineUser);
				}
			}else {
				onlineUsers.add(onlineUser);
			}
		}
		onlineUsers.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
		return onlineUsers;
	}

	/**
	 * 踢出用户
	 * @param val
	 * @throws Exception
	 */
	public void kickOut(String val)  {
		String key = onlineKey +val;
		redisUtils.del(key);
	}

	/**
	 * 退出登录
	 * @param token
	 */
	public void logout(String token) {
		String key = onlineKey + token;
		redisUtils.del(key);
	}

	/**
	 * 导出
	 * @param all
	 * @param response
	 * @throws IOException
	 */
	public void download(List<OnlineUser> all, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (OnlineUser user : all) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("账号", user.getUsername());
			map.put("昵称", user.getNickname());
			map.put("岗位", user.getJob());
			map.put("登录IP", user.getIp());
			map.put("登录地点", user.getAddress());
			map.put("浏览器", user.getBrowser());
			map.put("系统", user.getOs());
			map.put("登录日期", user.getLoginTime());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

	/**
	 * 查询用户
	 * @param onlineKey
	 * @return
	 */
	public  OnlineUser getOne(String key){
		return (OnlineUser) redisUtils.get(key);
	}

	/**
	 * 检测用户是否在之前已经登录, 已经登录踢下线(只能一个设备登录)
	 * @param userName
	 * @param igoreToken
	 */
	public void checkLoginOnUser(String userName,String igoreToken){
		List<OnlineUser> onlineUsers =getAll(userName);
		if (onlineUsers == null || onlineUsers.isEmpty()) {
			return;
		}
		for (OnlineUser onlineUser : onlineUsers) {
			if (onlineUser.getUsername().equals(userName)){
				try {
					String token =EncryptUtils.desDecrypt(onlineUser.getKey());
					if (StringUtils.isNotBlank(igoreToken) && !igoreToken.equals(token)) {
						this.kickOut(token);
					}else if (StringUtils.isBlank(igoreToken)){
						this.kickOut(token);
					}
				} catch (Exception e) {
					log.error("CheckUser is error", e);
				}
			}
		}
	}


	/**
	 * 根据用户名强退用户
	 * @param username /
	 */
	@Async
	public void kickOutForUsername(String username) {
		List<OnlineUser> onlineUsers = getAll(username);
		String token =null;
		for (OnlineUser onlineUser : onlineUsers) {
			if (onlineUser.getUsername().equals(username)) {
				try {
					token = EncryptUtils.desDecrypt(onlineUser.getKey());
					this.kickOut(token);
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
	}
}
