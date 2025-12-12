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
          # Documentación detallada de endpoints

          Este documento lista los endpoints disponibles, el método HTTP, requisitos de autorización (cuando aplica) y ejemplos JSON de las respuestas. Incluye también los endpoints del dashboard por rol.

          Formato general:
          - Todas las respuestas se envían en JSON cuando corresponda.
          - Fechas: ISO 8601 (ej. "2025-12-10T12:34:56").

          ---

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

          ---

          ## Usuarios

          - GET /api/usuarios
            - Descripción: Lista todos los usuarios.
            - Autorización: requiere rol `ADMIN`.
            - Respuesta 200 (application/json):
              ```json
              [
                {
                  "id": 1,
                  "username": "admin",
                  "rol": { "id": 1, "nombre": "ADMIN" },
                  "persona": { "id": 10, "nombres": "Juan", "apellidos": "Pérez", "email": "juan.perez@example.com" },
                  "ultimoAcceso": "2025-12-10T12:34:56",
                  "intentosFallidos": 0,
                  "bloqueado": false,
                  "estado": true,
                  "createdAt": "2025-01-01T10:00:00",
                  "updatedAt": "2025-06-01T08:00:00"
                }
              ]
              ```

          - GET /api/usuarios/{id}
            - Descripción: Devuelve un usuario por su ID.
            - Respuesta 200 (application/json): objeto `Usuario` como en el ejemplo anterior. Si no existe: 404.

          ---

          ## Estudiantes

          - GET /api/estudiantes
            - Descripción: Lista los estudiantes.
            - Autorización: requiere rol `ADMIN` o `DOCENTE`.
            - Respuesta 200 (application/json):
              ```json
              [
                {
                  "id": 5,
                  "usuario": { "id": 12, "username": "estudiante1" },
                  "codigoEstudiante": "EST2025001",
                  "unidadEducativa": "Colegio Nacional",
                  "anoEgresoColegio": 2020,
                  "tipoAdmision": "Regular",
                  "fechaAdmision": "2021-03-15",
                  "estadoAcademico": "Activo",
                  "estado": true,
                  "createdAt": "2021-03-15T09:00:00"
                }
              ]
              ```

          - GET /api/estudiantes/{id}
            - Descripción: Obtener estudiante por ID. Respuesta 200 con el objeto `Estudiante` o 404 si no existe.

          - GET /api/estudiantes/codigo/{codigo}
            - Descripción: Obtener estudiante por su código único (`codigoEstudiante`). Respuesta 200 con objeto `Estudiante` o 404.

          ---

          ## Carreras

          - GET /api/carreras
            - Descripción: Lista las carreras activas (campo `estado = true`).
            - Respuesta 200 (application/json):
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

          - GET /api/carreras/{id}
            - Descripción: Obtener una carrera por ID. Respuesta 200 con objeto `Carrera` o 404.

          ---

          ## Materias

          - GET /api/materias
            - Descripción: Lista materias activas.
            - Respuesta 200 (application/json):
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
                  "estado": true,
                  "createdAt": "2024-03-01T09:00:00",
                  "updatedAt": "2024-08-01T10:00:00"
                }
              ]
              ```

          - GET /api/materias/{id}
            - Descripción: Obtener materia por ID. Respuesta 200 con objeto `Materia` o 404.

          - GET /api/materias/carrera-sede/{carreraSedeId}
            - Descripción: Listar materias asociadas a una `carreraSede`. Respuesta 200 con arreglo de `Materia`.

          ---

          ## Grupos

          - GET /api/grupos
            - Descripción: Lista los grupos activos.
            - Respuesta 200 (application/json):
              ```json
              [
                {
                  "id": 21,
                  "materia": { "id": 11, "nombre": "Matemáticas I", "codigo": "MAT101" },
                  "docente": { "id": 7, "persona": { "nombres": "Ana", "apellidos": "García" } },
                  "periodoAcademico": { "id": 2, "nombre": "2025-1" },
                  "nombre": "A",
                  "cupoMaximo": 30,
                  "cupoActual": 25,
                  "estado": true,
                  "createdAt": "2025-02-01T08:00:00"
                }
              ]
              ```

          - GET /api/grupos/{id}
            - Descripción: Obtener grupo por ID. Respuesta 200 con objeto `Grupo` o 404.

          - GET /api/grupos/periodo/{periodoAcademicoId}
            - Descripción: Listar grupos del período académico indicado. Respuesta 200 con arreglo de `Grupo`.

          ---

          ## Dashboard (endpoints nuevos)

          Los siguientes endpoints proporcionan datos resumidos para mostrar en un dashboard, diferenciados por rol.

          - GET /api/dashboard/student
            - Descripción: Estadísticas y datos para el estudiante autenticado.
            - Autorización: requiere autenticación (recomendado: rol `ESTUDIANTE`).
            - Respuesta 200 (StudentDashboardResponse):
              ```json
              {
                "usuarioId": 12,
                "promedioGeneral": 85.75,
                "totalInscripciones": 6,
                "materiasInscritas": ["Matemáticas I", "Programación I", "Física"],
                "ultimoAcceso": "2025-12-10T12:34:56"
              }
              ```

          - GET /api/dashboard/teacher
            - Descripción: Estadísticas para el docente autenticado.
            - Autorización: requiere autenticación (recomendado: rol `DOCENTE`).
            - Respuesta 200 (TeacherDashboardResponse):
              ```json
              {
                "docenteId": 7,
                "usuarioId": 21,
                "materiasQueImparte": ["Programación I", "Estructuras de Datos"],
                "totalGrupos": 3
              }
              ```

          - GET /api/dashboard/general
            - Descripción: Estadísticas generales del sistema (para administradores o dashboards globales).
            - Autorización: opcionalmente `ADMIN`.
            - Respuesta 200 (GeneralDashboardResponse):
              ```json
              {
                "estudiantesActivos": 432,
                "docentesActivos": 28,
                "usuariosTotal": 700,
                "materiasActivas": 120,
                "gruposActivos": 85
              }
              ```

          ---

          ## Respuestas de error comunes

          - 400 Bad Request: errores de validación o payload inválido. Ejemplo de cuerpo (texto):

            "Error: <mensaje>"

          - 401 Unauthorized: token faltante o inválido. Respuesta típicamente vacía o JSON de error según configuración.
          - 403 Forbidden: falta de permisos (roles) para acceder a un recurso.
          - 404 Not Found: recurso no encontrado (body puede estar vacío).

          ---

          Notas finales
          - Los ejemplos JSON son representativos y pueden variar según la configuración de serialización (Jackson), la inicialización de colecciones LAZY y los DTOs usados en controladores.
          - Si quieres, puedo:
            - Añadir campos adicionales al dashboard (asistencias, última calificación, créditos aprobados, etc.).
            - Añadir filtros y parámetros (por ejemplo `?periodo=2025-1` en `/api/dashboard/teacher`).

          Estado: `docs/ENDPOINTS.md` actualizado con la documentación limpia y los ejemplos JSON.

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
