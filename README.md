# Poseidon API
Poseidon is an enterprise software deployed as a web application to generate more transactions on financial markets. The application gathers information from different sources.


## Technical Stack
- Backend : Java 8 and Spring Boot framework v2.0.4 (+ Spring Security)
- Frontend : HTML, Thymeleaf and Bootstrap v.4.3.1
- Database : MySQL

## Setting up the application
In a terminal/command prompt, connect to your MySQL local database system. When connected do the followings : 
- Create the API Database with : `CREATE DATABASE poseidonapi`
- Tell MySQL to use the created database with : `USE poseidonapi`
- In the main/resources folder of the application, load the SQL database creation file with : `source schema.sql`
- If everything is set up correctly, you can now start the application with : `mvn spring-boot:run`

## Testing the application
If you loaded the database creation file, there should be 2 pre-created users :
- An admin account : `admin:Passw0rd-`
- A simple user account : `user:Passw0rd-`

## Features
The application is made of 6 differents endpoints : 
- Bids
- Curve Points
- Ratings
- Trades
- Rules
- Users (for admin only)
