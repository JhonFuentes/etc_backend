# Análisis Detallado de la Estructura de Base de Datos

**Fecha:** 20 de diciembre de 2025  
**Versión:** 1.0  

---

## 1. OVERVIEW - Estructura General

La base de datos está diseñada para gestionar un sistema académico integral para una institución educativa. Se organiza en **4 capas principales**:

1. **Capa de Administración y Seguridad** (Personas, Usuarios, Roles)
2. **Capa Académica** (Carreras, Sedes, Materias, PeriodosAcadémicos)
3. **Capa de Recursos Humanos** (Estudiantes, Docentes)
4. **Capa de Operaciones Académicas** (Grupos, Inscripciones, Matrículas, Calificaciones, Asistencias, Aulas, Horarios)

---

## 2. TABLAS CORE Y SUS RELACIONES

### 2.1 **PERSONAS** (`personas`)
**Propósito:** Almacenar datos demográficos de todos los usuarios del sistema.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `cedula` | VARCHAR(20) | UNIQUE, NOT NULL | CI/Pasaporte, identificador natural |
| `nombres` | VARCHAR(100) | NOT NULL | Nombres de pila |
| `ap_paterno` | VARCHAR(50) | NOT NULL | Apellido paterno |
| `ap_materno` | VARCHAR(50) | - | Apellido materno (opcional) |
| `fecha_nac` | DATE | NOT NULL | Fecha de nacimiento |
| `genero` | ENUM(M,F,O) | NOT NULL | Género: Masculino, Femenino, Otro |
| `email` | VARCHAR(100) | UNIQUE, NOT NULL | Correo electrónico |
| `telefono1` | VARCHAR(15) | NOT NULL | Teléfono principal |
| `telefono2` | VARCHAR(15) | - | Teléfono secundario |
| `domicilio` | VARCHAR(200) | - | Dirección de residencia |
| `ciudad` | VARCHAR(50) | - | Ciudad de residencia |
| `pais` | VARCHAR(50) | DEFAULT 'Bolivia' | País, por defecto Bolivia |
| `foto_url` | VARCHAR(255) | - | URL de fotografía de perfil |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Registro activo/inactivo |
| `created_at` | DATETIME | AUTO | Timestamp de creación |
| `updated_at` | DATETIME | AUTO | Timestamp de última actualización |

**Índices:**
- PK: `id`
- UNIQUE: `cedula`, `email`

**Relaciones:**
- 1:1 con `usuarios` (cada persona tiene a lo sumo un usuario)
- 1:N con `usuarios` (si aplicara, en una futura estructura flexible)

---

### 2.2 **ROLES** (`roles`)
**Propósito:** Definir los roles de autorización disponibles en el sistema.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `nombre` | VARCHAR(50) | UNIQUE, NOT NULL | Nombre del rol (p.ej. "Administrador", "Docente") |
| `descripcion` | TEXT | - | Descripción del rol y sus responsabilidades |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Rol activo/inactivo |
| `created_at` | DATETIME | AUTO | Timestamp de creación |
| `updated_at` | DATETIME | AUTO | Timestamp de última actualización |

**Roles esperados en el sistema:**
- `Administrador` → normaliza a `ROLE_ADMIN` (acceso total)
- `Director` → `ROLE_DIRECTOR` (acceso académico y administrativo)
- `Docente` → `ROLE_DOCENTE` (acceso a calificaciones, asistencias de sus grupos)
- `Estudiante` → `ROLE_ESTUDIANTE` (acceso a inscripciones propias)
- `Secretaria` → `ROLE_SECRETARIA` (acceso administrativo limitado)

---

### 2.3 **USUARIOS** (`usuarios`)
**Propósito:** Gestionar credenciales de autenticación y mapeo a personas y roles.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `username` | VARCHAR(50) | UNIQUE, NOT NULL | Nombre de usuario para login |
| `password` | VARCHAR(255) | NOT NULL | Hash bcrypt de la contraseña |
| `rol_id` (FK) | INT | NOT NULL | Referencia a `roles.id` |
| `persona_id` (FK) | INT | NOT NULL, UNIQUE | Referencia 1:1 a `personas.id` |
| `ultimo_acceso` | DATETIME | - | Último login registrado |
| `intentos_fallidos` | SMALLINT | DEFAULT=0 | Contador de intentos fallidos |
| `bloqueado` | BOOLEAN | NOT NULL, DEFAULT=false | Usuario bloqueado tras múltiples intentos |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Cuenta activa/inactiva |
| `created_at` | DATETIME | AUTO | Timestamp de creación |
| `updated_at` | DATETIME | AUTO | Timestamp de última actualización |

**Índices:**
- PK: `id`
- UNIQUE: `username`, `persona_id`
- FK: `rol_id` → `roles(id)`

