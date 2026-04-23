package edu.universidad.notificaciones.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Resultado devuelto por el método polimórfico {@code enviar()} de cada
 * subclase de {@link Notificacion}. Encapsula el éxito del envío y la
 * respuesta del proveedor (SMTP, gateway SMS, FCM, etc.).
 */
@Getter
@ToString
@AllArgsConstructor
public class ResultadoEnvio {

    private final boolean exitoso;
    private final String mensajeProveedor;
    private final LocalDateTime fechaIntento;

    public static ResultadoEnvio ok(String mensajeProveedor) {
        return new ResultadoEnvio(true, mensajeProveedor, LocalDateTime.now());
    }

    public static ResultadoEnvio fallo(String mensajeProveedor) {
        return new ResultadoEnvio(false, mensajeProveedor, LocalDateTime.now());
    }
}
