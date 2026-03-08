package com.TaskTracker.Todo.List.RateLimiting;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class Bucket4JRateLimitApp implements WebMvcConfigurer {
    private final RateLimitInterceptor rateLimitInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("Interceptor triggered");
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/todos/get/**");
    }
}
