package gourmetflow_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El número de mesa es obligatorio")
    @Column(nullable = false)
    private Integer numeroMesa;

    @NotBlank(message = "El estado del pedido es obligatorio")
    @Column(nullable = false, length = 30)
    private String estado = "PENDIENTE"; // PENDIENTE, EN_PREPARACION, SERVIDO, PAGADO, CANCELADO

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Pedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(Integer numeroMesa) { this.numeroMesa = numeroMesa; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}