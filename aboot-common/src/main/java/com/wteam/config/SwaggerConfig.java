/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.config;

import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.schema.AlternateTypeRules.newRule;


/**
 * api页面 /doc.html
 * @author mission
 * @since 2019/07/08 8:29
 */
@ConditionalOnProperty(prefix = "swagger",value = {"enabled"},havingValue = "true")
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

	@Value("${jwt.header}")
	private  String tokenHeader;

	@Value("${swagger.enabled}")
	private final Boolean enabled=false;


	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.enable(enabled)
				.apiInfo(apiInfo())
				.useDefaultResponseMessages(true)
				.forCodeGeneration(true)
				.select()
				.paths(Predicates.not(regex("/error.*")))
				.paths(Predicates.not(regex("/swagger-mg-ui/.*")))
				.build()
				.securitySchemes(securitySchemes())
				.securityContexts(securityContexts());

	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("ABOOT 接口文档")
				.version("0.0.3")
				.description("Bearer Token")
				.build();
	}


	private List<ApiKey> securitySchemes() {
		return Lists.newArrayList(
				new ApiKey(tokenHeader, tokenHeader, "header"));
	}

	private List<SecurityContext> securityContexts() {
		return Lists.newArrayList(
				SecurityContext.builder()
						.securityReferences(defaultAuth())
						.forPaths(PathSelectors.regex("^(?!auth).*$"))
						.build()
		);
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Lists.newArrayList(
				new SecurityReference(tokenHeader, authorizationScopes));
	}


}

/**
 *  将Pageable转换展示在swagger中
 */
@ConditionalOnProperty(prefix = "swagger",value = {"enabled"},havingValue = "true")
@Configuration
class SwaggerDataConfig {

	@Bean
	public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
		return new AlternateTypeRuleConvention() {
			@Override
			public int getOrder() {
				return Ordered.HIGHEST_PRECEDENCE;
			}

			@Override
			public List<AlternateTypeRule> rules() {
				return newArrayList(
						newRule(resolver.resolve(Pageable.class), resolver.resolve(Page.class)),
						newRule(resolver.resolve(Timestamp.class), resolver.resolve(Date.class)));
			}
		};
	}

	@ApiModel
	@Data
	private static class Page {
		@ApiModelProperty("页码 (0..N)")
		private Integer page;

		@ApiModelProperty("每页显示的数目")
		private Integer size;

		@ApiModelProperty("以下列格式排序标准：property[,asc | desc]。 默认排序顺序为升序。 支持多种排序条件：如：id,asc")
		private List<String> sort;
	}
}
