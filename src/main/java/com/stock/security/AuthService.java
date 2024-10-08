package com.stock.security;

import com.stock.utils.DateUtils;
import com.stock.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
public class AuthService {

    private static final String AUTH_TOKEN_HEADER_NAME = "api-key";

    private final AuthProperties authProperties;

    private final JwtTokenUtil jwtTokenUtil;

    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs",
            "/swagger-ui",
            "/actuator",
            "/actuator/**"
    };

    public AuthService(AuthProperties authProperties, JwtTokenUtil jwtTokenUtil) {
        this.authProperties = authProperties;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        var path = request.getRequestURL().toString();
        if(!stringContainsItemFromList(path, AUTH_WHITELIST)) {
            var apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);

            if (apiKey==null || !apiKey.equals(authProperties.getApiKey())) {
                throw new BadCredentialsException("Invalid API Key");
            }

            return new ApiKeyAuth(apiKey, AuthorityUtils.NO_AUTHORITIES);
        }
        return new ApiKeyAuth("superSecretKey", AuthorityUtils.NO_AUTHORITIES);
    }

    public static boolean stringContainsItemFromList(String path, String[] whitelist) {
        return Arrays.stream(whitelist).anyMatch(path::contains);
    }

    public long getExipartion(String token) {
        try {
            return DateUtils.diff(new Date(), this.jwtTokenUtil.extractExpiration(token));
        } catch (ParseException e) {
            log.error("Error During Date Parsing");
        }
        return -1;
    }

}
