package edu.monash.monplan.config;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "https://monplan-dev.appspot.com")
    private String apiAudience;
    @Value(value = "https://monplan.au.auth0.com/")
    private String issuer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtWebSecurityConfigurer
                .forRS256(apiAudience, issuer)
                .configure(http)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/units/").permitAll()
                .antMatchers(HttpMethod.GET, "/photos/**").hasAuthority("read:photos")
                .antMatchers(HttpMethod.POST, "/photos/**").hasAuthority("create:photos")
                .antMatchers(HttpMethod.PUT, "/photos/**").hasAuthority("update:photos")
                .antMatchers(HttpMethod.DELETE, "/photos/**").hasAuthority("delete:photos")
                .anyRequest().authenticated();
    }
}
