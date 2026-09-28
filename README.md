# Responsive RESTful API — JWT Auth + DB Calculation

Spring Boot 3.3 / Java 17+ REST API that:

1. Accepts a JSON login, validates it against the database, and returns a **signed JWT**.
2. Exposes a protected endpoint that uses the JWT to identify the caller and computes
   `totalBill = baseBill + taxOrServiceCharge`.

The default profile uses **H2 in-memory**, so the app starts with zero setup.
MySQL and PostgreSQL drivers are included and pre-wired as separate profiles.

---

## 1. Project structure

```
src/main/java/com/example/restapi
├── RestapiApplication.java          # entry point
├── config/
│   ├── DataSeeder.java              # seeds 2 demo users at startup
│   └── SecurityConfig.java          # stateless JWT filter chain
├── controller/
│   ├── AuthController.java          # POST /api/auth/login
│   └── UserController.java          # GET  /api/user/total-bill
├── dto/                             # request/response records
├── entity/UserAccount.java          # JPA entity -> user_account table
├── exception/                       # custom exceptions + @RestControllerAdvice
├── repository/UserAccountRepository.java
├── security/
│   ├── JwtTokenProvider.java        # generate / parse / validate JWTs
│   └── JwtAuthenticationFilter.java # reads Authorization header
└── service/
    ├── AuthService.java
    └── UserBillService.java

src/main/resources
├── application.properties           # shared config (server port, JWT, etc.)
├── application-h2.properties        # default profile (in-memory DB)
├── application-mysql.properties     # --spring.profiles.active=mysql
└── application-postgres.properties  # --spring.profiles.active=postgres
```

## 2. Run it

Requires **JDK 17+** and **Maven 3.8+**.

```bash
# default — H2 in-memory, no DB install needed
mvn spring-boot:run

# MySQL — edit application-mysql.properties with your credentials,
#         then create the database (e.g. CREATE DATABASE restapidb;)
mvn spring-boot:run -Dspring-boot.run.profiles=mysql

# PostgreSQL — edit application-postgres.properties with your credentials,
#              then create the database (e.g. CREATE DATABASE restapidb;)
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

On startup the seeder logs two demo users:

| userId | password    | baseBill | taxOrServiceCharge |
|--------|-------------|----------|--------------------|
| alice  | password123 | 1000.00  | 150.00             |
| bob    | secret456   | 2500.50  | 375.25             |

## 3. API examples (cURL)

### 3.1 Login → JWT

```bash
curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"userId":"alice","password":"password123"}'
```

**200 OK**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZSIsImlhdCI6...",
  "tokenType": "Bearer",
  "expiresInMillis": 3600000,
  "userId": "alice"
}
```

**401 Unauthorized** (wrong password)
```json
{
  "status": 401,
  "error": "Invalid credentials",
  "message": "Invalid userId or password",
  "timestamp": "2026-01-01T12:00:00Z",
  "details": null
}
```

### 3.2 Calculate total bill (JWT required)

```bash
curl -X GET http://localhost:8080/api/user/total-bill \
     -H "Authorization: Bearer <paste-token-here>"
```

**200 OK**
```json
{
  "userId": "alice",
  "baseBill": 1000.0,
  "taxOrServiceCharge": 150.0,
  "totalBill": 1150.0
}
```

**401 Unauthorized** (missing or invalid token) — empty body.

### 3.3 Postman quick-start

1. `POST http://localhost:8080/api/auth/login`, **Body → raw → JSON**:
   `{"userId":"alice","password":"password123"}`
2. Copy `token` from the response.
3. `GET http://localhost:8080/api/user/total-bill`, **Headers**:
   `Authorization: Bearer <token>`.

## 4. Notes & next steps

* Passwords are stored in plaintext in the demo seed. In production, hash them with
  `BCryptPasswordEncoder` (already wired as a `PasswordEncoder` bean in `SecurityConfig`).
* The JWT secret in `application.properties` is a default for local use — override via the
  `JWT_SECRET` environment variable in production (must be Base64, ≥ 32 bytes decoded).
* All `/api/auth/**` routes are public; everything else needs a valid JWT.
* H2 web console (dev only): http://localhost:8080/h2-console  (JDBC URL `jdbc:h2:mem:restapidb`, user `sa`, no password).
