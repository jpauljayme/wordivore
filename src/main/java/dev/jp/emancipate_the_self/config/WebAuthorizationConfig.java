package dev.jp.emancipate_the_self.config;

import dev.jp.emancipate_the_self.constants.Role;
import dev.jp.emancipate_the_self.security.AuthenticationLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class WebAuthorizationConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        //Configure authentication approach
        httpSecurity.httpBasic(Customizer.withDefaults());

        //Configure rules at endpoint level
        httpSecurity
                .addFilterAfter( new AuthenticationLoggingFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(
                c -> c.anyRequest().
                        hasRole(Role.ADMIN.name())
                );

        return httpSecurity.build();
    }
}
