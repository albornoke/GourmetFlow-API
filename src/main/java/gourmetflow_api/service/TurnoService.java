package gourmetflow_api.service;

import gourmetflow_api.dto.TurnoRequestDTO;
import gourmetflow_api.model.Turno;
import gourmetflow_api.repository.TurnoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TurnoService {
    private final TurnoRepository turnoRepository;
    public TurnoService(TurnoRepository turnoRepository) { this.turnoRepository = turnoRepository; }

    @Transactional
    public Turno crearTurno(TurnoRequestDTO req) {
        Turno t = new Turno();
        t.setNombreTurno(req.nombreTurno());
        t.setHoraInicio(req.horaInicio());
        t.setHoraFin(req.horaFin());
        return turnoRepository.save(t);
    }

    public List<Turno> listarTurnos() { return turnoRepository.findAll(); }

    @Transactional
    public void eliminarTurno(Long id) {
        if (!turnoRepository.existsById(id)) throw new RuntimeException("Turno no encontrado con ID: " + id);
        turnoRepository.deleteById(id);
    }
}