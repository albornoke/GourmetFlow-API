package gourmetflow_api.service;

import gourmetflow_api.dto.MedioPagoRequestDTO;
import gourmetflow_api.model.MedioPago;
import gourmetflow_api.repository.MedioPagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MedioPagoService {
    private final MedioPagoRepository medioPagoRepository;
    public MedioPagoService(MedioPagoRepository medioPagoRepository) { this.medioPagoRepository = medioPagoRepository; }

    @Transactional
    public MedioPago crearMedioPago(MedioPagoRequestDTO req) {
        MedioPago mp = new MedioPago();
        mp.setNombre(req.nombre());
        mp.setActivo(req.activo() != null ? req.activo() : true);
        return medioPagoRepository.save(mp);
    }

    public List<MedioPago> listarMediosPago() { return medioPagoRepository.findAll(); }

    @Transactional
    public void eliminarMedioPago(Long id) {
        if (!medioPagoRepository.existsById(id)) throw new RuntimeException("Medio de pago no encontrado con ID: " + id);
        medioPagoRepository.deleteById(id);
    }
}