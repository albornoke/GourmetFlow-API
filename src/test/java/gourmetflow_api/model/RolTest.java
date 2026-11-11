package gourmetflow_api.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RolTest {

    @Test
    void valoresDelEnum_DeberianSerLosCincoDefinidos() {
        Rol[] roles = Rol.values();

        assertThat(roles).hasSize(5);
        assertThat(roles).containsExactly(
                Rol.SUPER_ADMIN,
                Rol.ADMIN,
                Rol.SUPERVISOR,
                Rol.CAJERO,
                Rol.MESERO
        );
    }

    @Test
    void cadaRol_DeberiaTenerDescripcionNoVacia() {
        for (Rol rol : Rol.values()) {
            assertThat(rol.getDescripcion())
                    .as("El rol %s debe tener descripción", rol)
                    .isNotNull()
                    .isNotEmpty();
        }
    }

    @Test
    void valueOf_DeberiaResolverPorNombre() {
        assertThat(Rol.valueOf("ADMIN")).isEqualTo(Rol.ADMIN);
        assertThat(Rol.valueOf("MESERO")).isEqualTo(Rol.MESERO);
    }

    @Test
    void admin_DeberiaTenerDescripcionCorrecta() {
        assertThat(Rol.ADMIN.getDescripcion())
                .contains("Administrador del Restaurante");
    }
}
