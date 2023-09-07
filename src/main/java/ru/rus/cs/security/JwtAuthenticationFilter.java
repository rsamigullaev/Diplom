package ru.rus.cs.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.rus.cs.service.UserService;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtAuthenticationFilter(final UserService userService, final JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(
            @NotNull final HttpServletRequest request,
            @NotNull final HttpServletResponse response,
            @NotNull final FilterChain filterChain
    ) throws ServletException, IOException {
        final var authHeader = request.getHeader("auth-token");

        String username = null;
        String jwtToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            try {
                username = jwtTokenUtil.extractUsername(jwtToken);
            } catch (IllegalArgumentException e) {
                log.error("Unable to get Token");
            } catch (ExpiredJwtException e) {
                log.error("Token has expired");
            }
        } else {
            log.warn("Token does not begin with 'Bearer'");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final var userDetails = this.userService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                final var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}