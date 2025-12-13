#+ Documentación detallada de endpoints

Este documento lista los endpoints públicos del proyecto, el método HTTP, requisitos de autorización cuando aplican y ejemplos JSON representativos de las respuestas.

- Notas generales:
- Todas las respuestas JSON usan fechas en formato ISO 8601 (ej. "2025-12-10T12:34:56").
- Muchas listas y relaciones JPA se devuelven como DTOs desde los controladores para evitar problemas de serialización (LazyInitializationException).
- En algunos endpoints se mantiene compatibilidad hacia el frontend devolviendo campos planos (ej. `materiaNombre`) y también un objeto anidado `materia` con datos mínimos cuando aplica.

## Autenticación

## Acceso y roles

Resumen de las reglas de acceso observadas en el código (Spring Security):

- Rutas públicas (sin autenticación):
  - /api/auth/** (login) — permitAll
  - /api/public/** — permitAll

- Rutas que requieren autenticación (cualquier usuario autenticado):
  - GET /api/materias
  - GET /api/materias/{id}
  - GET /api/materias/carrera-sede/{carreraSedeId}
  - GET /api/grupos
  - GET /api/grupos/{id}
  - GET /api/grupos/periodo/{periodoAcademicoId}
  - GET /api/dashboard/student (usa el usuario autenticado para buscar su dashboard)
  - GET /api/dashboard/teacher (usa el usuario autenticado para buscar su dashboard)
  - GET /api/dashboard/general (recomendado: admin en frontend, pero el endpoint está abierto a autenticados)

- Rutas con restricción por rol (anotaciones @PreAuthorize en controladores):
  - GET /api/usuarios  — requiere rol ADMIN (@PreAuthorize("hasRole('ADMIN')"))
  - GET /api/estudiantes — requiere rol ADMIN o DOCENTE (@PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE')"))

Si un usuario con rol `ADMIN` recibe un error con mensaje "Acceso denegado":
1) Verifica que el JWT/Authentication incluya la autoridad con prefijo `ROLE_` (ej. `ROLE_ADMIN`).
2) Revisa que el `UserDetailsService` esté cargando correctamente los roles desde la base de datos.
3) Antes de esta versión, una excepción de acceso podía llegar al `GlobalExceptionHandler` y convertirse en 500 — ahora está mapeada a 403. Si sigues viendo 500, revisa el log completo (stacktrace) para identificar si la excepción proviene de la capa de seguridad o de código del controlador/servicio.


- POST /api/auth/login
  - Descripción: autentica un usuario y devuelve un token JWT.
  - Request (application/json):
    ```json
    { "username": "tu_usuario", "password": "tu_contraseña" }
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

## Usuarios

- GET /api/usuarios
  - Descripción: lista los usuarios.
  - Autorización: requiere rol `ADMIN`.
  - Respuesta 200 (application/json) — ejemplo:
    ```json
    [
      {
        "id": 1,
        "username": "admin",
        "rol": { "id": 1, "nombre": "ADMIN" },
        "persona": { "id": 10, "nombres": "Juan", "apPaterno": "Pérez", "apMaterno": "Gómez", "email": "juan.perez@example.com" },
        "ultimoAcceso": "2025-12-10T12:34:56",
        "estado": true,
        "createdAt": "2025-01-01T10:00:00"
      }
    ]
    ```

- GET /api/usuarios/{id}
  - Descripción: devuelve un usuario por su ID (200) o 404 si no existe.

Notas: la entidad `Persona` usa campos `apPaterno` y `apMaterno` (no `apellidos`).

## Estudiantes

- GET /api/estudiantes
  - Descripción: lista los estudiantes.
  - Autorización: requiere rol `ADMIN` o `DOCENTE` según configuración.
  - Respuesta 200 (application/json) — ejemplo:
    ```json
    [
      {
        "id": 5,
        "usuario": { "id": 12, "username": "estudiante1" },
        "codigoEstudiante": "EST2025001",
        "fechaAdmision": "2021-03-15",
        "estadoAcademico": "Activo",
        "estado": true,
        "createdAt": "2021-03-15T09:00:00"
      }
    ]
    ```

- GET /api/estudiantes/{id}
  - Descripción: obtener estudiante por ID (200) o 404.

- GET /api/estudiantes/codigo/{codigo}
  - Descripción: obtener estudiante por código único (200) o 404.

## Carreras

- GET /api/carreras
  - Descripción: lista las carreras (por defecto activas).
  - Respuesta 200 — ejemplo:
    ```json
    [ { "id": 2, "codigo": "INGSIS", "nombre": "Ingeniería de Sistemas", "duracionSemestres": 10, "estado": true } ]
    ```

- GET /api/carreras/{id}
  - Descripción: obtener carrera por ID (200) o 404.

## Materias

- GET /api/materias
  - Descripción: lista materias (filtrables por `carreraSede` en controlador si aplica).
  - Respuesta 200 — ejemplo:
    ```json
    [
      {
        "id": 11,
        "carreraSede": { "id": 3 },
        "codigo": "MAT101",
        "nombre": "Matemáticas I",
        "semestre": 1,
        "creditos": 4,
        "estado": true
      }
    ]
    ```

- GET /api/materias/{id}
  - Descripción: obtener materia por ID (200) o 404.

- GET /api/materias/carrera-sede/{carreraSedeId}
  - Descripción: listar materias asociadas a una `carreraSede`.

## Grupos

Nota de compatibilidad: los controladores usan un DTO `GrupoSimpleResponse` que contiene campos planos (ej. `materiaId`, `materiaNombre`, `docenteId`, `docenteNombres`) y, para mantener compatibilidad con templates frontend antiguos, también puede incluir objetos anidados mínimos `materia` y `docente` con los campos esenciales.

- GET /api/grupos
  - Descripción: lista los grupos activos.
  - Respuesta 200 — ejemplo (DTO combinado):
    ```json
    [
      {
        "id": 21,
        "materiaId": 11,
        "materiaNombre": "Matemáticas I",
        "materia": { "id": 11, "nombre": "Matemáticas I" },
        "docenteId": 7,
        "docenteNombres": "Ana G. ",
        "docente": { "id": 7, "nombres": "Ana", "apPaterno": "García", "apMaterno": "Lopez" },
        "periodoAcademicoId": 2,
        "periodoAcademicoNombre": "2025-1",
        "nombre": "A",
        "cupoMaximo": 30,
        "cupoActual": 25,
        "estado": true,
        "createdAt": "2025-02-01T08:00:00"
      }
    ]
    ```

- GET /api/grupos/{id}
  - Descripción: obtener grupo por ID (200) o 404. Respuesta similar al ejemplo anterior.

- GET /api/grupos/periodo/{periodoAcademicoId}
  - Descripción: listar grupos del periodo indicado.

## Dashboard

Los endpoints del dashboard devuelven DTOs compactos según rol.

- GET /api/dashboard/student
  - Descripción: datos y estadísticas para el estudiante autenticado.
  - Respuesta 200 — ejemplo:
    ```json
    {
      "usuarioId": 12,
      "promedioGeneral": 85.75,
      "totalInscripciones": 6,
      "materiasInscritas": ["Matemáticas I", "Programación I"],
      "ultimoAcceso": "2025-12-10T12:34:56"
    }
    ```

- GET /api/dashboard/teacher
  - Descripción: estadísticas para el docente autenticado.
  - Respuesta 200 — ejemplo:
    ```json
    { "docenteId": 7, "usuarioId": 21, "materiasQueImparte": ["Programación I"], "totalGrupos": 3 }
    ```

- GET /api/dashboard/general
  - Descripción: estadísticas generales (recomendado: rol `ADMIN`).
  - Respuesta 200 — ejemplo:
    ```json
    { "estudiantesActivos": 432, "docentesActivos": 28, "usuariosTotal": 700, "materiasActivas": 120, "gruposActivos": 85 }
    ```

## Errores comunes

- 400 Bad Request: payload inválido o validación fallida. Body típico: texto con mensaje de error o un JSON con detalles de validación.
- 401 Unauthorized: token faltante o inválido.
- 403 Forbidden: usuario autenticado pero sin permisos (roles) necesarios.
- 404 Not Found: recurso inexistente.

## Notas de implementación y despliegue

- Si ves 5xx relacionados con serialización, revisar que los controladores devuelvan DTOs en lugar de entidades JPA completas.
- Para reducir ruido en logs de producción (por ejemplo, límites en Railway), ajustar `logging.level` a INFO o WARN para `org.springframework` y `com.etc.backend`.

Si quieres que formatee este documento para exportarlo (por ejemplo versión HTML o incluir ejemplos curl para cada endpoint), lo hago enseguida.


        ## Códigos de error comunes

        - 400 Bad Request: ejemplo de cuerpo (texto): "Error: <mensaje>"
        - 404 Not Found: no se devuelve JSON por defecto; endpoint suele responder con 404 status y body vacío.
        - 401 / 403: pueden devolver JSON de error o body vacío según la configuración de Spring Security.

        ---

        Estado: ejemplos JSON guardados en este archivo.
