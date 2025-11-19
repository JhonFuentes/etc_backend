package com.etc.backend.service;

import com.etc.backend.dto.request.LoginRequest;
import com.etc.backend.dto.response.JwtResponse;
import com.etc.backend.entity.Usuario;
import com.etc.backend.repository.UsuarioRepository;
import com.etc.backend.security.UserPrincipal;
import com.etc.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            Usuario usuario = usuarioRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Actualizar último acceso
            usuario.setUltimoAcceso(java.time.LocalDateTime.now());
            usuario.setIntentosFallidos((short) 0);
            usuarioRepository.save(usuario);

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", usuario.getId());
            claims.put("rol", usuario.getRol().getNombre());

            String jwt = jwtUtil.generateToken(userPrincipal, claims);

            String nombreCompleto = usuario.getPersona().getNombres() + " " +
                    usuario.getPersona().getApPaterno() + " " +
                    (usuario.getPersona().getApMaterno() != null ? usuario.getPersona().getApMaterno() : "");

            return new JwtResponse(
                jwt,
                "Bearer",
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPersona().getEmail(),
                usuario.getRol().getNombre(),
                nombreCompleto.trim()
            );
        } catch (BadCredentialsException e) {
            Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElse(null);
            if (usuario != null) {
                usuario.setIntentosFallidos((short) (usuario.getIntentosFallidos() + 1));
                if (usuario.getIntentosFallidos() >= 5) {
                    usuario.setBloqueado(true);
                }
                usuarioRepository.save(usuario);
            }
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}

