package com.example.config;

import com.example.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    public static String[] AUTH_WHITELIST = {"/api/v1/auth/**",
            "/api/v1/news/**",
            "/api/v1/region/lang",
            "/api/v1/attach/**",
            "/api/v1/article/public/*"};

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        // authentication (login,password)
//        String password = UUID.randomUUID().toString();
//        System.out.println("User Password mazgi: " + password);
//
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{noop}" + password)
//                .roles("U SER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{noop}" + password)
//                .roles("ADMIN")
//                .build();
//
//        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(user, admin));
//        return authenticationProvider;
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // authentication (login,password)
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization (ROLE)
        http
                .authorizeHttpRequests((c) -> {
                    c.requestMatchers("/api/v1/auth/**").permitAll()
                            .requestMatchers("/api/v1/news/**").permitAll()
                            .requestMatchers("/api/v1/region/admin", "/api/v1/region/admin/**").hasRole("ADMIN")
                            .anyRequest().authenticated();
                }).
                httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);
        return http.build();
    }

    private PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return MD5Util.encode(rawPassword.toString()).equals(encodedPassword);
            }
        };
    }
}
