package com.mi.espe.api_taller1.repository;

import com.mi.espe.api_taller1.model.Producto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Repositorio: acceso a datos. Aquí se guarda en memoria (un Map) en lugar de una base de datos.
@Repository
public class ProductoRepository {

    private final Map<Long, Producto> datos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    public ProductoRepository() {
        guardar(new Producto(null, "Laptop", 850.0, 10));
        guardar(new Producto(null, "Mouse", 15.5, 50));
    }

    public List<Producto> listar() {
        return new ArrayList<>(datos.values());
    }

    public Optional<Producto> buscarPorId(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    public Producto guardar(Producto producto) {
        if (producto.getId() == null) {
            producto.setId(secuencia.incrementAndGet());
        }
        datos.put(producto.getId(), producto);
        return producto;
    }

    public void eliminar(Long id) {
        datos.remove(id);
    }
}
