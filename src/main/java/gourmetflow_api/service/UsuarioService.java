package gourmetflow_api.service;

import gourmetflow_api.model.Rol;
import gourmetflow_api.model.Usuario;
import gourmetflow_api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias por constructor (Estándar profesional)
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrarUsuario(String username, String rawPassword, Rol rol, String email) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya se encuentra registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(rawPassword));
        usuario.setRol(rol);
        usuario.setEmail(email);
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void cambiarEstadoUsuario(Long usuarioId, boolean nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
  
        if (usuario.getRol() == Rol.ADMIN && !nuevoEstado) {
            long adminsActivos = usuarioRepository.countByRolAndActivoTrue(Rol.ADMIN);
            if (adminsActivos <= 1) {
                throw new IllegalStateException("Acción denegada: El sistema debe mantener al menos un Administrador activo.");
            }
        }

        usuario.setActivo(nuevoEstado);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void actualizarPassword(Long usuarioId, String nuevaPasswordRaw) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        usuario.setPassword(passwordEncoder.encode(nuevaPasswordRaw));
        usuarioRepository.save(usuario);
    }


    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}