**Relaciones:**
- N:1 con `roles` (muchos usuarios, un rol)
- 1:1 con `personas` (cada usuario tiene una persona asociada)

---

### 2.4 **SEDES** (`sedes`)
**Propósito:** Gestionar ubicaciones físicas donde se ofrecen programas académicos.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `nombre` | VARCHAR(100) | NOT NULL | Nombre de la sede (p.ej. "Sede Central") |
| `direccion` | VARCHAR(200) | - | Dirección física |
| `telefono` | VARCHAR(15) | - | Teléfono de contacto |
| `email` | VARCHAR(80) | - | Correo de la sede |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Sede operativa/inactiva |
| `created_at` | DATETIME | AUTO | Timestamp de creación |
| `updated_at` | DATETIME | AUTO | Timestamp de última actualización |

**Relaciones:**
- 1:N con `carreras_sedes` (una sede puede ofrecer múltiples carrera-sede combinaciones)
- 1:N con `aulas` (una sede tiene múltiples aulas)

---

### 2.5 **CARRERAS** (`carreras`)
**Propósito:** Definir programas académicos ofrecidos.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `codigo` | VARCHAR(10) | UNIQUE, NOT NULL | Código único de carrera (p.ej. "INGSIS") |
| `nombre` | VARCHAR(100) | NOT NULL | Nombre completo (p.ej. "Ingeniería de Sistemas") |
| `duracion_semestres` | SMALLINT | NOT NULL | Duración en semestres (p.ej. 10) |
| `titulo_otorgado` | VARCHAR(150) | - | Título que se otorga tras finalización |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Carrera ofertada/retirada |
| `created_at` | DATETIME | AUTO | Timestamp de creación |
| `updated_at` | DATETIME | AUTO | Timestamp de última actualización |

**Relaciones:**
- 1:N con `carreras_sedes` (una carrera se ofrece en múltiples sedes)

---

### 2.6 **CARRERAS_SEDES** (`carreras_sedes`)
**Propósito:** Relación de junction entre Carreras y Sedes, permitiendo que una carrera se ofrezca en múltiples sedes.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `carrera_id` (FK) | INT | NOT NULL | Referencia a `carreras.id` |
| `sede_id` (FK) | INT | NOT NULL | Referencia a `sedes.id` |
| `codigo` | VARCHAR(20) | - | Código específico de la combinación carrera-sede |
| `nombre` | VARCHAR(150) | - | Nombre único para esta combinación |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Combinación activa/inactiva |
| `created_at` | DATETIME | AUTO | Timestamp de creación |
| `updated_at` | DATETIME | AUTO | Timestamp de última actualización |

**Índices:**
- UNIQUE: (carrera_id, sede_id)
- FK: `carrera_id` → `carreras(id)`, `sede_id` → `sedes(id)`

**Relaciones:**
- N:1 con `carreras`
- N:1 con `sedes`
- 1:N con `materias` (una carrera-sede ofrece múltiples materias)
- 1:N con `matriculas` (estudiantes se matriculan en una carrera-sede)

---

### 2.7 **MATERIAS** (`materias`)
**Propósito:** Definir cursos académicos dentro de una carrera.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `carrera_sede_id` (FK) | INT | NOT NULL | Referencia a `carreras_sedes.id` |
| `codigo` | VARCHAR(10) | UNIQUE, NOT NULL | Código único (p.ej. "MAT101") |
| `nombre` | VARCHAR(100) | NOT NULL | Nombre del curso (p.ej. "Matemáticas I") |
| `semestre` | SMALLINT | NOT NULL | Semestre recomendado (1-10) |
| `horas_teoricas` | SMALLINT | DEFAULT=0 | Horas de teoría por semana |
| `horas_practicas` | SMALLINT | DEFAULT=0 | Horas de práctica por semana |
| `creditos` | SMALLINT | NOT NULL | Créditos académicos |
| `es_electiva` | BOOLEAN | NOT NULL, DEFAULT=false | Materia obligatoria/electiva |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Materia activa/retirada |
| `created_at` | DATETIME | AUTO | Timestamp de creación |
| `updated_at` | DATETIME | AUTO | Timestamp de última actualización |

**Índices:**
- PK: `id`
- UNIQUE: `codigo`
- FK: `carrera_sede_id` → `carreras_sedes(id)`

**Relaciones:**
- N:1 con `carreras_sedes`
- 1:N con `grupos` (una materia puede tener múltiples grupos)
- 1:N con `prerrequisitos` (una materia puede tener prerrequisitos)

---

