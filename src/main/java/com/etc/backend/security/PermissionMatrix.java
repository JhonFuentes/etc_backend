package com.etc.backend.security;

import com.etc.backend.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import java.util.*;

/**
 * PermissionMatrix - Define qué rol puede hacer qué en qué módulo.
 * Soporta operaciones: CREATE (C), READ (R), UPDATE (U), DELETE (D), APPROVE (A), DOWNLOAD (DL).
 */
public class PermissionMatrix {

    public enum Operation {
        CREATE, READ, UPDATE, DELETE, APPROVE, DOWNLOAD
    }

    public enum Module {
        // Sistema
        DASHBOARD, USERS, CONFIGURATION, AUDIT_LOGS,
        // Académico
        ACADEMIC_MANAGEMENT, COURSES, SCHEDULES, STUDENTS, TEACHERS, ENROLLMENTS,
        // Calificaciones
        GRADES, ATTENDANCE,
        // Prácticas
        INTERNSHIPS,
        // Historial
        ACADEMIC_HISTORY,
        // Reuniones
        MEETINGS,
        // Reportes
        REPORTS,
        // Finanzas
        PAYMENTS, FEES
    }

    /**
     * Matriz de permisos por rol: Map<Rol, Map<Módulo, Set<Operaciones>>>
     */
    private static final Map<String, Map<Module, Set<Operation>>> ROLE_PERMISSIONS = new HashMap<>();

    static {
        // ========== ADMINISTRADOR ==========
        Map<Module, Set<Operation>> adminPerms = new HashMap<>();
        adminPerms.put(Module.DASHBOARD, Set.of(Operation.READ));
        adminPerms.put(Module.USERS, Set.of(Operation.CREATE, Operation.READ, Operation.UPDATE, Operation.DELETE));
        adminPerms.put(Module.CONFIGURATION, Set.of(Operation.CREATE, Operation.READ, Operation.UPDATE));
        adminPerms.put(Module.AUDIT_LOGS, Set.of(Operation.READ, Operation.DOWNLOAD));
        // NO accede a académico, financiero, etc.
        ROLE_PERMISSIONS.put("ADMIN", adminPerms);

        // ========== DIRECTOR ==========
        Map<Module, Set<Operation>> directorPerms = new HashMap<>();
        directorPerms.put(Module.DASHBOARD, Set.of(Operation.READ)); // resumen académico
        directorPerms.put(Module.ACADEMIC_MANAGEMENT, Set.of(Operation.READ)); // ver cursos, docentes, estudiantes
        directorPerms.put(Module.COURSES, Set.of(Operation.READ));
        directorPerms.put(Module.SCHEDULES, Set.of(Operation.READ));
        directorPerms.put(Module.STUDENTS, Set.of(Operation.READ));
        directorPerms.put(Module.TEACHERS, Set.of(Operation.READ));
        directorPerms.put(Module.INTERNSHIPS, Set.of(Operation.READ, Operation.APPROVE, Operation.DOWNLOAD));
        directorPerms.put(Module.ACADEMIC_HISTORY, Set.of(Operation.READ, Operation.DOWNLOAD));
        directorPerms.put(Module.MEETINGS, Set.of(Operation.CREATE, Operation.READ, Operation.UPDATE, Operation.DELETE, Operation.DOWNLOAD));
        directorPerms.put(Module.REPORTS, Set.of(Operation.READ, Operation.DOWNLOAD));
        directorPerms.put(Module.PAYMENTS, Set.of(Operation.READ)); // sólo ver estado financiero
        // NO registra notas, pagos, etc.
        ROLE_PERMISSIONS.put("DIRECTOR", directorPerms);

        // ========== COORDINADORA ACADÉMICA / SECRETARIA ==========
        Map<Module, Set<Operation>> coordinadoraPerms = new HashMap<>();
        coordinadoraPerms.put(Module.DASHBOARD, Set.of(Operation.READ)); // ver matrículas, alertas
        coordinadoraPerms.put(Module.COURSES, Set.of(Operation.CREATE, Operation.READ, Operation.UPDATE)); // crear/editar cursos
        coordinadoraPerms.put(Module.ACADEMIC_MANAGEMENT, Set.of(Operation.CREATE, Operation.READ, Operation.UPDATE)); // inscribir estudiantes
        coordinadoraPerms.put(Module.SCHEDULES, Set.of(Operation.READ, Operation.UPDATE)); // asignar docentes a cursos
        coordinadoraPerms.put(Module.STUDENTS, Set.of(Operation.READ));
        coordinadoraPerms.put(Module.TEACHERS, Set.of(Operation.READ));
        coordinadoraPerms.put(Module.INTERNSHIPS, Set.of(Operation.READ, Operation.DOWNLOAD)); // ver horas, ver planillas, descargar reportes
        coordinadoraPerms.put(Module.ACADEMIC_HISTORY, Set.of(Operation.READ, Operation.DOWNLOAD));
        coordinadoraPerms.put(Module.MEETINGS, Set.of(Operation.CREATE, Operation.READ, Operation.UPDATE, Operation.DOWNLOAD));
        coordinadoraPerms.put(Module.REPORTS, Set.of(Operation.READ, Operation.DOWNLOAD));
        // NO aprueba planillas ni registra pagos
        ROLE_PERMISSIONS.put("SECRETARIA", coordinadoraPerms);

        // ========== CAJERA ==========
        Map<Module, Set<Operation>> cajeraPerms = new HashMap<>();
        cajeraPerms.put(Module.DASHBOARD, Set.of(Operation.READ)); // ver pagos del día y deudas
        cajeraPerms.put(Module.PAYMENTS, Set.of(Operation.CREATE, Operation.READ, Operation.UPDATE)); // registrar pagos, descuentos
        cajeraPerms.put(Module.FEES, Set.of(Operation.CREATE, Operation.READ)); // emitir recibos, ver estado
        cajeraPerms.put(Module.REPORTS, Set.of(Operation.READ, Operation.DOWNLOAD)); // descargar recibos y reportes
        // NO accede a notas, prácticas, historial
        ROLE_PERMISSIONS.put("CAJERA", cajeraPerms);

        // ========== DOCENTE ==========
        Map<Module, Set<Operation>> docentePerms = new HashMap<>();
        docentePerms.put(Module.DASHBOARD, Set.of(Operation.READ)); // ver cursos asignados, reuniones
        docentePerms.put(Module.ACADEMIC_MANAGEMENT, Set.of(Operation.READ)); // ver lista de estudiantes
        docentePerms.put(Module.GRADES, Set.of(Operation.CREATE, Operation.READ, Operation.UPDATE)); // registrar/editar notas
        docentePerms.put(Module.ATTENDANCE, Set.of(Operation.CREATE, Operation.READ, Operation.UPDATE)); // registrar asistencias
        docentePerms.put(Module.INTERNSHIPS, Set.of(Operation.CREATE, Operation.READ, Operation.UPDATE, Operation.DOWNLOAD)); // registrar horas, generar/enviar/descargar planilla
        docentePerms.put(Module.ACADEMIC_HISTORY, Set.of(Operation.READ)); // ver historial de sus estudiantes
        docentePerms.put(Module.MEETINGS, Set.of(Operation.READ)); // ver reuniones programadas
        // NO aprueba planillas ni ve pagos
        ROLE_PERMISSIONS.put("DOCENTE", docentePerms);

        // ========== ESTUDIANTE ==========
        Map<Module, Set<Operation>> estudiantePerms = new HashMap<>();
        estudiantePerms.put(Module.DASHBOARD, Set.of(Operation.READ)); // ver resumen personal
        estudiantePerms.put(Module.COURSES, Set.of(Operation.READ)); // ver cursos inscritos
        estudiantePerms.put(Module.GRADES, Set.of(Operation.READ)); // ver notas
        estudiantePerms.put(Module.ATTENDANCE, Set.of(Operation.READ)); // ver asistencias
        estudiantePerms.put(Module.INTERNSHIPS, Set.of(Operation.READ)); // ver horas registradas, estado aprobación
        estudiantePerms.put(Module.ACADEMIC_HISTORY, Set.of(Operation.READ, Operation.DOWNLOAD)); // ver historial, descargar
        estudiantePerms.put(Module.PAYMENTS, Set.of(Operation.READ)); // ver estado de pagos
        // NO modifica nada
        ROLE_PERMISSIONS.put("ESTUDIANTE", estudiantePerms);
    }

