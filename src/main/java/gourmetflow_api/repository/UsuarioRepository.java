package gourmetflow_api.repository;

import gourmetflow_api.model.Rol;
import gourmetflow_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByUsername(String username);
    
    long countByRolAndActivoTrue(Rol rol);
}