### 2.8 **PERIODOS_ACADEMICOS** (`periodos_academicos`)
**Propósito:** Definir períodos de tiempo en que se ofrecen académicos (semestrales, anuales, etc.).

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `gestion` | INT | NOT NULL | Año académico (p.ej. 2025) |
| `periodo` | SMALLINT | NOT NULL | Período dentro del año (1=primer semestre, 2=segundo) |
| `nombre` | VARCHAR(50) | NOT NULL | Nombre del período (p.ej. "2025-1") |
| `fecha_inicio` | DATE | NOT NULL | Fecha de inicio del período |
| `fecha_fin` | DATE | NOT NULL | Fecha de fin del período |
| `fecha_inicio_inscripciones` | DATE | - | Inicio del período de inscripción |
| `fecha_fin_inscripciones` | DATE | - | Fin del período de inscripción |
| `activo` | BOOLEAN | NOT NULL, DEFAULT=false | Período académico actual |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Período registrado/eliminado |
| `created_at` | DATETIME | AUTO | Timestamp de creación |

**Índices:**
- UNIQUE: (gestion, periodo)

**Relaciones:**
- 1:N con `grupos` (un período tiene múltiples grupos)
- 1:N con `matriculas` (estudiantes se matriculan en un período)

---

### 2.9 **ESTUDIANTES** (`estudiantes`)
**Propósito:** Extender información de estudiantes vinculados a un usuario.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `usuario_id` (FK) | INT | NOT NULL, UNIQUE | Referencia 1:1 a `usuarios.id` |
| `codigo_estudiante` | VARCHAR(20) | UNIQUE, NOT NULL | Carnet de estudiante (p.ej. "EST2025001") |
| `unidad_educativa` | VARCHAR(150) | - | Colegio de procedencia |
| `año_egreso_colegio` | INT | - | Año de egreso de colegio |
| `tipo_admision` | VARCHAR(30) | - | Modalidad de admisión (Examen, Exonerado, etc.) |
| `fecha_admision` | DATE | - | Fecha de ingreso al programa |
| `estado_academico` | ENUM | DEFAULT=Activo | Estado: Activo, Egresado, Retirado, Suspendido |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Registro activo/inactivo |
| `created_at` | DATETIME | AUTO | Timestamp de creación |

**Índices:**
- PK: `id`
- UNIQUE: `usuario_id`, `codigo_estudiante`
- FK: `usuario_id` → `usuarios(id)`

**Relaciones:**
- 1:1 con `usuarios` (un estudiante por usuario)
- 1:N con `inscripciones` (un estudiante se inscribe en múltiples grupos)
- 1:N con `matriculas` (un estudiante se matricula en múltiples períodos)

---

### 2.10 **DOCENTES** (`docentes`)
**Propósito:** Extender información de docentes vinculados a un usuario.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `usuario_id` (FK) | INT | NOT NULL, UNIQUE | Referencia 1:1 a `usuarios.id` |
| `titulo_profesional` | VARCHAR(150) | NOT NULL | Título académico principal |
| `grado_academico` | VARCHAR(50) | - | Grado: Licenciado, Máster, Doctor, etc. |
| `especialidad` | VARCHAR(100) | - | Área de especialización |
| `fecha_contratacion` | DATE | - | Fecha de ingreso a la institución |
| `tipo_contrato` | VARCHAR(30) | - | Tipo: Tiempo Completo, Medio Tiempo, etc. |
| `curriculum_url` | VARCHAR(255) | - | URL del CV |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Docente activo/inactivo |
| `created_at` | DATETIME | AUTO | Timestamp de creación |

**Índices:**
- PK: `id`
- UNIQUE: `usuario_id`
- FK: `usuario_id` → `usuarios(id)`

**Relaciones:**
- 1:1 con `usuarios` (un docente por usuario)
- 1:N con `grupos` (un docente enseña múltiples grupos)

---

### 2.11 **GRUPOS** (`grupos`)
**Propósito:** Agrupar estudiantes en secciones de una materia para un período académico.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `materia_id` (FK) | INT | NOT NULL | Referencia a `materias.id` |
| `docente_id` (FK) | INT | NOT NULL | Referencia a `docentes.id` (profesor a cargo) |
| `periodo_academico_id` (FK) | INT | NOT NULL | Referencia a `periodos_academicos.id` |
| `nombre` | VARCHAR(50) | NOT NULL | Nombre del grupo (p.ej. "A", "B", "Turno Vespertino") |
| `cupo_maximo` | INT | DEFAULT=30 | Capacidad máxima de estudiantes |
| `cupo_actual` | INT | DEFAULT=0 | Número actual de inscritos |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Grupo activo/inactivo |
| `created_at` | DATETIME | AUTO | Timestamp de creación |

**Índices:**
- PK: `id`
- UNIQUE: (materia_id, periodo_academico_id, nombre)
- FK: `materia_id` → `materias(id)`, `docente_id` → `docentes(id)`, `periodo_academico_id` → `periodos_academicos(id)`

**Relaciones:**
- N:1 con `materias`
- N:1 con `docentes` (un docente puede tener múltiples grupos)
- N:1 con `periodos_academicos`
- 1:N con `inscripciones` (un grupo tiene múltiples inscripciones)
- 1:N con `horarios` (un grupo tiene múltiples horarios semanales)

