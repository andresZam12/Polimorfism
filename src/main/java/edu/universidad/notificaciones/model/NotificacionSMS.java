package edu.universidad.notificaciones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notificación enviada por mensaje de texto (SMS).
 * Maneja datos adicionales como número de teléfono y proveedor.
 */
@Entity
@Table(name = "notificaciones_sms")
@Getter
@Setter
@NoArgsConstructor
public class NotificacionSMS extends Notificacion {

    private static final Logger log = LoggerFactory.getLogger(NotificacionSMS.class);

    @Column(name = "numero_telefono", length = 20)
    private String numeroTelefono;

    @Column(name = "proveedor_sms", length = 50)
    private String proveedorSMS;

    @Override
    public ResultadoEnvio enviar() {
        // En un entorno real aquí se invocaría Twilio / AWS SNS / gateway local.
        log.info("[SMS:{}] Enviando a {} | Mensaje: {}",
                proveedorSMS, numeroTelefono, getMensaje());

        if (numeroTelefono == null || numeroTelefono.length() < 7) {
            marcarFallida();
            return ResultadoEnvio.fallo("Número de teléfono inválido");
        }
        if (getMensaje().length() > 160) {
            marcarFallida();
            return ResultadoEnvio.fallo("El mensaje excede 160 caracteres");
        }

        marcarEnviada();
        return ResultadoEnvio.ok("SMS entregado al gateway " + proveedorSMS + " (simulado)");
    }

    @Override
    public String getMedio() {
        return "SMS";
    }
}
