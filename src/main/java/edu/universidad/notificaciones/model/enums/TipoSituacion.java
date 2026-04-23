package edu.universidad.notificaciones.model.enums;

/**
 * Situaciones académicas que disparan el envío de una notificación.
 * Si la universidad define un nuevo tipo de evento, basta con agregar
 * un nuevo valor a este enum.
 */
public enum TipoSituacion {
    PUBLICACION_CALIFICACIONES,
    RECORDATORIO_PAGO_MATRICULA,
    CANCELACION_CLASE,
    CONFIRMACION_EVENTO_ACADEMICO
}
