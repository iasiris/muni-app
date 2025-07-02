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

Aquí puedes ver algunas capturas de pantalla de la app:
![Login](screenshots/login.png)
![Login](screenshots/login_-_verificacion_de_datos.png)
![Login](screenshots/login_-_inicio_enabled_y_mensaje_de_error.png)

![Creación de nuevo usuario](screenshots/register.png)
![Creación de nuevo usuario - verificación de datos](screenshots/register_-_verificacion_de_datos.png)
![Creación de nuevo usuario - verificación de contraseñas](screenshots/register_-_check_pass.png)

![Pantalla principal](screenshots/product_catalog.png)
![Pantalla principal - Búsqueda](screenshots/product_catalog_-_busqueda.png)
![Pantalla principal - Filtrado por precio](screenshots/product_catalog_-_filtrado_por_precio.png)
![Pantalla principal - Filtrado por categoria](screenshots/product_catalog_-_filtrado_por_categoria.png)

![Detalle de producto](screenshots/product_detail.png)

![Carrito de compras](screenshots/carrito.png)
![Carrito de compras vacio](screenshots/carrito_vacio.png)

![Perfil](screenshots/perfil_con_historial_de_ordenes.png)
![Perfil - Notificacion de guardado exitoso](screenshots/perfil_-_guardado_exitoso.png)

![Historial de perdidos](screenshots/historial_de_pedidos.png)
