package com.vnny8.gerenciamento_de_gastos.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.vnny8.gerenciamento_de_gastos.rateLimiter.RateLimiterFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    private final CustomOidcUserService customOidcUserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final RateLimiterFilter rateLimiterFilter;
    private final ApiKeyFilter apiKeyFilter;

    public SecurityConfig(CustomOidcUserService customOidcUserService,
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
    RateLimiterFilter rateLimiterFilter,
    ApiKeyFilter apiKeyFilter){
        this.customOidcUserService = customOidcUserService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.rateLimiterFilter = rateLimiterFilter;
        this.apiKeyFilter = apiKeyFilter;
    }

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, DelegatingJwtDecoder delegatingJwtDecoder) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        authorizeConfig -> {
                            authorizeConfig.requestMatchers("/usuario/criar").permitAll();
                            authorizeConfig.requestMatchers(HttpMethod.GET, "/usuario").hasRole(ROLE_USER);
                            authorizeConfig.requestMatchers(HttpMethod.DELETE, "/usuario/apagar").hasRole(ROLE_ADMIN);
                            authorizeConfig.requestMatchers(HttpMethod.GET, "/usuario/listarTodos").hasRole(ROLE_ADMIN);
                            authorizeConfig.requestMatchers(HttpMethod.GET, "/usuario/encontrarUsuarioPorId").hasRole(ROLE_ADMIN);
                            authorizeConfig.requestMatchers(HttpMethod.GET, "/usuario/encontrarPorEmail").hasRole(ROLE_ADMIN);
                            authorizeConfig.requestMatchers(HttpMethod.POST, "/usuario/confirmarConta").permitAll();
                            authorizeConfig.requestMatchers(HttpMethod.POST, "/usuario/esqueciSenha").permitAll();
                            authorizeConfig.requestMatchers(HttpMethod.POST, "/usuario/alterarSenha").permitAll();
                            authorizeConfig.requestMatchers("/loginGoogleApi").permitAll();
                            authorizeConfig.requestMatchers("/authenticate").permitAll();
                            authorizeConfig.requestMatchers("/jwt/**").permitAll();
                            authorizeConfig.anyRequest().authenticated();
                        }
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOidcUserService)
                        )
                        .successHandler(customAuthenticationSuccessHandler)
                        .defaultSuccessUrl("/loginGoogleApi")
                )
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer -> {
                                    jwtConfigurer.decoder(delegatingJwtDecoder);
                                    jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter());
                                })
                                ) // Usa o decoder dinâmico
                .addFilterBefore(apiKeyFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimiterFilter, apiKeyFilter.getClass())
                                .build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    // Validação de JWT do Google
    @Bean
    JwtDecoder googleJwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        var jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // Nome da claim onde as roles estão
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // Remove o prefixo padrão 'SCOPE_'

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
