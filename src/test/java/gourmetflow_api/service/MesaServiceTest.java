package gourmetflow_api.service;

import gourmetflow_api.dto.MesaRequestDTO;
import gourmetflow_api.model.Mesa;
import gourmetflow_api.repository.MesaRepository;
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
class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;

    @InjectMocks
    private MesaService mesaService;

    private Mesa mesa;

    @BeforeEach
    void setUp() {
        mesa = new Mesa();
        mesa.setId(1L);
        mesa.setNumeroMesa(5);
        mesa.setCapacidad(4);
        mesa.setEstado("LIBRE");
    }

    @Test
    void listarMesas_DeberiaRetornarLista() {
        when(mesaRepository.findAll()).thenReturn(List.of(mesa));

        List<Mesa> resultado = mesaService.listarMesas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNumeroMesa()).isEqualTo(5);
    }

    @Test
    void registrarMesa_ConNumeroUnico_DeberiaGuardar() {
        when(mesaRepository.findByNumeroMesa(5)).thenReturn(Optional.empty());
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);

        Mesa resultado = mesaService.registrarMesa(mesa);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNumeroMesa()).isEqualTo(5);
        verify(mesaRepository).save(mesa);
    }

    @Test
    void registrarMesa_ConNumeroDuplicado_DeberiaLanzarExcepcion() {
        when(mesaRepository.findByNumeroMesa(5)).thenReturn(Optional.of(mesa));

        assertThatThrownBy(() -> mesaService.registrarMesa(mesa))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe una mesa registrada con el número 5");

        verify(mesaRepository, never()).save(any(Mesa.class));
    }

    @Test
    void crearMesa_ConNumeroUnico_DeberiaGuardar() {
        MesaRequestDTO request = new MesaRequestDTO();
        request.setNumeroMesa(8);
        request.setCapacidad(6);
        request.setEstado("LIBRE");

        when(mesaRepository.findByNumeroMesa(8)).thenReturn(Optional.empty());
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);

        Mesa resultado = mesaService.crearMesa(request);

        assertThat(resultado).isNotNull();
        verify(mesaRepository).save(any(Mesa.class));
    }

    @Test
    void crearMesa_ConNumeroDuplicado_DeberiaLanzarExcepcion() {
        MesaRequestDTO request = new MesaRequestDTO();
        request.setNumeroMesa(5);
        request.setCapacidad(4);
        request.setEstado("LIBRE");

        when(mesaRepository.findByNumeroMesa(5)).thenReturn(Optional.of(mesa));

        assertThatThrownBy(() -> mesaService.crearMesa(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe una mesa registrada con el número 5");

        verify(mesaRepository, never()).save(any(Mesa.class));
    }

    @Test
    void actualizarMesa_ConIdExistente_DeberiaActualizar() {
        MesaRequestDTO request = new MesaRequestDTO();
        request.setNumeroMesa(5);
        request.setCapacidad(8);
        request.setEstado("OCUPADA");

        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);

        Mesa resultado = mesaService.actualizarMesa(1L, request);

        assertThat(resultado.getCapacidad()).isEqualTo(8);
        assertThat(resultado.getEstado()).isEqualTo("OCUPADA");
        verify(mesaRepository).save(mesa);
    }

    @Test
    void actualizarMesa_ConIdInexistente_DeberiaLanzarExcepcion() {
        MesaRequestDTO request = new MesaRequestDTO();
        request.setNumeroMesa(5);
        request.setCapacidad(4);
        request.setEstado("LIBRE");

        when(mesaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mesaService.actualizarMesa(99L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mesa no encontrada con ID: 99");
    }

    @Test
    void eliminarMesa_ConIdExistente_DeberiaEliminar() {
        when(mesaRepository.existsById(1L)).thenReturn(true);

        mesaService.eliminarMesa(1L);

        verify(mesaRepository).deleteById(1L);
    }

    @Test
    void eliminarMesa_ConIdInexistente_DeberiaLanzarExcepcion() {
        when(mesaRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> mesaService.eliminarMesa(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mesa no encontrada con ID: 99");

        verify(mesaRepository, never()).deleteById(any());
    }
}