---

### 2.12 **MATRICULAS** (`matriculas`)
**Propósito:** Registrar la matrícula de un estudiante en una carrera-sede para un período académico.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `estudiante_id` (FK) | INT | NOT NULL | Referencia a `estudiantes.id` |
| `carrera_sede_id` (FK) | INT | NOT NULL | Referencia a `carreras_sedes.id` |
| `periodo_academico_id` (FK) | INT | NOT NULL | Referencia a `periodos_academicos.id` |
| `fecha_matricula` | DATE | NOT NULL, DEFAULT=NOW() | Fecha de registro |
| `semestre_cursando` | SMALLINT | NOT NULL | Semestre actual (1-10) |
| `tipo_matricula` | ENUM | DEFAULT=Regular | Tipo: Regular, Especial, Oyente |
| `monto_matricula` | DECIMAL(10,2) | - | Monto a pagar |
| `monto_pagado` | DECIMAL(10,2) | DEFAULT=0 | Monto ya pagado |
| `estado_pago` | ENUM | DEFAULT=Pendiente | Pendiente, Pagado, Parcial |
| `estado` | ENUM | DEFAULT=Activa | Activa, Retirada, Anulada, Finalizada |
| `observaciones` | TEXT | - | Notas adicionales |
| `created_at` | DATETIME | AUTO | Timestamp de creación |
| `updated_at` | DATETIME | AUTO | Timestamp de última actualización |

**Índices:**
- UNIQUE: (estudiante_id, periodo_academico_id)
- FK: `estudiante_id` → `estudiantes(id)`, `carrera_sede_id` → `carreras_sedes(id)`, `periodo_academico_id` → `periodos_academicos(id)`

**Relaciones:**
- N:1 con `estudiantes`
- N:1 con `carreras_sedes`
- N:1 con `periodos_academicos`
- 1:N con `inscripciones` (una matrícula agrupa múltiples inscripciones)

---

### 2.13 **INSCRIPCIONES** (`inscripciones`)
**Propósito:** Registrar la inscripción de un estudiante en un grupo (materia específica) dentro de una matrícula.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `estudiante_id` (FK) | INT | NOT NULL | Referencia a `estudiantes.id` |
| `grupo_id` (FK) | INT | NOT NULL | Referencia a `grupos.id` |
| `matricula_id` (FK) | INT | NOT NULL | Referencia a `matriculas.id` (contexto) |
| `fecha_inscripcion` | DATE | NOT NULL, DEFAULT=NOW() | Fecha de inscripción |
| `tipo_inscripcion` | ENUM | DEFAULT=Regular | Regular, Segunda_matrícula, Tercera_matrícula |
| `estado` | ENUM | DEFAULT=Inscrito | Inscrito, Retirado, Aprobado, Reprobado |
| `fecha_retiro` | DATE | - | Fecha si se retira |
| `motivo_retiro` | TEXT | - | Razón del retiro |
| `created_at` | DATETIME | AUTO | Timestamp de creación |

**Índices:**
- PK: `id`
- UNIQUE: (estudiante_id, grupo_id)
- FK: `estudiante_id` → `estudiantes(id)`, `grupo_id` → `grupos(id)`, `matricula_id` → `matriculas(id)`

**Relaciones:**
- N:1 con `estudiantes`
- N:1 con `grupos`
- N:1 con `matriculas`
- 1:N con `calificaciones` (una inscripción tiene múltiples calificaciones)
- 1:N con `asistencias` (una inscripción tiene múltiples registros de asistencia)

---

### 2.14 **AULAS** (`aulas`)
**Propósito:** Gestionar espacios físicos donde se imparten clases.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `sede_id` (FK) | INT | NOT NULL | Referencia a `sedes.id` |
| `nombre` | VARCHAR(50) | NOT NULL | Nombre del aula (p.ej. "A-101", "Laboratorio 3") |
| `capacidad` | INT | - | Capacidad máxima de personas |
| `tipo` | VARCHAR(30) | - | Tipo: Aula Teórica, Laboratorio, Taller, etc. |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Aula disponible/no disponible |
| `created_at` | DATETIME | AUTO | Timestamp de creación |

**Índices:**
- PK: `id`
- FK: `sede_id` → `sedes(id)`

**Relaciones:**
- N:1 con `sedes`
- 1:N con `horarios` (un aula tiene múltiples horarios)

---

