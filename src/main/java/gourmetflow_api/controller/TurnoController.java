package gourmetflow_api.controller;

import gourmetflow_api.dto.TurnoRequestDTO;
import gourmetflow_api.model.Turno;
import gourmetflow_api.service.TurnoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/turnos")
public class TurnoController {
    private final TurnoService turnoService;
    public TurnoController(TurnoService turnoService) { this.turnoService = turnoService; }

    @PostMapping
    public ResponseEntity<Turno> crear(@Valid @RequestBody TurnoRequestDTO req) {
        return new ResponseEntity<>(turnoService.crearTurno(req), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Turno>> listar() { return ResponseEntity.ok(turnoService.listarTurnos()); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }
}