package edu.universidad.notificaciones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notificación enviada por correo electrónico.
 * Maneja datos adicionales como asunto y dirección de destino.
 */
@Entity
@Table(name = "notificaciones_email")
@Getter
@Setter
@NoArgsConstructor
public class NotificacionEmail extends Notificacion {

    private static final Logger log = LoggerFactory.getLogger(NotificacionEmail.class);

    @Column(length = 200)
    private String asunto;

    @Column(name = "correo_destinatario", length = 200)
    private String correoDestinatario;

    @Column(name = "copia_oculta", length = 500)
    private String copiaOculta;

    @Override
    public ResultadoEnvio enviar() {
        // En un entorno real aquí se invocaría JavaMailSender / SendGrid / SES.
        // Para este proyecto académico se simula el envío con un log.
        log.info("[EMAIL] Enviando a {} | Asunto: {} | Mensaje: {}",
                correoDestinatario, asunto, getMensaje());

        if (correoDestinatario == null || !correoDestinatario.contains("@")) {
            marcarFallida();
            return ResultadoEnvio.fallo("Dirección de correo inválida");
        }

        marcarEnviada();
        return ResultadoEnvio.ok("Correo entregado al servidor SMTP (simulado)");
    }

    @Override
    public String getMedio() {
        return "EMAIL";
    }
}
