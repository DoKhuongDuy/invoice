package com.viettel.bccs3.config;

import com.viettel.bccs3.config.accesstoken.ActionUserHolder;
import com.viettel.bccs3.domain.dto.ActionUserDTO;
import com.viettel.core.feign.ClientFeignConfiguration;
import com.viettel.core.monitoring.EnableTimedCommand;
import com.viettel.core.security.EnableSecurityService;
import com.viettel.core.trace.TraceInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Configuration
@Aspect
@EnableSecurityService
@EnableFeignClients("com.viettel.bccs3.service")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Import({ClientFeignConfiguration.class})
@EnableTimedCommand
public class ServiceConfiguration {

    @Pointcut("execution(public * com.viettel.bccs3.service..*.*(..)) " +
            "|| execution(public * com.viettel.bccs3.repository..*.*(..))")
    public void trace() {
        //nothing
    }

    @Bean
    public TraceInterceptor traceInterceptor() {
        return new TraceInterceptor();
    }

    @Bean
    public Advisor traceAdvisor() {
        var pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.viettel.bccs3.config.ServiceConfiguration.trace()");
        return new DefaultPointcutAdvisor(pointcut, traceInterceptor());
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> ofNullable(ActionUserHolder.getActionUser())
                .map(ActionUserDTO::getStaffCode)
                .or(() -> of(""));
    }
}
