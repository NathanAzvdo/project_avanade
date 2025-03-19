# Use uma imagem base com Java
FROM eclipse-temurin:17-jdk

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos do projeto
COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn ./.mvn

# Dar permissão de execução ao mvnw
RUN chmod +x mvnw

# Construir o aplicativo
RUN ./mvnw clean package

# Expor a porta que a aplicação usa (ajuste conforme necessário)
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "target/nome-do-seu-arquivo.jar"]