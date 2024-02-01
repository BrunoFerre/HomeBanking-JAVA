package com.mindhub.brothers.homebanking.configurations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;


@EnableWebSecurity
@Configuration
public class WebAuthorization {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.POST, "/api/clients", "/api/login", "/api/logout", "/api/payments").permitAll()
                                .requestMatchers("/web/public/**").permitAll()
                                .requestMatchers("/web/index.html", "/web/login.html", "/web/register.html", "/web/images/**", "/web/posnet.html", "/web/posnet.js").permitAll()
                                .requestMatchers("/web/assets/styles/**", "/web/assets/images/**").permitAll()
                                .requestMatchers("/web/login.js", "/web/register.js").permitAll()
                                .requestMatchers("/api/clients").hasAuthority("ADMIN")
                                .requestMatchers("/rest/**", "/h2-console/**").hasAuthority("ADMIN")
                                .requestMatchers("/web/adminPages/**").hasAuthority("ADMIN")
                                .requestMatchers("/web/adminPages/manager.js").hasAuthority("ADMIN")
                                .requestMatchers("/web/adminPages/manager.html").hasAuthority("ADMIN")
                                .requestMatchers("/web/adminPages/bank.png").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/loans/create").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/clients/current", "/api/clients/current/**", "/api/clients/current/cards", "/api/cards", "/api/loans", "/api/clients/current/loans",
                                        "/web/**", "/api/transactions/findDate").hasAuthority("CLIENT")
                                .requestMatchers("/api/clients/accounts/{id}").hasAuthority("CLIENT")
                                .requestMatchers(HttpMethod.POST, "/api/clients/current/accounts", "/api/clients/current/cards", "/api/transactions", "/api/loans").hasAuthority("CLIENT")
                                .requestMatchers(HttpMethod.PUT, "/api/clients/current/accounts/{id}", "/api/clients/current/cards").hasAuthority("CLIENT")
                                .anyRequest().denyAll()).csrf().disable()
                .formLogin(cfg -> cfg.usernameParameter("email").passwordParameter("password").loginPage("/api/login"));
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