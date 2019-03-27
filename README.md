# Java Developer coding task by Twinero

1) Configuring the DB connection:<br>
	- You must create a PostgreSQL DB called: simple_banking.<br>
	- Edit the application.properties file and change the username and password accordingly.
	
2) To compile:<br>
    $ mvn clean package

This will compile, run every single test and generate the Jacoco report.

Jacoco  report is in: <b>target/site/jacoco/index.html</b>

Adicionally it will generate the API REST documentacion in <b>target/generated-docs/api-guide.html</b>

3) For generates the javadoc documentation:<br>
$ mvn javadoc:javadoc

The javadoc documentation will be generate in: <b>target/site/apidocs/index.html</b>

4) To run:
	$ mvn spring-boot:run

## The project meets the following requirements:

Coding challenge task is to create backend for a simple "banking" application:
* Client should be able to sign up with email & password
* Client should be able to deposit money
* Client should be able to withdraw money
* Client should be able to see account balance and statement

You should create database structure, backend and REST API.

Technology stack
* Java 8
* Spring Boot
* PostgreSQL or any in-memory database (like H2/HSQLDB)
* Maven or Gradle as build system
* Host your code on GitHub/Bitbucket/GitLab public repo

Note: Spring security usage is not necessary.

What we will take into consideration on final valuation:
- How well business requirements are understood
- Quality of the code
- Test coverage
