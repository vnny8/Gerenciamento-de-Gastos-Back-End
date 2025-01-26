package com.vnny8.gerenciamento_de_gastos.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiKeyFilter implements Filter {

    @Value("${API_KEY}") // Obtém a API Key configurada
    private String validApiKey;

    @Override
    public void doFilter(jakarta.servlet.ServletRequest servletRequest, jakarta.servlet.ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();

        // Ignorar a validação da API Key para logar pelo Google
        if (path.equals("/loginGoogleApi")) {
            chain.doFilter(request, response);
            return;
        }

        // Obtém o header "X-API-KEY" da requisição
        String apiKey = request.getHeader("X-API-KEY");

        // Verifica se a chave está presente e é válida
        if (apiKey == null || !apiKey.equals(validApiKey)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Acesso negado: API Key inválida.");
            return;
        }

        chain.doFilter(request, response); // Continua com a requisição se a chave for válida
    }
}
