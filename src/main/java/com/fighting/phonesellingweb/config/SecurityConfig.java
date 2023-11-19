package com.fighting.phonesellingweb.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private AuthenticationProvider authenticationProvider;
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // disable csrf
        http.csrf(AbstractHttpConfigurer::disable);
        // add custom filter
        // authenticationProvider provide :
        // 1.UserDetailsService to load user by username
        // 2.PasswordEncoder to check if password is correct compare to the password in database
        http.authenticationProvider(authenticationProvider);
        // authorize requests
        http.authorizeHttpRequests(auth -> auth
                // allow access to authentication page
                .requestMatchers("account/login").permitAll()
                .requestMatchers("account/register").permitAll()
                // allow access to admin page
                .requestMatchers("admin").hasRole("ADMIN")

                // any request must be authenticated
                .anyRequest().authenticated());

        http.formLogin(form -> form
                // set login url
                .loginPage("/account/login")
                // form parameter name for email
                .usernameParameter("email")
                // form parameter name for password
                .passwordParameter("password")
                // add cookie after login success and redirect to home page after login success
                .successHandler(authenticationSuccessHandler)
                // we don't use default login page of http config because it will redirect to specific url before the cookie is added
        );

        http.logout(logout -> logout
                // set logout url
                .logoutUrl("account/logout")
                // delete cookie after logout
                .deleteCookies("email")
                // redirect to home page after logout success
                .logoutSuccessUrl("/")
        );

        // set session creation policy to IF_REQUIRED
        // only create session if the user not login yet
        // however, if the user is logged in, then Spring Security will use the existing session to store their authentication information
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );

        return http.build();
    }

}

