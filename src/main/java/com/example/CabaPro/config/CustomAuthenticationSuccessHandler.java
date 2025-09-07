package com.example.CabaPro.config;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response,
                          Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))) {
            return "/superAdmin";
        }
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "/admin";
        }
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ARBITRO"))) {
            return "/arbitro";
        }

        // fallback
        return "/";
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        if (request == null) return;
        var session = request.getSession(false);
        if (session == null) return;
        session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
    }
}
