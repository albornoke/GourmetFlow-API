package gourmetflow_api.controller;

import gourmetflow_api.dto.PasswordRequestDTO;
import gourmetflow_api.dto.RegistroRequestDTO;
import gourmetflow_api.model.Usuario;
import gourmetflow_api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody RegistroRequestDTO request) {
        Usuario nuevoUsuario = usuarioService.registrarUsuario(
                request.username(),
                request.password(),
                request.rol(),
                request.email()
        );
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam boolean activo) {
        usuarioService.cambiarEstadoUsuario(id, activo);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> actualizarPassword(@PathVariable Long id, @Valid @RequestBody PasswordRequestDTO request) {
        usuarioService.actualizarPassword(id, request.nuevaPassword());
        return ResponseEntity.noContent().build();
    }
}