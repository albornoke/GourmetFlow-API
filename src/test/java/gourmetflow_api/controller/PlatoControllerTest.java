package gourmetflow_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gourmetflow_api.config.SecurityConfig;
import gourmetflow_api.dto.PlatoRequestDTO;
import gourmetflow_api.model.Plato;
import gourmetflow_api.service.PlatoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlatoController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class PlatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private PlatoService platoService;

    @Test
    void crearPlato_ConDatosValidos_DeberiaRetornar201() throws Exception {
        PlatoRequestDTO request = new PlatoRequestDTO();
        request.setNombre("Arroz con Pato");
        request.setPrecio(new BigDecimal("38.00"));
        request.setDisponible(true);

        Plato platoCreado = new Plato();
        platoCreado.setId(1L);
        platoCreado.setNombre("Arroz con Pato");
        platoCreado.setPrecio(new BigDecimal("38.00"));
        platoCreado.setDisponible(true);

        when(platoService.crearPlato(any(PlatoRequestDTO.class))).thenReturn(platoCreado);

        mockMvc.perform(post("/api/v1/platos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Arroz con Pato"))
                .andExpect(jsonPath("$.precio").value(38.00))
                .andExpect(jsonPath("$.disponible").value(true));
    }

    @Test
    void crearPlato_ConDatosInvalidos_DeberiaRetornar400() throws Exception {
        PlatoRequestDTO request = new PlatoRequestDTO();
        request.setNombre(""); // nombre vacío
        request.setPrecio(null);
        request.setDisponible(null);

        mockMvc.perform(post("/api/v1/platos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarPlatos_DeberiaRetornar200() throws Exception {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setNombre("Ceviche");
        plato.setPrecio(new BigDecimal("45.50"));
        plato.setDisponible(true);

        when(platoService.listarPlatos()).thenReturn(List.of(plato));

        mockMvc.perform(get("/api/v1/platos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Ceviche"));
    }

    @Test
    void actualizarPlato_ConIdExistente_DeberiaRetornar200() throws Exception {
        PlatoRequestDTO request = new PlatoRequestDTO();
        request.setNombre("Ceviche Mixto Editado");
        request.setPrecio(new BigDecimal("50.00"));
        request.setDisponible(false);

        Plato platoActualizado = new Plato();
        platoActualizado.setId(1L);
        platoActualizado.setNombre("Ceviche Mixto Editado");
        platoActualizado.setPrecio(new BigDecimal("50.00"));
        platoActualizado.setDisponible(false);

        when(platoService.actualizarPlato(eq(1L), any(PlatoRequestDTO.class))).thenReturn(platoActualizado);

        mockMvc.perform(put("/api/v1/platos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ceviche Mixto Editado"))
                .andExpect(jsonPath("$.disponible").value(false));
    }

    @Test
    void eliminarPlato_ConIdExistente_DeberiaRetornar204() throws Exception {
        doNothing().when(platoService).eliminarPlato(1L);

        mockMvc.perform(delete("/api/v1/platos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarPlato_ConIdInexistente_DeberiaRetornar500() throws Exception {
        doThrow(new RuntimeException("Plato no encontrado con ID: 99"))
                .when(platoService).eliminarPlato(99L);

        mockMvc.perform(delete("/api/v1/platos/99"))
                .andExpect(status().isInternalServerError());
    }
}
