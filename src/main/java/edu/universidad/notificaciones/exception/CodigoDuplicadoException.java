package edu.universidad.notificaciones.exception;

public class CodigoDuplicadoException extends RuntimeException {
    public CodigoDuplicadoException(String codigo) {
        super("Ya existe una notificación con el código '" + codigo + "'");
    }
}
