package com.si.broker.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.si.broker.api.v1.model.ProviderDTO;
import com.si.broker.api.v1.model.UserDTO;
import com.si.broker.model.Provider;
import com.si.broker.model.Role;
import com.si.broker.model.User;
import com.si.broker.services.IProviderService;
import com.si.broker.services.IUserService;
import com.si.broker.utils.CachedBodyHttpServletRequest;
import com.si.broker.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final IProviderService providerService;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, IUserService userService, IProviderService providerService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.providerService = providerService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User userCreds = null;
        Provider providerCreds = null;
        try {
            CachedBodyHttpServletRequest cachedBodyHttpServletRequest =
                    new CachedBodyHttpServletRequest(request);
            userCreds = new ObjectMapper().readValue(cachedBodyHttpServletRequest.getInputStream(), User.class);
            providerCreds = new ObjectMapper().readValue(cachedBodyHttpServletRequest.getInputStream(), Provider.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (isUser(userCreds))
                return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCreds.getUsername(), userCreds.getPassword(), new ArrayList<>()));
            else if (isProvider(providerCreds))
                return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(providerCreds.getUsername(), providerCreds.getPassword(), new ArrayList<>()));
        } catch (BadCredentialsException e) {
            e.printStackTrace();
        } finally {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(providerCreds.getUsername(), providerCreds.getPassword(), new ArrayList<>()));
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDTO user = userService.getUserDtoByUsername(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername());
        ProviderDTO provider = providerService.getProviderByUsername(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername());

        Long id = null;
        String email = null;
        String username = null;
        Long provider_id = null;
        Boolean isProvider = false;
        Boolean isAdmin = false;
        Boolean isKorisnik = false;
        Boolean isSaradnik = false;

        if (user != null && provider == null) {
            List<Role> roles = user.getRoles();
            for (Role r : roles) {
                if (r.getName().equals("ADMIN"))
                    isAdmin = true;
                else if (r.getName().equals("KORISNIK"))
                    isKorisnik = true;
                else if (r.getName().equals("SARADNIK"))
                    isSaradnik = true;
            }
            id = user.getId();
            email = user.getEmail();
            username = user.getUsername();
            provider_id = user.getProviders_id();
        } else if (provider != null && user == null) {
            isProvider = true;
            id = provider.getId();
            email = provider.getEmail();
            username = provider.getUsername();
            provider_id = provider.getId();
        }

        String token = JWT.create()
                .withSubject(username)
                .withClaim("ID", id)
                .withClaim("EMAIL", email)
                .withClaim("ADMIN", isAdmin)
                .withClaim("KORISNIK", isKorisnik)
                .withClaim("SARADNIK", isSaradnik)
                .withClaim("PROVIDER", isProvider)
                .withClaim("PROVIDER_ID", provider_id)
                .withExpiresAt(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(Constants.SECRET_KEY));
        response.addHeader(Constants.HEADER_STRING, Constants.TOKEN_PREFIX + token);
    }

    //UTIL
    private boolean isUser(User userCreds) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCreds.getUsername(), userCreds.getPassword(), new ArrayList<>()));
            return true;
        } catch (BadCredentialsException e) {
            return false;
        }
    }

    private boolean isProvider(Provider providerCreds) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(providerCreds.getUsername(), providerCreds.getPassword(), new ArrayList<>()));
            return true;
        } catch (BadCredentialsException e) {
            return false;
        }
    }
}