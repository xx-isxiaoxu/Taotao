package taotaomall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Webconfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptors())
                .addPathPatterns("/**")//所有接口进行拦截
                .excludePathPatterns("/user/login","/user/register")//放行登录注册
                .addPathPatterns("/img/**");//放行图片

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
                
        registry.addResourceHandler("/product/**")
                .addResourceLocations("file:" + filePath)
                .setCachePeriod(3600)
                .resourceChain(true);
                
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
