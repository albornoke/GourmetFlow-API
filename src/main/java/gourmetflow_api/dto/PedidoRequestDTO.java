package gourmetflow_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PedidoRequestDTO(
        @NotNull(message = "El número de mesa es obligatorio")
        @Min(value = 1, message = "El número de mesa debe ser válido")
        Integer numeroMesa,

        @NotBlank(message = "El estado del pedido es obligatorio")
        String estado // EJ: 'PENDIENTE', 'EN_PREPARACION', 'LISTO', 'ENTREGADO'
) {}