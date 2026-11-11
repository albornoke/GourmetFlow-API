package gourmetflow_api.controller;

import gourmetflow_api.dto.MesaRequestDTO;
import gourmetflow_api.model.Mesa;
import gourmetflow_api.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @PostMapping
    public ResponseEntity<Mesa> crearMesa(@Valid @RequestBody MesaRequestDTO request) {
        Mesa nuevaMesa = mesaService.crearMesa(request);
        return new ResponseEntity<>(nuevaMesa, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Mesa>> listarMesas() {
        List<Mesa> mesas = mesaService.listarMesas();
        return ResponseEntity.ok(mesas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mesa> actualizarMesa(@PathVariable Long id, @Valid @RequestBody MesaRequestDTO request) {
        Mesa mesaActualizada = mesaService.actualizarMesa(id, request);
        return ResponseEntity.ok(mesaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMesa(@PathVariable Long id) {
        mesaService.eliminarMesa(id);
        return ResponseEntity.noContent().build();
    }
}