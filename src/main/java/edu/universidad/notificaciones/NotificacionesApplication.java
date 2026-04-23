package edu.universidad.notificaciones;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal del sistema de notificaciones de la universidad.
 *
 * Expone una API REST con Spring Boot para gestionar notificaciones por
 * distintos medios (email, SMS, app móvil). El diseño utiliza herencia
 * para permitir la incorporación de nuevos medios sin modificar los
 * existentes (Open/Closed Principle).
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "API de Notificaciones - Universidad",
        version = "1.0.0",
        description = "Servicio REST para el envío de notificaciones a usuarios de la universidad.",
        contact = @Contact(name = "Andrés", email = "andresze2001@gmail.com")
    )
)
public class NotificacionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificacionesApplication.class, args);
    }
}
