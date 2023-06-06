package com.project.wine_store.api.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.project.wine_store.model.Client;
import com.project.wine_store.repository.ClientRepository;
import com.project.wine_store.service.VerificationTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private VerificationTokenService verificationTokenService;
    private ClientRepository clientRepository;

    public JWTRequestFilter(VerificationTokenService verificationTokenService, ClientRepository clientRepository) {
        this.verificationTokenService = verificationTokenService;
        this.clientRepository = clientRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");

        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            // eliminam partea cu "Bearer "
            String token = tokenHeader.substring(7);
            try {
                // scoatem username din jwt token
                String username = verificationTokenService.getUsername(token);
                // luam clientul
                Optional<Client> optionalClient = clientRepository.findByUsernameIgnoreCase(username);
                if (optionalClient.isPresent()) {
                    Client client = optionalClient.get();
                    if (client.getIsEmailVerified()) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(client, null, new ArrayList());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (JWTDecodeException e) {
            }
        }
        filterChain.doFilter(request, response);
    }

}
