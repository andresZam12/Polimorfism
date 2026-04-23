package edu.universidad.notificaciones.repository;

import edu.universidad.notificaciones.model.Notificacion;
import edu.universidad.notificaciones.model.enums.EstadoNotificacion;
import edu.universidad.notificaciones.model.enums.TipoSituacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para {@link Notificacion}. Por estar declarado sobre la
 * clase base abstracta, opera de forma polimórfica sobre las tres
 * subclases (Email, SMS, AppMovil) gracias a la estrategia de herencia
 * JOINED definida en la entidad.
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    Optional<Notificacion> findByCodigo(String codigo);

    List<Notificacion> findByEstado(EstadoNotificacion estado);

    List<Notificacion> findByTipoSituacion(TipoSituacion tipoSituacion);

    boolean existsByCodigo(String codigo);
}
