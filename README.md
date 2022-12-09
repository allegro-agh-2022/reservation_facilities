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
