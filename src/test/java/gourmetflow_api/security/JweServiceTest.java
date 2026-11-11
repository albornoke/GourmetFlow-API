package gourmetflow_api.security;

import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JweServiceTest {

    private JweService jweService;
    private static final String CLAVE_TEST = "x2pDMbdL&3rycHLu!oJP$da5YjKUsy";

    @BeforeEach
    void setUp() {
        jweService = new JweService(CLAVE_TEST);
    }

    @Test
    void generarToken_ConDatosValidos_DeberiaRetornarTokenNoVacio() throws Exception {
        String token = jweService.generarToken("admin_gourmet", null, 60);

        assertThat(token).isNotNull().isNotEmpty();
        // Un JWE serializado tiene 5 partes separadas por puntos
        assertThat(token.split("\\.")).hasSize(5);
    }

    @Test
    void generarToken_DeberiaCifrarElContenido() throws Exception {
        String token = jweService.generarToken("admin", Map.of("rol", "ADMIN"), 60);

        // El token no debe contener el subject en texto plano (está cifrado)
        assertThat(token).doesNotContain("admin");
        assertThat(token).doesNotContain("ADMIN");
    }

    @Test
    void generarToken_ConClaims_DeberiaIncluirlas() throws Exception {
        Map<String, Object> claims = Map.of("rol", "ADMIN", "sucursal", "sede-central");
        String token = jweService.generarToken("usuario1", claims, 60);

        JWTClaimsSet claimsSet = jweService.extraerClaims(token);

        assertThat(claimsSet.getSubject()).isEqualTo("usuario1");
        assertThat(claimsSet.getStringClaim("rol")).isEqualTo("ADMIN");
        assertThat(claimsSet.getStringClaim("sucursal")).isEqualTo("sede-central");
        assertThat(claimsSet.getIssueTime()).isNotNull();
        assertThat(claimsSet.getExpirationTime()).isNotNull();
    }

    @Test
    void validarToken_ConTokenValido_DeberiaRetornarTrue() throws Exception {
        String token = jweService.generarToken("admin", null, 60);

        boolean valido = jweService.validarToken(token);

        assertThat(valido).isTrue();
    }

    @Test
    void validarToken_ConTokenNulo_DeberiaRetornarFalse() {
        assertThat(jweService.validarToken(null)).isFalse();
    }

    @Test
    void validarToken_ConTokenVacio_DeberiaRetornarFalse() {
        assertThat(jweService.validarToken("")).isFalse();
        assertThat(jweService.validarToken("   ")).isFalse();
    }

    @Test
    void validarToken_ConTokenExpirado_DeberiaRetornarFalse() throws Exception {
        // Generar token con expiración en el pasado (-1 minuto)
        String token = jweService.generarToken("admin", null, -1);

        boolean valido = jweService.validarToken(token);

        assertThat(valido).isFalse();
    }

    @Test
    void validarToken_ConTokenModificado_DeberiaRetornarFalse() throws Exception {
        String token = jweService.generarToken("admin", null, 60);
        // Alterar un carácter en la parte central (ciphertext) del token
        int posicionMedia = token.length() / 2;
        char original = token.charAt(posicionMedia);
        char alterado = (original == 'A') ? 'B' : 'A';
        String tokenTampered = token.substring(0, posicionMedia) + alterado + token.substring(posicionMedia + 1);

        boolean valido = jweService.validarToken(tokenTampered);

        assertThat(valido).isFalse();
    }

    @Test
    void validarToken_ConTokenBasura_DeberiaRetornarFalse() {
        assertThat(jweService.validarToken("esto.no.es.un.token.jwe.valido")).isFalse();
    }

    @Test
    void validarToken_GeneradoConOtraClave_DeberiaRetornarFalse() throws Exception {
        String token = jweService.generarToken("admin", null, 60);

        // Crear otro servicio con una clave distinta e intentar validar el token original
        JweService otroServicio = new JweService("una-clave-completamente-diferente");

        assertThat(otroServicio.validarToken(token)).isFalse();
    }

    @Test
    void extraerClaims_ConTokenInvalido_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> jweService.extraerClaims("token.invalido"))
                .isInstanceOf(ParseException.class);
    }

    @Test
    void extraerClaims_DeberiaRespetarTiempoExpiracion() throws Exception {
        String token = jweService.generarToken("usuario", null, 30);

        JWTClaimsSet claimsSet = jweService.extraerClaims(token);
        long diferenciaMs = claimsSet.getExpirationTime().getTime() - claimsSet.getIssueTime().getTime();

        // ~30 minutos en milisegundos (con tolerancia de 1 segundo)
        assertThat(diferenciaMs).isBetween(29L * 60 * 1000, 31L * 60 * 1000);
    }
}
