# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Define diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Garante permissão ao mvnw
RUN chmod +x ./mvnw

# Executa o build (sem os testes)
RUN ./mvnw clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jdk

# Define diretório de trabalho
WORKDIR /app

# Copia o JAR da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta que sua aplicação usa
EXPOSE 8080

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
