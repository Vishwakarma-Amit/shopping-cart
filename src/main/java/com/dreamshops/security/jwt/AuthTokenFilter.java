package com.dreamshops.security.jwt;


import com.dreamshops.security.user.ShopUserDetailService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ShopUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = parseJwtToken(request);
        if (StringUtils.hasText(jwtToken)) {
            try {
                log.info("doFilterInternal - jwtToken: {}", jwtToken);

                // Validate the token
                if (jwtUtils.validateToken(jwtToken)) {
                    // Extract username from token
                    String username = jwtUtils.getUsernameFromToken(jwtToken);
                    log.info("Extracted username: {}", username);

                    // Load user details and set authentication
                    UserDetails userDetails = userDetailService.loadUserByUsername(username);
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException e) {
                log.error("JWT validation failed: {}", e.getMessage());
                handleErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return; // Exit filter chain on error
            } catch (Exception e) {
                log.error("Unexpected error in AuthTokenFilter: {}", e.getMessage());
                handleErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong!");
                return; // Exit filter chain on error
            }
        }

        // Continue filter chain if no errors
        filterChain.doFilter(request, response);
    }

    private void handleErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(status);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + message + "\"}");
        }
    }

    private String parseJwtToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        log.warn("No valid Authorization header found");
        return null;
    }
}
