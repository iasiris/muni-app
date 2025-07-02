# MuniApp

Aplicación móvil para Android desarrollada en Kotlin, utilizando Jetpack Compose para la interfaz de usuario, Hilt para inyección de dependencias, Room para persistencia local y Retrofit para consumo de APIs.

## Características

- Interfaz moderna con Jetpack Compose
- Navegación con Navigation Compose
- Inyección de dependencias con Hilt
- Persistencia local con Room
- Consumo de servicios REST con Retrofit y OkHttp
- Carga de imágenes con Coil
- Soporte para fuentes personalizadas de Google Fonts

## Requisitos

- Android Studio Flamingo o superior
- JDK 11 o superior
- Android SDK 30 o superior

## Instalación

1. Clona el repositorio:git clone git@iasiris:iasiris/muni-app.git
2. Abre el proyecto en Android Studio.
3. Sincroniza los gradle scripts y descarga las dependencias.
4. Ejecuta la app en un emulador o dispositivo físico.

## Estructura del proyecto

- `app/src/main/java/com/iasiris/muniapp/` — Código fuente principal
- `app/src/main/res/` — Recursos (layouts, strings, imágenes, etc.)
- `app/build.gradle.kts` — Configuración de dependencias y plugins

## Dependencias principales

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Hilt](https://dagger.dev/hilt/)
- [Room](https://developer.android.com/jetpack/androidx/releases/room)
- [Retrofit](https://square.github.io/retrofit/)
- [Coil]([https://coil-kt.github.io/coil/](https://coil-kt.github.io/coil/))

## Screenshots

Login:

Pantalla de inicio de sesión

<img src="screenshots/login.png" alt="Login" width="300"/>

Verificación de datos en login

<img src="screenshots/login_-_verificacion_de_datos.png" alt="Login - Verificación de datos" width="300"/>

Login con botón habilitado y mensaje de error

<img src="screenshots/login_-_inicio_enabled_y_mensaje_de_error.png" alt="Login - Error" width="300"/>

Registro de Usuario:

Pantalla de registro

<img src="screenshots/register.png" alt="Registro" width="300"/>

Verificación de datos en registro

<img src="screenshots/register_-_verificacion_de_datos.png" alt="Registro - Verificación" width="300"/>

Verificación de contraseñas

<img src="screenshots/register_-_check_pass.png" alt="Registro - Verificación de contraseñas" width="300"/>

Catálogo de Productos:

Catálogo de productos

<img src="screenshots/product_catalog.png" alt="Catálogo" width="300"/>

Búsqueda en el catálogo

<img src="screenshots/product_catalog_-_busqueda.png" alt="Catálogo - Búsqueda" width="300"/>

Filtrado por precio

<img src="screenshots/product_catalog_-_filtrado_por_precio.png" alt="Catálogo - Precio" width="300"/>

Filtrado por categoría

<img src="screenshots/product_catalog_-_filtrado_por_categoria.png" alt="Catálogo - Categoría" width="300"/>

Detalle de Producto:

<img src="screenshots/product_detail.png" alt="Detalle" width="300"/>

Carrito:

Carrito con productos

<img src="screenshots/carrito.png" alt="Carrito lleno" width="300"/>

Carrito vacío

<img src="screenshots/carrito_vacio.png" alt="Carrito vacío" width="300"/>

Perfil:

Perfil con historial de órdenes

<img src="screenshots/perfil_con_historial_de_ordenes.png" alt="Perfil" width="300"/>

Guardado exitoso en perfil

<img src="screenshots/perfil_-_guardado_exitoso.png" alt="Perfil - Guardado exitoso" width="300"/>

Historial de Pedidos:

<img src="screenshots/historial_de_pedidos.png" alt="Historial" width="300"/>
