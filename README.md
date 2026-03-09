# URL Shortener Backend

This project is a simple URL Shortener backend built using Spring Boot and PostgreSQL.  
It allows users to generate a short URL for any long URL and retrieve the original URL using the short code.

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## Features

- Generate short URLs from long URLs
- Retrieve original URL using shortCode
- REST API based architecture
- Layered design (Controller → Service → Repository)

## API Endpoints

### Create Short URL

POST /api/shorten

Request Body:

{
"url": "https://google.com"
}

Response:

http://localhost:8080/8591fd7e

---

### Redirect to Original URL

GET /{shortCode}

Example:

http://localhost:8080/8591fd7e

Response:

Redirects to original URL.

## Project Structure
controller/
service/
repository/
model/
dto/


## Future Improvements

- Click analytics for short URLs
- Custom short codes
- URL expiration support

## How to Run

1. Clone the repository
2. Configure PostgreSQL in `application.properties`
3. Run the Spring Boot application

mvn spring-boot:run


## Author

Shivanand Madar
