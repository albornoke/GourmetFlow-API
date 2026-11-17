package gourmetflow_api.controller;

import gourmetflow_api.dto.MedioPagoRequestDTO;
import gourmetflow_api.model.MedioPago;
import gourmetflow_api.service.MedioPagoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medios-pago")
public class MedioPagoController {
    private final MedioPagoService medioPagoService;
    public MedioPagoController(MedioPagoService medioPagoService) { this.medioPagoService = medioPagoService; }

    @PostMapping
    public ResponseEntity<MedioPago> crear(@Valid @RequestBody MedioPagoRequestDTO req) {
        return new ResponseEntity<>(medioPagoService.crearMedioPago(req), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedioPago>> listar() { return ResponseEntity.ok(medioPagoService.listarMediosPago()); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medioPagoService.eliminarMedioPago(id);
        return ResponseEntity.noContent().build();
    }
}