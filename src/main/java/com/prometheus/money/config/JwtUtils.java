package com.prometheus.money.config;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {
    // Use a 256-bit key for the HS256 algorithm
    //private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generates a secure key for HS256
    private final SecretKey key;
    public JwtUtils() {
        // 从配置文件或环境变量中读取密钥
        String base64Key = "12ywiFwmkUwYrRIrisf3Pds4PyZC71DJ5fC36eZst3M="; // 替换成你的Base64密钥
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }
    
    // Generate a JWT token with expiration time
    public String generateToken(String username) {
    	System.out.println(key);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token valid for 10 hours
                .signWith(key) // Use the secure key here
                .compact();
    }

    // Extract username from the token
    public String extractUsername(String token) throws Exception {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration time from the token
    public Date extractExpiration(String token) throws Exception {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract specific claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws Exception {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims
    private Claims extractAllClaims(String token) throws Exception {
    	try {
    		 String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
    	        System.out.println("Base64 Encoded Key: " + base64Key);
    	        
        return Jwts.parserBuilder()
                .setSigningKey(key) // Use the secure key for parsing the token
                .build()
                .parseClaimsJws(token)
                .getBody();
        }catch (SignatureException e){
        	throw new SignatureException(token);
        }
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) throws Exception {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token (check if it's valid and matches the user)
    public boolean validateToken(String token, String username) throws Exception {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}