### 2.15 **HORARIOS** (`horarios`)
**Propósito:** Definir los horarios de clase para cada grupo.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `grupo_id` (FK) | INT | NOT NULL | Referencia a `grupos.id` |
| `aula_id` (FK) | INT | NOT NULL | Referencia a `aulas.id` |
| `dia_semana` | SMALLINT | NOT NULL | Día: 1=Lunes, 7=Domingo |
| `hora_inicio` | TIME | NOT NULL | Hora de inicio de clase |
| `hora_fin` | TIME | NOT NULL | Hora de fin de clase |
| `tipo` | ENUM | - | Tipo: Teórico, Práctico, Mixto |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Horario activo/inactivo |
| `created_at` | DATETIME | AUTO | Timestamp de creación |

**Índices:**
- PK: `id`
- FK: `grupo_id` → `grupos(id)`, `aula_id` → `aulas(id)`

**Relaciones:**
- N:1 con `grupos`
- N:1 con `aulas`
- 1:N con `asistencias` (un horario puede tener múltiples registros de asistencia)

---

### 2.16 **TIPO_EVALUACION** (`tipo_evaluacion`)
**Propósito:** Definir tipos de evaluación (examen, tarea, trabajo práctico, etc.).

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `nombre` | VARCHAR(50) | NOT NULL, UNIQUE | Tipo: Examen Parcial, Examen Final, Tarea, etc. |
| `porcentaje` | DECIMAL(5,2) | - | Porcentaje de la nota final |
| `descripcion` | TEXT | - | Descripción del tipo |
| `estado` | BOOLEAN | NOT NULL, DEFAULT=true | Tipo activo/inactivo |
| `created_at` | DATETIME | AUTO | Timestamp de creación |

**Relaciones:**
- 1:N con `calificaciones` (múltiples calificaciones de un tipo)

---

### 2.17 **CALIFICACIONES** (`calificaciones`)
**Propósito:** Registrar las evaluaciones y notas de estudiantes en sus inscripciones.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `inscripcion_id` (FK) | INT | NOT NULL | Referencia a `inscripciones.id` |
| `tipo_evaluacion_id` (FK) | INT | NOT NULL | Referencia a `tipo_evaluacion.id` |
| `nota` | DECIMAL(5,2) | NOT NULL | Nota (0-100 o 0-20) |
| `nota_maxima` | DECIMAL(5,2) | DEFAULT=100 | Máximo posible para la evaluación |
| `fecha_evaluacion` | DATE | - | Fecha de realización |
| `fecha_registro` | DATE | NOT NULL, DEFAULT=NOW() | Fecha de registro en sistema |
| `registrado_por` (FK) | INT | - | Referencia a `usuarios.id` (quien registró) |
| `observaciones` | TEXT | - | Comentarios adicionales |
| `estado` | ENUM | DEFAULT=Definitiva | Definitiva, Provisional, Anulada |
| `created_at` | DATETIME | AUTO | Timestamp de creación |
| `updated_at` | DATETIME | AUTO | Timestamp de última actualización |

**Índices:**
- UNIQUE: (inscripcion_id, tipo_evaluacion_id)
- FK: `inscripcion_id` → `inscripciones(id)`, `tipo_evaluacion_id` → `tipo_evaluacion(id)`, `registrado_por` → `usuarios(id)`

**Relaciones:**
- N:1 con `inscripciones`
- N:1 con `tipo_evaluacion`
- N:1 con `usuarios` (docente que registró)

---

### 2.18 **ASISTENCIAS** (`asistencias`)
**Propósito:** Registrar la asistencia de estudiantes a sesiones de clase.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `inscripcion_id` (FK) | INT | NOT NULL | Referencia a `inscripciones.id` |
| `horario_id` (FK) | INT | NOT NULL | Referencia a `horarios.id` (sesión específica) |
| `fecha` | DATE | NOT NULL | Fecha de la clase |
| `estado` | ENUM | DEFAULT=PRESENTE | PRESENTE, AUSENTE, TARDANZA, EXCUSA |
| `minutos_tardanza` | INT | DEFAULT=0 | Minutos de atraso (si aplica) |
| `observaciones` | TEXT | - | Notas adicionales |
| `registrado_por` (FK) | INT | - | Referencia a `usuarios.id` (quien registró) |
| `created_at` | DATETIME | AUTO | Timestamp de creación |
| `updated_at` | DATETIME | AUTO | Timestamp de última actualización |

**Índices:**
- FK: `inscripcion_id` → `inscripciones(id)`, `horario_id` → `horarios(id)`, `registrado_por` → `usuarios(id)`

**Relaciones:**
- N:1 con `inscripciones`
- N:1 con `horarios`
- N:1 con `usuarios` (docente que registró)

---

