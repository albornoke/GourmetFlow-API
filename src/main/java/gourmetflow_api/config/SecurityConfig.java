package gourmetflow_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitado para APIs REST stateless
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos de consulta
                .requestMatchers(HttpMethod.GET, "/api/v1/platos/**").permitAll()
                // Endpoints protegidos que requieren rol ADMIN (ej. Creación de platos)
                .requestMatchers(HttpMethod.POST, "/api/v1/platos/**").hasRole("ADMIN")
                // Actuator y salud del sistema seguros
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults()); // Autenticación HTTP Basic segura

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Usuario administrador simulado con credenciales cifradas con BCrypt
        UserDetails admin = User.builder()
                .username("admin_gourmet")
                .password(passwordEncoder.encode("SecurePass2026!"))
                .roles("ADMIN")
                .build();

        // Usuario mesero con permisos limitados
        UserDetails mesero = User.builder()
                .username("mesero_flow")
                .password(passwordEncoder.encode("WaiterView2026!"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, mesero);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encriptación robusta de contraseñas
    }
}