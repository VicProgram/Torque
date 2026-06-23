# Torque 🏍️

Aplicación Android para el control de mantenimiento de motocicletas. Desarrollada con Jetpack Compose, Material 3, Room y DataStore.

## Funcionalidades

### Garaje (Gestión de Motos)
- **Lista de motos** — Vista con todas las motos registradas, filtros por estado y acceso rápido a detalles.
- **Añadir moto** — Formulario con campos: marca, modelo, año, matrícula, cilindrada, caballos, kilómetros, foto desde galería, y opción de marcar como principal.
- **Detalle de moto** — Fotos, especificaciones técnicas, galería adicional y menú con opciones: establecer como principal, registrar mantenimiento, ver historial, eliminar.
- **Foto principal** — Cada moto puede tener una foto principal y fotos adicionales.

### Mantenimiento
- **Registro de mantenimiento** — Lista de tareas predefinidas con checkboxes: Cambio de Aceite, Filtro de Aceite, Limpieza de Cadena, Presión de Neumáticos, Pastillas de Freno, etc. Incluye buscador y filtros por categoría.
- **Formulario de mantenimiento** — Guarda kilómetros actuales, intervalo, precio, tiempo y notas.
- **Historial de mantenimiento** — Registros filtrables por tipo (Aceite, Filtro, Neumático, Cadena, Frenos) con diálogo de detalle al pulsar.

### Dashboard (Inicio)
- Tarjeta de "próximo servicio" con cuenta atrás.
- Acciones rápidas: Garaje y Mantenimiento.
- Estadísticas: kilometraje total de la moto principal.
- Barra de navegación inferior con botón de retroceso e inicio.
- Acceso a Perfil y Ajustes desde la cabecera.

### Perfil
- Nombre de usuario y fecha de nacimiento (guardados en DataStore).
- Foto de perfil (placeholder).
- Carnets de moto: AM, A1, A2, A.

### Ajustes
- Notificaciones activadas/desactivadas.
- Modo oscuro (siempre activo por defecto mediante tema oscuro de Compose).

## Base de Datos

**Room** (SQLite) con **KSP** — archivo: `torque_room.db`

### Tablas

| Tabla | Descripción | Campos principales |
|---|---|---|
| `motos` | Motocicletas registradas | idMoto, marca, modelo, año, matrícula, cilindrada, caballos, kilómetros, esPrincipal, uriFoto, creadoEn, actualizadoEn |
| `mantenimientos` | Registros de mantenimiento | id, idMoto, nombreMantenimiento, fecha, kms, intervaloKms, precio, tiempo, notas, tareas, creadoEn |
| `fotos_moto` | Fotos adicionales | id, idMoto, ruta (archivo interno), creadoEn |

### Almacenamiento adicional
- **DataStore Preferences** (`settings`): notificaciones activadas, modo oscuro, nombre de perfil, fecha de nacimiento.
- **Almacenamiento interno**: fotos copiadas desde la galería a `filesDir`.

## Tecnologías

| Tecnología | Versión |
|---|---|
| Kotlin | 2.1.20 |
| Android Gradle Plugin | 8.9.2 |
| Gradle | 8.12.1 |
| Jetpack Compose BOM | 2025.05.01 |
| Material 3 | via BOM |
| Room | 2.6.1 |
| KSP | 2.1.20-1.0.32 |
| Coil (imágenes) | 2.7.0 |
| DataStore Preferences | 1.1.2 |
| Navigation Compose | 2.8.8 |
| compileSdk / targetSdk | 35 |
| minSdk | 28 |

## Estructura del Proyecto

```
com.example.torque/
├── MenuPrincipal/       Pantalla de inicio (dashboard)
├── garage/              Gestión de motos (lista, añadir, detalle)
├── maintenance/         Mantenimiento (registro, formulario, historial, opciones)
├── perfil/              Perfil de usuario
├── ajustes/             Ajustes y repositorio DataStore
├── room/                Base de datos, repositorio, entidades, DAOs
├── components/          Componentes UI reutilizables (dashboard)
├── ui/theme/            Tema, colores, tipografía, fuentes
└── utils/               Utilidades (copia de imágenes)
```

## Navegación

```
MenuPrincipal (Dashboard)
  ├── → ListaGaraje
  │     ├── → MotoDetalle
  │     │     ├── → RegistroMantenimiento → FormularioMantenimiento
  │     │     └── → MantenimientoHistorial
  │     └── → AnadirMotoActivity
  ├── → OpcionesMantenimiento
  │     ├── → RegistroMantenimiento → FormularioMantenimiento
  │     └── → MantenimientoHistorial
  ├── → PerfilActivity
  ├── → AjustesActivity
  └── → AnadirMotoActivity
```

## Construir

```bash
export JAVA_HOME=/home/vic/jdk17
export ANDROID_HOME=/home/vic/android-sdk
./gradlew assembleDebug
```
