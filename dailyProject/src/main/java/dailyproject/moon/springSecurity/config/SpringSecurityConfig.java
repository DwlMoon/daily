package dailyproject.moon.springSecurity.config;

import dailyproject.moon.springSecurity.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @program: moon
 * @description: SpringSecurity配置文件(实现数据库存储和自定义登录页面)
 * @create: 2020-12-24 17:18
 **/

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {



    @Autowired
    CustomUserDetailsService securityService;

    @Override
    protected void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService).passwordEncoder(passwordEncoder());
    }

//    @Override
//    protected void configure (HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .formLogin().loginPage("/templates/haha.html").loginProcessingUrl("/authentication/form").failureForwardUrl("/SecurityController/fail")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/SecurityController/insertUserData","/SecurityController/fail").permitAll()
//                .antMatchers("/templates/**").permitAll()
//                .antMatchers("/oauth/**").permitAll()
//                .anyRequest().authenticated()
//
//                ;
//    }


    /**
     * 放过所有请求
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure (HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

}
