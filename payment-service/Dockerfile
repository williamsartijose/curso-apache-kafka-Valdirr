# Utiliza uma imagem base do OpenJDK 17, que é adequada para rodar aplicações Java 17.
FROM openjdk:17
  
  # Define uma variável de argumento chamada JAR_FILE que aponta para o arquivo JAR a ser copiado.
  # O valor padrão é 'target/*.jar', o que assume que o JAR final está localizado no diretório 'target'.
ARG JAR_FILE=target/*.jar
  
  # Copia o arquivo JAR especificado pela variável JAR_FILE para o contêiner.
  # O arquivo é renomeado como 'app.jar' no contêiner.
COPY ${JAR_FILE} app.jar
  
  # Define o comando que será executado quando o contêiner iniciar.
  # Aqui, o comando 'java -jar app.jar' é usado para iniciar a aplicação Java.
ENTRYPOINT ["java", "-jar", "app.jar"]
