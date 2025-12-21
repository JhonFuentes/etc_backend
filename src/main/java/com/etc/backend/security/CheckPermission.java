package com.etc.backend.security;

import java.lang.annotation.*;

/**
 * @CheckPermission - Anotación para validar permisos en endpoints de forma declarativa.
 * 
 * Uso:
 *   @PostMapping
 *   @CheckPermission(module = USERS, operation = CREATE)
 *   public ResponseEntity<?> createUser(...) { ... }
 *
 * Se procesa mediante un AspectJ o interceptor. Para simplicidad, puede usarse en
 * controladores junto con @PreAuthorize.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPermission {
    PermissionMatrix.Module module();
    PermissionMatrix.Operation operation();
}
