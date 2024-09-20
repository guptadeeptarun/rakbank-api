### Build the application
./mvnw clean install

### Database Setup
Application uses a MySQL database.

You can set up the MySQL Database in two ways.
1. MySQL as a docker container. This will automatically
- Bring up the MySQL database on port 3306
- Set the root password as 'root'
- create a database with name 'rakbank'
- create the user 'rakbank_rw' with password 'changeme'


docker run --name docker-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=rakbank -e MYSQL_USER=rakbank_rw -e MYSQL_PASSWORD=changeme -d mysql:latest

2.If you already have MySQL installed, just run the below from root account. 
Please make sure the database is accessible on port 3306 or otherwise you can change the port in 
application.properties file for the property spring.datasource.url. Please rebuild the application in that case.

- create database rakbank;
- create user 'rakbank_rw' identified by 'changeme';
- GRANT ALL ON rakbank.* TO 'rakbank_rw';


### Run the application.

You can run the application in either of the 3 ways.

1. Run the application as SpringBoot Application

Run the below command from inside the src folder.

    ./mvnw spring-boot:run 


2. Run as Jar file

Run the below command from inside the target folder.

    java -jar userservice-0.0.1-SNAPSHOT.jar 

3. Run as Docker container

Build the docker image. Run below command from inside src folder.

    sudo docker build . -t userservice:latest

Run the docker container. Make sure to replace the IP in datasoure url to the ip of your machine. 
This property is externalised since localhost is not reachable from inside the container.

    sudo docker run --name userservice -p 8080:8080 -e spring.datasource.url=jdbc:mysql://192.168.56.1:3306/rakbank -d userservice:latest


### Access Swagger
http://localhost:8080/swagger-ui/index.html

### Live Demo
For a Live Demo, you can access the application on below URL. Please note in case you are behind a firewall, this may not work.

http://193.123.75.224:8080/swagger-ui/index.html

### Assumptions:

- Spring security is bypassed for the assessment. 
- CORS settings are allowed for all only for assessment purpose.
- Password (hashed) is sent in responses of API only for assessment purpose. 
- Create user API to only allow unique email address signup. 
- GetAll user API response is paginated. Request parameter 'page' is optional and set to 1 when page is not specified. Page size is default set to 10. 
- Service uses Optimistic lock to protect updates on a stale date.
- The postman collection file and openapi doc are included in the src code.