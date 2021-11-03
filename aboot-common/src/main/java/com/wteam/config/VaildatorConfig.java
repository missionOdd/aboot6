/*
 * copyleft © 2019-2021
 */
package com.wteam.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * 快速模式 验证配置
 * 当第一个验证有误,则返回
 * @author mission
 * @since 2020/01/10 14:08
 */
@Configuration
public class VaildatorConfig {
    @Bean
    public Validator validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory().getValidator();
    }
}
