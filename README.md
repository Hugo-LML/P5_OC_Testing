# P5_OC_Testing

This project is the fifth of the OpenClassrooms full stack developer formation. Its goal is to learn unitary, integration and functional tests for the back, as for the front (Java / Angular).

## Prerequisites

- MySQL >= 8
- Angular >= 14.1.0
- Java >= 8
- Maven

## Create the DB

1. Copy the SQL script located in ./resources/sql/script.sql
2. Import it inside your MySQL shell
3. In the ./back/src/main/resources/application.properties file, replace the followings by your informations:
- spring.datasource.url=jdbc:mysql://localhost:3306/test?allowPublicKeyRetrieval=true
- spring.datasource.username=user
- spring.datasource.password=123456

Here are the credentials of the default admin account:
- login: yoga@studio.com
- password: test!1234

## Start the project

Clone the repository:
- `git clone https://github.com/Hugo-LML/P5_OC_Testing.git`

### Back

Open a terminal in the back folder and run:
- `mvn clean install`
- `mvn spring-boot:run`

### Front

Open a terminal in the front folder and run:
- `npm install`
- `npm run start`

### Tests

#### Back

To launch all back tests, run the following command:
-  `mvn test`

To generate the report, run the following command:
- `mvn jacoco:report`

To see the test coverage, go to this url:
- `back/target/site/jacoco/index.html`

#### Front

To launch all front tests, run the following command:
-  `npm run test`

To see the test coverage, run the following command:
- `npm run test:coverage`

#### E2E

To launch all front end to end tests, run the following command:
-  `npm run e2e`

To see the test coverage, run the following command:
- `npm run e2e:coverage`