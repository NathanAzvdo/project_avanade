# Text Summarizer API

## 📌 Sobre o Projeto
Este projeto consiste em uma API REST desenvolvida com **Spring Boot**, que permite o armazenamento e resumir de textos utilizando **OpenNLP**.

## 🚀 Tecnologias Utilizadas
- **Spring Boot** (Backend)
- **PostgreSQL** (Banco de Dados)
- **Flyway** (Migrações do banco)
- **Springdoc OpenAPI** (Documentação da API)
- **JUnit & Mockito** (Testes)

## 🛠️ Como Executar o Projeto

### ✅ Pré-requisitos
Certifique-se de ter instalado:
- [JDK 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)

### 🔧 Passos para rodar a aplicação
1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   cd seu-repositorio
   ```
2. Configure as variáveis de ambiente necessárias no sistema:
   ```bash
   export DB_USER="seu_usuario"
   export DB_PASS="sua_senha"
   ```
   Ou, no Windows (PowerShell):
   ```powershell
   $env:DB_USER="seu_usuario"
   $env:DB_PASS="sua_senha"
   ```
3. Compile e execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```
4. Acesse a documentação Swagger:
   ```
   http://localhost:8080/swagger-ui.html
   ```

## 📌 Configuração do Swagger

O projeto utiliza o **Springdoc OpenAPI** para gerar a documentação da API automaticamente. A documentação pode ser acessada em:

🔗 **http://localhost:8080/swagger-ui.html** (ou na porta configurada no seu projeto)

## 🛠 Testando os Endpoints
1. Acesse **Swagger UI** (`/swagger-ui.html`).
2. Escolha um endpoint.
3. Clique em "**Try it out**" para testar diretamente pelo navegador.

