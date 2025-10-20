package com.abanamoses.xpense.service;

import com.abanamoses.xpense.entities.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    // Use a BASE64 encoded key (at least 256 bits for HS256)
    private final String secretKey = "d67ceb7c5ae30d16bffcf56af6bad58d0cf82c3d76be9131032f42aa6082b7911afc5dec9866410b2e404ae7d801c876";

    public String generateToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());


        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hrs
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        // If secretKey is HEX: use javax.xml.bind.DatatypeConverter.parseHexBinary(secretKey)
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token); // verifies signature & expiration
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
