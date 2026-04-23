package edu.universidad.notificaciones.dto;

import edu.universidad.notificaciones.model.ResultadoEnvio;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/** DTO que refleja el resultado del envío polimórfico. */
@Data
@AllArgsConstructor
public class ResultadoEnvioResponse {

    private boolean exitoso;
    private String mensajeProveedor;
    private LocalDateTime fechaIntento;
    private NotificacionResponse notificacion;

    public static ResultadoEnvioResponse from(ResultadoEnvio r, NotificacionResponse n) {
        return new ResultadoEnvioResponse(r.isExitoso(), r.getMensajeProveedor(), r.getFechaIntento(), n);
    }
}
