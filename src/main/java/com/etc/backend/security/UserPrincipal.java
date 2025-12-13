package com.etc.backend.security;

import com.etc.backend.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {
    private Integer id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean enabled;
    private Boolean accountNonLocked;

    public UserPrincipal(Integer id, String username, String password,
                        Collection<? extends GrantedAuthority> authorities,
                        Boolean enabled, Boolean accountNonLocked) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.accountNonLocked = accountNonLocked;
    }

    public static UserPrincipal create(Usuario usuario) {
        String raw = usuario.getRol() != null && usuario.getRol().getNombre() != null
                ? usuario.getRol().getNombre()
                : "";
        String normalized = normalizeRoleName(raw);
        Collection<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + normalized)
        );

        return new UserPrincipal(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getPassword(),
            authorities,
            usuario.getEstado(),
            !usuario.getBloqueado()
        );
    }

    private static String normalizeRoleName(String raw) {
        if (raw == null) return "USER";
        String r = raw.trim().toLowerCase();
        // map common Spanish role names to canonical codes used in @PreAuthorize
        if (r.contains("admin")) return "ADMIN"; // Administrador, Administración
        if (r.contains("director")) return "DIRECTOR";
        if (r.contains("docent")) return "DOCENTE";
        if (r.contains("estudiant")) return "ESTUDIANTE";
        if (r.contains("secret")) return "SECRETARIA";
        // fallback: remove spaces and uppercase
        return r.replaceAll("\\s+", "_").toUpperCase();
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

