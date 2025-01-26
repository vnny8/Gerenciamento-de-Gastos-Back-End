package com.vnny8.gerenciamento_de_gastos.rateLimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Criação de um bucket para cada usuário
    private Bucket createBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(50, Refill.greedy(50, Duration.ofMinutes(1))))
                .build();
    }

    @Override
    public void doFilter(jakarta.servlet.ServletRequest servletRequest, jakarta.servlet.ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String userKey = getUserKey(request);

        // Obtenha ou crie o bucket para o usuário
        Bucket bucket = buckets.computeIfAbsent(userKey, k -> createBucket());

        // Verifica se o usuário pode fazer mais requisições
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            // Responde com erro 429 (Too Many Requests)
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Limite de requisições excedido. Tente novamente mais tarde.");
        }
    }

    // Identifica o usuário com base no token ou IP
    private String getUserKey(HttpServletRequest request) {
        // Caso use autenticação com JWT, você pode extrair o e-mail ou o ID do token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Retorna o token JWT
        }

        // Como fallback, use o IP do cliente
        return request.getRemoteAddr();
    }
}
