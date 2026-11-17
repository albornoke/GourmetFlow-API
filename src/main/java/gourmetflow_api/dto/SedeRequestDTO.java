package gourmetflow_api.dto;

import jakarta.validation.constraints.NotBlank;

public record SedeRequestDTO(
        @NotBlank(message = "El nombre de la sede es obligatorio")
        String nombre,

        @NotBlank(message = "La dirección es obligatoria")
        String direccion,

        String telefono,

        Boolean activa
) {}
