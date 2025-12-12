# ETC Backend

Backend desarrollado con Spring Boot y Maven, configurado para trabajar con PostgreSQL.

## Requisitos Previos

- Java 17 o superior
- Maven 3.6 o superior
- PostgreSQL 12 o superior

## Configuración de la Base de Datos

1. Asegúrate de tener PostgreSQL instalado y ejecutándose
2. Crea la base de datos:
   ```sql
   CREATE DATABASE etc_db;
   ```
3. Actualiza las credenciales en `src/main/resources/application.properties`:
   - `spring.datasource.username`: tu usuario de PostgreSQL
   - `spring.datasource.password`: tu contraseña de PostgreSQL
   - `spring.datasource.url`: ajusta el host y puerto si es necesario

## Ejecución

### Compilar el proyecto
```bash
mvn clean install
```

### Ejecutar la aplicación
```bash
mvn spring-boot:run
```

O ejecutar el JAR:
```bash
java -jar target/etc-backend-1.0.0.jar
```

La aplicación estará disponible en: `http://localhost:8080`

## Estructura del Proyecto

```
etc_backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/etc/backend/
│   │   │       └── EtcBackendApplication.java
│   │   └── resources/
│   │       └── application.properties
```markdown
# ETC Backend

Backend desarrollado con Spring Boot y Maven, configurado para trabajar con PostgreSQL.

## Requisitos Previos

- Java 17 o superior
- Maven 3.6 o superior
- PostgreSQL 12 o superior

## Configuración de la Base de Datos

1. Asegúrate de tener PostgreSQL instalado y ejecutándose
2. Crea la base de datos:
   ```sql
   CREATE DATABASE etc_db;
   ```
3. Actualiza las credenciales en `src/main/resources/application.properties`:
   - `spring.datasource.username`: tu usuario de PostgreSQL
   - `spring.datasource.password`: tu contraseña de PostgreSQL
   - `spring.datasource.url`: ajusta el host y puerto si es necesario

## Ejecución

### Compilar el proyecto
```bash
mvn clean install
```

### Ejecutar la aplicación
```bash
mvn spring-boot:run
```

O ejecutar el JAR:
```bash
java -jar target/etc-backend-1.0.0.jar
```

La aplicación estará disponible en: `http://localhost:8080`

## Estructura del Proyecto

```
etc_backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/etc/backend/
│   │   │       └── EtcBackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/etc/backend/
├── pom.xml
└── README.md
```

## Dependencias Principales

- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security
- JWT (JSON Web Tokens)
- PostgreSQL Driver
- Spring Boot Web
- Spring Boot Validation
- Lombok
- Spring Boot DevTools (desarrollo)

## Autenticación JWT

El proyecto incluye autenticación basada en JWT. Para autenticarte:

### Endpoint de Login
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "tu_usuario",
  "password": "tu_contraseña"
}
```

### Respuesta
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "tu_usuario",
  "email": "usuario@example.com",
  "rol": "ADMIN",
  "nombreCompleto": "Nombre Apellido"
}
```

### Uso del Token
Para acceder a endpoints protegidos, incluye el token en el header:
```
Authorization: Bearer <tu_token>
```

## APIs Disponibles

### Autenticación
- `POST /api/auth/login` - Iniciar sesión

### Usuarios
- `GET /api/usuarios` - Listar usuarios (requiere rol ADMIN)
- `GET /api/usuarios/{id}` - Obtener usuario por ID

### Estudiantes
- `GET /api/estudiantes` - Listar estudiantes (requiere rol ADMIN o DOCENTE)
- `GET /api/estudiantes/{id}` - Obtener estudiante por ID
- `GET /api/estudiantes/codigo/{codigo}` - Obtener estudiante por código

### Carreras
- `GET /api/carreras` - Listar carreras activas
- `GET /api/carreras/{id}` - Obtener carrera por ID

### Materias
- `GET /api/materias` - Listar materias activas
- `GET /api/materias/{id}` - Obtener materia por ID
- `GET /api/materias/carrera-sede/{carreraSedeId}` - Listar materias por carrera-sede

### Grupos
- `GET /api/grupos` - Listar grupos activos
- `GET /api/grupos/{id}` - Obtener grupo por ID
- `GET /api/grupos/periodo/{periodoAcademicoId}` - Listar grupos por período académico

## Estructura del Proyecto

```
etc_backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/etc/backend/
│   │   │       ├── config/          # Configuraciones (Security, etc.)
│   │   │       ├── controller/      # Controladores REST
│   │   │       ├── dto/             # DTOs (Data Transfer Objects)
│   │   │       ├── entity/          # Entidades JPA
│   │   │       ├── exception/       # Manejo de excepciones
│   │   │       ├── repository/      # Repositorios JPA
│   │   │       ├── security/        # Configuración de seguridad
│   │   │       ├── service/         # Servicios de negocio
│   │   │       └── util/            # Utilidades (JWT, etc.)
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## Entidades Principales

El proyecto incluye todas las entidades del esquema de base de datos:
- Roles, Sedes, Carreras, CarrerasSede
- PeriodosAcademico, Aulas, TiposEvaluacion
- Personas, Usuarios
- Materias, Prerrequisitos, Docentes
- Grupos, Horarios
- Estudiantes, Matriculas, Inscripciones
- Calificaciones, Asistencias
- NotasFinales, HistorialAcademico, Auditoria

## Notas

- El proyecto está configurado para usar `spring.jpa.hibernate.ddl-auto=update`, lo que significa que Hibernate actualizará automáticamente el esquema de la base de datos.
- Para producción, considera cambiar a `validate` o `none` y usar migraciones con Flyway o Liquibase.
- La configuración de JWT se encuentra en `application.properties` con las propiedades `jwt.secret` y `jwt.expiration`.
- El sistema incluye bloqueo automático de usuarios después de 5 intentos fallidos de login.


```

Para la documentación completa de endpoints y ejemplos JSON, consulta `docs/ENDPOINTS.md`.

