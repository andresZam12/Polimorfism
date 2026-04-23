package edu.universidad.notificaciones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notificación enviada como push a la aplicación móvil.
 * Maneja datos adicionales como token de dispositivo, prioridad y sonido.
 */
@Entity
@Table(name = "notificaciones_app_movil")
@Getter
@Setter
@NoArgsConstructor
public class NotificacionAppMovil extends Notificacion {

    private static final Logger log = LoggerFactory.getLogger(NotificacionAppMovil.class);

    @Column(name = "token_dispositivo", length = 300)
    private String tokenDispositivo;

    @Column(length = 20)
    private String prioridad; // ALTA, NORMAL, BAJA

    @Column(name = "sonido_alerta")
    private boolean sonidoAlerta;

    @Override
    public ResultadoEnvio enviar() {
        // En un entorno real aquí se invocaría Firebase Cloud Messaging / APNs.
        log.info("[APP_MOVIL] Push a token={} | prioridad={} | sonido={} | msg={}",
                tokenDispositivo, prioridad, sonidoAlerta, getMensaje());

        if (tokenDispositivo == null || tokenDispositivo.isBlank()) {
            marcarFallida();
            return ResultadoEnvio.fallo("Token de dispositivo inválido");
        }

        marcarEnviada();
        return ResultadoEnvio.ok("Push entregado a FCM (simulado)");
    }

    @Override
    public String getMedio() {
        return "APP_MOVIL";
    }
}
