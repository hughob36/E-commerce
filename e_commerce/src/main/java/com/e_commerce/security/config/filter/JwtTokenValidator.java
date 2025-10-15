package com.e_commerce.security.config.filter;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.e_commerce.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;


public class JwtTokenValidator extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtTokenValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

           String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

           if(jwtToken != null) {

              jwtToken = jwtToken.substring(7);

               DecodedJWT decodedJWT = jwtUtil.validateToken(jwtToken);
               String username = jwtUtil.extractUsername(decodedJWT);
               String authorities  =jwtUtil.getEspecificClaim(decodedJWT, "authorities").asString();

               Collection<? extends GrantedAuthority> authoritiesList =
                       AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

               SecurityContext securityContext = SecurityContextHolder.getContext();
               Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,authoritiesList);
               securityContext.setAuthentication(authentication);
               SecurityContextHolder.setContext(securityContext);
           }
        filterChain.doFilter(request,response);
    }
}
