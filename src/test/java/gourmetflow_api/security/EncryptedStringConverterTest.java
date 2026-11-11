package gourmetflow_api.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EncryptedStringConverterTest {

    private final EncryptedStringConverter converter = new EncryptedStringConverter();

    @Test
    void convertToDatabaseColumn_ConValorNulo_DeberiaRetornarNulo() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    void convertToEntityAttribute_ConValorNulo_DeberiaRetornarNulo() {
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

    @Test
    void cifrarYDescifrar_DeberiaRecuperarValorOriginal() {
        String original = "correo@gourmetflow.com";

        String cifrado = converter.convertToDatabaseColumn(original);
        String descifrado = converter.convertToEntityAttribute(cifrado);

        assertThat(cifrado).isNotNull();
        assertThat(cifrado).isNotEqualTo(original);
        assertThat(descifrado).isEqualTo(original);
    }

    @Test
    void cifrar_DosVecesMismoValor_DeberiaProducirSalidaConsistente() {
        String original = "dato sensible";

        String cifrado1 = converter.convertToDatabaseColumn(original);
        String cifrado2 = converter.convertToDatabaseColumn(original);

        // AES ECB sin IV produce la misma salida para la misma entrada
        assertThat(cifrado1).isEqualTo(cifrado2);
    }

    @Test
    void descifrar_ConValorNoBase64_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> converter.convertToEntityAttribute("@@@ no es base64 @@@"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error crítico al desencriptar");
    }
}
