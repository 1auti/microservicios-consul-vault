package com.lautaro.cliente_service.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditingConfig {

    private static final String DEFAULT_AUDITOR = "system";
    private static final String USER_ID_HEADER = "X-User-Id";

    @SuppressWarnings("null")
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            try {
                return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                        .filter(ServletRequestAttributes.class::isInstance)
                        .map(ServletRequestAttributes.class::cast)
                        .map(ServletRequestAttributes::getRequest)
                        .map(request -> request.getHeader(USER_ID_HEADER))
                        .filter(userId -> userId != null && !userId.isBlank())
                        .or(() -> Optional.of(DEFAULT_AUDITOR));
            } catch (Exception e) {
                return Optional.of(DEFAULT_AUDITOR);
            }
        };
    }
}