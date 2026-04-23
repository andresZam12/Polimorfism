package edu.universidad.notificaciones.dto;

import edu.universidad.notificaciones.model.enums.TipoSituacion;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Datos necesarios para crear una notificación por correo electrónico. */
@Data
public class EmailRequest {

    @NotBlank @Size(max = 50)
    private String codigo;

    @NotBlank @Size(max = 200)
    private String destinatario;

    @NotBlank @Size(max = 2000)
    private String mensaje;

    @NotNull
    private TipoSituacion tipoSituacion;

    @NotBlank @Size(max = 200)
    private String asunto;

    @NotBlank @Email @Size(max = 200)
    private String correoDestinatario;

    @Size(max = 500)
    private String copiaOculta;
}
