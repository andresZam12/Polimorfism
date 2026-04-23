package edu.universidad.notificaciones.service;

import edu.universidad.notificaciones.dto.AppMovilRequest;
import edu.universidad.notificaciones.dto.EmailRequest;
import edu.universidad.notificaciones.dto.SmsRequest;
import edu.universidad.notificaciones.exception.CodigoDuplicadoException;
import edu.universidad.notificaciones.exception.NotificacionNotFoundException;
import edu.universidad.notificaciones.model.*;
import edu.universidad.notificaciones.model.enums.EstadoNotificacion;
import edu.universidad.notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de dominio para la gestión de notificaciones.
 *
 * <p>Orquesta la creación, persistencia y envío polimórfico de las
 * notificaciones. El método {@link #enviar(Long)} es el mejor ejemplo
 * del polimorfismo de la solución: invoca {@code n.enviar()} sin saber
 * si es Email, SMS o AppMovil — la JVM despacha al método de la subclase
 * concreta.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NotificacionService {

    private final NotificacionRepository repository;

    // ---------------- Creación ----------------

    public Notificacion crearEmail(EmailRequest req) {
        validarCodigoUnico(req.getCodigo());
        NotificacionEmail n = new NotificacionEmail();
        copiarComunes(n, req.getCodigo(), req.getDestinatario(), req.getMensaje());
        n.setTipoSituacion(req.getTipoSituacion());
        n.setAsunto(req.getAsunto());
        n.setCorreoDestinatario(req.getCorreoDestinatario());
        n.setCopiaOculta(req.getCopiaOculta());
        return repository.save(n);
    }

    public Notificacion crearSMS(SmsRequest req) {
        validarCodigoUnico(req.getCodigo());
        NotificacionSMS n = new NotificacionSMS();
        copiarComunes(n, req.getCodigo(), req.getDestinatario(), req.getMensaje());
        n.setTipoSituacion(req.getTipoSituacion());
        n.setNumeroTelefono(req.getNumeroTelefono());
        n.setProveedorSMS(req.getProveedorSMS());
        return repository.save(n);
    }

    public Notificacion crearAppMovil(AppMovilRequest req) {
        validarCodigoUnico(req.getCodigo());
        NotificacionAppMovil n = new NotificacionAppMovil();
        copiarComunes(n, req.getCodigo(), req.getDestinatario(), req.getMensaje());
        n.setTipoSituacion(req.getTipoSituacion());
        n.setTokenDispositivo(req.getTokenDispositivo());
        n.setPrioridad(req.getPrioridad());
        n.setSonidoAlerta(req.isSonidoAlerta());
        return repository.save(n);
    }

    // ---------------- Envío polimórfico ----------------

    /**
     * Ejecuta el envío de una notificación. Polimorfismo puro:
     * {@code n.enviar()} ejecuta la implementación de la subclase
     * correspondiente (Email / SMS / App Movil).
     */
    public ResultadoEnvio enviar(Long id) {
        Notificacion n = obtener(id);
        ResultadoEnvio resultado = n.enviar();
        repository.save(n);           // persiste estado + fechaEnvio actualizados
        return resultado;
    }

    // ---------------- Consulta ----------------

    @Transactional(readOnly = true)
    public List<Notificacion> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Notificacion obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotificacionNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Notificacion> listarPorEstado(EstadoNotificacion estado) {
        return repository.findByEstado(estado);
    }

    // ---------------- Helpers ----------------

    private void copiarComunes(Notificacion n, String codigo, String destinatario, String mensaje) {
        n.setCodigo(codigo);
        n.setDestinatario(destinatario);
        n.setMensaje(mensaje);
        n.setEstado(EstadoNotificacion.PENDIENTE);
    }

    private void validarCodigoUnico(String codigo) {
        if (repository.existsByCodigo(codigo)) {
            throw new CodigoDuplicadoException(codigo);
        }
    }
}
