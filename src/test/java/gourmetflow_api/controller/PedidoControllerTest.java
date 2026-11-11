package gourmetflow_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gourmetflow_api.config.SecurityConfig;
import gourmetflow_api.dto.PedidoRequestDTO;
import gourmetflow_api.model.Pedido;
import gourmetflow_api.service.PedidoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private PedidoService pedidoService;

    @Test
    void obtenerPedidos_DeberiaRetornar200() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNumeroMesa(7);
        pedido.setEstado("PENDIENTE");

        when(pedidoService.listarPedidos()).thenReturn(List.of(pedido));

        mockMvc.perform(get("/api/v1/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroMesa").value(7))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    void registrarPedido_ConDatosValidos_DeberiaRetornar201() throws Exception {
        PedidoRequestDTO request = new PedidoRequestDTO(3, "PENDIENTE");

        Pedido pedidoCreado = new Pedido();
        pedidoCreado.setId(1L);
        pedidoCreado.setNumeroMesa(3);
        pedidoCreado.setEstado("PENDIENTE");

        when(pedidoService.crearPedido(any(PedidoRequestDTO.class))).thenReturn(pedidoCreado);

        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numeroMesa").value(3));
    }

    @Test
    void registrarPedido_ConMesaNula_DeberiaRetornar400() throws Exception {
        PedidoRequestDTO request = new PedidoRequestDTO(null, "PENDIENTE");

        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarEstado_DeberiaRetornar200() throws Exception {
        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setId(1L);
        pedidoActualizado.setNumeroMesa(3);
        pedidoActualizado.setEstado("SERVIDO");

        when(pedidoService.actualizarEstadoPedido(eq(1L), eq("SERVIDO"))).thenReturn(pedidoActualizado);

        mockMvc.perform(patch("/api/v1/pedidos/1/estado")
                        .param("estado", "SERVIDO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("SERVIDO"));
    }

    @Test
    void eliminarPedido_ConIdExistente_DeberiaRetornar204() throws Exception {
        doNothing().when(pedidoService).eliminarPedido(1L);

        mockMvc.perform(delete("/api/v1/pedidos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarPedido_ConIdInexistente_DeberiaRetornar500() throws Exception {
        doThrow(new RuntimeException("Pedido no encontrado con ID: 99"))
                .when(pedidoService).eliminarPedido(99L);

        mockMvc.perform(delete("/api/v1/pedidos/99"))
                .andExpect(status().isInternalServerError());
    }
}
