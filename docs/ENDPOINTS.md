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
        ## ENDPOINTS - API ETC Backend

        Este documento presenta, por recurso, cada endpoint con su ruta, método HTTP, roles autorizados y la forma de la respuesta (DTO resumido). Está pensado para ser un resumen práctico: endpoint → método → roles → respuesta.

        Notas rápidas:
        - Las respuestas devueltas por los controladores están normalizadas como DTOs (ej. `UsuarioResponse`, `EstudianteResponse`, `InscripcionResponse`, etc.) para evitar serializar entidades JPA completas.
        - Los roles en las anotaciones usan el prefijo `ROLE_` (p. ej. `ROLE_ADMIN`, `ROLE_DOCENTE`, `ROLE_ESTUDIANTE`, `ROLE_SECRETARIA`, `ROLE_DIRECTOR`).
        - Para restricciones de propiedad (ownership) se usa un bean `securityService` con métodos como `isDocenteOfInscripcion(...)`, `isEstudianteOwner(...)` y similares en `@PreAuthorize`.

        Formato por entrada:
        - Path: (método)
        - Roles: lista de roles autorizados o expresión @PreAuthorize indicada
        - Respuesta: nombre del DTO / esquema resumido (campos principales)

        ----------------------------------------

        ## Autenticación

        - /api/auth/login (POST)
          - Roles: public (no auth)
          - Request: { "username": "...", "password": "..." }
          - Respuesta 200: { "token": "<jwt>", "type": "Bearer", "id": 1, "username": "...", "email": "...", "rol": "ADMIN", "nombreCompleto": "..." }

        ## Salud

        - /api/health (GET)
          - Roles: public
          - Respuesta 200: { "status": "UP" }

        ## Usuarios

        - /api/usuarios (GET)
          - Roles: ROLE_ADMIN
          - Respuesta 200: [ UsuarioResponse ]
            - UsuarioResponse: { id, username, rol: {id, nombre}, persona: {id, nombres, apPaterno, apMaterno, email}, ultimoAcceso, estado, createdAt }

        - /api/usuarios/{id} (GET)
          - Roles: ROLE_ADMIN
          - Respuesta 200: UsuarioResponse

        - /api/usuarios (POST)
          - Roles: ROLE_ADMIN
          - Request: UsuarioCreateRequest { username, password, personaId, rolId }
          - Respuesta 201: UsuarioResponse

        - /api/usuarios/{id} (PUT)
          - Roles: ROLE_ADMIN
          - Request: UsuarioUpdateRequest
          - Respuesta 200: UsuarioResponse

        - /api/usuarios/{id} (DELETE)
          - Roles: ROLE_ADMIN
          - Respuesta 204: vacío

        ## Personas (si aplica)

        - /api/personas/{id} (GET)
          - Roles: ROLE_ADMIN, ROLE_SECRETARIA (u otros según implementación)
          - Respuesta 200: PersonaResponse { id, nombres, apPaterno, apMaterno, dni, email, telefono }

        ## Estudiantes

        - /api/estudiantes (GET)
          - Roles: ROLE_ADMIN, ROLE_DOCENTE
          - Respuesta 200: [ EstudianteResponse ]
            - EstudianteResponse: { id, usuario: {id, username}, codigoEstudiante, fechaAdmision, estadoAcademico, estado, createdAt }

        - /api/estudiantes/{id} (GET)
          - Roles: ROLE_ADMIN, ROLE_DOCENTE, ROLE_ESTUDIANTE (propio)
          - Ejemplo @PreAuthorize: hasRole('ADMIN') or hasAnyRole('DOCENTE') or (hasRole('ESTUDIANTE') and @securityService.isEstudianteOwner(authentication, #id))
          - Respuesta 200: EstudianteResponse

        - /api/estudiantes/codigo/{codigo} (GET)
          - Roles: ROLE_ADMIN, ROLE_DOCENTE
          - Respuesta 200: EstudianteResponse

        ## Docentes

        - /api/docentes (GET)
          - Roles: ROLE_ADMIN
          - Respuesta 200: [ DocenteResponse ]
            - DocenteResponse: { id, usuario: {id, username}, nombres, apPaterno, apMaterno, especialidad, estado }

        - /api/docentes/{id} (GET)
          - Roles: ROLE_ADMIN, ROLE_DIRECTOR, ROLE_DOCENTE (propio)
          - Respuesta 200: DocenteResponse

        ## Carreras / Sedes / CarreraSede

        - /api/carreras (GET)
          - Roles: autenticados
          - Respuesta 200: [ CarreraResponse ]

        - /api/carreras/{id} (GET)
          - Roles: autenticados
          - Respuesta 200: CarreraResponse

        - /api/carrera-sede/{id} (GET)
          - Roles: autenticados
          - Respuesta 200: CarreraSedeResponse { id, carreraId, sedeId, codigo, nombre }

        ## Materias

        - /api/materias (GET)
          - Roles: autenticados
          - Query: opcional `carreraSedeId`
          - Respuesta 200: [ MateriaResponse ]
            - MateriaResponse: { id, carreraSede: {id}, codigo, nombre, semestre, creditos, estado }

        - /api/materias/{id} (GET)
          - Roles: autenticados
          - Respuesta 200: MateriaResponse

        - /api/materias/carrera-sede/{carreraSedeId} (GET)
          - Roles: autenticados
          - Respuesta 200: [ MateriaResponse ]

        ## Grupos

        - /api/grupos (GET)
          - Roles: autenticados
          - Respuesta 200: [ GrupoSimpleResponse ]
            - GrupoSimpleResponse: { id, materiaId, materiaNombre, materia: {id,nombre}?, docenteId, docenteNombres, docente: {id,nombres,apPaterno,apMaterno}?, periodoAcademicoId, periodoAcademicoNombre, nombre, cupoMaximo, cupoActual, estado, createdAt }

        - /api/grupos/{id} (GET)
          - Roles: autenticados
          - Respuesta 200: GrupoSimpleResponse

        - /api/grupos/periodo/{periodoAcademicoId} (GET)
          - Roles: autenticados
          - Respuesta 200: [ GrupoSimpleResponse ]

        - /api/grupos (POST)
          - Roles: ROLE_ADMIN, ROLE_SECRETARIA
          - Request: GrupoRequest { materiaId, docenteId, periodoAcademicoId, nombre, cupoMaximo }
          - Respuesta 201: GrupoSimpleResponse

        - /api/grupos/{id} (PUT/DELETE)
          - Roles: ROLE_ADMIN
          - Respuesta: 200/204 según acción

        ## Inscripciones

        - /api/inscripciones (POST)
          - Roles / validación (@PreAuthorize):
            - ADMIN y SECRETARIA pueden crear para cualquier estudiante.
            - ESTUDIANTE puede crear sólo para sí mismo: @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARIA') or (hasRole('ESTUDIANTE') and @securityService.isEstudianteOwner(authentication, #req.estudianteId))")
          - Request: InscripcionRequest { estudianteId, grupoId, matriculaId }
          - Respuesta 201: InscripcionResponse { id, estudianteId, grupoId, matriculaId, fechaInscripcion, tipoInscripcion, estado }

        - /api/inscripciones (GET)
          - Roles: ROLE_ADMIN, ROLE_SECRETARIA, ROLE_DIRECTOR
          - Respuesta 200: [ InscripcionResponse ]

        - /api/inscripciones/{id} (GET)
          - Roles / validación (@PreAuthorize):
            - ADMIN, SECRETARIA, DIRECTOR pueden ver cualquiera.
            - DOCENTE puede ver sólo si está vinculado: @securityService.isDocenteOfInscripcion(authentication, #id)
            - ESTUDIANTE puede ver sólo su propia inscripción: @securityService.isInscripcionOwnedByStudent(authentication, #id)
          - Respuesta 200: InscripcionResponse

        - /api/inscripciones/{id} (PUT)
          - Roles: ROLE_ADMIN, ROLE_SECRETARIA
          - Respuesta 200: InscripcionResponse (actualizada)

        - /api/inscripciones/{id} (DELETE)
          - Roles: ROLE_ADMIN
          - Respuesta 204: vacío

        ## Matrículas

        - /api/matriculas (POST)
          - Roles: ROLE_ADMIN, ROLE_SECRETARIA
          - Request: MatriculaRequest { estudianteId, carreraSedeId, periodoAcademicoId, semestreCursando, montoMatricula }
          - Respuesta 201: MatriculaResponse { id, estudianteId, carreraSedeId, periodoAcademicoId, semestreCursando, monto }

        - /api/matriculas/{id} (GET)
          - Roles: autenticados (lectura dependiendo del uso) / administradores
          - Respuesta 200: MatriculaResponse

        - /api/matriculas/{id} (PUT/DELETE)
          - Roles: ROLE_ADMIN, ROLE_SECRETARIA (PUT), ROLE_ADMIN (DELETE)

        ## Calificaciones

        - /api/calificaciones (POST)
          - Roles / validación: ADMIN o (DOCENTE y pertenece al grupo/inscripción)
            - Ejemplo @PreAuthorize: @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCENTE') and @securityService.isDocenteOfInscripcion(authentication, #req.inscripcionId))")
          - Request: CalificacionRequest { inscripcionId, tipoEvaluacionId, nota, fechaEvaluacion }
          - Respuesta 201: CalificacionResponse { id, inscripcionId, tipoEvaluacionId, nota, fechaEvaluacion, creadoPor }

        - /api/calificaciones/{id} (PUT)
          - Roles: ADMIN o (DOCENTE y es docente de la calificación) — @securityService.isDocenteOfCalificacion(authentication, #id)
          - Respuesta 200: CalificacionResponse

        - /api/calificaciones/{id} (DELETE)
          - Roles: ROLE_ADMIN
          - Respuesta 204: vacío

        ## Asistencias

        - /api/asistencias (POST)
          - Roles / validación: ADMIN o (DOCENTE y pertenece al grupo correspondiente)
            - Ejemplo @PreAuthorize: @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCENTE') and @securityService.isDocenteOfAsistencia(authentication, #req.grupoId))")
          - Request: AsistenciaRequest { inscripcionId, horarioId, fecha, estado, minutosTardanza, observaciones }
          - Respuesta 201: AsistenciaResponse { id, inscripcionId, horarioId, fecha, estado, minutosTardanza, observaciones }

        - /api/asistencias/{id} (PUT)
          - Roles: ADMIN o DOCENTE (propio) — validar con isDocenteOfAsistencia(authentication, #id)
          - Respuesta 200: AsistenciaResponse

        ## Aulas

        - /api/aulas (GET)
          - Roles: autenticados
          - Respuesta 200: [ AulaResponse ] { id, sedeId, nombre, capacidad, tipo, estado }

        - /api/aulas (POST)
          - Roles: ROLE_ADMIN, ROLE_SECRETARIA
          - Request: AulaRequest { sedeId, nombre, capacidad, tipo }
          - Respuesta 201: AulaResponse

        ## Horarios

        - /api/horarios/grupo/{grupoId} (GET)
          - Roles: autenticados
          - Respuesta 200: [ HorarioResponse ] { id, grupoId, aulaId, diaSemana, horaInicio, horaFin, tipo }

        - /api/horarios (POST)
          - Roles: ROLE_ADMIN, ROLE_SECRETARIA, ROLE_DIRECTOR
          - Request: HorarioRequest { grupoId, aulaId, diaSemana, horaInicio, horaFin, tipo }
          - Respuesta 201: HorarioResponse

        ## Dashboard

        - /api/dashboard/student (GET)
          - Roles: ROLE_ESTUDIANTE
          - Respuesta 200: DashboardStudentResponse { usuarioId, promedioGeneral, totalInscripciones, materiasInscritas, ultimoAcceso }

        - /api/dashboard/teacher (GET)
          - Roles: ROLE_DOCENTE
          - Respuesta 200: DashboardTeacherResponse { docenteId, usuarioId, materiasQueImparte, totalGrupos }

        - /api/dashboard/general (GET)
          - Roles: ROLE_ADMIN (recomendado)
          - Respuesta 200: DashboardGeneralResponse { estudiantesActivos, docentesActivos, usuariosTotal, materiasActivas, gruposActivos }

        ## Códigos de error y comportamiento

        - 200 OK: petición exitosa, cuerpo JSON con DTO indicado.
        - 201 Created: recurso creado, Location header con la ruta del nuevo recurso y cuerpo con el DTO creado.
        - 204 No Content: operación exitosa sin cuerpo (DELETE, etc.).
        - 400 Bad Request: payload inválido o validación fallida (cuerpo con mensaje/errores).
        - 401 Unauthorized: token faltante o inválido.
        - 403 Forbidden: usuario autenticado pero sin permiso para la acción (se usan 403 para AccessDenied ahora).
        - 404 Not Found: recurso inexistente.

       

        Archivo actualizado: `docs/ENDPOINTS.md`.

