# Sprint Boot Multi-Tenant

Sample project of multi tenant application using database per tenant with Spring Boot 3 and JWT

A master database exists to maintain information about the tenants or the application itself, then each tenant will have its own database

All components (entities, services, repositories) **bellow com.multitenant.example.master will be scanned as part of the master scope**, while everything **bellow com.multitenant.example.tenant will be scanned as part of the tenants scope** 

## Main technologies

* Spring Framework 6
* Spring Boot 3
* Spring Security
* Gradle
* JWT
* PostgreSQL

## How to run

You will **need to create a database named multitenant** to be the master database, will also need to **create a database named tenant for the default tenant client**, then each new tenant should have its own database and be registered in testuser table at the master database. 

Tables will be auto generated on the first run

## Endpoins

* **/auth/login (public)** - For login 
* **/logout/logout (private)** - For logout
* **/newuser (public)** - Creates users
* **/testuser (private)** - RUD (yes, CRUD without C) of users