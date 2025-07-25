package com.tours.backend.Configuration;

import com.tours.backend.Service.CustomUserDetailsService;
import com.tours.backend.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext applicationContext;

    private static final Set<String> blacklistedTokens = Collections.synchronizedSet(new HashSet<>());

    public static void addToBlackList(String token){
        blacklistedTokens.add(token);
    }

    private boolean isTokenBlacklisted(String token){return blacklistedTokens.contains(token);}

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);

            if(isTokenBlacklisted(token)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has been expired. Please login again");
                return;
            }

            try{
                userName = jwtService.extractUserName(token);
            }catch(Exception e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            try{
                UserDetails userDetails = applicationContext.getBean(CustomUserDetailsService.class).loadUserByUserName(userName);
                if(jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else{
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } catch (Exception e) {
                    throw new RuntimeException(e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
