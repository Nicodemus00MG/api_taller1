package com.mi.espe.api_taller1.exception;

import java.time.LocalDateTime;

// Formato uniforme de los errores que devuelve la API.
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        String ruta
) {
}
