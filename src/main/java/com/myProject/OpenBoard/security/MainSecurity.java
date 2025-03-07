package com.myProject.OpenBoard.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class MainSecurity {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager
                .setUsersByUsernameQuery("SELECT username, password, active FROM user WHERE username=?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.username, r.role_name " +
                        "FROM user u " +
                        "JOIN user_role ur ON u.id = ur.user_id " +
                        "JOIN role r ON ur.role_id = r.id " +
                        "WHERE u.username=?"
        );

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeHttpRequests(configurer ->
                    configurer
                            .requestMatchers("/", "/images/**", "/login", "/register", "/processSignupForm").permitAll()
                            .requestMatchers(HttpMethod.GET, "/deletePublicPost/**").hasAnyRole("MODERATOR", "ADMIN")
                            .requestMatchers(HttpMethod.GET, "/viewUser/**").hasRole("ADMIN")
                            .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateUser")
                                .defaultSuccessUrl("/home", true)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .clearAuthentication(true)
                                .permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer
                                .accessDeniedPage("/accessDenied")
                )
        ;
        return httpSecurity.build();
    }

}
