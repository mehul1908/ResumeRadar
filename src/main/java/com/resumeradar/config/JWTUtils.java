package com.resumeradar.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtils 
{
	private String SECRET_KEY = "f7b1f947bda24255a48671cf9e8d0847f7b1f947bda24255a";
	
	 public String generateToken(String userid) 
	 {
	        Map<String, Object> claims = new HashMap<>();
	        claims.put("userid", userid);
	        return createToken(claims, "loginuser");
	 }

	 private String createToken(Map<String, Object> claims, String subject) 
	 {
		 Date createDate = new Date(System.currentTimeMillis());
		 Date expireDate = new Date(System.currentTimeMillis() + 1000*60*100);
		 
//	        return Jwts.builder()
//	                .setClaims(claims)
//	                .setSubject(subject)
//	                .setIssuedAt(createDate)
//	                .setExpiration(expireDate) // 10 minute validity
//	                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//	                .compact();

		 
		 SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		 
	        return Jwts.builder()
	                .setClaims(claims)
	                .setSubject(subject)
	                .setIssuedAt(createDate)
	                .setExpiration(expireDate) // 10 minute validity
	                .signWith(key)
	                .compact();
	 }
	 
	 public String extractUserID(String token) {
	    	final Claims claims = extractAllClaims(token);
	        String id = claims.get("userid", String.class);
	        return id;
	    }
	 
	 public Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	 }
	 
	 private Date extractExpiration(String token) {
	    	final Claims claims = extractAllClaims(token);
	    	return claims.getExpiration();
	 }
	 
	 private Claims extractAllClaims(String token) {
//	        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		 SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

		    return Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token)
		            .getBody();
	 }
}

