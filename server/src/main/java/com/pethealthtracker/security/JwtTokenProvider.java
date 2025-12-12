package com.pethealthtracker.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    public String generateToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        String email;
        Long id = 0L; // ID por defecto para usuarios de Google (temporal)
        Boolean emailVerified = false;
        List<String> roles = null;

        // Caso 1: Usuario logueado con Email/Password (Tu DB)
        if (principal instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) principal;
            email = userPrincipal.getEmail();
            id = userPrincipal.getId();
            emailVerified = userPrincipal.getEmailVerified();
            // Obtener roles del UserPrincipal
            roles = userPrincipal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }
        // Caso 2: Usuario logueado con Google (OAuth2)
        else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            email = oidcUser.getEmail();
            emailVerified = true; // Google siempre verifica el email
            // Por defecto, los usuarios de OAuth2 obtienen ROLE_USER
            roles = List.of("ROLE_USER");
        }
        // Fallback
        else {
            email = principal.toString();
            roles = List.of("ROLE_USER"); // Rol por defecto
        }

        return Jwts.builder()
                .subject(email)
                .claim("id", id)
                .claim("email", email)
                .claim("emailVerified", emailVerified)
                .claim("roles", roles) // Añadimos los roles al token
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Primero intentamos obtener el ID del claim "id"
        Long id = claims.get("id", Long.class);
        if (id != null) {
            return id;
        }

        // Si no está en el claim "id", intentamos con el subject
        try {
            return Long.parseLong(claims.getSubject());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Obtener roles del token
        List<String> roles = claims.get("roles", List.class);
        return roles != null ? roles : List.of("ROLE_USER"); // Rol por defecto si no hay roles
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SecurityException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }
}