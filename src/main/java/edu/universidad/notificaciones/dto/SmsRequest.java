package edu.universidad.notificaciones.dto;

import edu.universidad.notificaciones.model.enums.TipoSituacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Datos necesarios para crear una notificación por SMS. */
@Data
public class SmsRequest {

    @NotBlank @Size(max = 50)
    private String codigo;

    @NotBlank @Size(max = 200)
    private String destinatario;

    @NotBlank @Size(max = 160, message = "El mensaje SMS no puede exceder 160 caracteres")
    private String mensaje;

    @NotNull
    private TipoSituacion tipoSituacion;

    @NotBlank
    @Pattern(regexp = "\\+?\\d{7,15}", message = "Número de teléfono inválido")
    private String numeroTelefono;

    @NotBlank @Size(max = 50)
    private String proveedorSMS;
}
