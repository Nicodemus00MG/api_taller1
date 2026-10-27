package com.mi.espe.api_taller1.service;

import com.mi.espe.api_taller1.model.Producto;

import java.util.List;

// Interfaz del servicio: define QUÉ hace la lógica de negocio, sin decir CÓMO.
public interface ProductoService {

    List<Producto> listar();

    Producto obtenerPorId(Long id);

    Producto crear(Producto producto);

    void eliminar(Long id);
}
