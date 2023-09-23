# PruebaTecnica
Puedes acceder a la demo en la siguiente dirección [DEMO PRUEBA TECNICA](http://129.146.87.236:8080/)

# Desarrollo

Asegurese de tener una conecciona a bdd en el puerto especificado en archivo properties
Para ejecutar puede usar instalacion local maven
```
    maven spring-boot:run 
```
con con el wrapper
```
    ./mvnw spring-boot:run
```
Crear artefacto 

```
    ./mvnw package
    java -jar <archivo>.war
```

Utiliza JDK 1.8. mysql, spring-boot con JSF y PrimeFaces, tomcat emmbed y JasperReports, Spring data Jpa/hibernate
*Si es la primera vez que ejecutas el proyecto usar `spring.jpa.hibernate.ddl-auto=create` para crear la bdd automaticamente, despues puedes usar `spring.jpa.hibernate.ddl-auto=validate`
*Si quieres ejecutar directamente sql usar [script bdd](script.sql)
