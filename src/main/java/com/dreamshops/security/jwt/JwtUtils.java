package com.dreamshops.security.jwt;

import com.dreamshops.security.user.ShopUserDetail;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Data
@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationTime}")
    private long expirationTime;

    public String generateTokenForUser(Authentication authentication) {
        ShopUserDetail userPrincipal = (ShopUserDetail) authentication.getPrincipal();
        log.info("Expiration time: {}", expirationTime);

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")  // Explicitly set the type header
                .setSubject(userPrincipal.getEmail())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token) {
        try {
            String username = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token) // Parse signed JWTs
                    .getBody()
                    .getSubject();
            log.info("Username extracted from token: {}", username);
            return username;
        } catch (JwtException e) {
            log.error("Failed to extract username from token: {}", e.getMessage());
            throw new JwtException("Invalid or expired token");
        }
    }

    public boolean validateToken(String token) {
        log.info("Validating token: {}", token);
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token); // Parse signed JWTs
            log.info("Token is valid. Claims: {}", claimsJws.getBody());
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
            throw new JwtException("Token expired");
        } catch (JwtException e) {
            log.error("Token validation failed: {}", e.getMessage());
            throw new JwtException("Invalid or expired token");
        }
    }
}
