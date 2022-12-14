package com.subteno.vuo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        private final UserDetailsService userDetailsService;

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(userDetailsService);

                AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
                return http
                                .authorizeRequests()
                                .mvcMatchers("/api/getitem", "/api/addorder")
                                .permitAll()
                                .mvcMatchers(HttpMethod.GET, "/additem", "/")
                                .hasAnyAuthority("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN")
                                .mvcMatchers(HttpMethod.POST, "/api/additem")
                                .hasAnyAuthority("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN")

                                .mvcMatchers(HttpMethod.POST, "/api/add", "/api/get", "/api/delete")
                                .hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                                .mvcMatchers(HttpMethod.GET, "/add")
                                .hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")

                                .mvcMatchers("/api/assignrole", "/assignrole")
                                .hasAuthority("ROLE_ADMIN")

                                .and()
                                .authenticationManager(authenticationManager)
                                .formLogin()
                                .and()
                                .build();
        }

}
