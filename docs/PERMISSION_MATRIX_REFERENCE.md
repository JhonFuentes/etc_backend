# Permission Matrix Quick Reference

## Role-Module-Operation Mapping

### ADMIN (Administrador)
| Module | CREATE | READ | UPDATE | DELETE | APPROVE | DOWNLOAD |
|--------|--------|------|--------|--------|---------|----------|
| DASHBOARD | ✓ | ✓ | ✓ | ✓ | — | — |
| USERS | ✓ | ✓ | ✓ | ✓ | — | — |
| CONFIGURATION | ✓ | ✓ | ✓ | ✓ | — | — |
| AUDIT_LOGS | — | ✓ | — | — | — | ✓ |
| ACADEMIC_MANAGEMENT | — | ✓ | — | — | — | — |
| COURSES | — | ✓ | — | — | — | — |
| SCHEDULES | — | ✓ | — | — | — | — |
| STUDENTS | — | ✓ | — | — | — | — |
| TEACHERS | ✓ | ✓ | ✓ | ✓ | — | — |
| ENROLLMENTS | — | ✓ | — | — | — | — |
| GRADES | — | ✓ | — | — | — | — |
| ATTENDANCE | — | ✓ | — | — | — | — |
| INTERNSHIPS | — | ✓ | — | — | — | — |
| ACADEMIC_HISTORY | — | ✓ | — | — | — | ✓ |
| MEETINGS | — | ✓ | — | — | — | — |
| REPORTS | — | ✓ | — | — | — | ✓ |
| PAYMENTS | — | ✓ | — | — | — | — |
| FEES | — | ✓ | — | — | — | — |

### DIRECTOR
| Module | CREATE | READ | UPDATE | DELETE | APPROVE | DOWNLOAD |
|--------|--------|------|--------|--------|---------|----------|
| DASHBOARD | — | ✓ | — | — | — | — |
| USERS | — | ✓ | — | — | — | — |
| ACADEMIC_MANAGEMENT | — | ✓ | — | — | — | — |
| COURSES | — | ✓ | — | — | — | — |
| SCHEDULES | — | ✓ | — | — | — | — |
| STUDENTS | — | ✓ | — | — | — | — |
| TEACHERS | — | ✓ | — | — | — | — |
| ENROLLMENTS | — | ✓ | — | — | — | — |
| GRADES | — | ✓ | — | — | — | — |
| ATTENDANCE | — | ✓ | — | — | — | — |
| INTERNSHIPS | — | ✓ | — | — | ✓ | — |
| ACADEMIC_HISTORY | — | ✓ | — | — | — | — |
| MEETINGS | — | ✓ | — | — | — | — |
| REPORTS | — | ✓ | — | — | — | ✓ |

### SECRETARIA (Coordinadora)
| Module | CREATE | READ | UPDATE | DELETE | APPROVE | DOWNLOAD |
|--------|--------|------|--------|--------|---------|----------|
| DASHBOARD | — | ✓ | — | — | — | — |
| ACADEMIC_MANAGEMENT | ✓ | ✓ | ✓ | — | — | — |
| COURSES | ✓ | ✓ | ✓ | — | — | — |
| SCHEDULES | ✓ | ✓ | ✓ | — | — | — |
| STUDENTS | ✓ | ✓ | ✓ | — | — | — |
| TEACHERS | — | ✓ | — | — | — | — |
| ENROLLMENTS | ✓ | ✓ | ✓ | — | — | — |
| GRADES | — | ✓ | — | — | — | — |
| ATTENDANCE | — | ✓ | — | — | — | — |
| INTERNSHIPS | — | ✓ | — | — | — | — |
| ACADEMIC_HISTORY | — | ✓ | — | — | — | — |

### DOCENTE
| Module | CREATE | READ | UPDATE | DELETE | APPROVE | DOWNLOAD |
|--------|--------|------|--------|--------|---------|----------|
| DASHBOARD | — | ✓ | — | — | — | — |
| COURSES | — | ✓ | — | — | — | — |
| SCHEDULES | — | ✓ | — | — | — | — |
| GRADES | ✓ | ✓ | ✓ | — | — | — |
| ATTENDANCE | ✓ | ✓ | ✓ | — | — | — |
| INTERNSHIPS | ✓ | ✓ | ✓ | — | — | — |
| ACADEMIC_HISTORY | — | ✓ | — | — | — | ✓ |
| MEETINGS | ✓ | ✓ | ✓ | — | — | — |

### CAJERA
| Module | CREATE | READ | UPDATE | DELETE | APPROVE | DOWNLOAD |
|--------|--------|------|--------|--------|---------|----------|
| DASHBOARD | — | ✓ | — | — | — | — |
| PAYMENTS | ✓ | ✓ | ✓ | — | — | — |
| FEES | ✓ | ✓ | ✓ | — | — | — |

### ESTUDIANTE
| Module | CREATE | READ | UPDATE | DELETE | APPROVE | DOWNLOAD |
|--------|--------|------|--------|--------|---------|----------|
| DASHBOARD | — | ✓ | — | — | — | — |
| COURSES | — | ✓ | — | — | — | — |
| SCHEDULES | — | ✓ | — | — | — | — |
| GRADES | — | ✓ | — | — | — | — |
| ATTENDANCE | — | ✓ | — | — | — | — |
| ACADEMIC_HISTORY | — | ✓ | — | — | — | ✓ |
| PAYMENTS | — | ✓ | — | — | — | ✓ |

---

## API Endpoint Permission Examples

