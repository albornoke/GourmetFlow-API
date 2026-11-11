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

    @Transactional(readOnly = true)
    public List<Plato> listarPlatosDisponibles() {
        return platoRepository.findAll();
    }

    @Transactional
    public Plato guardarPlato(PlatoRequestDTO dto) {
        if (platoRepository.findByNombreIgnoreCase(dto.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un plato registrado con ese nombre.");
        }

        Plato plato = new Plato();
        plato.setNombre(dto.getNombre());
        plato.setPrecio(dto.getPrecio());
        plato.setDisponible(dto.getDisponible());

        return platoRepository.save(plato);
    }
}