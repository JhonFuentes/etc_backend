# API Endpoints - Complete Reference

**Last Updated:** 2025-12-20  
**Status:** ✅ All 18 controllers implemented and compiled  
**Base URL:** `http://localhost:8080/api`

---

## Authentication & Health

### Health Check
```
GET /health
Method: GET
Public: Yes (no auth required)
Response: { "status": "UP" }
```

### Login
```
POST /auth/login
Method: POST
Public: Yes
Request Body:
{
  "username": "string",
  "password": "string"
}
Response:
{
  "token": "jwt_token_string",
  "role": "ROLE_ADMIN|ROLE_DIRECTOR|ROLE_SECRETARIA|ROLE_CAJERA|ROLE_DOCENTE|ROLE_ESTUDIANTE"
}
```

---

## Usuarios (Users) - `/api/usuarios`

| Method | Endpoint | Roles | Permission | Description |
|--------|----------|-------|-----------|-------------|
| GET | `/` | ADMIN | READ | List all usuarios |
| GET | `/{id}` | ADMIN | READ | Get specific usuario |
| GET | `/search` | ADMIN | READ | Search usuarios by username, email, rol, estado |
| POST | `/` | ADMIN | CREATE | Create new usuario |
| PUT | `/{id}` | ADMIN | UPDATE | Update usuario |
| DELETE | `/{id}` | ADMIN | DELETE | Delete usuario |

**Request/Response:**
- Request: `Usuario { id, username, rol, persona, estado }`
- Response: `UsuarioSimpleResponse { id, username, rolId, rolNombre, personaId, personaNombres, personaApellidos, etc }`

---

## Personas - `/api/personas`

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET | `/search` | ADMIN, SECRETARIA, DOCENTE | Search personas by cedula, nombres, email, ciudad |

**Response:** `PersonaResponse { id, cedula, nombres, apPaterno, apMaterno, email, telefono1, telefono2, ciudad, genero }`

---

## Docentes (Teachers) - `/api/docentes`

| Method | Endpoint | Roles | Permission | Description |
|--------|----------|-------|-----------|-------------|
| GET | `/` | ADMIN, DIRECTOR, SECRETARIA, DOCENTE | READ | List all docentes |
| GET | `/{id}` | ADMIN, DIRECTOR, SECRETARIA, DOCENTE | READ | Get specific docente |
| GET | `/search` | ADMIN, DIRECTOR, SECRETARIA, DOCENTE | READ | Search docentes by especialidad, tipoContrato, estado |
| POST | `/` | ADMIN, SECRETARIA | CREATE | Create new docente |
| PUT | `/{id}` | ADMIN, SECRETARIA | UPDATE | Update docente |
| DELETE | `/{id}` | ADMIN | DELETE | Delete docente |

**Response:** `DocenteResponse { id, usuarioId, usuarioUsername, personaId, personaNombres, personaApellidos, tituloProfesional, gradoAcademico, especialidad, tipoContrato }`

---

## Estudiantes (Students) - `/api/estudiantes`

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET | `/` | ADMIN, DIRECTOR, SECRETARIA | List all estudiantes |
| GET | `/{id}` | ADMIN, DIRECTOR, SECRETARIA | Get specific estudiante |
| GET | `/search` | ADMIN, SECRETARIA | Search by codigoEstudiante, estadoAcademico |

**Response:** `EstudianteResponse { id, usuarioId, usuarioUsername, personaId, personaNombres, codigoEstudiante, estadoAcademico, estado }`

---

## Carreras (Careers) - `/api/carreras`

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET | `/` | All | List all carreras |
| GET | `/{id}` | All | Get specific carrera |
| POST | `/` | ADMIN | Create carrera |
| PUT | `/{id}` | ADMIN | Update carrera |
| DELETE | `/{id}` | ADMIN | Delete carrera |

**Response:** `CarreraResponse { id, codigo, nombre, duracionSemestres, tituloOtorgado, estado }`

---

## Sedes (Campuses) - `/api/sedes`

| Method | Endpoint | Roles | Permission | Description |
|--------|----------|-------|-----------|-------------|
| GET | `/` | All | READ | List all sedes |
| GET | `/{id}` | All | READ | Get specific sede |
| GET | `/search` | All | READ | Search sedes by nombre, ciudad |
| POST | `/` | ADMIN | CREATE | Create sede |
| PUT | `/{id}` | ADMIN | UPDATE | Update sede |
| DELETE | `/{id}` | ADMIN | DELETE | Delete sede |

