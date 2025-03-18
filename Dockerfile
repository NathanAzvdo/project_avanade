# Etapa 1: Build com Maven + JDK 17
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas os arquivos necessários do Maven antes para aproveitar cache
COPY pom.xml mvnw .mvn/ ./
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline

# Copia o restante do projeto e gera o JAR
COPY . .
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagem final só com JDK 17 para rodar o JAR
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o JAR construído na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando de execução do app
ENTRYPOINT ["java", "-jar", "app.jar"]
