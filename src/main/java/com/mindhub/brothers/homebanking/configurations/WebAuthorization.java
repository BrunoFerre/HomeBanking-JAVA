package com.mindhub.brothers.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
         .antMatchers(HttpMethod.POST,"/api/clients","/api/login","/api/logout").permitAll()
                        .antMatchers("/web/index.html","/web/login.html","/web/register.html","/web/images/**").permitAll()
                        .antMatchers("/web/assets/styles/**","/web/assets/images/**").permitAll()
                        .antMatchers("/web/login.js","/web/register.js").permitAll()
                        .antMatchers(HttpMethod.GET,"/api/clients/current/**","/api/cards",
                        "/web/**").hasAuthority("CLIENT")
                        .antMatchers("/api/clients/accounts/{id}").hasAuthority("CLIENT")
                        .antMatchers(HttpMethod.POST,"/api/clients/current/accounts",
                                "/api/clients/current/cards",
                                "/api/transactions").hasAuthority("CLIENT")
                        .antMatchers("/rest/**","/h2-console/**").hasAnyAuthority("ADMIN")
                        .antMatchers("/web/adminPages/manager.html").hasAnyAuthority("ADMIN")
                        .antMatchers("/web/adminPages/manager.js").hasAnyAuthority("ADMIN")
                        .antMatchers("/web/adminPages/**").hasAnyAuthority("ADMIN")
                        .antMatchers("/api/clients").hasAuthority("ADMIN")

                        .anyRequest().denyAll();

        http.formLogin().usernameParameter("email").passwordParameter("password").loginPage("/api/login");

        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        http.csrf().disable();

        http.headers().frameOptions().disable();

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}