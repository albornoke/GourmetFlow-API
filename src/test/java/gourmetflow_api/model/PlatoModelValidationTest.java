package gourmetflow_api.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PlatoModelValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void platoValido_NoDeberiaTenerViolaciones() {
        Plato plato = new Plato();
        plato.setNombre("Ceviche Mixto");
        plato.setPrecio(new BigDecimal("45.50"));
        plato.setDisponible(true);

        Set<ConstraintViolation<Plato>> violaciones = validator.validate(plato);

        assertThat(violaciones).isEmpty();
    }

    @Test
    void nombreEnBlanco_DeberiaSerInvalido() {
        Plato plato = new Plato();
        plato.setNombre("");
        plato.setPrecio(new BigDecimal("10.00"));
        plato.setDisponible(true);

        Set<ConstraintViolation<Plato>> violaciones = validator.validate(plato);

        assertThat(violaciones).isNotEmpty();
        assertThat(violaciones).anyMatch(v -> v.getPropertyPath().toString().equals("nombre"));
    }

    @Test
    void nombreMuyCorto_DeberiaSerInvalido() {
        Plato plato = new Plato();
        plato.setNombre("AB"); // menos de 3 caracteres
        plato.setPrecio(new BigDecimal("10.00"));
        plato.setDisponible(true);

        Set<ConstraintViolation<Plato>> violaciones = validator.validate(plato);

        assertThat(violaciones).anyMatch(v -> v.getMessage().contains("entre 3 y 100"));
    }

    @Test
    void precioNulo_DeberiaSerInvalido() {
        Plato plato = new Plato();
        plato.setNombre("Plato Válido");
        plato.setPrecio(null);
        plato.setDisponible(true);

        Set<ConstraintViolation<Plato>> violaciones = validator.validate(plato);

        assertThat(violaciones).anyMatch(v -> v.getPropertyPath().toString().equals("precio"));
    }

    @Test
    void precioCero_DeberiaSerInvalido() {
        Plato plato = new Plato();
        plato.setNombre("Plato Válido");
        plato.setPrecio(BigDecimal.ZERO);
        plato.setDisponible(true);

        Set<ConstraintViolation<Plato>> violaciones = validator.validate(plato);

        assertThat(violaciones).anyMatch(v -> v.getMessage().contains("mayor a cero"));
    }

    @Test
    void disponibleNulo_DeberiaSerInvalido() {
        Plato plato = new Plato();
        plato.setNombre("Plato Válido");
        plato.setPrecio(new BigDecimal("10.00"));
        plato.setDisponible(null);

        Set<ConstraintViolation<Plato>> violaciones = validator.validate(plato);

        assertThat(violaciones).anyMatch(v -> v.getPropertyPath().toString().equals("disponible"));
    }
}
