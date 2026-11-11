package gourmetflow_api.controller;

import gourmetflow_api.dto.PlatoRequestDTO;
import gourmetflow_api.model.Plato;
import gourmetflow_api.service.PlatoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/platos")
public class PlatoController {

    private final PlatoService platoService;

    public PlatoController(PlatoService platoService) {
        this.platoService = platoService;
    }

    @GetMapping
    public ResponseEntity<List<Plato>> obtenerPlatos() {
        return ResponseEntity.ok(platoService.listarPlatosDisponibles());
    }

    @PostMapping
    public ResponseEntity<Plato> crearPlato(@Valid @RequestBody PlatoRequestDTO platoDto) {
        Plato nuevoPlato = platoService.guardarPlato(platoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPlato);
    }
}