/*
 * copyleft © 2019-2021
 */
package com.wteam.base.monitor;

import com.wteam.utils.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 设置审计 - 创建、修改
 * @author mission
 * @since 2019/6/7 9:47
 */
@Component("auditorAware")
public class AuditorConfig implements AuditorAware<Long> {


    @Override
    public Optional<Long> getCurrentAuditor() {
        try {
            return Optional.of(SecurityUtils.getId());
        } catch (Exception ignored) {}
        return Optional.of(0L);
    }
}
