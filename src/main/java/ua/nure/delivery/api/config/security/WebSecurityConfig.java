package ua.nure.delivery.api.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ua.nure.delivery.api.filter.JWTAuthenticationFilter;
import ua.nure.delivery.api.filter.JWTAuthorizationFilter;
import ua.nure.delivery.service.UserService;

import java.util.Arrays;
import java.util.Collections;

/**
 * Web security configuration class
 */
@Order(1)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;

    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), userService))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userService))
                .authorizeRequests()
                .antMatchers("/api/v1/login").permitAll()
                .antMatchers("/api/v1/users/registration").permitAll()

                .antMatchers(HttpMethod.GET, "/api/v1/users/me").access("hasRole('ADMIN') or hasRole('USER') or hasRole('MODER')")
                .antMatchers(HttpMethod.GET, "/api/v1/users/update").access("hasRole('ADMIN') or hasRole('USER') or hasRole('MODER')")
                .antMatchers(HttpMethod.GET, "/api/v1/users/change-user-status").access("hasRole('ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/v1/users/{*[0-9]}").access("hasRole('ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/v1/users").access("hasRole('ADMIN')")

                .antMatchers(HttpMethod.GET, "/api/v1/orders/create").access("hasRole('ADMIN') or hasRole('USER') or hasRole('MODER')")
                .antMatchers(HttpMethod.GET, "/api/v1/orders/change-delivery-state").access("hasRole('ADMIN') or hasRole('USER') or hasRole('MODER')")
                .antMatchers(HttpMethod.GET, "/api/v1/orders/change-order-state").access("hasRole('ADMIN') or hasRole('USER') or hasRole('MODER')")
                .antMatchers(HttpMethod.GET, "/api/v1/orders/{*[0-9]}").access("hasRole('ADMIN') or hasRole('USER') or hasRole('MODER')")
                .antMatchers(HttpMethod.GET, "/api/v1/orders/all-orders").access("hasRole('ADMIN') or hasRole('USER') or hasRole('MODER')")

                .antMatchers(HttpMethod.GET, "/api/v1/excitements/create").access("hasRole('ADMIN') or hasRole('USER') or hasRole('MODER')")

                .antMatchers(HttpMethod.GET, "/api/v1/comments/create").access("hasRole('ADMIN') or hasRole('USER') or hasRole('MODER')")
                .antMatchers(HttpMethod.GET, "/api/v1/comments").access("hasRole('ADMIN') or hasRole('USER') or hasRole('MODER')")

                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}