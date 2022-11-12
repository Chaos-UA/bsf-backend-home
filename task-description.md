Create a backend application with the following details:
Tech stack:
* Programming Language: Either Java or Kotlin
* Web Framework: Spring Boot

## Application Requirements:
1. Service should expose a REST API to accept money transfers to other accounts. Money
   transfers should persist new balance of accounts
2. Service should expose a REST API for getting the account details. You can disregard
   currencies at this time
   Points to consider:
1. Keep the design simple and to the point. The architecture should be scalable for adding new
   features
2. Proper handling of concurrent transactions for the accounts (with unit tests)
3. The datastore should run in-memory for the tests
4. Proper unit testing and decent coverage is a must
5. Upload the code to a repository
6. Disregard Currency or Rate Conversion
7. Improvise where details are not provided

## Plus Points:
1. Assignment implemented in Kotlin
2. Documentation of API (e.g. Swagger)
3. Dockerized app