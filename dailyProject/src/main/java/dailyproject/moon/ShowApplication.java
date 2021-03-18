package dailyproject.moon;

import dailyproject.moon.common.filterAndInterceptor.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ShowApplication implements WebMvcConfigurer {

    @Autowired
    SessionInterceptor sessionInterceptor;


    public static void main (String[] args) {
        SpringApplication.run(ShowApplication.class, args);
    }


    /**
     * 设置拦截器
     * @param registry
     */
    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }

    //    /**
//     * 设置跨域
//     * @param registry
//     */
//    @Override
//    public void addCorsMappings (CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("*").maxAge(3600);
//
//    }
//
//
    private CorsConfiguration corsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        /* 请求常用的三种配置，*代表允许所有，当时你也可以自定义属性（比如header只能带什么，只能是post方式等等）*/
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }

    /**
     * 解决配置自定义拦截器以后跨域问题带来的坑
     *
     * 原因是客户端请求经过的先后顺序问题，当服务端接收到一个请求时，该请求会先经过过滤器，
     *  然后进入拦截器中，然后再进入Mapping映射中的路径所指向的资源，所以跨域配置在mapping上并不起作用，
     *  返回的头信息中并没有配置的跨域信息，浏览器就会报跨域异常。
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig());
        return new CorsFilter(source);
    }


}