### 2.19 **AUDITORIA** (`auditoria`)
**Propósito:** Registrar cambios en registros críticos para auditoría y trazabilidad.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `entidad` | VARCHAR(50) | NOT NULL | Nombre de la tabla (p.ej. "inscripciones") |
| `id_registro` | INT | NOT NULL | ID del registro modificado |
| `operacion` | ENUM | NOT NULL | INSERT, UPDATE, DELETE |
| `usuario_id` (FK) | INT | - | Referencia a `usuarios.id` (quién hizo el cambio) |
| `datos_anteriores` | JSON | - | Estado previo (para UPDATE/DELETE) |
| `datos_nuevos` | JSON | - | Estado nuevo (para INSERT/UPDATE) |
| `fecha` | DATETIME | NOT NULL, DEFAULT=NOW() | Timestamp del cambio |
| `ip_address` | VARCHAR(45) | - | IP del usuario que hizo el cambio |

**Relaciones:**
- N:1 con `usuarios`

---

### 2.20 **NOTAS_FINALES** (`notas_finales`)
**Propósito:** Almacenar la nota final consolidada de cada inscripción (si aplica cálculo automático).

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `inscripcion_id` (FK) | INT | NOT NULL, UNIQUE | Referencia a `inscripciones.id` |
| `nota_final` | DECIMAL(5,2) | - | Nota final (promedio ponderado) |
| `estado_final` | ENUM | - | APROBADO, REPROBADO, PENDIENTE |
| `fecha_calculo` | DATETIME | - | Cuándo se calculó |
| `observaciones` | TEXT | - | Comentarios finales |
| `updated_at` | DATETIME | AUTO | Última actualización |

**Índices:**
- UNIQUE: `inscripcion_id`
- FK: `inscripcion_id` → `inscripciones(id)`

---

### 2.21 **HISTORIAL_ACADEMICO** (`historial_academico`)
**Propósito:** Mantener un historial consolidado de desempeño de estudiantes.

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `estudiante_id` (FK) | INT | NOT NULL | Referencia a `estudiantes.id` |
| `periodo_academico_id` (FK) | INT | NOT NULL | Referencia a `periodos_academicos.id` |
| `promedio_periodo` | DECIMAL(5,2) | - | Promedio en ese período |
| `creditos_aprobados` | INT | - | Créditos aprobados en el período |
| `creditos_inscritos` | INT | - | Créditos inscritos en el período |
| `estado_academico` | ENUM | - | Buen desempeño, Bajo desempeño, etc. |
| `observaciones` | TEXT | - | Notas |
| `created_at` | DATETIME | AUTO | Timestamp de creación |
| `updated_at` | DATETIME | AUTO | Timestamp de última actualización |

**Índices:**
- FK: `estudiante_id` → `estudiantes(id)`, `periodo_academico_id` → `periodos_academicos(id)`

---

### 2.22 **PRERREQUISITOS** (`prerrequisitos`)
**Propósito:** Definir dependencias entre materias (p.ej. Cálculo I es prerrequisito de Cálculo II).

| Campo | Tipo | Restricciones | Notas |
|-------|------|---------------|-------|
| `id` (PK) | INT | AUTO_INCREMENT | Identificador único |
| `materia_id` (FK) | INT | NOT NULL | Referencia a `materias.id` (materia que requiere) |
| `materia_prerequisito_id` (FK) | INT | NOT NULL | Referencia a `materias.id` (materia requerida) |
| `tipo` | ENUM | - | APROBACION, COAPROBACION, COREQUISITO |
| `created_at` | DATETIME | AUTO | Timestamp de creación |

**Índices:**
- FK: `materia_id` → `materias(id)`, `materia_prerequisito_id` → `materias(id)`

---

## 3. DIAGRAMA DE RELACIONES (Texto)

