package gourmetflow_api.controller;

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

    @GetMapping
    public ResponseEntity<List<Mesa>> obtenerMesas() {
        return ResponseEntity.ok(mesaService.listarMesas());
    }

    @PostMapping
    public ResponseEntity<Mesa> crearMesa(@Valid @RequestBody Mesa mesa) {
        Mesa nuevaMesa = mesaService.registrarMesa(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa);
    }
}