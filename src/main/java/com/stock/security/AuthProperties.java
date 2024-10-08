package com.stock.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AuthProperties {

    @Value("${api.key}")
    private String apiKey;

}
