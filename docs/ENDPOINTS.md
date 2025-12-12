# Documentación detallada de endpoints

A continuación se listan todos los endpoints disponibles en la API, su método HTTP, requisitos de autorización cuando aplica, y un ejemplo del formato JSON que devuelven.

## Autenticación

- POST /api/auth/login
  - Descripción: Autentica un usuario y devuelve un token JWT.
  - Request (application/json):
    ```json
    {
      "username": "tu_usuario",
      "password": "tu_contraseña"
    }
    ```
  - Respuesta 200 (application/json):
    ```json
    {
      "token": "<jwt_token>",
      "type": "Bearer",
      "id": 1,
      "username": "tu_usuario",
      "email": "usuario@example.com",
      "rol": "ADMIN",
      "nombreCompleto": "Nombre Apellido"
    }
    ```
  - Errores: 400 con body tipo texto: `"Error: <mensaje>"`.

## Usuarios

- GET /api/usuarios
  - Descripción: Lista todos los usuarios.
  - Autorización: requiere rol `ADMIN`.
  - Respuesta 200 (application/json): arreglo de usuarios. Ejemplo (cada usuario puede incluir relaciones anidadas como `rol` y `persona`):
    ```json
    [
      {
        "id": 1,
        "username": "admin",
        "rol": { "id": 1, "nombre": "ADMIN" },
        "persona": { "id": 10, "nombres": "Juan", "apellidos": "Pérez" },
        "ultimoAcceso": "2025-12-10T12:34:56",
        # Documentación detallada de endpoints (JSON de respuesta)

        Este archivo contiene ejemplos JSON completos que devuelven los endpoints públicos del proyecto. Incluye objetos anidados cuando aplica.

        ---

        POST /api/auth/login
        - Respuesta 200 (JwtResponse)
        ```json
        {
          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
          "type": "Bearer",
          "id": 1,
          "username": "admin",
          "email": "admin@example.com",
          "rol": "ADMIN",
          "nombreCompleto": "Juan Pérez"
        }
        ```

        ---

        GET /api/usuarios
        - Respuesta 200 (List<Usuario>)
        ```json
        [
          {
            "id": 1,
            "username": "admin",
            "rol": {
              "id": 1,
              "nombre": "ADMIN",
              "descripcion": "Administrador del sistema"
            },
            "persona": {
              "id": 10,
              "nombres": "Juan",
              "apellidos": "Pérez",
              "email": "juan.perez@example.com",
              "telefono": null,
              "dni": "12345678"
            },
            "ultimoAcceso": "2025-12-10T12:34:56",
            "intentosFallidos": 0,
            "bloqueado": false,
            "estado": true,
            "createdAt": "2024-01-01T09:00:00",
            "updatedAt": "2025-06-01T08:00:00"
          }
        ]
        ```

        ---

        GET /api/usuarios/{id}
        - Respuesta 200 (Usuario)
        ```json
        {
          "id": 1,
          "username": "admin",
          "rol": { "id": 1, "nombre": "ADMIN" },
          "persona": { "id": 10, "nombres": "Juan", "apellidos": "Pérez", "email": "juan.perez@example.com" },
          "ultimoAcceso": "2025-12-10T12:34:56",
          "intentosFallidos": 0,
          "bloqueado": false,
          "estado": true,
          "createdAt": "2024-01-01T09:00:00",
          "updatedAt": "2025-06-01T08:00:00"
        }
        ```

        ---

        GET /api/estudiantes
        - Respuesta 200 (List<Estudiante>)
        ```json
        [
          {
            "id": 5,
            "usuario": {
              "id": 12,
              "username": "estudiante1",
              "persona": { "id": 22, "nombres": "María", "apellidos": "Gómez", "email": "maria.gomez@example.com" }
            },
            "codigoEstudiante": "EST2025001",
            "unidadEducativa": "Colegio Nacional",
            "añoEgresoColegio": 2020,
            "tipoAdmision": "Regular",
            "fechaAdmision": "2021-03-15",
            "estadoAcademico": "Activo",
            "estado": true,
            "createdAt": "2021-03-15T09:00:00"
          }
        ]
        ```

        ---

        GET /api/estudiantes/{id}
        - Respuesta 200 (Estudiante)
        ```json
        {
          "id": 5,
          "usuario": { "id": 12, "username": "estudiante1", "persona": { "id": 22, "nombres": "María", "apellidos": "Gómez" } },
          "codigoEstudiante": "EST2025001",
          "unidadEducativa": "Colegio Nacional",
          "añoEgresoColegio": 2020,
          "tipoAdmision": "Regular",
          "fechaAdmision": "2021-03-15",
          "estadoAcademico": "Activo",
          "estado": true,
          "createdAt": "2021-03-15T09:00:00"
        }
        ```

        ---

        GET /api/estudiantes/codigo/{codigo}
        - Respuesta 200 (Estudiante)
        ```json
        {
          "id": 5,
          "usuario": { "id": 12, "username": "estudiante1" },
          "codigoEstudiante": "EST2025001",
          "unidadEducativa": "Colegio Nacional",
          "añoEgresoColegio": 2020,
          "tipoAdmision": "Regular",
          "fechaAdmision": "2021-03-15",
          "estadoAcademico": "Activo",
          "estado": true,
          "createdAt": "2021-03-15T09:00:00"
        }
        ```

        ---

        GET /api/carreras
        - Respuesta 200 (List<Carrera>)
        ```json
        [
          {
            "id": 2,
            "codigo": "INGSIS",
            "nombre": "Ingeniería de Sistemas",
            "duracionSemestres": 10,
            "tituloOtorgado": "Ingeniero de Sistemas",
            "estado": true,
            "createdAt": "2020-02-01T10:00:00",
            "updatedAt": "2025-01-01T08:00:00"
          }
        ]
        ```

        ---

        GET /api/carreras/{id}
        - Respuesta 200 (Carrera)
        ```json
        {
          "id": 2,
          "codigo": "INGSIS",
          "nombre": "Ingeniería de Sistemas",
          "duracionSemestres": 10,
          "tituloOtorgado": "Ingeniero de Sistemas",
          "estado": true,
          "createdAt": "2020-02-01T10:00:00",
          "updatedAt": "2025-01-01T08:00:00"
        }
        ```

        ---

        GET /api/materias
        - Respuesta 200 (List<Materia>)
        ```json
        [
          {
            "id": 11,
            "carreraSede": {
              "id": 3,
              "carrera": { "id": 2, "nombre": "Ingeniería de Sistemas" },
              "sede": { "id": 1, "nombre": "Sede Central" }
            },
            "codigo": "MAT101",
            "nombre": "Matemáticas I",
            "semestre": 1,
            "horasTeoricas": 3,
            "horasPracticas": 2,
            "creditos": 4,
            "esElectiva": false,
            "estado": true,
            "createdAt": "2024-03-01T09:00:00",
            "updatedAt": "2024-08-01T10:00:00"
          }
        ]
        ```

        ---

        GET /api/materias/{id}
        - Respuesta 200 (Materia)
        ```json
        {
          "id": 11,
          "carreraSede": { "id": 3 },
          "codigo": "MAT101",
          "nombre": "Matemáticas I",
          "semestre": 1,
          "horasTeoricas": 3,
          "horasPracticas": 2,
          "creditos": 4,
          "esElectiva": false,
          "estado": true,
          "createdAt": "2024-03-01T09:00:00",
          "updatedAt": "2024-08-01T10:00:00"
        }
        ```

        ---

        GET /api/materias/carrera-sede/{carreraSedeId}
        - Respuesta 200 (List<Materia>)
        ```json
        [
          {
            "id": 11,
            "carreraSede": { "id": 3 },
            "codigo": "MAT101",
            "nombre": "Matemáticas I",
            "semestre": 1,
            "horasTeoricas": 3,
            "horasPracticas": 2,
            "creditos": 4,
            "esElectiva": false,
            "estado": true
          }
        ]
        ```

        ---

        GET /api/grupos
        - Respuesta 200 (List<Grupo>)
        ```json
        [
          {
            "id": 21,
            "materia": { "id": 11, "codigo": "MAT101", "nombre": "Matemáticas I" },
            "docente": { "id": 7, "persona": { "id": 50, "nombres": "Ana", "apellidos": "García" } },
            "periodoAcademico": { "id": 2, "nombre": "2025-1", "fechaInicio": "2025-02-01", "fechaFin": "2025-06-30" },
            "nombre": "A",
            "cupoMaximo": 30,
            "cupoActual": 25,
            "estado": true,
            "createdAt": "2025-02-01T08:00:00"
          }
        ]
        ```

        ---

        GET /api/grupos/{id}
        - Respuesta 200 (Grupo)
        ```json
        {
          "id": 21,
          "materia": { "id": 11, "codigo": "MAT101", "nombre": "Matemáticas I" },
          "docente": { "id": 7, "persona": { "nombres": "Ana", "apellidos": "García" } },
          "periodoAcademico": { "id": 2, "nombre": "2025-1" },
          "nombre": "A",
          "cupoMaximo": 30,
          "cupoActual": 25,
          "estado": true,
          "createdAt": "2025-02-01T08:00:00"
        }
        ```

        ---

        GET /api/grupos/periodo/{periodoAcademicoId}
        - Respuesta 200 (List<Grupo>)
        ```json
        [
          {
            "id": 21,
            "materia": { "id": 11, "nombre": "Matemáticas I", "codigo": "MAT101" },
            "docente": { "id": 7 },
            "periodoAcademico": { "id": 2, "nombre": "2025-1" },
            "nombre": "A",
            "cupoMaximo": 30,
            "cupoActual": 25,
            "estado": true
          }
        ]
        ```

        ---

        ## Códigos de error comunes

        - 400 Bad Request: ejemplo de cuerpo (texto): "Error: <mensaje>"
        - 404 Not Found: no se devuelve JSON por defecto; endpoint suele responder con 404 status y body vacío.
        - 401 / 403: pueden devolver JSON de error o body vacío según la configuración de Spring Security.

        ---

        Estado: ejemplos JSON guardados en este archivo.
