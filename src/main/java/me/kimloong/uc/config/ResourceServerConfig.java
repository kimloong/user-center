package me.kimloong.uc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by closer on 16-8-20.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "user_resource";

    @Override
    public void configure(HttpSecurity http) throws Exception {

        // @formatter:off
        http
                .anonymous().disable()
                //下面这句很重要，将资源请求在/users/**下，从而不会影响到WebSecurityConfig
                .requestMatchers().antMatchers("/users/**")
                .and().authorizeRequests().antMatchers("/users/**").access("#oauth2.hasScope('read')");
        // @formatter:on
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID);
    }
}
