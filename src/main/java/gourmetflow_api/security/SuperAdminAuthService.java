package gourmetflow_api.security;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;

@Service
public class SuperAdminAuthService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public String generarQrSecretoParaMicrosoftAuthenticator(String emailSuperAdmin) {
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        return GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(
                "GourmetFlow-Enterprise", 
                emailSuperAdmin, 
                key
        );
    }

    public boolean validarCodigoTotp(String secretKey, int codigoIngresado) {
        GoogleAuthenticator gAuthCustom = new GoogleAuthenticator();
        return gAuthCustom.authorize(secretKey, codigoIngresado);
    }
}