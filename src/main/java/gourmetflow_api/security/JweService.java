package gourmetflow_api.security;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * Servicio de seguridad para generar y validar tokens JWE (JSON Web Encryption).
 * <p>
 * Los tokens se cifran con AES-256-GCM usando una clave derivada de la propiedad
 * {@code app.security.jwe.secret-key} mediante SHA-256, garantizando blindaje
 * de la información sensible transportada en las claims.
 */
@Service
public class JweService {

    private static final JWEAlgorithm ALGORITMO = JWEAlgorithm.DIR;
    private static final EncryptionMethod METODO_CIFRADO = EncryptionMethod.A256GCM;

    private final SecretKeySpec claveCifrado;

    public JweService(@Value("${app.security.jwe.secret-key}") String secretKey) {
        this.claveCifrado = derivarClaveAES(secretKey);
    }

    /**
     * Genera un token JWE cifrado con las claims proporcionadas.
     *
     * @param subject           Identificador del sujeto (ej. username).
     * @param claimsAdicionales Mapa de claims personalizadas a incluir en el token.
     * @param expiracionMinutos Tiempo de vida del token en minutos.
     * @return Token JWE serializado y cifrado.
     * @throws JOSEException Si ocurre un error durante el cifrado.
     */
    public String generarToken(String subject, Map<String, Object> claimsAdicionales, long expiracionMinutos)
            throws JOSEException {

        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expiracionMinutos * 60 * 1000);

        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .subject(subject)
                .issueTime(ahora)
                .expirationTime(expiracion);

        if (claimsAdicionales != null) {
            for (Map.Entry<String, Object> entrada : claimsAdicionales.entrySet()) {
                builder.claim(entrada.getKey(), entrada.getValue());
            }
        }

        JWTClaimsSet claims = builder.build();

        JWEHeader header = new JWEHeader.Builder(ALGORITMO, METODO_CIFRADO)
                .contentType("JWT")
                .build();

        JWEObject jweObject = new JWEObject(header, new Payload(claims.toJSONObject()));
        JWEEncrypter encrypter = new DirectEncrypter(claveCifrado);
        jweObject.encrypt(encrypter);

        return jweObject.serialize();
    }

    /**
     * Valida que un token JWE sea íntegro, esté cifrado con la clave del sistema y
     * no haya expirado.
     *
     * @param token Token JWE serializado.
     * @return {@code true} si el token es válido y no expiró; {@code false} en caso contrario.
     */
    public boolean validarToken(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        try {
            JWEObject jweObject = JWEObject.parse(token);
            JWEDecrypter decrypter = new DirectDecrypter(claveCifrado);
            jweObject.decrypt(decrypter);

            JWTClaimsSet claims = JWTClaimsSet.parse(jweObject.getPayload().toJSONObject());
            Date expiracion = claims.getExpirationTime();

            if (expiracion == null) {
                return false;
            }
            return new Date().before(expiracion);
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }

    /**
     * Extrae las claims de un token JWE previamente cifrado con la clave del sistema.
     *
     * @param token Token JWE serializado.
     * @return Conjunto de claims contenidas en el token.
     * @throws ParseException Si el token no se puede analizar.
     * @throws JOSEException  Si el token no se puede descifrar.
     */
    public JWTClaimsSet extraerClaims(String token) throws ParseException, JOSEException {
        JWEObject jweObject = JWEObject.parse(token);
        JWEDecrypter decrypter = new DirectDecrypter(claveCifrado);
        jweObject.decrypt(decrypter);
        return JWTClaimsSet.parse(jweObject.getPayload().toJSONObject());
    }

    /**
     * Deriva una clave AES-256 de 32 bytes a partir de una cadena de longitud
     * arbitraria usando SHA-256, permitiendo usar cualquier valor de configuración.
     */
    private SecretKeySpec derivarClaveAES(String secretKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(secretKey.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(hash, "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("El algoritmo SHA-256 no está disponible en la JVM", e);
        }
    }
}
