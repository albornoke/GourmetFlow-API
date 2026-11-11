package gourmetflow_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gourmetflow_api.config.SecurityConfig;
import gourmetflow_api.dto.MesaRequestDTO;
import gourmetflow_api.model.Mesa;
import gourmetflow_api.service.MesaService;
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

@WebMvcTest(MesaController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class MesaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private MesaService mesaService;

    @Test
    void obtenerMesas_DeberiaRetornar200() throws Exception {
        Mesa mesa = new Mesa();
        mesa.setId(1L);
        mesa.setNumeroMesa(10);
        mesa.setCapacidad(6);
        mesa.setEstado("LIBRE");

        when(mesaService.listarMesas()).thenReturn(List.of(mesa));

        mockMvc.perform(get("/api/v1/mesas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroMesa").value(10))
                .andExpect(jsonPath("$[0].capacidad").value(6))
                .andExpect(jsonPath("$[0].estado").value("LIBRE"));
    }

    @Test
    void crearMesa_ConDatosValidos_DeberiaRetornar201() throws Exception {
        MesaRequestDTO request = new MesaRequestDTO();
        request.setNumeroMesa(15);
        request.setCapacidad(4);
        request.setEstado("LIBRE");

        Mesa mesaCreada = new Mesa();
        mesaCreada.setId(1L);
        mesaCreada.setNumeroMesa(15);
        mesaCreada.setCapacidad(4);
        mesaCreada.setEstado("LIBRE");

        when(mesaService.crearMesa(any(MesaRequestDTO.class))).thenReturn(mesaCreada);

        mockMvc.perform(post("/api/v1/mesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numeroMesa").value(15));
    }

    @Test
    void crearMesa_ConDatosInvalidos_DeberiaRetornar400() throws Exception {
        MesaRequestDTO request = new MesaRequestDTO();
        // numeroMesa y capacidad nulos -> @NotNull falla
        request.setEstado("LIBRE");

        mockMvc.perform(post("/api/v1/mesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarMesa_ConIdExistente_DeberiaRetornar200() throws Exception {
        MesaRequestDTO request = new MesaRequestDTO();
        request.setNumeroMesa(15);
        request.setCapacidad(8);
        request.setEstado("OCUPADA");

        Mesa mesaActualizada = new Mesa();
        mesaActualizada.setId(1L);
        mesaActualizada.setNumeroMesa(15);
        mesaActualizada.setCapacidad(8);
        mesaActualizada.setEstado("OCUPADA");

        when(mesaService.actualizarMesa(eq(1L), any(MesaRequestDTO.class))).thenReturn(mesaActualizada);

        mockMvc.perform(put("/api/v1/mesas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacidad").value(8))
                .andExpect(jsonPath("$.estado").value("OCUPADA"));
    }

    @Test
    void eliminarMesa_ConIdExistente_DeberiaRetornar204() throws Exception {
        doNothing().when(mesaService).eliminarMesa(1L);

        mockMvc.perform(delete("/api/v1/mesas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarMesa_ConIdInexistente_DeberiaRetornar500() throws Exception {
        doThrow(new RuntimeException("Mesa no encontrada con ID: 99"))
                .when(mesaService).eliminarMesa(99L);

        mockMvc.perform(delete("/api/v1/mesas/99"))
                .andExpect(status().isInternalServerError());
    }
}