```
┌─────────────┐
│   PERSONAS  │ (base de datos demográfica)
│─────────────│
│ id (PK)     │
│ cedula (U)  │
│ nombres     │
│ email (U)   │
└────────┬────┘
         │ 1:1 (persona_id)
         │
┌────────▼──────────┐
│    USUARIOS        │ (autenticación y autorización)
│────────────────────│
│ id (PK)            │
│ username (U)       │
│ password           │
│ rol_id (FK)────────┐
│ persona_id (FK)    │ (1:1 - cada usuario asociado a persona)
│ estado             │
└────┬───────────────┘
     │               ┌──────────┐
     │               │   ROLES  │ (definición de permisos)
     └──────────────▶│──────────│
                     │ id (PK)  │
                     │ nombre   │
                     └──────────┘

┌────────────────┐
│   ESTUDIANTES  │ (extensión de Usuario)
│────────────────│
│ id (PK)        │
│ usuario_id (FK)│ (1:1)
│ código         │
└────┬───────────┘
     │ 1:N
     │     ┌──────────────────┐
     │     │   INSCRIPCIONES  │ (estudiante en un grupo)
     │     │──────────────────│
     ├────▶│ id (PK)          │
     │     │ estudiante_id(FK)│
     │     │ grupo_id (FK)    │
     │     │ matricula_id (FK)│
     │     │ fecha_inscripcion│
     │     │ estado           │
     │     └────┬─────────────┘
     │          │ 1:N
     │          │        ┌─────────────────┐
     │          │        │  CALIFICACIONES │ (nota evaluada)
     │          │        │─────────────────│
     │          │        │ id (PK)         │
     │          │        │ inscripcion_id(F│
     │          │        │ tipo_eval_id(FK)│
     │          │        │ nota            │
     │          │        └─────────────────┘
     │          │
     │          └───────▶ ASISTENCIAS (registro de presencia)
     │                   
     │
     └───────────────────┐
                         │ 1:N
                    ┌────▼────────────┐
                    │   MATRICULAS    │ (estudiante en carrera-semestre)
                    │─────────────────│
                    │ id (PK)         │
                    │ estudiante_id(FK)│
                    │ carrera_sede_id(F│
                    │ periodo_acad_id(F│
                    │ estado_pago      │
                    └─────────────────┘

┌──────────────┐
│   DOCENTES   │ (extensión de Usuario)
│──────────────│
│ id (PK)      │
│ usuario_id(FK)│ (1:1)
│ título       │
└────┬─────────┘
     │ 1:N
     │     ┌──────────┐
     │     │  GRUPOS  │ (sección de una materia)
     │     │──────────│
     │     │ id (PK)  │
     ├────▶│ docente_id│
     │     │ materia_id│
     │     │ periodo.. │
     │     └────┬─────┘
     │          │
     └──────────┘

┌──────────┐
│  CARRERAS│ (programa académico)
│──────────│
│ id (PK)  │
│ nombre   │
│ código   │
└────┬─────┘
     │ N:N (via CARRERAS_SEDES)
     │        ┌─────────────────┐
     │        │  CARRERAS_SEDES │ (carrera en sede)
     │        │─────────────────│
     ├───────▶│ id (PK)         │
     │        │ carrera_id (FK) │
     │        │ sede_id (FK)    │
     │        └────┬────────────┘
     │             │ 1:N
     │             │        ┌──────────┐
     │             │        │ MATERIAS │ (cursos)
     │             │        │──────────│
     │             │        │ id (PK)  │
     │             │        │carrera..=│
     │             │        │ código   │
     │             │        │ nombre   │
     │             │        │ semestre │
     │             │        └────┬─────┘
     │             │             │ 1:N
     │             │             │
     │             │        ┌────▼──────┐
     │             │        │   GRUPOS  │
     │             │        └───────────┘
     │             │
     │             └──────▶ MATRICULAS
     │                      (estudiante en carrera-semestre)
     │
     │
    └────────────────────┐
                         │ N (via CARRERAS_SEDES)
                    ┌────▼────┐
                    │  SEDES   │ (ubicaciones físicas)
                    │──────────│
                    │ id (PK)  │
                    │ nombre   │
                    └────┬─────┘
                         │ 1:N
                         │
                    ┌────▼────┐
                    │  AULAS   │ (espacios)
                    │──────────│
                    │ id (PK)  │
                    │ nombre   │
                    │ capacidad│
                    └────┬─────┘
                         │ 1:N
                         │
                    ┌────▼────────┐
                    │  HORARIOS   │ (sesiones de clase)
                    │─────────────│
                    │ id (PK)     │
                    │ grupo_id (F)│
                    │ aula_id (FK)│
                    │ dia_semana  │
                    │ hora_inicio │
                    │ hora_fin    │
                    └─────────────┘

┌────────────────────┐
│ PERIODOS_ACADEMICOS│ (períodos de tiempo)
│────────────────────│
│ id (PK)            │
│ gestion            │
│ periodo            │
│ nombre (2025-1)    │
│ fecha_inicio       │
│ fecha_fin          │
└────┬───────────────┘
     │ 1:N
     ├──────────────▶ GRUPOS
     └──────────────▶ MATRICULAS
```

---

## 4. DATOS CRÍTICOS Y RESTRICCIONES

### 4.1 Restricciones de Integridad
- **Unicidad de CI (cedula):** Cada persona tiene un CI único.
- **Unicidad de Email:** Cada persona tiene un correo único.
- **1:1 Usuario-Persona:** Cada usuario está vinculado a exactamente una persona.
- **1:1 Usuario-Estudiante:** Un estudiante tiene exactamente un usuario.
- **1:1 Usuario-Docente:** Un docente tiene exactamente un usuario.
- **Grupo único en período:** No se permite (materia, período, nombre) duplicado.
- **Inscripción única:** No se permite (estudiante, grupo) duplicado (cada estudiante se inscribe una sola vez por grupo).
- **Matrícula única por período:** (estudiante, período) es única.

