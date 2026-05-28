# E-Commerce API REST 🛒

¡Bienvenido al backend de nuestra plataforma de E-Commerce! Esta es una API REST robusta, escalable y segura construida con **Spring Boot**. El sistema gestiona todo el ciclo de venta de un comercio electrónico: desde la autenticación de usuarios, gestión de catálogos y carritos de compra, hasta el procesamiento de órdenes.

---

## 🚀 Despliegue (Próximamente)

El proyecto está diseñado para ser agnóstico al entorno y se desplegará utilizando la siguiente infraestructura:

*   **Backend:** Desplegado en [Render](https://render.com/).
*   **Base de Datos:** [TiDB Cloud](https://pingcap.com/tidb-cloud) (Base de datos SQL distribuida compatible con MySQL, ideal para manejar alta concurrencia en e-commerce).

> 🔗 **Links del Proyecto:**
> *   **API en Vivo:** [Próximamente - URL de Render](https://render.com)
> *   **Documentación Swagger UI:** [Próximamente - URL de Swagger en Render](https://render.com)

---

## 🛠️ Tecnologías Utilizadas

*   **Java 21** 
*   **Spring Boot 3**
    *   **Spring Security & JWT:** Autenticación y autorización basada en roles (`ADMIN`, `USER`) y permisos específicos.
    *   **Spring Data JPA:** Capa de persistencia.
    *   **Spring Validation:** Validación estricta de datos de entrada (`@Valid`).
*   **TiDB / MySQL:** Motor de base de datos relacional.
*   **Lombok:** Reducción de código repetitivo (Boilerplate).
*   **Springdoc-openapi (Swagger v3):** Documentación interactiva de la API.

---

## 🔐 Seguridad y Roles

La API implementa una política de seguridad estricta mediante `@PreAuthorize("denyAll()")` a nivel de clase en la mayoría de los controladores, abriendo endpoints específicos según el rol del usuario:

*   **Público:** Registro de usuarios (`/api/user` [POST]) e Inicio de sesión (`/auth/login`).
*   **USER:** Puede gestionar su propio carrito, agregar items, ver productos y crear órdenes de compra.
*   **ADMIN:** Control total del sistema. Puede gestionar categorías, roles, permisos, listar todos los usuarios y auditar carritos/órdenes globales.

---

## 🗺️ Estructura de Endpoints (Resumen de Controladores)

### Autenticación (`/auth`)
*   `POST /auth/login` - Inicio de sesión de usuarios (Devuelve Token JWT).

### Usuarios (`/api/user`)
*   `GET /api/user` - Listar todos los usuarios `[ADMIN]`
*   `GET /api/user/{id}` - Buscar usuario por ID `[USER/ADMIN]`
*   `POST /api/user` - Registrar un nuevo usuario `[PÚBLICO]`
*   `PUT /api/user/{id}` - Actualizar datos de usuario `[USER/ADMIN]`
*   `DELETE /api/user/{id}` - Eliminar cuenta `[USER/ADMIN]`

### Productos y Categorías (`/api/product`, `/api/category`, `/api/productImage`)
*   `GET /api/product` y `/api/category` - Listar catálogo disponible `[USER/ADMIN]`
*   `POST /api/category` - Crear categorías de productos `[ADMIN]`
*   `POST, PUT, DELETE /api/product` - ABM de productos `[USER/ADMIN]`

### Carrito de Compras (`/api/cart`, `/api/cartItem`)
*   `POST /api/cart` - Crear o inicializar un carrito `[USER/ADMIN]`
*   `POST /api/cartItem` - Añadir productos al carrito `[USER/ADMIN]`
*   `GET /api/cart/{id}` - Ver el estado del carrito `[USER/ADMIN]`

### Órdenes de Compra (`/api/order`, `/api/orderItem`)
*   `POST /api/order` - Checkout del carrito para convertirlo en una Orden `[USER/ADMIN]`
*   `GET /api/order` - Historial de órdenes `[USER/ADMIN]`

---

## 💻 Configuración Local y Ejecución

Sigue estos pasos para replicar el entorno de desarrollo en tu máquina local.

### Prerrequisitos
*   JDK 21 instalado.
*   Maven instalado.
*   Una instancia local de MySQL o TiDB activa.

### 1. Clonar el repositorio
```bash
git clone [https://github.com/TU_USUARIO/TU_REPOSITORIO.git](https://github.com/TU_USUARIO/TU_REPOSITORIO.git)
cd TU_REPOSITORIO
