package gourmetflow_api.service;

import gourmetflow_api.dto.PlatoRequestDTO;
import gourmetflow_api.model.Plato;
import gourmetflow_api.repository.PlatoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatoServiceTest {

    @Mock
    private PlatoRepository platoRepository;

    @InjectMocks
    private PlatoService platoService;

    private Plato plato;
    private PlatoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        plato = new Plato();
        plato.setId(1L);
        plato.setNombre("Ceviche Mixto");
        plato.setPrecio(new BigDecimal("45.50"));
        plato.setDisponible(true);

        requestDTO = new PlatoRequestDTO();
        requestDTO.setNombre("Ceviche Mixto");
        requestDTO.setPrecio(new BigDecimal("45.50"));
        requestDTO.setDisponible(true);
    }

    @Test
    void crearPlato_ConNombreUnico_DeberiaGuardar() {
        when(platoRepository.findByNombreIgnoreCase("Ceviche Mixto")).thenReturn(Optional.empty());
        when(platoRepository.save(any(Plato.class))).thenAnswer(invocation -> {
            Plato p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        Plato resultado = platoService.crearPlato(requestDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Ceviche Mixto");
        assertThat(resultado.getPrecio()).isEqualByComparingTo("45.50");
        assertThat(resultado.getDisponible()).isTrue();
        verify(platoRepository).save(any(Plato.class));
    }

    @Test
    void crearPlato_ConNombreDuplicado_DeberiaLanzarExcepcion() {
        when(platoRepository.findByNombreIgnoreCase("Ceviche Mixto")).thenReturn(Optional.of(plato));

        assertThatThrownBy(() -> platoService.crearPlato(requestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un plato registrado con ese nombre.");

        verify(platoRepository, never()).save(any(Plato.class));
    }

    @Test
    void listarPlatos_DeberiaRetornarLista() {
        when(platoRepository.findAll()).thenReturn(List.of(plato));

        List<Plato> resultado = platoService.listarPlatos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Ceviche Mixto");
    }

    @Test
    void actualizarPlato_ConIdExistente_DeberiaActualizar() {
        when(platoRepository.findById(1L)).thenReturn(Optional.of(plato));
        when(platoRepository.save(any(Plato.class))).thenReturn(plato);

        PlatoRequestDTO updateDTO = new PlatoRequestDTO();
        updateDTO.setNombre("Ceviche de Pescado");
        updateDTO.setPrecio(new BigDecimal("50.00"));
        updateDTO.setDisponible(false);

        Plato resultado = platoService.actualizarPlato(1L, updateDTO);

        assertThat(resultado.getNombre()).isEqualTo("Ceviche de Pescado");
        assertThat(resultado.getPrecio()).isEqualByComparingTo("50.00");
        assertThat(resultado.getDisponible()).isFalse();
    }

    @Test
    void actualizarPlato_ConIdInexistente_DeberiaLanzarExcepcion() {
        when(platoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> platoService.actualizarPlato(99L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Plato no encontrado con ID: 99");
    }

    @Test
    void eliminarPlato_ConIdExistente_DeberiaEliminar() {
        when(platoRepository.existsById(1L)).thenReturn(true);

        platoService.eliminarPlato(1L);

        verify(platoRepository).deleteById(1L);
    }

    @Test
    void eliminarPlato_ConIdInexistente_DeberiaLanzarExcepcion() {
        when(platoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> platoService.eliminarPlato(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Plato no encontrado con ID: 99");

        verify(platoRepository, never()).deleteById(any());
    }
}
