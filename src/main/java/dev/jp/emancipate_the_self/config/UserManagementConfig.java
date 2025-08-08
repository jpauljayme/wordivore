package dev.jp.emancipate_the_self.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

    @Configuration
    public class UserManagementConfig {

        @Bean
        UserDetailsService userDetailsService(DataSource dataSource){
            return new JdbcUserDetailsManager(dataSource);
        }

        @Bean
        PasswordEncoder passwordEncoder(){
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
    }
