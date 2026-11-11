package gourmetflow_api.dto;

import gourmetflow_api.model.Rol;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PlatoRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void dtoValido_NoDeberiaTenerViolaciones() {
        PlatoRequestDTO dto = new PlatoRequestDTO();
        dto.setNombre("Ceviche Mixto");
        dto.setPrecio(new BigDecimal("45.50"));
        dto.setDisponible(true);

        Set<ConstraintViolation<PlatoRequestDTO>> violaciones = validator.validate(dto);

        assertThat(violaciones).isEmpty();
    }

    @Test
    void nombreVacio_DeberiaSerInvalido() {
        PlatoRequestDTO dto = new PlatoRequestDTO();
        dto.setNombre("");
        dto.setPrecio(new BigDecimal("10.00"));
        dto.setDisponible(true);

        Set<ConstraintViolation<PlatoRequestDTO>> violaciones = validator.validate(dto);

        assertThat(violaciones).anyMatch(v -> v.getPropertyPath().toString().equals("nombre"));
    }

    @Test
    void precioNulo_DeberiaSerInvalido() {
        PlatoRequestDTO dto = new PlatoRequestDTO();
        dto.setNombre("Plato Válido");
        dto.setPrecio(null);
        dto.setDisponible(true);

        Set<ConstraintViolation<PlatoRequestDTO>> violaciones = validator.validate(dto);

        assertThat(violaciones).anyMatch(v -> v.getPropertyPath().toString().equals("precio"));
    }

    @Test
    void precioNegativo_DeberiaSerInvalido() {
        PlatoRequestDTO dto = new PlatoRequestDTO();
        dto.setNombre("Plato Válido");
        dto.setPrecio(new BigDecimal("-5.00"));
        dto.setDisponible(true);

        Set<ConstraintViolation<PlatoRequestDTO>> violaciones = validator.validate(dto);

        assertThat(violaciones).anyMatch(v -> v.getMessage().contains("mayor a cero"));
    }

    @Test
    void disponibleNulo_DeberiaSerInvalido() {
        PlatoRequestDTO dto = new PlatoRequestDTO();
        dto.setNombre("Plato Válido");
        dto.setPrecio(new BigDecimal("10.00"));
        dto.setDisponible(null);

        Set<ConstraintViolation<PlatoRequestDTO>> violaciones = validator.validate(dto);

        assertThat(violaciones).anyMatch(v -> v.getPropertyPath().toString().equals("disponible"));
    }
}
