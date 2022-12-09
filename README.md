# reservation_facilities
To make this app work in **src/main/resources** add file **secrets.properties** with such content:

```
spring.datasource.url=[YOUR DB URL]
spring.datasource.username=[YOUR DB USERNAME]
spring.datasource.password={YOUR DB PASSWORD]
```
Also add environmental variable SECRET_KEY=[SECRET]

To access **swagger**, you need to follow this link:
```http://localhost:8080/swagger-ui/index.html```
If you need to be authenticated to use an endpoint, then you may use "Authorize" button on swagger page and type in "Bearer your-actual-token" and save this. Unfortunately for now on, in order to get your token you need to use postman, because swagger doesn't get /login route - it is provided by auth0 and somehow hidden. 
