package gourmetflow_api.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MesaModelValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void mesaValida_NoDeberiaTenerViolaciones() {
        Mesa mesa = new Mesa();
        mesa.setNumeroMesa(1);
        mesa.setCapacidad(4);
        mesa.setEstado("LIBRE");

        Set<ConstraintViolation<Mesa>> violaciones = validator.validate(mesa);

        assertThat(violaciones).isEmpty();
    }

    @Test
    void numeroMesaNulo_DeberiaSerInvalido() {
        Mesa mesa = new Mesa();
        mesa.setNumeroMesa(null);
        mesa.setCapacidad(4);
        mesa.setEstado("LIBRE");

        Set<ConstraintViolation<Mesa>> violaciones = validator.validate(mesa);

        assertThat(violaciones).anyMatch(v -> v.getPropertyPath().toString().equals("numeroMesa"));
    }

    @Test
    void numeroMesaCero_DeberiaSerInvalido() {
        Mesa mesa = new Mesa();
        mesa.setNumeroMesa(0);
        mesa.setCapacidad(4);
        mesa.setEstado("LIBRE");

        Set<ConstraintViolation<Mesa>> violaciones = validator.validate(mesa);

        assertThat(violaciones).anyMatch(v -> v.getMessage().contains("al menos 1"));
    }

    @Test
    void capacidadNula_DeberiaSerInvalida() {
        Mesa mesa = new Mesa();
        mesa.setNumeroMesa(1);
        mesa.setCapacidad(null);
        mesa.setEstado("LIBRE");

        Set<ConstraintViolation<Mesa>> violaciones = validator.validate(mesa);

        assertThat(violaciones).anyMatch(v -> v.getPropertyPath().toString().equals("capacidad"));
    }

    @Test
    void capacidadMayorA20_DeberiaSerInvalida() {
        Mesa mesa = new Mesa();
        mesa.setNumeroMesa(1);
        mesa.setCapacidad(21);
        mesa.setEstado("LIBRE");

        Set<ConstraintViolation<Mesa>> violaciones = validator.validate(mesa);

        assertThat(violaciones).anyMatch(v -> v.getMessage().contains("máxima permitida"));
    }

    @Test
    void estadoVacio_DeberiaSerInvalido() {
        Mesa mesa = new Mesa();
        mesa.setNumeroMesa(1);
        mesa.setCapacidad(4);
        mesa.setEstado("");

        Set<ConstraintViolation<Mesa>> violaciones = validator.validate(mesa);

        assertThat(violaciones).anyMatch(v -> v.getPropertyPath().toString().equals("estado"));
    }
}
