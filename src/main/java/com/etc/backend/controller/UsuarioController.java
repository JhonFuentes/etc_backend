package com.etc.backend.controller;

import com.etc.backend.dto.response.UsuarioSimpleResponse;
import com.etc.backend.entity.Usuario;
import com.etc.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private UsuarioSimpleResponse toDto(Usuario u) {
        Integer rolId = null;
        String rolNombre = null;
        Integer personaId = null;
        String personaNombres = null;
        String personaApellidos = null;
        try {
            if (u.getRol() != null) {
                rolId = u.getRol().getId();
                rolNombre = u.getRol().getNombre();
            }
            if (u.getPersona() != null) {
                personaId = u.getPersona().getId();
                personaNombres = u.getPersona().getNombres();
                // Persona stores apellido paterno/materno as apPaterno/apMaterno
                String apP = u.getPersona().getApPaterno();
                String apM = u.getPersona().getApMaterno();
                personaApellidos = (apP == null ? "" : apP) + (apM == null ? "" : " " + apM);
            }
        } catch (Exception ex) {
            // ignore lazy init
        }
        return new UsuarioSimpleResponse(
                u.getId(),
                u.getUsername(),
                rolId,
                rolNombre,
                personaId,
                personaNombres,
                personaApellidos,
                u.getUltimoAcceso(),
                u.getIntentosFallidos(),
                u.getBloqueado(),
                u.getEstado(),
                u.getCreatedAt(),
                u.getUpdatedAt()
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioSimpleResponse>> getAllUsuarios() {
        List<Usuario> list = usuarioRepository.findAll();
        List<UsuarioSimpleResponse> dto = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioSimpleResponse> getUsuarioById(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .map(u -> ResponseEntity.ok(toDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }
}

