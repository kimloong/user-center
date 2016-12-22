package me.kimloong.uc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

/**
 * Created by closer on 16-8-31.
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
//                .withDefaultSchema()
//                .withUser("user").password(passwordEncoder.encode("password")).roles("USER").and()
//                .withUser("admin").password(passwordEncoder.encode("password")).roles("USER", "ADMIN").and()
                .passwordEncoder(passwordEncoder);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // @formatter:off
        http
                .csrf().disable()
                .anonymous().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and().formLogin()
                .and().httpBasic();
        // @formatter:on
    }
}
