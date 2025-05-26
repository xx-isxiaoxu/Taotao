package taotaomall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JWTInterceptors jwtInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptors)
                .addPathPatterns("/**")//所有接口进行拦截
                .excludePathPatterns(
                    "/user/login",
                    "/user/register",
                    "/img/**",      // 放行图片
                    "/Images/**",   // 放行大写
                    "/images/**",   // 放行小写
                    "/product/**",
                    "/api/goods/**",
                    "/api/goodsDetail/**"
                );
    }

    @Value("${file.save-path}")
    String filePath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + filePath);

        registry.addResourceHandler("/product/**")
                .addResourceLocations("file:E:/ADZY/Taotao/backend/src/main/resources/static/product/");

        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
