//package com.example.demo.Filter;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//
//@Configuration
//public class FilterConfig implements WebMvcConfigurer {
//
//    @Autowired
//    DashboardFilter dashboardFilter;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new DashboardFilter()).addPathPatterns("/Dashboard/*/**");
//        WebMvcConfigurer.super.addInterceptors(registry);
//    }
//
//}
