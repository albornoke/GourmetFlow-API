package gourmetflow_api.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SuperAdminAuthServiceTest {

    private final SuperAdminAuthService service = new SuperAdminAuthService();

    @Test
    void generarQrSecreto_DeberiaRetornarUrlNoVacia() {
        String url = service.generarQrSecretoParaMicrosoftAuthenticator("superadmin@gourmetflow.com");

        assertThat(url).isNotNull();
        assertThat(url).isNotEmpty();
        // El formato de URL TOTP debe comenzar con otpauth://totp/
        assertThat(url).startsWith("otpauth://totp/");
        assertThat(url).contains("GourmetFlow-Enterprise");
        assertThat(url).contains("superadmin@gourmetflow.com");
    }

    @Test
    void generarQrSecreto_DosLlamadas_DeberianDiferirEnSecreto() {
        String url1 = service.generarQrSecretoParaMicrosoftAuthenticator("admin@mail.com");
        String url2 = service.generarQrSecretoParaMicrosoftAuthenticator("admin@mail.com");

        // Cada llamada genera un secreto distinto
        assertThat(url1).isNotEqualTo(url2);
    }

    @Test
    void validarCodigoTotp_ConCodigoIncorrecto_DeberiaRetornarFalso() {
        // Secreto fijo de prueba y un código que casi seguro no coincide
        String secretKey = "JBSWY3DPEHPK3PXP";
        boolean resultado = service.validarCodigoTotp(secretKey, 000000);

        assertThat(resultado).isFalse();
    }
}
