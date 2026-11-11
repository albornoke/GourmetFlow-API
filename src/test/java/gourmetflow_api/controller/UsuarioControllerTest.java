package gourmetflow_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gourmetflow_api.config.SecurityConfig;
import gourmetflow_api.dto.PasswordRequestDTO;
import gourmetflow_api.dto.RegistroRequestDTO;
import gourmetflow_api.model.Rol;
import gourmetflow_api.model.Usuario;
import gourmetflow_api.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void registrarUsuario_ConDatosValidos_DeberiaRetornar201() throws Exception {
        RegistroRequestDTO request = new RegistroRequestDTO(
                "nuevo_mesero", "pass123", Rol.MESERO, "mesero@gourmetflow.com");

        Usuario usuario = new Usuario("nuevo_mesero", "hashPass", Rol.MESERO, "mesero@gourmetflow.com");
        usuario.setId(1L);

        when(usuarioService.registrarUsuario(anyString(), anyString(), any(Rol.class), anyString()))
                .thenReturn(usuario);

        mockMvc.perform(post("/api/v1/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("nuevo_mesero"))
                .andExpect(jsonPath("$.rol").value("MESERO"))
                .andExpect(jsonPath("$.email").value("mesero@gourmetflow.com"));
    }

    @Test
    void registrarUsuario_ConEmailInvalido_DeberiaRetornar400() throws Exception {
        RegistroRequestDTO request = new RegistroRequestDTO(
                "user1", "pass123", Rol.MESERO, "correo-invalido");

        mockMvc.perform(post("/api/v1/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrarUsuario_ConUsernameVacio_DeberiaRetornar400() throws Exception {
        RegistroRequestDTO request = new RegistroRequestDTO(
                "", "pass123", Rol.MESERO, "user@mail.com");

        mockMvc.perform(post("/api/v1/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarUsuarios_DeberiaRetornar200() throws Exception {
        Usuario usuario = new Usuario("admin", "hash", Rol.ADMIN, "admin@mail.com");
        usuario.setId(1L);
        when(usuarioService.listarTodos()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[0].rol").value("ADMIN"));
    }

    @Test
    void cambiarEstado_DeberiaRetornar204() throws Exception {
        doNothing().when(usuarioService).cambiarEstadoUsuario(eq(1L), anyBoolean());

        mockMvc.perform(patch("/api/v1/usuarios/1/estado")
                        .param("activo", "false"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cambiarEstado_UsuarioInexistente_DeberiaRetornar500() throws Exception {
        doThrow(new RuntimeException("Usuario no encontrado con ID: 99"))
                .when(usuarioService).cambiarEstadoUsuario(eq(99L), anyBoolean());

        mockMvc.perform(patch("/api/v1/usuarios/99/estado")
                        .param("activo", "false"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void actualizarPassword_ConDatosValidos_DeberiaRetornar204() throws Exception {
        PasswordRequestDTO request = new PasswordRequestDTO("nuevaPass123");
        doNothing().when(usuarioService).actualizarPassword(eq(1L), anyString());

        mockMvc.perform(patch("/api/v1/usuarios/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void actualizarPassword_ConPasswordVacia_DeberiaRetornar400() throws Exception {
        PasswordRequestDTO request = new PasswordRequestDTO("");

        mockMvc.perform(patch("/api/v1/usuarios/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
