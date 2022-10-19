package io.zetch.app.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(logInterceptor()).addPathPatterns("/**");
    }
}
