# Etapa 1: build da aplicação
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copia pom.xml e baixa dependências (cache eficiente)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte e builda
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: imagem final de runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/*.jar app.jar

# Define a variável de ambiente padrão
ENV SPRING_PROFILES_ACTIVE=prod

# Render define automaticamente PORT
EXPOSE 8080

# Comando de inicialização
CMD ["java", "-jar", "app.jar"]

ENV CLOUDINARY_URL=cloudinary://445736445162696:FAbyY1JZcagy1PzwCzvf0EsFtBs@droxfew60

