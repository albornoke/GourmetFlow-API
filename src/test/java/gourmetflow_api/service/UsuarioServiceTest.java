package gourmetflow_api.service;

import gourmetflow_api.model.Rol;
import gourmetflow_api.model.Usuario;
import gourmetflow_api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioAdmin;

    @BeforeEach
    void setUp() {
        usuarioAdmin = new Usuario("admin_test", "hashedPass123", Rol.ADMIN, "admin@gourmetflow.com");
        usuarioAdmin.setId(1L);
    }

    @Test
    void registrarUsuario_ConDatosValidos_DeberiaGuardarUsuario() {
        when(usuarioRepository.findByUsername("nuevo_usuario")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawPass123")).thenReturn("hashedEncoded");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(10L);
            return u;
        });

        Usuario resultado = usuarioService.registrarUsuario("nuevo_usuario", "rawPass123", Rol.MESERO, "mesero@gourmetflow.com");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getUsername()).isEqualTo("nuevo_usuario");
        assertThat(resultado.getPassword()).isEqualTo("hashedEncoded");
        assertThat(resultado.getRol()).isEqualTo(Rol.MESERO);
        assertThat(resultado.getEmail()).isEqualTo("mesero@gourmetflow.com");
        assertThat(resultado.isActivo()).isTrue();

        verify(usuarioRepository).findByUsername("nuevo_usuario");
        verify(passwordEncoder).encode("rawPass123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_ConUsernameExistente_DeberiaLanzarExcepcion() {
        when(usuarioRepository.findByUsername("admin_test")).thenReturn(Optional.of(usuarioAdmin));

        assertThatThrownBy(() -> usuarioService.registrarUsuario("admin_test", "pass", Rol.ADMIN, "a@b.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El nombre de usuario ya se encuentra registrado.");

        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void cambiarEstadoUsuario_DesactivarAdminUnico_DeberiaLanzarExcepcion() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioAdmin));
        when(usuarioRepository.countByRolAndActivoTrue(Rol.ADMIN)).thenReturn(1L);

        assertThatThrownBy(() -> usuarioService.cambiarEstadoUsuario(1L, false))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("El sistema debe mantener al menos un Administrador activo.");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void cambiarEstadoUsuario_DesactivarAdminConVariosActivos_DeberiaGuardar() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioAdmin));
        when(usuarioRepository.countByRolAndActivoTrue(Rol.ADMIN)).thenReturn(2L);

        usuarioService.cambiarEstadoUsuario(1L, false);

        assertThat(usuarioAdmin.isActivo()).isFalse();
        verify(usuarioRepository).save(usuarioAdmin);
    }

    @Test
    void cambiarEstadoUsuario_DesactivarNoAdmin_DeberiaGuardarDirectamente() {
        Usuario mesero = new Usuario("mesero1", "hash", Rol.MESERO, "m@b.com");
        mesero.setId(2L);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(mesero));

        usuarioService.cambiarEstadoUsuario(2L, false);

        assertThat(mesero.isActivo()).isFalse();
        verify(usuarioRepository).save(mesero);
        verify(usuarioRepository, never()).countByRolAndActivoTrue(any());
    }

    @Test
    void cambiarEstadoUsuario_UsuarioInexistente_DeberiaLanzarExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.cambiarEstadoUsuario(99L, false))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado con ID: 99");
    }

    @Test
    void actualizarPassword_ConUsuarioExistente_DeberiaActualizar() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioAdmin));
        when(passwordEncoder.encode("nuevaPass")).thenReturn("hashNuevo");

        usuarioService.actualizarPassword(1L, "nuevaPass");

        assertThat(usuarioAdmin.getPassword()).isEqualTo("hashNuevo");
        verify(usuarioRepository).save(usuarioAdmin);
    }

    @Test
    void actualizarPassword_UsuarioInexistente_DeberiaLanzarExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.actualizarPassword(99L, "pass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado con ID: 99");

        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void listarTodos_DeberiaRetornarListaUsuarios() {
        List<Usuario> lista = List.of(usuarioAdmin);
        when(usuarioRepository.findAll()).thenReturn(lista);

        List<Usuario> resultado = usuarioService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado).containsExactly(usuarioAdmin);
    }
}
