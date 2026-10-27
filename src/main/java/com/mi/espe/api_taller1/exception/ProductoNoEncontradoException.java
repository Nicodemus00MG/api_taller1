package com.mi.espe.api_taller1.exception;

// Se lanza cuando se consulta o elimina un producto cuyo id no existe (HTTP 404).
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