    /**
     * Verifica si un rol tiene permiso para una operación en un módulo.
     *
     * @param role    nombre del rol (ej. "ADMIN", "DOCENTE", "ESTUDIANTE")
     * @param module  módulo a acceder
     * @param op      operación solicitada
     * @return true si el rol tiene permiso
     */
    public static boolean hasPermission(String role, Module module, Operation op) {
        // Normalizar nombre de rol (eliminar "ROLE_" si existe)
        String normalizedRole = role.replace("ROLE_", "").toUpperCase();
        
        Map<Module, Set<Operation>> perms = ROLE_PERMISSIONS.get(normalizedRole);
        if (perms == null) {
            return false; // rol no encontrado
        }

        Set<Operation> moduleOps = perms.get(module);
        if (moduleOps == null) {
            return false; // módulo no permitido para este rol
        }

        return moduleOps.contains(op);
    }

    /**
     * Verifica si un rol tiene permiso para una operación en un módulo.
     * Versión que acepta GrantedAuthority de Spring Security.
     *
     * @param authorities autoridades del usuario
     * @param module      módulo a acceder
     * @param op          operación solicitada
     * @return true si alguna autoridad tiene permiso
     */
    public static boolean hasPermission(Collection<? extends GrantedAuthority> authorities, Module module, Operation op) {
        for (GrantedAuthority auth : authorities) {
            if (hasPermission(auth.getAuthority(), module, op)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene todas las operaciones permitidas para un rol en un módulo.
     */
    public static Set<Operation> getOperations(String role, Module module) {
        String normalizedRole = role.replace("ROLE_", "").toUpperCase();
        Map<Module, Set<Operation>> perms = ROLE_PERMISSIONS.get(normalizedRole);
        if (perms == null) return Set.of();
        Set<Operation> ops = perms.get(module);
        return ops != null ? ops : Set.of();
    }

    /**
     * Obtiene todos los módulos accesibles para un rol.
     */
    public static Set<Module> getAccessibleModules(String role) {
        String normalizedRole = role.replace("ROLE_", "").toUpperCase();
        Map<Module, Set<Operation>> perms = ROLE_PERMISSIONS.get(normalizedRole);
        if (perms == null) return Set.of();
        return perms.keySet();
    }

    /**
     * Debug: imprime la matriz completa (para testing)
     */
    public static void printMatrix() {
        ROLE_PERMISSIONS.forEach((role, modules) -> {
            System.out.println("\n=== ROLE: " + role + " ===");
            modules.forEach((module, ops) -> {
                System.out.println("  " + module + ": " + ops);
            });
        });
    }
}
