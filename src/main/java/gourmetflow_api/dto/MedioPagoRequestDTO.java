package gourmetflow_api.dto;

import jakarta.validation.constraints.NotBlank;

public record MedioPagoRequestDTO(
        @NotBlank(message = "El nombre del medio de pago es obligatorio")
        String nombre,
        Boolean activo
) {}