**Response:** `SedeResponse { id, nombre, direccion, telefono, email, estado }`

---

## Carreras-Sede - `/api/carreras-sede`

| Method | Endpoint | Roles | Permission | Description |
|--------|----------|-------|-----------|-------------|
| GET | `/` | ADMIN, DIRECTOR, SECRETARIA | READ | List all carrera-sede combinations |
| GET | `/{id}` | ADMIN, DIRECTOR, SECRETARIA | READ | Get specific carrera-sede |
| GET | `/search` | ADMIN, DIRECTOR, SECRETARIA | READ | Search by carreraId, sedeId |
| POST | `/` | ADMIN | CREATE | Create carrera-sede |
| PUT | `/{id}` | ADMIN | UPDATE | Update carrera-sede |
| DELETE | `/{id}` | ADMIN | DELETE | Delete carrera-sede |

**Response:** `CarreraSedeResponse { id, carreraId, carreraNombre, sedeId, sedeNombre, cupoMaximo }`

---

## Materias (Subjects) - `/api/materias`

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET | `/` | ADMIN, DIRECTOR, SECRETARIA | List all materias |
| GET | `/{id}` | ADMIN, DIRECTOR, SECRETARIA | Get specific materia |
| POST | `/` | ADMIN, SECRETARIA | Create materia |
| PUT | `/{id}` | ADMIN, SECRETARIA | Update materia |

**Response:** `MateriaResponse { id, codigo, nombre, semestre, creditos, horasTeoricas, horasPracticas, esElectiva }`

---

## Periodos Academicos (Academic Periods) - `/api/periodos-academicos`

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET | `/` | ADMIN, SECRETARIA | List all periodos |
| GET | `/{id}` | ADMIN, SECRETARIA | Get specific periodo |

**Response:** `PeriodoAcademicoResponse { id, gestion, periodo, nombre, fechaInicio, fechaFin, activo }`

---

## Grupos (Groups/Classes) - `/api/grupos`

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET | `/` | ADMIN, SECRETARIA | List all grupos |
| GET | `/{id}` | ADMIN, SECRETARIA | Get specific grupo |
| GET | `/search` | ADMIN, SECRETARIA | Search by materia, docente, periodo |

---

## Inscripciones (Enrollments) - `/api/inscripciones`

| Method | Endpoint | Roles | Permission | Description |
|--------|----------|-------|-----------|-------------|
| GET | `/` | ADMIN, SECRETARIA, DIRECTOR | READ | List inscripciones with filters |
| GET | `/{id}` | ADMIN, SECRETARIA, DIRECTOR | READ | Get specific inscripcion |
| POST | `/` | ADMIN, SECRETARIA, ESTUDIANTE* | CREATE | Create inscripcion (ESTUDIANTE if owner) |
| PUT | `/{id}` | ADMIN, SECRETARIA | UPDATE | Update inscripcion |
| DELETE | `/{id}` | ADMIN | DELETE | Delete inscripcion |

**Query Parameters:**
- `estudianteId`, `grupoId`, `matriculaId`, `estado`, `tipoInscripcion`, `desdeFecha`, `hastaFecha`

**Response:** `InscripcionResponse { id, estudianteId, grupoId, matriculaId, fechaInscripcion, tipoInscripcion, estado }`

---

## Matriculas (Enrollments/Registration) - `/api/matriculas`

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET | `/` | ADMIN, SECRETARIA | List all matriculas |
| GET | `/{id}` | ADMIN, SECRETARIA | Get specific matricula |
| POST | `/` | ADMIN, SECRETARIA | Create matricula |
| PUT | `/{id}` | ADMIN, SECRETARIA | Update matricula |

**Response:** `MatriculaResponse { id, estudianteId, periodoAcademicoId, fechaMatricula, estado }`

---

## Calificaciones (Grades) - `/api/calificaciones`

| Method | Endpoint | Roles | Permission | Description |
|--------|----------|-------|-----------|-------------|
| GET | `/` | ADMIN, DOCENTE | READ | List calificaciones with filters |
| POST | `/` | ADMIN, DOCENTE* | CREATE | Create grade (DOCENTE if owner of group) |
| PUT | `/{id}` | ADMIN, DOCENTE* | UPDATE | Update grade (DOCENTE if owner) |
| DELETE | `/{id}` | ADMIN | DELETE | Delete grade |

