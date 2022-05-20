package com.smd21.smdinsole.app.security;

import com.smd21.smdinsole.app.security.filter.JwtAuthFilter;
import com.smd21.smdinsole.app.security.handler.AuthenticationAccessDeniedHanler;
import com.smd21.smdinsole.app.security.handler.RestAuthenticationEntryPoint;
import com.smd21.smdinsole.app.security.provider.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public static final String HEADER_TOKEN = "X-Auth-Token";
    public static final String SECRET_KEY = "smdinsole@smd21";

    private static AuthenticationAccessDeniedHanler accessDeniedHandler;

    @Autowired
    public void setAuthenticationAccessDeniedHanler(AuthenticationAccessDeniedHanler handler) {
        SecurityConfig.accessDeniedHandler = handler;
    }

    private static RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint handler) {
        SecurityConfig.restAuthenticationEntryPoint = (RestAuthenticationEntryPoint) handler;
    }


    // Api service
    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        JwtTokenProvider jwtTokenProvider;

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
                    .antMatchers(HttpMethod.OPTIONS, "/*/api/**")
                    .antMatchers("/static/**")
                    .antMatchers("/error")
                    .antMatchers("/*/api/guard/get/token")
                    .antMatchers("/*/api/admin/get/token")
                    .antMatchers("/*/api/shoes/loc/ins")
                    .antMatchers("/*/api/check/alive");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .cors().and()
                .antMatcher("/*/api/**")

                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/*/api/guard/**", "/*/api/shoes/**").access("hasAnyRole('ROLE_ACCESS')")
                .antMatchers("/*/api/admin/**").access("hasAnyRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint);		// 인증오류
    //					.accessDeniedHandler(accessDeniedHandler);					// 접근 제한
        }
    }

    /*
    // Front service
    @Configuration
    @Order(2)
    public static class UserWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private ServiceAuthenticationProvider authProvider;

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
                    .antMatchers("/static/**")
                    .antMatchers("/error");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .csrf().disable()
                    .cors().and()
                    .antMatcher("/**")
                    .addFilterBefore(new AuthFilter(this.authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(new ViewFilter(), UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()
                    .authorizeRequests()
                    .antMatchers(
                            "/favicon.ico"
                    ).permitAll()
                    .antMatchers("/error/**").permitAll()
                    .antMatchers("/**").permitAll() //.access("hasAnyRole('ROLE_ACCESS')")
                    .anyRequest().authenticated()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(restAuthenticationEntryPoint)		// 인증오류
                    .accessDeniedHandler(accessDeniedHandler)					// 접근 제한
            ;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authProvider);
        }

    }

     */
}
