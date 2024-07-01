package com.sass.business.others;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class AuthUtil {
    // region VARIABLES

    @Value("${auth.jwt.secret}")
    private String secret;

    @Value("${auth.expiration:1}")
    private int expiration;

    // endregion

    // region AUTH METHODS

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000L * 24 * expiration))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)), SignatureAlgorithm.HS256)
                .compact();
    }

    public ResponseCookie generateCookie(String cookieName, String token) {
        return ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .path("/")
                .maxAge(3600L * 24 * expiration)
                .sameSite("None")
                .build();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String email;

        if (userDetails == null) {
            return false;
        }

        email = extractClaim(token, "email");
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // endregion

    // region JWT EXTRACTION METHODS

    public String extractClaim(String token, String key) {
        return extractClaims(token).get(key).toString();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // endregion
}
