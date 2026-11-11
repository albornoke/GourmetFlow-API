package gourmetflow_api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PlatoRequestDTO {

    @NotBlank(message = "El nombre del plato es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
    @Digits(integer = 8, fraction = 2, message = "Formato de precio inválido")
    private BigDecimal precio;

    @NotNull(message = "El estado de disponibilidad es obligatorio")
    private Boolean disponible;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
}