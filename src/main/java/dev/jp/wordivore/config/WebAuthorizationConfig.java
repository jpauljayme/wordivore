package dev.jp.wordivore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

    @Configuration
    @RequiredArgsConstructor
    public class WebAuthorizationConfig {

        private final AuthenticationSuccessHandler authenticationSuccessHandler;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

            httpSecurity.authenticationProvider(authenticationProvider);

            httpSecurity
                    .sessionManagement( sessionManagement -> sessionManagement
                            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                            .maximumSessions(1)
                    )
                    .authorizeHttpRequests(
                    auth -> auth
                            //static assets
                            .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/fonts/**").permitAll()
                            //login page
                            .requestMatchers("/", "/login", "/logout").permitAll()
                            //everything else
                            .anyRequest().authenticated()
                    )
                    .formLogin(form -> form
                            .loginPage("/login")
                            .successHandler(authenticationSuccessHandler)
                            .permitAll()
                    ).logout(logout -> logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/")
                            .invalidateHttpSession(true)
                            .deleteCookies("JESSIONID")
                    )

            ;

            return httpSecurity.build();
        }
    }
