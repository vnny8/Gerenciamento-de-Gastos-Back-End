package com.vnny8.gerenciamento_de_gastos;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GerenciamentoDeGastosApplication {

	public static void main(String[] args) {
		// Carregar variáveis do .env
		Dotenv dotenv = Dotenv.configure().load();

		// Configurar o Spring Boot com essas variáveis
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("JWT_PUBLIC_KEY", dotenv.get("JWT_PUBLIC_KEY"));
		System.setProperty("JWT_PRIVATE_KEY", dotenv.get("JWT_PRIVATE_KEY"));
		System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
		System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
		System.setProperty("ADMIN_EMAIL", dotenv.get("ADMIN_EMAIL"));
		SpringApplication.run(GerenciamentoDeGastosApplication.class, args);
	}

}
