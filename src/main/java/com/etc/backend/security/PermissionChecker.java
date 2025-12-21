package com.etc.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collection;

/**
 * PermissionChecker - Servicio para validar permisos en controladores.
 * Se inyecta como @securityService.checkPermission(...) en @PreAuthorize.
 */
@Service
public class PermissionChecker {

    /**
     * Verifica si el usuario autenticado tiene permiso para una operación en un módulo.
     *
     * @param authentication del usuario
     * @param module         módulo a acceder
     * @param operation      operación solicitada (CREATE, READ, UPDATE, DELETE, APPROVE, DOWNLOAD)
     * @return true si tiene permiso
     */
    public boolean checkPermission(Authentication authentication, PermissionMatrix.Module module, PermissionMatrix.Operation operation) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return PermissionMatrix.hasPermission(authorities, module, operation);
    }

    /**
     * Verifica permiso a nivel módulo (cualquier operación).
     */
    public boolean canAccessModule(Authentication authentication, PermissionMatrix.Module module) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        // Si puede hacer al menos una operación en el módulo, puede acceder
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority auth : authorities) {
            String role = auth.getAuthority().replace("ROLE_", "").toUpperCase();
            if (!PermissionMatrix.getOperations(role, module).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // ===== Métodos de conveniencia por operación =====

    public boolean canCreate(Authentication auth, PermissionMatrix.Module module) {
        return checkPermission(auth, module, PermissionMatrix.Operation.CREATE);
    }

    public boolean canRead(Authentication auth, PermissionMatrix.Module module) {
        return checkPermission(auth, module, PermissionMatrix.Operation.READ);
    }

    public boolean canUpdate(Authentication auth, PermissionMatrix.Module module) {
        return checkPermission(auth, module, PermissionMatrix.Operation.UPDATE);
    }

    public boolean canDelete(Authentication auth, PermissionMatrix.Module module) {
        return checkPermission(auth, module, PermissionMatrix.Operation.DELETE);
    }

    public boolean canApprove(Authentication auth, PermissionMatrix.Module module) {
        return checkPermission(auth, module, PermissionMatrix.Operation.APPROVE);
    }

    public boolean canDownload(Authentication auth, PermissionMatrix.Module module) {
        return checkPermission(auth, module, PermissionMatrix.Operation.DOWNLOAD);
    }
}
