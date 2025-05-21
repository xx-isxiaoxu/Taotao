package taotaomall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 允许所有来源
                .allowedMethods("*")        // 允许所有方法（POST、GET、OPTIONS等）
                .allowedHeaders("*")        // 允许所有请求头
                .allowCredentials(true);    // 允许携带cookie
    }
}