### 4.2 Enumeraciones Importantes
- **Persona.Genero:** M, F, O
- **Estudiante.EstadoAcademico:** Activo, Egresado, Retirado, Suspendido
- **Inscripcion.TipoInscripcion:** Regular, Segunda_matrícula, Tercera_matrícula
- **Inscripcion.EstadoInscripcion:** Inscrito, Retirado, Aprobado, Reprobado
- **Matricula.TipoMatricula:** Regular, Especial, Oyente
- **Matricula.EstadoPago:** Pendiente, Pagado, Parcial
- **Matricula.EstadoMatricula:** Activa, Retirada, Anulada, Finalizada
- **Asistencia.EstadoAsistencia:** PRESENTE, AUSENTE, TARDANZA, EXCUSA
- **Calificacion.EstadoCalificacion:** Definitiva, Provisional, Anulada
- **Horario.TipoHorario:** (según implementación; típicamente Teórico, Práctico, Mixto)

---

## 5. ÍNDICES RECOMENDADOS (para performance)

### Índices Frecuentes por Filtro
```sql
-- Búsquedas por estudiante
CREATE INDEX idx_inscripciones_estudiante ON inscripciones(estudiante_id);
CREATE INDEX idx_matriculas_estudiante ON matriculas(estudiante_id);
CREATE INDEX idx_calificaciones_estudiante ON calificaciones(inscripcion_id);
CREATE INDEX idx_asistencias_estudiante ON asistencias(inscripcion_id);

-- Búsquedas por docente
CREATE INDEX idx_grupos_docente ON grupos(docente_id);
CREATE INDEX idx_horarios_grupo ON horarios(grupo_id);

-- Búsquedas por período
CREATE INDEX idx_grupos_periodo ON grupos(periodo_academico_id);
CREATE INDEX idx_matriculas_periodo ON matriculas(periodo_academico_id);

-- Búsquedas por fecha
CREATE INDEX idx_inscripciones_fecha ON inscripciones(fecha_inscripcion);
CREATE INDEX idx_calificaciones_fecha ON calificaciones(fecha_evaluacion);
CREATE INDEX idx_asistencias_fecha ON asistencias(fecha);

-- Búsquedas por carrera-sede
CREATE INDEX idx_materias_carrera_sede ON materias(carrera_sede_id);
CREATE INDEX idx_matriculas_carrera_sede ON matriculas(carrera_sede_id);
```

---

## 6. FLUJOS DE NEGOCIO CLAVE

### 6.1 Inscripción de Estudiante
1. Estudiante existe con estado Activo.
2. Se crea una `Matricula` para el estudiante en carrera-sede y período (si no existe).
3. Se crea una `Inscripcion` asociando estudiante → grupo (materia) → matrícula.
4. Se actualiza `cupo_actual` del grupo.

### 6.2 Registro de Calificación
1. Docente registra nota en `Calificaciones` para inscripción específica.
2. Se asocia tipo de evaluación (parcial, final, tarea, etc.).
3. Sistema puede calcular automáticamente `NotaFinal` si aplica.
4. Estado se actualiza en `Inscripcion` (Aprobado/Reprobado).

### 6.3 Registro de Asistencia
1. Docente marca asistencia en sesión específica (horario).
2. Se registra en `Asistencias` con estado (PRESENTE, AUSENTE, TARDANZA).
3. Sistema puede calcular automáticamente inasistencia (si > N%).

### 6.4 Cierre de Período
1. Se calculan notas finales para todas las inscripciones.
2. Se actualiza estado académico de estudiantes (`HistorialAcademico`).
3. Período marcado como inactivo.

---

## 7. NOTAS Y OBSERVACIONES

- **Soft Deletes:** El sistema usa campos `estado = boolean` (soft deletes) en lugar de borrados físicos para auditoría.
- **Auditoría:** La tabla `Auditoria` permite registrar todos los cambios para cumplimiento y trazabilidad.
- **Timestamps:** Todas las tablas tienen `created_at` y algunos tienen `updated_at` para control de cambios.
- **Lazy Loading:** Las relaciones JPA usan `FetchType.LAZY` para evitar sobrecarga de queries; debe tener cuidado en DTOs y serialización.
- **Enumeraciones:** Almacenadas como ENUM SQL y enum Java para seguridad de tipos.
- **BigDecimal para finanzas:** Notas, montos de matrícula usain `BigDecimal` para precisión decimal.

---

## 8. MEJORAS FUTURAS SUGERIDAS

1. **Particionamiento por período:** Si hay muchos registros, particionar por `periodo_academico_id`.
2. **Materialized Views:** Para reportes (p.ej. promedio por carrera, por período).
3. **Triggers de auditoría:** Triggers SQL automáticos para registrar cambios.
4. **Índices de texto completo:** Para búsquedas de nombres (si se requiere búsqueda fuzzy).
5. **Tabla de configuración:** Para parámetros (ej. porcentajes, límites de inasistencia).
6. **Caché:** Implementar caché Redis para períodos, carreras, materias que cambiar poco.

---

**Documento generado automáticamente: análisis completo de DB.**
