package gourmetflow_api.service;

import gourmetflow_api.dto.SedeRequestDTO;
import gourmetflow_api.model.Sede;
import gourmetflow_api.repository.SedeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SedeService {
    private final SedeRepository sedeRepository;
    public SedeService(SedeRepository sedeRepository) { this.sedeRepository = sedeRepository; }

    @Transactional
    public Sede crearSede(SedeRequestDTO req) {
        Sede s = new Sede();
        s.setNombre(req.nombre());
        s.setDireccion(req.direccion());
        s.setTelefono(req.telefono());
        s.setActiva(req.activa() != null ? req.activa() : true);
        return sedeRepository.save(s);
    }

    public List<Sede> listarSedes() { return sedeRepository.findAll(); }

    @Transactional
    public void eliminarSede(Long id) {
        if (!sedeRepository.existsById(id)) throw new RuntimeException("Sede no encontrada con ID: " + id);
        sedeRepository.deleteById(id);
    }
}