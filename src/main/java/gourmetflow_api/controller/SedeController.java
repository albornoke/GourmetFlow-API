package gourmetflow_api.controller;

import gourmetflow_api.dto.SedeRequestDTO;
import gourmetflow_api.model.Sede;
import gourmetflow_api.service.SedeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sedes")
public class SedeController {
    private final SedeService sedeService;
    public SedeController(SedeService sedeService) { this.sedeService = sedeService; }

    @PostMapping
    public ResponseEntity<Sede> crear(@Valid @RequestBody SedeRequestDTO req) {
        return new ResponseEntity<>(sedeService.crearSede(req), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Sede>> listar() { return ResponseEntity.ok(sedeService.listarSedes()); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sedeService.eliminarSede(id);
        return ResponseEntity.noContent().build();
    }
}