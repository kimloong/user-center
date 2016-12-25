package me.kimloong.uc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security配置
 *
 * @author KimLoong
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
//                .withDefaultSchema()
//                .withUser("user").password(passwordEncoder.encode("password")).roles("USER").and()
//                .withUser("admin").password(passwordEncoder.encode("password")).roles("USER", "ADMIN").and()
                .passwordEncoder(passwordEncoder());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // @formatter:off
        http
//                .csrf().disable()
//                .anonymous().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/users").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().httpBasic();
        // @formatter:on
    }
}
