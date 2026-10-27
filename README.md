# API REST de Gestión de Productos

Práctica 1.1 – Programación Avanzada
**Tema:** Desarrollo de una API REST con inyección de dependencias y manejo de excepciones.

API REST desarrollada con **Spring Boot** y **Spring Web** para la gestión básica de productos de una tienda. Aplica los conceptos de **Inversión de Control**, **inyección de dependencias por constructor** y **manejo centralizado de excepciones**, con separación de responsabilidades entre capas.

> No se utiliza base de datos: los productos se almacenan temporalmente en una colección en memoria y se reinician cada vez que la aplicación arranca.

## Tecnologías

- Java 21
- Spring Boot 4.1.2
- Spring Web (única dependencia de la aplicación)
- Maven (incluye Maven Wrapper)

## Estructura del proyecto

```
src/main/java/com/mi/espe/api_taller1/
├── ApiTaller1Application.java        # Clase principal
├── controller/
│   └── ProductoController.java       # Recibe y responde las solicitudes HTTP
├── service/
│   ├── ProductoService.java          # Interfaz con las operaciones de negocio
│   └── ProductoServiceImpl.java      # Reglas de negocio y validaciones
├── repository/
│   └── ProductoRepository.java       # Colección en memoria (ConcurrentHashMap)
├── model/
│   └── Producto.java                 # Datos del producto: id, nombre, precio, stock
└── exception/
    ├── ProductoNoEncontradoException.java
    ├── ProductoInvalidoException.java
    ├── ErrorResponse.java            # Formato uniforme de los errores
    └── GlobalExceptionHandler.java   # Manejo centralizado (@RestControllerAdvice)
```

## Arquitectura y responsabilidades

```
Cliente (Navegador / Postman)
        │ HTTP
        ▼
ProductoController   → recibe la petición y devuelve el código HTTP
        │ inyección por constructor
        ▼
ProductoServiceImpl  → aplica las reglas de negocio y lanza excepciones
        │ inyección por constructor
        ▼
ProductoRepository   → guarda los productos en memoria

GlobalExceptionHandler → convierte las excepciones en respuestas JSON
```

- Spring administra las instancias (`@RestController`, `@Service`, `@Repository`) y las inyecta por **constructor**.
- El Controller **no crea** el Service con `new`: depende de la interfaz `ProductoService`.
- Las reglas de negocio están solo en el Service, no en el Controller.

## Endpoints

| Método | Ruta | Descripción | Respuesta exitosa |
|---|---|---|---|
| GET | `/api/productos` | Lista todos los productos | 200 OK |
| GET | `/api/productos/{id}` | Obtiene un producto por id | 200 OK |
| POST | `/api/productos` | Registra un nuevo producto | 201 Created |
| DELETE | `/api/productos/{id}` | Elimina un producto existente | 204 No Content |

Ejemplo de cuerpo para POST:

```json
{
  "nombre": "Teclado",
  "precio": 25.0,
  "stock": 5
}
```

## Reglas de negocio

| Regla | Excepción | Código HTTP |
|---|---|---|
| El precio debe ser mayor a cero | `ProductoInvalidoException` | 400 Bad Request |
| El stock no puede ser negativo | `ProductoInvalidoException` | 400 Bad Request |
| El nombre es obligatorio | `ProductoInvalidoException` | 400 Bad Request |
| Consultar un id inexistente | `ProductoNoEncontradoException` | 404 Not Found |
| Eliminar un id inexistente | `ProductoNoEncontradoException` | 404 Not Found |

## Formato de error

Todas las excepciones se manejan de forma centralizada en `GlobalExceptionHandler` y devuelven:

```json
{
  "timestamp": "2026-10-27T12:45:38.092",
  "status": 404,
  "error": "Not Found",
  "mensaje": "Producto con id 99 no encontrado",
  "ruta": "/api/productos/99"
}
```

Si se solicita una ruta que no existe (por ejemplo `/api/xyz`), también se responde 404, pero con el mensaje `"La ruta solicitada no existe"`, lo que permite distinguirla de un producto inexistente.

## Ejecución

Requisito: tener instalado Java 21.

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

La API queda disponible en `http://localhost:8080/api/productos`.
Al iniciar se cargan dos productos de ejemplo: **Laptop** (id 1) y **Mouse** (id 2).

## Pruebas

Los endpoints GET pueden probarse desde el navegador; POST y DELETE con Postman o curl.

| # | Caso | Solicitud | Resultado esperado |
|---|---|---|---|
| 1 | Consulta de todos los productos | `GET /api/productos` | 200 |
| 2 | Consulta de un producto existente | `GET /api/productos/1` | 200 |
| 3 | Consulta de un producto inexistente | `GET /api/productos/99` | 404 |
| 4 | Registro de un producto válido | `POST /api/productos` `{"nombre":"Teclado","precio":25.0,"stock":5}` | 201 |
| 5 | Registro con precio inválido | `POST /api/productos` `{"nombre":"X","precio":0,"stock":5}` | 400 |
| 6 | Registro con stock negativo | `POST /api/productos` `{"nombre":"X","precio":10,"stock":-1}` | 400 |
| 7 | Eliminación de un producto existente | `DELETE /api/productos/2` | 204 |
| 8 | Eliminación de un producto inexistente | `DELETE /api/productos/99` | 404 |

Ejemplos con curl:

```bash
curl http://localhost:8080/api/productos
curl http://localhost:8080/api/productos/1
curl -X POST http://localhost:8080/api/productos -H "Content-Type: application/json" -d '{"nombre":"Teclado","precio":25.0,"stock":5}'
curl -X POST http://localhost:8080/api/productos -H "Content-Type: application/json" -d '{"nombre":"X","precio":0,"stock":5}'
curl -X DELETE http://localhost:8080/api/productos/2
```
