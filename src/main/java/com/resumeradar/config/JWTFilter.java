package com.resumeradar.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.resumeradar.entity.User;
import com.resumeradar.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter 
{
	@Autowired
	private JWTUtils jwtutils;

	@Autowired
	private UserService userService;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException 
	{
//		String url = request.getRequestURI();
//		System.out.println(url);
//		return url.contains("/auth") || 
//				url.contains("/swagger-ui") ||
//				url.contains("/v3/api-docs") || 
//				url.contains("/apidocs.html");
		return true;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{
		final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        	response.setStatus(403);
        	response.getWriter().write("Token Not Found !");
        	return;
        }
        
        jwt = authHeader.substring(7);
        
        boolean expiryStatus = jwtutils.isTokenExpired(jwt);
        if(expiryStatus)
        {
        	response.setStatus(403);
        	response.getWriter().write("Token Expire !");
        }else 
        {
        	String userid = jwtutils.extractUserID(jwt);
            Optional<User> op = userService.getUserById(userid);
																				
            if(op.isPresent())
            {
            	User user = op.get();
	            UsernamePasswordAuthenticationToken authToken =
	                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	            SecurityContextHolder.getContext().setAuthentication(authToken);
	            filterChain.doFilter(request, response);
            }else {
            	response.setStatus(403);
            	response.getWriter().write("Wrong Token !");
            }
        }
		
	}
}