### Creating a User (Users Module)
```
POST /api/usuarios
Roles allowed: ADMIN
Permission check: canCreate(auth, USERS)
Response on fail: 403 Forbidden
```

### Creating a Grade (Grades Module)
```
POST /api/calificaciones
Roles allowed: ADMIN, DOCENTE (if owner of group)
Permission check: canCreate(auth, GRADES)
Ownership check: @securityService.isDocenteOfInscripcion()
Response on fail: 403 Forbidden (permission) or 403 Forbidden (ownership)
```

### Viewing Reports (Reports Module)
```
GET /api/reports
Roles allowed: ADMIN, DIRECTOR
Permission check: canRead(auth, REPORTS)
Response on fail: 403 Forbidden
```

### Downloading Academic History (Academic History Module)
```
GET /api/historial-academico/download
Roles allowed: DOCENTE, ESTUDIANTE
Permission check: canDownload(auth, ACADEMIC_HISTORY)
Response on fail: 403 Forbidden
```

### Approving Internship (Internships Module)
```
PUT /api/internships/{id}/approve
Roles allowed: DIRECTOR
Permission check: canApprove(auth, INTERNSHIPS)
Response on fail: 403 Forbidden
```

---

## Controller Implementation Checklist

### For Each Controller Endpoint

- [ ] Add `@Autowired private PermissionChecker permissionChecker;`
- [ ] Add permission check at start of method:
  ```java
  if (!permissionChecker.canCreate(auth, PermissionMatrix.Module.MODULE_NAME)) {
      return ResponseEntity.status(403).body("No tiene permiso...");
  }
  ```
- [ ] Use appropriate method: `canCreate()`, `canRead()`, `canUpdate()`, `canDelete()`, `canApprove()`, `canDownload()`
- [ ] Return 403 Forbidden with descriptive message on permission denied
- [ ] Test with multiple roles to verify permission matrix is correct

### Already Implemented
- ✅ UsuarioController (POST, PUT, DELETE)
- ✅ CalificacionController (POST, PUT, DELETE)
- ✅ AsistenciaController (POST, PUT, DELETE)
- ✅ InscripcionController (POST, PUT, DELETE)
- ✅ DocenteController (POST, PUT, DELETE)
- ✅ SedeController (POST, PUT, DELETE)
- ✅ CarreraSedeController (POST, PUT, DELETE)

### Recommended for Future Enhancement
- [ ] MatriculaController - Add permission checks
- [ ] HorarioController - Add permission checks
- [ ] GrupoController - Add permission checks
- [ ] CarreraController - Add permission checks
- [ ] MateriaController - Add permission checks
- [ ] PersonaController - Add permission checks

---

## Testing Permissions

### Test Scenario 1: Admin Can Create User, Docente Cannot

```bash
# 1. Login as ADMIN
JWT_ADMIN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -d '{"username":"admin","password":"pass"}' | jq -r '.token')

# 2. Create user as ADMIN → Should succeed (201)
curl -X POST http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer $JWT_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{"username":"test","rol":{"id":1}}'

# 3. Login as DOCENTE
JWT_DOCENTE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -d '{"username":"docente1","password":"pass"}' | jq -r '.token')

# 4. Attempt to create user as DOCENTE → Should fail (403)
curl -X POST http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer $JWT_DOCENTE" \
  -H "Content-Type: application/json" \
  -d '{"username":"test2","rol":{"id":1}}'
# Expected: 403 "No tiene permiso para crear usuarios"
```

### Test Scenario 2: Docente Can Create Grade, Estudiante Cannot

```bash
# 1. Login as DOCENTE
JWT_DOCENTE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -d '{"username":"docente1","password":"pass"}' | jq -r '.token')

# 2. Create grade as DOCENTE → Should succeed (201)
curl -X POST http://localhost:8080/api/calificaciones \
  -H "Authorization: Bearer $JWT_DOCENTE" \
  -H "Content-Type: application/json" \
  -d '{"inscripcionId":1,"tipoEvaluacionId":1,"nota":85.0}'

# 3. Login as ESTUDIANTE
JWT_ESTUDIANTE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -d '{"username":"student1","password":"pass"}' | jq -r '.token')

# 4. Attempt to create grade as ESTUDIANTE → Should fail (403)
curl -X POST http://localhost:8080/api/calificaciones \
  -H "Authorization: Bearer $JWT_ESTUDIANTE" \
  -H "Content-Type: application/json" \
  -d '{"inscripcionId":2,"tipoEvaluacionId":1,"nota":90.0}'
# Expected: 403 "No tiene permiso para crear calificaciones"
```

---

## Troubleshooting

### Issue: User has role but still getting 403

**Possible causes:**
1. User's role is not mapped correctly (check UserPrincipal.create())
2. Permission matrix doesn't grant operation for this role+module
3. Ownership check failed (@securityService validation)

**Debug steps:**
1. Check JWT token: `jq .role <token>`
2. Verify role is prefixed with ROLE_: `echo $JWT | jq '.authorities'`
3. Check PermissionMatrix static block for role+module+operation
4. Add logging to PermissionChecker.checkPermission()

### Issue: All endpoints return 403 for all users

**Possible causes:**
1. PermissionMatrix static block not initialized correctly
2. All roles removed from PermissionMatrix mappings
3. Authentication object is null

**Debug steps:**
1. Test with @PreAuthorize only (without PermissionChecker)
2. Add debug logging to PermissionChecker
3. Verify PermissionMatrix.ROLE_PERMISSIONS.size() > 0

---

**Last Updated:** 2025-12-20  
**Permission Matrix Version:** 1.0  
**Applies To:** ETC Backend v1.0.0+
