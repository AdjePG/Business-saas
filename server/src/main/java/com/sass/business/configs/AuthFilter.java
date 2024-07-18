package com.sass.business.configs;

import com.sass.business.models.User;
import com.sass.business.others.AuthUtil;
import com.sass.business.others.UuidConverterUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class AuthFilter extends OncePerRequestFilter {
    // region INJECTED DEPENDENCIES

    private final AuthUtil authUtil;
    private final UserDetailsService userDetailsService;

    public AuthFilter(
            AuthUtil authUtil,
            UserDetailsService userDetailsService) {
        this.authUtil = authUtil;
        this.userDetailsService = userDetailsService;
    }

    // endregion

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerAuth;
        String jwt;
        String email;

        headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //jwt = getTokenFromCookie(request);
        jwt = headerAuth.substring(7);

        if (!jwt.equals("null") && SecurityContextHolder.getContext().getAuthentication() == null) {
            email = authUtil.extractClaim(jwt, "email");

            if (email != null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

                if (authUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }


        filterChain.doFilter(request, response);
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Cookie" + cookie);
                if (cookie.getName().equals("user-auth")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
