/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam;


import com.wteam.base.impl.BaseRepositoryImpl;
import com.wteam.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableAsync
@EnableScheduling
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
@SpringBootApplication
public class AbootRun {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(AbootRun.class, args);
		Environment env = run.getBean(Environment.class);
		String port = env.getProperty("server.port");
		log.info("link(接口文档):  "+"http://localhost:{}/doc.html",port);
		log.info("link(api导出):   "+"http://localhost:{}/v2/api-docs",port);
	}


	@Bean
	public SpringContextUtil springContextHolder() {
		return new SpringContextUtil();
	}

}
