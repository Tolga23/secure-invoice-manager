package com.thardal.secureinvoicemanager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security Section will configure later.
    // For now, we will use in-memory authentication and permit all requests.
    // For 403 error, we will disable csrf. (Cross Site Request Forgery)
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("root")
                .password(passwordEncoder().encode("root"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest()
//                        .permitAll())
//                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC_URLS).permitAll()
                .antMatchers(HttpMethod.DELETE, "/user/delete/**").hasAnyAuthority("DELETE:USER")
                .antMatchers(HttpMethod.DELETE, "/customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(null)
                .authenticationEntryPoint(null);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(null);
        authProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authProvider);
    }
}