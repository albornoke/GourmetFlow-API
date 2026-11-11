package gourmetflow_api.model;

import gourmetflow_api.security.EncryptedStringConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String password; // Hash BCrypt

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Rol rol;
   
    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "correo_electronico", nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean activo = true; 

    public Usuario() {}

    public Usuario(String username, String password, Rol rol, String email) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.email = email;
        this.activo = true;
    }
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}