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
                    "/Images/**",   // 如果你有这些静态资源
                    "/images/**",   // 新增小写
                    "/product/**"
                );
    }

    @Value("${file.save-path}")
    String filePath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //其中img表示访问的前缀。"file:"是文件真实的存储路径
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:" + filePath)
                .setCachePeriod(3600)
                .resourceChain(true);
                
        //增加/Images/** 和 /product/** 的映射
        registry.addResourceHandler("/Images/**")
                .addResourceLocations("file:" + filePath)
                .setCachePeriod(3600)
                .resourceChain(true);
                
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + filePath)
                .setCachePeriod(3600)
                .resourceChain(true);
                
        registry.addResourceHandler("/product/**")
                .addResourceLocations("file:" + filePath)
                .setCachePeriod(3600)
                .resourceChain(true);
                
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
