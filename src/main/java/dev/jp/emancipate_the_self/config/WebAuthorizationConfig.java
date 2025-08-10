package dev.jp.emancipate_the_self.config;

import dev.jp.emancipate_the_self.constants.Role;
import dev.jp.emancipate_the_self.security.AuthenticationLoggingFilter;
import dev.jp.emancipate_the_self.security.MySimpleUrlAuthenticationSuccessHandler;
import dev.jp.emancipate_the_self.security.RequestValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class WebAuthorizationConfig {


    @Bean
    public AuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .addFilterAfter( new AuthenticationLoggingFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(
                c -> c.anyRequest().
                        authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(mySimpleUrlAuthenticationSuccessHandler())
                        .permitAll()
                )
        ;

        return httpSecurity.build();
    }
}
