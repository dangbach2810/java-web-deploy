package com.laptrinhjavaweb.config;

import com.laptrinhjavaweb.security.CustomSuccessHandler;
import com.laptrinhjavaweb.service.impl.CustomUserDetailService;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }*/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/admin/building/edit").hasRole("MANAGER")
                        .requestMatchers("/admin/customer/edit").hasRole("MANAGER")
                        .requestMatchers("/admin/user**").hasRole("ADMIN")
                        .requestMatchers("/admin/profile**").authenticated()
                        .requestMatchers("/login", "/resource/**", "/api/**").permitAll()
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/template/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").usernameParameter("j_username").passwordParameter("j_password").permitAll()
                        .loginProcessingUrl("/j_spring_security_check")
                        .successHandler(myAuthenticationSuccessHandler())
                        .failureUrl("/login?incorrectAccount"))

                .logout(logout -> logout.logoutUrl("/logout").deleteCookies("JSESSIONID"))

                .exceptionHandling(e -> e.accessDeniedPage("/access-denied"))

                .sessionManagement(session -> session.maximumSessions(1).expiredUrl("/login?sessionTimeout"));
                http.authenticationProvider(authenticationProvider());
                return http.build();

    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new CustomSuccessHandler();
    }
}
