package com.mi.espe.api_taller1.exception;

// Se lanza cuando los datos de un producto incumplen una regla de negocio (HTTP 400).
public class ProductoInvalidoException extends RuntimeException {

    public ProductoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
