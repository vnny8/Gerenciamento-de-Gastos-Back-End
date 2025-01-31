# Gerenciamento de Gastos - Back-End

## 📌 Introdução
Este documento descreve os requisitos para o desenvolvimento de um software para gerenciamento de gastos pessoais, com Front-End em ReactJs, Back-End em Java Spring Boot e Banco de Dados em PostgreSQL. O objetivo é criar uma solução simples e eficaz que permita o gerenciamento de gastos do mês.

## 📂 Arquitetura e Organização

### 🔹 Ponto de Entrada da Aplicação
- **`GerenciamentoDeGastosApplication.java`** → Classe principal do Spring Boot que inicia a aplicação.

### 🔹 Módulos Funcionais

#### 📌 **Categorias**
- `CategoriaController.java` → Controla endpoints para **criar, acessar, editar e deletar** categorias de gastos.
- `CategoriaService.java` → Contém a **lógica de negócios** para manipular categorias.
- `CategoriaRepository.java` → Interface que interage com o **banco de dados** para salvar e recuperar categorias.

#### 📌 **Gastos**
- `GastoController.java` → Gerencia os gastos do usuário.
- `GastoService.java` → Processa os dados dos gastos e realiza cálculos financeiros.
- `GastoRepository.java` → Interface para acesso ao banco de dados.

#### 📌 **Salários**
- `SalarioController.java` → Permite operações de **CRUD** sobre salários.
- `SalarioService.java` → Processa os salários e os associa a usuários.
- `SalarioRepository.java` → Interage com o banco de dados para armazenar os salários.

#### 📌 **Usuários**
- `UsuarioController.java` → Gerencia o cadastro, autenticação e recuperação de senha.
- `UsuarioService.java` → Lida com a lógica de criação e edição de usuários.
- `UsuarioRepository.java` → Repositório de persistência dos dados dos usuários.

#### 📌 **Segurança**
- `SecurityConfig.java` → Configura **JWT, OAuth2 (Google Login)** e permissões de acesso.
- `JwtService.java` → Responsável por gerar **tokens JWT**.
- `ApiKeyFilter.java` → Filtra requisições baseadas em uma API Key.

---

## 📋 Requisitos do Sistema

### ✅ Requisitos Funcionais
- **Autenticação e autorização:** O sistema deve possuir autenticação de usuários.
- **Armazenamento de gastos:** Criar novos gastos para armazenar no mês.
- **Adicionar uma categoria a um gasto:** Vincular cada gasto a uma categoria.
- **Gerar relatórios:** Realizar relatórios de gastos mensais, trimestrais, semestrais e anuais.
- **Tratamento de exceções:** O sistema deve possuir tratamento de exceções com respostas personalizadas.
- **Testes de integração:** Deve realizar testes automatizados de integração.

### 🔒 Requisitos Não Funcionais
- **Linguagem:** O sistema será feito em Java utilizando o framework Spring Boot.
- **Banco de dados:** O banco de dados será em PostgreSQL.
- **Desempenho:** O sistema deve responder rapidamente às solicitações dos usuários.
- **Segurança:** O sistema deve possuir segurança para os dados transmitidos e armazenados.

---

## 🚀 Como Executar

### 🔧 **Pré-requisitos**
- Java 11+
- Maven
- PostgreSQL

### 📌 **Passos**
1. Configure as variáveis de ambiente (`DB_USERNAME`, `DB_PASSWORD`, `JWT_PUBLIC_KEY`, etc.).
2. Execute o comando:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🌐 Endpoints Principais

### 🔹 **Categorias**
```
POST /categoria/criar - Cria uma nova categoria
GET  /categoria/acessar - Retorna uma categoria específica
PUT  /categoria/editar - Edita uma categoria
DELETE /categoria/deletar - Deleta uma categoria
```

### 🔹 **Gastos**
```
POST /gasto/criar - Registra um novo gasto
GET  /gasto/acessar - Retorna um gasto específico
GET  /gasto/listar - Lista gastos do usuário
GET  /gasto/listarPorData - Lista gastos por data
```

### 🔹 **Salários**
```
POST /salario/criar - Adiciona um salário
GET  /salario/acessar - Retorna um salário específico
PUT  /salario/editar - Edita um salário
DELETE /salario/deletar - Deleta um salário
```

### 🔹 **Usuários**
```
POST /usuario/criar - Cria um novo usuário
POST /usuario/confirmarConta - Confirmação via e-mail
POST /usuario/esqueciSenha - Inicia recuperação de senha
POST /usuario/alterarSenha - Altera a senha
```

---

## 📜 Tecnologias Utilizadas

- **Back-End:** Java com Spring Boot.
- **Banco de Dados:** PostgreSQL.
- **Segurança:**
  - OAuth 2.0 com Google.
  - Spring Security com JWT.

---

## 📜 Licença

Este projeto é de código aberto e pode ser utilizado conforme necessário.

---

📌 Criado com ❤️ por [vnny8](https://github.com/vnny8)
