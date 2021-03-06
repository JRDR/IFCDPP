package com.ifcdpp.ifcdpp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/profile", "/profile/{id}", "/support", "/review").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/blog/add", "/catalog/add", "/product/edit/{id}", "/support-admin",
                        "/blog/edit/{id}", "/blog/delete/{id}", "/product/delete/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/product/{id}", "/ban/{id}", "/support/delete/{id}", "/review/delete/{id}").hasAuthority("ADMIN")
                .anyRequest().permitAll()
        .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
        .and()
                .logout()
                .permitAll()
        .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select email, password, active from usr where email=?")
                .authoritiesByUsernameQuery("select u.email, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.email=?");
    }
}
