package com.rendezvous.backend.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
	        Long userId = null;

	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            jwt = authorizationHeader.substring(7);
	            
	          

	            // Validate the JWT token
	            if (jwtUtil.validateToken(jwt)) {
	                username = jwtUtil.extractUsername(jwt);
	                userId = jwtUtil.extractUserId(jwt);
	                List<SimpleGrantedAuthority> roles = jwtUtil.extractUserRoles(jwt);
	                System.out.println(jwtUtil.extractAllClaims(jwt));
	                System.out.println("Roles: " + roles);
	                
	                
	                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
							new UsernamePasswordAuthenticationToken(userId, username, roles);
					
					usernamePasswordAuthenticationToken.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request) );
					
					System.out.println(usernamePasswordAuthenticationToken);
					
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	                
	            }
	        }

	        // You can now use 'username' and 'userId' in your service logic for access control

	        // Continue with the filter chain
	        filterChain.doFilter(request, response);
	    }

}
