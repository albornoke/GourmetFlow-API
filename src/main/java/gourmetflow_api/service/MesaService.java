package gourmetflow_api.service;

import gourmetflow_api.dto.MesaRequestDTO;
import gourmetflow_api.model.Mesa;
import gourmetflow_api.repository.MesaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    @Transactional(readOnly = true)
    public List<Mesa> listarMesas() {
        return mesaRepository.findAll();
    }

    @Transactional
    public Mesa registrarMesa(Mesa mesa) {
        if (mesaRepository.findByNumeroMesa(mesa.getNumeroMesa()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una mesa registrada con el número " + mesa.getNumeroMesa());
        }
        return mesaRepository.save(mesa);
    }

    @Transactional
    public Mesa crearMesa(MesaRequestDTO request) {
        if (mesaRepository.findByNumeroMesa(request.getNumeroMesa()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una mesa registrada con el número " + request.getNumeroMesa());
        }

        Mesa mesa = new Mesa();
        mesa.setNumeroMesa(request.getNumeroMesa());
        mesa.setCapacidad(request.getCapacidad());
        mesa.setEstado(request.getEstado());

        return mesaRepository.save(mesa);
    }

    @Transactional
    public Mesa actualizarMesa(Long id, MesaRequestDTO request) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada con ID: " + id));

        mesa.setNumeroMesa(request.getNumeroMesa());
        mesa.setCapacidad(request.getCapacidad());
        mesa.setEstado(request.getEstado());

        return mesaRepository.save(mesa);
    }

    @Transactional
    public void eliminarMesa(Long id) {
        if (!mesaRepository.existsById(id)) {
            throw new RuntimeException("Mesa no encontrada con ID: " + id);
        }
        mesaRepository.deleteById(id);
    }
}
