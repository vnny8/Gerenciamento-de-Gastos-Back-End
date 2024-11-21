package com.vnny8.gerenciamento_de_gastos.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import java.util.Base64;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DelegatingJwtDecoder implements JwtDecoder {

    private final JwtDecoder jwtDecoder; // Decoder da sua aplicação (chave privada)
    private final JwtDecoder googleJwtDecoder; // Decoder do Google

    private final ObjectMapper objectMapper = new ObjectMapper();

    public DelegatingJwtDecoder(JwtDecoder jwtDecoder, JwtDecoder googleJwtDecoder) {
        this.jwtDecoder = jwtDecoder;
        this.googleJwtDecoder = googleJwtDecoder;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Decodifica a parte do payload do JWT (segundo segmento)
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

            // Converte o payload para um Map
            Map<String, Object> claims = objectMapper.readValue(payload, Map.class);
            String issuer = (String) claims.get("iss");

            System.out.println("issuer: " + issuer);

            if ("https://accounts.google.com".equals(issuer)) {
                // Usar o decoder do Google
                return googleJwtDecoder.decode(token);
            } else {
                // Usar o decoder da sua aplicação
                return jwtDecoder.decode(token);
            }

        } catch (Exception e) {
            throw new JwtException("Falha ao decodificar o JWT: " + e.getMessage(), e);
        }
    }
}
