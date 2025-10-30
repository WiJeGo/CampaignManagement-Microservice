package upc.edu.muusmart.campaignmanagement.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Importar para deshabilitar CSRF
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configuración con la Lambda DSL de Spring Security 6+
        http
                // Deshabilitar CSRF (Cross-Site Request Forgery)
                .csrf(AbstractHttpConfigurer::disable)

                // Configurar la política de sesión como STATELESS (sin estado)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configurar la autorización de peticiones HTTP
                .authorizeHttpRequests(authz -> authz
                        // CORRECCIÓN: Cambiado /api-docs/ a /v3/api-docs/
                        .requestMatchers(
                                "/swagger-ui/**",      // Permite la UI de Swagger
                                "/v3/api-docs/**",     // Permite el JSON de la API
                                "/swagger-ui.html"     // Permite la página principal de Swagger
                        ).permitAll()

                        // Permitir endpoints de salud y métricas
                        .requestMatchers("/health", "/actuator/**").permitAll()

                        // Todas las demás peticiones deben estar autenticadas
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}