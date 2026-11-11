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

    @PostMapping
    public ResponseEntity<Plato> crearPlato(@Valid @RequestBody PlatoRequestDTO request) {
        Plato nuevoPlato = platoService.crearPlato(request);
        return new ResponseEntity<>(nuevoPlato, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Plato>> listarPlatos() {
        List<Plato> platos = platoService.listarPlatos();
        return ResponseEntity.ok(platos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plato> actualizarPlato(@PathVariable Long id, @Valid @RequestBody PlatoRequestDTO request) {
        Plato platoActualizado = platoService.actualizarPlato(id, request);
        return ResponseEntity.ok(platoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlato(@PathVariable Long id) {
        platoService.eliminarPlato(id);
        return ResponseEntity.noContent().build();
    }
}