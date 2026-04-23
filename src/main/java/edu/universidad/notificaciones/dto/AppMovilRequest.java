package edu.universidad.notificaciones.dto;

import edu.universidad.notificaciones.model.enums.TipoSituacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Datos necesarios para crear una notificación push a la app móvil. */
@Data
public class AppMovilRequest {

    @NotBlank @Size(max = 50)
    private String codigo;

    @NotBlank @Size(max = 200)
    private String destinatario;

    @NotBlank @Size(max = 2000)
    private String mensaje;

    @NotNull
    private TipoSituacion tipoSituacion;

    @NotBlank @Size(max = 300)
    private String tokenDispositivo;

    @NotBlank
    @Pattern(regexp = "ALTA|NORMAL|BAJA", message = "prioridad debe ser ALTA, NORMAL o BAJA")
    private String prioridad;

    private boolean sonidoAlerta;
}
