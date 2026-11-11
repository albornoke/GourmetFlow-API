package gourmetflow_api.repository;

import gourmetflow_api.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {
    Optional<Plato> findByNombreIgnoreCase(String nombre);
}