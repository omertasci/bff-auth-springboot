
# Authentication and Authorization to LDAP, AzureAD and Sharepoint with Spring Boot BFF

This project aims to demonstrate how to integrate LDAP, Azure AD and SharePoint resources by a Spring Boot BFF application.
For example: a user who has LDAP account on LDAP server tries to authenticate by calling "api/auth/authenticate" endpoint of this application, and unless any exception occures, he may be authenticated.



## Installation

1. Run docker-compose.yml file as:
    ```bash
    cd bff-auth-springboot/docker
    docker-compose up --build
    ```
2. Login the PhpLDAPAdmin page on http://localhost:8090/ with the credentials username: "cn=admin,dc=mycompany,dc=com" and password: "admin".(see in application.yml)

3. Upload bootstrap_firm.ldif file to PhpLDAPAdmin page due to instantiate some LDAP groups and accounts.
4. Run the bff-auth-springboot application.
5. Check the user authentication with the below curl request.
   ```bash
   curl --location 'http://localhost:8080/api/auth/authenticate' \
    --header 'Content-Type: application/json' \
    --header 'Cookie: JSESSIONID=8F8FC161DD4665B3E766EB948F590916' \
    --data '{
    "username":"alice",
    "password":"password123"
    }'
   ```
   
## Authors

- [@omertasci](https://www.github.com/omertasci)