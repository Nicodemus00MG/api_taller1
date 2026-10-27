package com.mi.espe.api_taller1.service;

import com.mi.espe.api_taller1.exception.ProductoInvalidoException;
import com.mi.espe.api_taller1.exception.ProductoNoEncontradoException;
import com.mi.espe.api_taller1.model.Producto;
import com.mi.espe.api_taller1.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementación del servicio: contiene las reglas de negocio y lanza excepciones cuando algo falla.
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;

    // Inyección de dependencias por constructor: Spring crea el repositorio y lo pasa aquí.
    public ProductoServiceImpl(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Producto> listar() {
        return repository.listar();
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto con id " + id + " no encontrado"));
    }

    @Override
    public Producto crear(Producto producto) {
        validar(producto);
        producto.setId(null);
        return repository.guardar(producto);
    }

    @Override
    public void eliminar(Long id) {
        obtenerPorId(id);
        repository.eliminar(id);
    }

    // Reglas de negocio: precio mayor a cero y stock no negativo.
    private void validar(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new ProductoInvalidoException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new ProductoInvalidoException("El precio debe ser mayor a cero");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new ProductoInvalidoException("El stock no puede ser negativo");
        }
    }
}
