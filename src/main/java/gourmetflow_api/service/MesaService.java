package gourmetflow_api.service;

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
}