package edu.universidad.notificaciones.exception;

public class NotificacionNotFoundException extends RuntimeException {
    public NotificacionNotFoundException(Long id) {
        super("No se encontró la notificación con id=" + id);
    }
}
