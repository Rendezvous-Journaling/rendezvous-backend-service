package com.rendezvous.backend.utilities;

import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.annotation.PostConstruct;

@Service
public class JwtUtil {
	
	@Value("${aws.cognito.region}")
	private String cognitoRegion;

	@Value("${aws.cognito.user.pool.id}")
	private String cognitoUserPoolId;

	private RSAPublicKey publicKey;
	
	@PostConstruct
	private void initialize() {
		this.publicKey = getPublicKey();
	}
	
	private RSAPublicKey getPublicKey() {
		
		try {
			String cognitoJwksUrl = String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", cognitoRegion, cognitoUserPoolId);
			JWKSet jwkSet = JWKSet.load(new URL(cognitoJwksUrl));
			List<JWK> jwkList = jwkSet.getKeys();
			RSAKey rsaKey = (RSAKey) jwkList.get(0);
			return rsaKey.toRSAPublicKey();
			
		} catch (IOException | ParseException | JOSEException e) {
			throw new RuntimeException("Failed to get RSA public key from Cognito");		
		}
	}
	
	// get the username for this token
	public String extractUsername(String token) throws ParseException, JOSEException {
		JWTClaimsSet claimsSet = extractAllClaims(token);
		return claimsSet.getStringClaim("cognito:username");
	}
	
	public String extractUserId(String token) throws ParseException, JOSEException {
		JWTClaimsSet claimsSet = extractAllClaims(token);
		System.out.println("User ID: " + claimsSet.getStringClaim("sub"));
		return claimsSet.getStringClaim("sub");
	}
	
	public List<SimpleGrantedAuthority> extractUserRoles(String token) throws JsonMappingException, JsonProcessingException, ParseException, JOSEException{
		JWTClaimsSet claims = extractAllClaims(token);
		
		// Extract the roles claim as JSON string
//		String rolesJson = claims.getStringClaim("roles");
		
		// ObjectMapper created for deserialization
//		ObjectMapper objectMapper = new ObjectMapper();
	
		// Deserialize the JSON string into a List of SimpleGrantedAuthority
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		
//		try {
//			JsonNode rolesArray = objectMapper.readTree(rolesJson);
//			
//			for(JsonNode roleObject : rolesArray) {
//				String authority = roleObject.get("authority").asText();
//				roles.add(new SimpleGrantedAuthority(authority));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		roles.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		return roles;
	}
	
	
	// get expiration date for this token
	public Date extractExpiration(String token) throws ParseException, JOSEException {
		
		// :: -> method reference
		//    -> pointer, access a method in a class
		//	  -> that way we can pass a method as an argument
		
		JWTClaimsSet claimsSet = extractAllClaims(token);
		return claimsSet.getExpirationTime();
	}
	
	
	// takes a token and a claims resolver to find out what the claims are for that particular token
	// so find that data that was passed in through the token and be able to access it again (username, expiration date)
	public <T> T extractClaim(String token, Function<JWTClaimsSet, T> claimsResolver ) throws ParseException, JOSEException {
		final JWTClaimsSet claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	// Access modifier is originally private. It is public for testing purposes.
	public JWTClaimsSet extractAllClaims(String token) throws ParseException, JOSEException {

		SignedJWT signedJWT = SignedJWT.parse(token);
		JWSVerifier verifier = new RSASSAVerifier(publicKey);
		if(!signedJWT.verify(verifier)) {
			throw new RuntimeException("JWT signature verification failed");
		}
		return signedJWT.getJWTClaimsSet();
	}
	
	// checks if the token has expired yet by checking the current date & time and comparing it to the expiration
	private Boolean isTokenExpired(String token) throws ParseException, JOSEException {
		return extractExpiration(token).before(new Date());
	}

	// will validate the token and check if the current token is for the right user requesting it and that the token isn't expired
	public Boolean validateToken(String token) throws ParseException, JOSEException {
		
		final String username = extractUsername(token);
		return (!isTokenExpired(token) );
	}
}


