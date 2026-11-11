package gourmetflow_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "tb_mesas")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El número de mesa es obligatorio")
    @Min(value = 1, message = "El número de mesa debe ser al menos 1")
    @Column(nullable = false, unique = true)
    private Integer numeroMesa;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad mínima de la mesa debe ser de 1 persona")
    @Max(value = 20, message = "La capacidad máxima permitida por mesa es de 20 personas")
    @Column(nullable = false)
    private Integer capacidad;

    @NotBlank(message = "El estado de la mesa es obligatorio")
    @Column(nullable = false, length = 20)
    private String estado = "LIBRE"; // LIBRE, OCUPADA, RESERVADA

    // Constructores
    public Mesa() {}

    // Getters y Setters seguros
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(Integer numeroMesa) { this.numeroMesa = numeroMesa; }
    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}