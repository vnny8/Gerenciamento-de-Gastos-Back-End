# Gerenciamento de Gastos - Back-End

Repositório do Front-End: https://github.com/vnny8/Gerenciamento-de-Gastos-Front-End

## 📌 Introdução
Este documento descreve os requisitos para o desenvolvimento de um software para gerenciamento de gastos pessoais, com Front-End em ReactJs, Back-End em Java Spring Boot e Banco de Dados em PostgreSQL. O objetivo é criar uma solução simples e eficaz que permita o gerenciamento de gastos do mês.

## 📂 Arquitetura e Organização

### 🔹 Ponto de Entrada da Aplicação
- **`GerenciamentoDeGastosApplication.java`** → Classe principal do Spring Boot que inicia a aplicação.

### 🔹 Módulos Funcionais

#### 📌 **Categorias**
- `dtos` → Possui DTOs de request e response para utilizar nos endpoints da Controller.
- `CategoriaController.java` → Controla endpoints para **criar, acessar, editar e deletar** categorias de gastos.
- `CategoriaService.java` → Contém a **lógica de negócios** para manipular categorias.
- `CategoriaRepository.java` → Interface que interage com o **banco de dados** para salvar e recuperar categorias.

#### 📌 **Gastos**
- `dtos` → Possui DTOs de request e response para utilizar nos endpoints da Controller.
- `GastoController.java` → Gerencia os gastos do usuário.
- `GastoService.java` → Processa os dados dos gastos e realiza cálculos financeiros.
- `GastoRepository.java` → Interface para acesso ao banco de dados.

#### 📌 **Salários**
- `dtos` → Possui DTOs de request e response para utilizar nos endpoints da Controller.
- `SalarioController.java` → Permite operações de **CRUD** sobre salários.
- `SalarioService.java` → Processa os salários e os associa a usuários.
- `SalarioRepository.java` → Interage com o banco de dados para armazenar os salários.

#### 📌 **Usuários**
- `dtos` → Possui DTOs de request e response para utilizar nos endpoints da Controller.
- `UsuarioController.java` → Gerencia o cadastro, autenticação e recuperação de senha.
- `UsuarioService.java` → Lida com a lógica de criação e edição de usuários.
- `UsuarioRepository.java` → Repositório de persistência dos dados dos usuários.

#### 📌 **Segurança**
- `rateLimiter/`
  - **`RateLimiterConfig.java`** → Define as configurações para limitar a quantidade de requisições por minuto de um usuário.
  - **`RateLimiterFilter.java`** → Aplica as regras de rate limiting nos endpoints da API.
- **`SecurityConfig.java`** → Configura **JWT, OAuth2 (Google Login)** e permissões de acesso.
- **`JwtService.java`** → Responsável por gerar e validar **tokens JWT**.
- **`ApiKeyFilter.java`** → Filtra requisições para garantir que uma API Key válida seja usada.
- **`AuthenticationController.java`** → Controla o login e registro de usuários, incluindo a autenticação com Google OAuth2.
- **`CustomAuthenticationProvider.java`** → Implementa um provedor de autenticação personalizado para login de usuários.

#### 📌 **Infraestrutura e Utilitários**
- **`application.properties`** → Arquivo de configuração contendo informações sobre banco de dados, segurança e outras propriedades da aplicação.
- **`CustomizedResponseEntityException.java e ExceptionResponse.java`** → Controla o tratamento de exceções e respostas personalizadas de erro.
- **`EmailService.java`** → Serviço para envio de e-mails automáticos, incluindo confirmação de conta e recuperação de senha.


---

## 📋 Requisitos do Sistema

### ✅ Requisitos Funcionais
- **Autenticação e autorização:** O sistema deve possuir autenticação de usuários via **JWT** e **OAuth2 (Google Login)**.
- **Cadastro e gerenciamento de usuários:**
  - Criar um novo usuário manualmente pelo sistema.
  - **Login via Google cria a conta automaticamente caso não exista.**
  - Confirmar a conta através de um código de **6 dígitos** enviado por e-mail.
  - Recuperar a senha utilizando um código enviado por e-mail.
  - Alterar a senha com base no código de recuperação.
- **Armazenamento de salário do mês:** Criar salário e vinculá-lo a um determinado mês e ano.
- **Armazenamento de categorias:** Criar novas categorias de gastos e vinculá-las ao usuário.
- **Armazenamento de gastos:** Criar novos gastos e armazená-los por mês e ano.
- **Adicionar uma categoria a um gasto:** Vincular cada gasto a uma categoria e a um usuário.
- **Gerar relatórios:** Realizar relatórios de gastos de acordo com o usuário, mês e ano.
- **Tratamento de exceções:** O sistema deve possuir tratamento de exceções com respostas personalizadas.
- **Testes de integração:** Deve realizar testes automatizados para garantir a estabilidade do sistema.

### 🔒 Requisitos Não Funcionais
- **Linguagem:** O sistema será feito em Java utilizando o framework Spring Boot.
- **Banco de dados:** O banco de dados será em PostgreSQL.
- **Rate Limiting:** O sistema deve ter um limite de requisições para evitar abusos.
- **Desempenho:** O sistema deve responder rapidamente às solicitações dos usuários.
- **Segurança:** 
  - Uso de **JWT** e **OAuth2** para autenticação.
  - **Senhas devem ser armazenadas criptografadas** utilizando **BCrypt** ou equivalente.
  - **Rate Limiting:** O sistema deve limitar a quantidade de requisições feitas por um usuário dentro de um período de tempo para evitar abusos, ataques de força bruta e sobrecarga do servidor.
