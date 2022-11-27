package com.example.demo.security;

import com.example.demo.filter.CustomAuthenticationFilter;
import com.example.demo.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //Order is important
        http.authorizeRequests().antMatchers(GET, "/api/v1/login/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/api/v1/user/save").permitAll();

        http.authorizeRequests().antMatchers( POST,"/api/v1/user/save/admin").hasAnyAuthority( "ROLE_ADMIN");
        http.authorizeRequests().antMatchers( "/api/v1/user/").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/v1/users/").hasAnyAuthority("ADMIN_USER");
        http.authorizeRequests().antMatchers( POST,"/api/v1/role/save").hasAnyAuthority( "ROLE_ADMIN");

        http.authorizeRequests().antMatchers( "/api/v1/reservations/").hasAnyAuthority( "ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers( "/api/v1/reservation/").hasAnyAuthority( "ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers( "/api/v1/room/{name}").hasAnyAuthority( "ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers( "/api/v1/rooms/get").hasAnyAuthority( "ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers( "/api/v1/room_reservations/{name}").hasAnyAuthority( "ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers( "/api/v1/room/").hasAnyAuthority(  "ROLE_ADMIN");
        http.authorizeRequests().antMatchers( "/api/v1/room/").hasAnyAuthority(  "ROLE_ADMIN");
//        http.authorizeRequests().anyRequest().authenticated();
        http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.httpBasic();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
