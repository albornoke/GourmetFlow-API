package gourmetflow_api.service;

import gourmetflow_api.dto.PlatoRequestDTO;
import gourmetflow_api.model.Plato;
import gourmetflow_api.repository.PlatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlatoService {

    private final PlatoRepository platoRepository;

    public PlatoService(PlatoRepository platoRepository) {
        this.platoRepository = platoRepository;
    }

    @Transactional
    public Plato crearPlato(PlatoRequestDTO request) {
        if (platoRepository.findByNombreIgnoreCase(request.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un plato registrado con ese nombre.");
        }

        Plato plato = new Plato();
        plato.setNombre(request.getNombre());
        plato.setPrecio(request.getPrecio());
        plato.setDisponible(request.getDisponible());

        return platoRepository.save(plato);
    }

    public List<Plato> listarPlatos() {
        return platoRepository.findAll();
    }

    @Transactional
    public Plato actualizarPlato(Long id, PlatoRequestDTO request) {
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado con ID: " + id));

        plato.setNombre(request.getNombre());
        plato.setPrecio(request.getPrecio());
        plato.setDisponible(request.getDisponible());

        return platoRepository.save(plato);
    }

    @Transactional
    public void eliminarPlato(Long id) {
        if (!platoRepository.existsById(id)) {
            throw new RuntimeException("Plato no encontrado con ID: " + id);
        }
        platoRepository.deleteById(id);
    }
}