**Query Parameters:**
- `inscripcionId`, `tipoEvaluacionId`, `docenteId`, `minNota`, `maxNota`, `desdeFecha`, `hastaFecha`

**Response:** `CalificacionResponse { id, inscripcionId, tipoEvaluacionId, nota, fechaEvaluacion }`

---

## Asistencias (Attendance) - `/api/asistencias`

| Method | Endpoint | Roles | Permission | Description |
|--------|----------|-------|-----------|-------------|
| GET | `/` | ADMIN, DOCENTE | READ | List asistencias |
| POST | `/` | ADMIN, DOCENTE* | CREATE | Register attendance (DOCENTE if owner) |
| PUT | `/{id}` | ADMIN, DOCENTE* | UPDATE | Update attendance (DOCENTE if owner) |
| DELETE | `/{id}` | ADMIN | DELETE | Delete attendance |

**Response:** `AsistenciaResponse { id, inscripcionId, horarioId, fecha, estado, minutosTardanza }`

---

## Horarios (Schedules) - `/api/horarios`

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET | `/grupo/{grupoId}` | ADMIN, SECRETARIA, DOCENTE | Get schedules for a group |
| POST | `/` | ADMIN, SECRETARIA | Create horario |
| PUT | `/{id}` | ADMIN, SECRETARIA | Update horario |

**Response:** `HorarioResponse { id, grupoId, diaSemana, horaInicio, horaFin, aula }`

---

## Aulas (Classrooms) - `/api/aulas`

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET | `/` | ADMIN, SECRETARIA | List all aulas |
| GET | `/{id}` | ADMIN, SECRETARIA | Get specific aula |
| POST | `/` | ADMIN | Create aula |
| PUT | `/{id}` | ADMIN | Update aula |

**Response:** `AulaResponse { id, numero, piso, capacidad, ubicacion, estado }`

---

## Dashboard - `/api/dashboard`

| Method | Endpoint | Roles | Description |
|--------|----------|-------|-------------|
| GET | `/` | All authenticated | Get user's dashboard summary |

**Response:** Varies by role (ADMIN → system stats, DOCENTE → class summaries, ESTUDIANTE → personal grades)

---

## Response Status Codes

| Code | Meaning | Example |
|------|---------|---------|
| 200 | OK | Successful GET, PUT |
| 201 | Created | Successful POST |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Invalid request body |
| 401 | Unauthorized | Missing or invalid JWT token |
| 403 | Forbidden | User lacks permission for operation |
| 404 | Not Found | Resource doesn't exist |
| 500 | Server Error | Unhandled exception |

---

## Error Response Format

```json
{
  "message": "No tiene permiso para crear usuarios",
  "status": 403,
  "timestamp": "2025-12-20T22:00:00Z"
}
```

---

## Authentication Header

All protected endpoints require:
```
Authorization: Bearer <JWT_TOKEN>
```

**Example:**
```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  http://localhost:8080/api/usuarios
```

---

## Filtering Examples

### Example 1: Get Calificaciones with filters
```
GET /api/calificaciones?minNota=80&maxNota=100&desdeFecha=2025-01-01&hastaFecha=2025-12-31
```

### Example 2: Search Personas
```
GET /api/personas/search?nombres=Juan&email=juan@example.com&ciudad=La Paz
```

### Example 3: List Inscripciones by Estudiante
```
GET /api/inscripciones?estudianteId=5&estado=ACTIVA
```

---

## Rate Limiting & Pagination

- **Rate Limiting:** Not yet implemented (recommended: 100 req/min per user)
- **Pagination:** Not yet implemented (recommended: add `page` and `size` parameters)

---

## Deprecated Endpoints

None currently.

---

## Future Enhancements

- [ ] Add pagination to list endpoints
- [ ] Add rate limiting per user/role
- [ ] Add sorting parameters (orderBy, sortDirection)
- [ ] Add more granular timestamp filtering
- [ ] Add batch operations (POST multiple items)
- [ ] Add partial update support (PATCH)

---

**For detailed permission matrix, see:** `docs/PERMISSION_MATRIX_REFERENCE.md`  
**For implementation details, see:** `docs/IMPLEMENTATION_SUMMARY.md`  
**For database schema, see:** `docs/DATABASE_ANALYSIS.md`
