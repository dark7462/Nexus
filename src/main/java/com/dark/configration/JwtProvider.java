package com.dark.configration;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtProvider {
	
	private static final SecretKey key = Keys.hmacShaKeyFor(jwtConstant.SECRET_KEY.getBytes());
	
	public static String generateToken(Authentication auth) {
		String jwt = Jwts.builder()
				.issuer("Dark")
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 99999999)) 
				.claim("email", auth.getName())
				.signWith(key)
				.compact();
		return jwt;
	}
	public static String getEmailFromJwtToken(String jwt) {
		jwt = jwt.substring(7);
		Claims claims = Jwts.parser()
			    .verifyWith(key)           // Replaces setSigningKey
			    .build()
			    .parseSignedClaims(jwt)    // Replaces parseClaimsJws
			    .getPayload();
		String email = String.valueOf(claims.get("email"));
		
		return email;
	}
	
}