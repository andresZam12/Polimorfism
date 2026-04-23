package edu.universidad.notificaciones.controller;

import edu.universidad.notificaciones.dto.*;
import edu.universidad.notificaciones.model.Notificacion;
import edu.universidad.notificaciones.model.ResultadoEnvio;
import edu.universidad.notificaciones.model.enums.EstadoNotificacion;
import edu.universidad.notificaciones.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controlador REST del sistema de notificaciones.
 *
 * <p>Expone un endpoint por cada tipo de notificación para aprovechar la
 * validación específica (Bean Validation) y un endpoint genérico para
 * listar/obtener/enviar — donde la clave es el polimorfismo a través
 * del servicio.
 */
@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Gestión y envío de notificaciones universitarias")
public class NotificacionController {

    private final NotificacionService service;

    // ---------- Creación ----------

    @Operation(summary = "Crear una notificación por correo electrónico")
    @PostMapping("/email")
    public ResponseEntity<NotificacionResponse> crearEmail(@Valid @RequestBody EmailRequest req) {
        Notificacion creada = service.crearEmail(req);
        return creada(creada);
    }

    @Operation(summary = "Crear una notificación por SMS")
    @PostMapping("/sms")
    public ResponseEntity<NotificacionResponse> crearSMS(@Valid @RequestBody SmsRequest req) {
        Notificacion creada = service.crearSMS(req);
        return creada(creada);
    }

    @Operation(summary = "Crear una notificación push para la app móvil")
    @PostMapping("/app-movil")
    public ResponseEntity<NotificacionResponse> crearAppMovil(@Valid @RequestBody AppMovilRequest req) {
        Notificacion creada = service.crearAppMovil(req);
        return creada(creada);
    }

    // ---------- Envío polimórfico ----------

    @Operation(summary = "Ejecutar el envío polimórfico de una notificación existente")
    @PostMapping("/{id}/enviar")
    public ResponseEntity<ResultadoEnvioResponse> enviar(@PathVariable Long id) {
        ResultadoEnvio resultado = service.enviar(id);
        Notificacion actual = service.obtener(id);
        return ResponseEntity.ok(ResultadoEnvioResponse.from(resultado, NotificacionResponse.from(actual)));
    }

    // ---------- Consulta ----------

    @Operation(summary = "Listar todas las notificaciones")
    @GetMapping
    public ResponseEntity<List<NotificacionResponse>> listar(
            @RequestParam(required = false) EstadoNotificacion estado) {
        List<Notificacion> result = (estado == null)
                ? service.listar()
                : service.listarPorEstado(estado);
        return ResponseEntity.ok(result.stream().map(NotificacionResponse::from).toList());
    }

    @Operation(summary = "Obtener una notificación por id")
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(NotificacionResponse.from(service.obtener(id)));
    }

    // ---------- Helpers ----------

    private ResponseEntity<NotificacionResponse> creada(Notificacion n) {
        return ResponseEntity
                .created(URI.create("/api/v1/notificaciones/" + n.getId()))
                .body(NotificacionResponse.from(n));
    }
}