- **Testes Automatizados:** O sistema deve ser validado através de testes automatizados utilizando:
  - **JUnit** → Para testes unitários das regras de negócio.
  - **Mockito** → Para criação de objetos mock e simulação de dependências durante os testes.
- **Scripts de Automação:** O sistema deve contar com scripts para facilitar a configuração do banco de dados usando Docker:
  - **`start.sh`** → Cria uma **Network Docker isolada** e inicia os containers **PostgreSQL** e **pgAdmin** dentro dessa rede.
  - **`stop.sh`** → Para e remove os containers do banco de dados e pgAdmin, além de remover a **Network Docker** criada.

---

## 🚀 Como Executar

### 🔧 **Pré-requisitos**
- Java 11+
- Maven
- PostgreSQL

### 📌 **Passos**
1. **Configure as variáveis de ambiente** criando um arquivo `.env` na raiz do projeto e preenchendo os valores conforme necessário:

```plaintext
# Configuração do Banco de Dados
DB_USERNAME=postgres
DB_PASSWORD=sua_senha

# Chaves JWT para autenticação
JWT_PUBLIC_KEY=
JWT_PRIVATE_KEY=

# Chave de API para segurança
API_KEY=

# Configuração do OAuth2 (Login com Google)
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=

# Configuração de E-mail (Envio de notificações)
ADMIN_EMAIL=
EMAIL_COMUNICACAO=
SENHA_EMAIL_COMUNICACAO=
```

3. Execute os comandos:
   ```bash
   ./start.sh
   ./mvnw spring-boot:run
   ```

---

## 🌐 Endpoints Principais

### 🔹 **Categorias**
| Método  | Endpoint                | Descrição                     |
|---------|-------------------------|--------------------------------|
| `POST`  | `/categoria/criar`      | Cria uma nova categoria       |
| `GET`   | `/categoria/acessar`    | Retorna uma categoria específica |
| `GET`   | `/categoria/listarPorUsuario`    | Lista todas as categorias que um usuário criou |
| `PUT`   | `/categoria/editar`     | Edita uma categoria           |
| `DELETE`| `/categoria/deletar`    | Deleta uma categoria          |

### 🔹 **Gastos**
| Método  | Endpoint                | Descrição                      |
|---------|-------------------------|---------------------------------|
| `POST`  | `/gasto/criar`          | Registra um novo gasto         |
| `GET`   | `/gasto/acessar`        | Retorna um gasto específico    |
| `GET`   | `/gasto/listar`         | Lista gastos do usuário        |
| `GET`   | `/gasto/listarPorData`  | Lista gastos de um usuário por uma data focando no mês e ano          |
| `PUT`   | `/gasto/editar`     | Edita um gasto         |
| `DELETE`   | `/gasto/deletar`        | Deleta um gasto específico    |

### 🔹 **Salários**
| Método  | Endpoint                | Descrição                      |
|---------|-------------------------|---------------------------------|
| `POST`  | `/salario/criar`        | Adiciona um salário            |
| `GET`   | `/salario/acessar`      | Retorna um salário específico  |
| `PUT`   | `/salario/editar`       | Edita um salário               |
| `DELETE`| `/salario/deletar`      | Deleta um salário              |

### 🔹 **Usuários**
| Método  | Endpoint                 | Descrição                      |
|---------|--------------------------|---------------------------------|
| `POST`  | `/usuario/criar`         | Cria um novo usuário           |
| `POST`  | `/usuario/confirmarConta`| Confirmação via e-mail         |
| `POST`  | `/usuario/esqueciSenha`  | Inicia recuperação de senha    |
| `POST`  | `/usuario/alterarSenha`  | Altera a senha                 |
| `GET`  | `/usuario/encontrarPorEmail`         | Retorna um usuário através do e-mail           |
| `GET`  | `/usuario/encontrarUsuarioPorId`         | Retorna um usuário através do ID           |
| `GET`  | `/usuario/listarTodos`         | Lista todos os usuários armazenados          |
| `PUT`   | `/usuario/editar`       | Edita um usuário               |
| `DELETE`| `/usuario/deletar`      | Deleta um usuario              |


---

## 📜 Tecnologias Utilizadas

### 🔹 **Back-End**
- **Linguagem:** Java  
- **Framework:** Spring Boot  
- **Gerenciamento de Dependências:** Maven  

### 🔹 **Banco de Dados**
- **Banco de Dados Relacional:** PostgreSQL  
- **ORM:** Spring Data JPA  

### 🔹 **Segurança**
- **Autenticação:** Spring Security  
- **Autenticação via Terceiros:** OAuth 2.0 com Google  
- **Token de Segurança:** JWT (JSON Web Token)  
- **Criptografia de Senhas:** BCrypt  
- **Rate Limiting:** Bucket4j

### 🔹 **Infraestrutura e DevOps**
- **Containerização:** Docker  
- **Gerenciamento de Containers:** Docker Compose  
- **Administração do Banco de Dados:** pgAdmin  
- **Scripts de Automação:** Bash Scripts (`start.sh` e `stop.sh`)  

### 🔹 **Testes Automatizados**
- **Testes Unitários e de Integração:** JUnit  
- **Mock de Dependências:** Mockito  
- **Testes de Segurança:** Spring Security Test

### 🔹 **Outras Bibliotecas e Utilitários**
- **Manipulação de JSON:** Jackson  
- **Envio de E-mails:** Spring Mail  
- **Logs e Monitoramento:** Spring Boot Actuator e SLF4J  

---

## 📜 Licença

Este projeto é de código aberto e pode ser utilizado conforme necessário.

---

📌 Criado por [vnny8](https://github.com/vnny8)
