package edu.universidad.notificaciones;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test: verifica que el contexto de Spring levanta correctamente.
 * Usa el perfil H2 para no depender de una base PostgreSQL real.
 */
@SpringBootTest
@ActiveProfiles("h2")
class NotificacionesApplicationTests {

    @Test
    void contextLoads() {
    }
}
