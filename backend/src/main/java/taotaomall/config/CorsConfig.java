package taotaomall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许任何来源
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        //允许任何请求头
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        //允许任何方法
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        //允许凭证
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
