package gourmetflow_api.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordRequestDTO(
        @NotBlank(message = "La nueva contraseña es obligatoria")
        String nuevaPassword
) {
}
