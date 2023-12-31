# Sprint Boot Multi-Tenant

Sample project of multi tenant application using schema or database per tenant with Spring Boot 3 and JWT

A master database exists to maintain information about the tenants or the application itself, each tenant will have its own schema/database

All components (entities, services, repositories) **bellow com.multitenant.example.master will be scanned as part of the master scope**, while everything **bellow com.multitenant.example.tenant will be scanned as part of the tenants scope** 

## Main technologies

* Spring Framework 6
* Spring Boot 3
* Spring Security
* Gradle
* JWT
* PostgreSQL

## How to run

1. **Create a database named 'multitenant'** to be the master database
2. **Create a database named 'tenant'** for the default tenant client
3. **Execute SQL** in resources/default.sql at multitenant database, it will create the tenants management tables and insert the default tenant register  
4. **Run** the application to generate default tenant tables

## Extra information

* If you want to use schema per tenant, set multitenant.per-schema to true in application.yml
* When multitenant.per-schema is active, the value in tenant's database field will be used as schema name
* Tables will be auto-generated for the default tenant schema/database only
* Use TenantContext.setCurrentTenant(<tenant_id>) to change the active thread tenant

## Endpoins

* **/auth/login (public)** - For login 
* **/logout/logout (private)** - For logout
* **/newuser (public)** - Creates users
* **/testuser (private)** - RUD (yes, CRUD without C) of users