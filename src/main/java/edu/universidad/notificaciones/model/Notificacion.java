package edu.universidad.notificaciones.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.universidad.notificaciones.model.enums.EstadoNotificacion;
import edu.universidad.notificaciones.model.enums.TipoSituacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Clase abstracta que representa una notificación genérica del sistema.
 *
 * <p>Encapsula los atributos comunes a todos los medios
 * (código, destinatario, mensaje, fecha de envío y estado) y declara
 * el método {@link #enviar()} como <b>abstracto</b>: cada subclase
 * define cómo se envía la notificación según el medio.
 *
 * <p>La estrategia de herencia JPA es {@code JOINED} para mantener
 * tablas separadas por cada medio concreto (cada subclase agrega sus
 * atributos particulares).
 *
 * <p>Para permitir agregar nuevos medios sin tocar el código existente,
 * basta con crear una nueva subclase que herede de esta (OCP).
 */
@Entity
@Table(name = "notificaciones")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "medio")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NotificacionEmail.class,    name = "EMAIL"),
        @JsonSubTypes.Type(value = NotificacionSMS.class,      name = "SMS"),
        @JsonSubTypes.Type(value = NotificacionAppMovil.class, name = "APP_MOVIL")
})
public abstract class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String destinatario;

    @Column(nullable = false, length = 2000)
    private String mensaje;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoNotificacion estado = EstadoNotificacion.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_situacion", nullable = false, length = 40)
    private TipoSituacion tipoSituacion;

    /**
     * Método polimórfico: cada subclase implementa cómo enviar la
     * notificación según su medio (correo, SMS, push, etc.).
     */
    public abstract ResultadoEnvio enviar();

    /**
     * Identifica el medio de la notificación. Usado para serialización
     * y para exponerlo cómodamente en la API.
     */
    public abstract String getMedio();

    /** Marca la notificación como enviada y registra la fecha. */
    public void marcarEnviada() {
        this.estado = EstadoNotificacion.ENVIADA;
        this.fechaEnvio = LocalDateTime.now();
    }

    /** Marca la notificación como fallida. */
    public void marcarFallida() {
        this.estado = EstadoNotificacion.FALLIDA;
        this.fechaEnvio = LocalDateTime.now();
    }
}
