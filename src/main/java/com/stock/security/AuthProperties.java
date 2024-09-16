package com.stock.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class AuthProperties {

    @Value("${api.key}")
    private String apiKey;
}
