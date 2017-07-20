# F4U-Bank
This repository is part of a ING Honours research project of ING Bank.

This project implements a simple banking system using the Spring framework.

How to Run
----------
Before starting the program, make sure that a MySQL database server is running,  locally hosted and create an empty database with name 'bank'. It is also required to set the PATH variable to the MySQL bin. 

Create an `application.properties` configuration file in `src/main/resources` and update it with the new database credentials:
```
spring.datasource.url = jdbc:mysql://<domain>:<port>/bank
spring.datasource.username = root
spring.datasource.password = ****
spring.jpa.show-sql = true

spring.jpa.hibernate.ddl-auto = update

spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect
```

Download the Maven dependencies and run Main in the `com.bank` package.

Database tables should automatically be created on first start.