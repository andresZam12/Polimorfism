package edu.universidad.notificaciones.dto;

import edu.universidad.notificaciones.model.Notificacion;
import edu.universidad.notificaciones.model.NotificacionAppMovil;
import edu.universidad.notificaciones.model.NotificacionEmail;
import edu.universidad.notificaciones.model.NotificacionSMS;
import edu.universidad.notificaciones.model.enums.EstadoNotificacion;
import edu.universidad.notificaciones.model.enums.TipoSituacion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Respuesta genérica para las notificaciones. Incluye los atributos
 * comunes y, en {@code detalleMedio}, la información adicional según
 * el tipo concreto.
 */
@Data
@Builder
public class NotificacionResponse {

    private Long id;
    private String codigo;
    private String medio;
    private String destinatario;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private EstadoNotificacion estado;
    private TipoSituacion tipoSituacion;
    private Map<String, Object> detalleMedio;

    public static NotificacionResponse from(Notificacion n) {
        Map<String, Object> detalle = new HashMap<>();
        if (n instanceof NotificacionEmail e) {
            detalle.put("asunto", e.getAsunto());
            detalle.put("correoDestinatario", e.getCorreoDestinatario());
            detalle.put("copiaOculta", e.getCopiaOculta());
        } else if (n instanceof NotificacionSMS s) {
            detalle.put("numeroTelefono", s.getNumeroTelefono());
            detalle.put("proveedorSMS", s.getProveedorSMS());
        } else if (n instanceof NotificacionAppMovil a) {
            detalle.put("tokenDispositivo", a.getTokenDispositivo());
            detalle.put("prioridad", a.getPrioridad());
            detalle.put("sonidoAlerta", a.isSonidoAlerta());
        }
        return NotificacionResponse.builder()
                .id(n.getId())
                .codigo(n.getCodigo())
                .medio(n.getMedio())
                .destinatario(n.getDestinatario())
                .mensaje(n.getMensaje())
                .fechaEnvio(n.getFechaEnvio())
                .estado(n.getEstado())
                .tipoSituacion(n.getTipoSituacion())
                .detalleMedio(detalle)
                .build();
    }
}
