package gourmetflow_api.service;

import gourmetflow_api.dto.PedidoRequestDTO;
import gourmetflow_api.model.Pedido;
import gourmetflow_api.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNumeroMesa(3);
        pedido.setEstado("PENDIENTE");
    }

    @Test
    void listarPedidos_DeberiaRetornarLista() {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoService.listarPedidos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNumeroMesa()).isEqualTo(3);
        assertThat(resultado.get(0).getEstado()).isEqualTo("PENDIENTE");
    }

    @Test
    void crearPedido_ConDatosValidos_DeberiaGuardar() {
        PedidoRequestDTO request = new PedidoRequestDTO(3, "PENDIENTE");
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido resultado = pedidoService.crearPedido(request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNumeroMesa()).isEqualTo(3);
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void actualizarEstadoPedido_ConIdExistente_DeberiaActualizar() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido resultado = pedidoService.actualizarEstadoPedido(1L, "SERVIDO");

        assertThat(resultado.getEstado()).isEqualTo("SERVIDO");
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void actualizarEstadoPedido_ConIdInexistente_DeberiaLanzarExcepcion() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.actualizarEstadoPedido(99L, "SERVIDO"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Pedido no encontrado con ID: 99");
    }

    @Test
    void eliminarPedido_ConIdExistente_DeberiaEliminar() {
        when(pedidoRepository.existsById(1L)).thenReturn(true);

        pedidoService.eliminarPedido(1L);

        verify(pedidoRepository).deleteById(1L);
    }

    @Test
    void eliminarPedido_ConIdInexistente_DeberiaLanzarExcepcion() {
        when(pedidoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> pedidoService.eliminarPedido(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Pedido no encontrado con ID: 99");

        verify(pedidoRepository, never()).deleteById(any());
    }
}
