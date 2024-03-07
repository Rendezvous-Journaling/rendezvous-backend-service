package com.rendezvous.backend.filter;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import com.rendezvous.backend.exceptions.InvalidTokenException;
import com.rendezvous.backend.utilities.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	 @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	            throws ServletException, IOException {

	        final String authorizationHeader = request.getHeader("Authorization");

	        String jwt = null;
	        String username = null;
	        String userId = "";

	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            jwt = authorizationHeader.substring(7);
	            
	            // Validate the JWT token
	            try {
					if (jwtUtil.validateToken(jwt)) {
					    username = jwtUtil.extractUsername(jwt);
					    userId = jwtUtil.extractUserId(jwt);
					    List<SimpleGrantedAuthority> roles = jwtUtil.extractUserRoles(jwt);	                
					    
					    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
								new UsernamePasswordAuthenticationToken(userId, username, roles);
						
						usernamePasswordAuthenticationToken.setDetails(
								new WebAuthenticationDetailsSource().buildDetails(request) );
											
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					    
					} else {
						throw new InvalidTokenException();
					}
				} catch (JsonProcessingException | ParseException | JOSEException | InvalidTokenException e) {
					System.out.println(e.getMessage());
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write(e.getMessage());
					return;
					
				}
	        }

	        // You can now use 'username' and 'userId' in your service logic for access control

	        // Continue with the filter chain
	        filterChain.doFilter(request, response);
	    }

}
