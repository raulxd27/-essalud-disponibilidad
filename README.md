# Sistema de Disponibilidad de Medicamentos y Citas Médicas – EsSalud

## 1. Descripción del proyecto

Aplicación web académica que simula la gestión y consulta de disponibilidad de medicamentos y citas médicas en establecimientos de salud.

El sistema permite consultar información, administrar la disponibilidad de citas médicas y gestionar inventarios de medicamentos mediante una arquitectura cliente-servidor.

**Importante:** Este proyecto es una simulación académica. No está integrado con los sistemas oficiales de EsSalud, no consulta información institucional en tiempo real y no realiza reservas médicas reales.

## 2. Arquitectura del proyecto

El repositorio está organizado de la siguiente manera:

```text
essalud-api/
│
├── database/
│   └── 01_esquema.sql
│
├── essalud-api/
│   ├── src/
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   └── ...
│
├── essalud-frontend/
│   ├── src/
│   ├── angular.json
│   ├── package.json
│   ├── package-lock.json
│   └── ...
│
├── .gitignore
└── README.md
```

### Componentes principales

- **database:** contiene los scripts SQL para preparar la base de datos.
- **essalud-api:** contiene el backend desarrollado con Spring Boot.
- **essalud-frontend:** contiene la interfaz web desarrollada con Angular.
- **README.md:** contiene la documentación e instrucciones de instalación y ejecución.

## 3. Tecnologías utilizadas

### Backend

- Java 25.
- Spring Boot 4.
- Spring Security.
- Spring Data JPA.
- Maven.
- JSON Web Token (JWT).
- Microsoft SQL Server JDBC Driver.

### Frontend

- Angular 22.
- TypeScript.
- HTML5.
- SCSS.
- Node.js.
- npm.

### Base de datos

- Microsoft SQL Server 2022.
- SQL Server Management Studio (SSMS).

### Herramientas de desarrollo

- Visual Studio Code.
- PowerShell.
- Git.
- GitHub.

## 4. Requisitos previos

Antes de ejecutar el proyecto, se deben instalar las siguientes herramientas:

| Herramienta | Versión |
|---|---|
| Java JDK | 25 |
| Node.js | 24 o compatible |
| npm | Incluido con Node.js |
| SQL Server | 2022 |
| SQL Server Management Studio | Versión compatible |
| Visual Studio Code | Recomendado |
| Git | Versión compatible |

El proyecto incluye Maven Wrapper, por lo que no es necesario instalar Maven globalmente.

### Verificar las instalaciones

Abrir PowerShell y ejecutar:

```powershell
java -version
node -v
npm -v
git --version
```

## 5. Configuración de la base de datos

El sistema utiliza Microsoft SQL Server como motor de base de datos.

### Paso 1. Iniciar SQL Server

Verificar que el servicio de SQL Server esté iniciado y disponible.

### Paso 2. Abrir SQL Server Management Studio

Conectarse a la instancia de SQL Server correspondiente.

### Paso 3. Crear la base de datos

Crear la base de datos con el siguiente nombre:

```sql
CREATE DATABASE EsSaludDisponibilidadDB;
```

Si la base de datos ya existe, no es necesario volver a crearla.

### Paso 4. Ejecutar el script SQL

Abrir el archivo:

```text
database/01_esquema.sql
```

Ejecutar su contenido desde SQL Server Management Studio, siguiendo el orden de creación de objetos definido en el script.

Verificar que se hayan creado las tablas, relaciones y vistas necesarias.

### Paso 5. Configurar el usuario de conexión

El backend utiliza los siguientes parámetros de conexión:

| Parámetro | Configuración |
|---|---|
| Servidor | 127.0.0.1 |
| Puerto | 1433 |
| Base de datos | EsSaludDisponibilidadDB |
| Usuario | essalud_app |
| Contraseña | Configurada localmente |

El usuario SQL debe tener los permisos necesarios sobre la base de datos.

**Importante:** Cada integrante debe configurar sus propias credenciales de conexión. No se deben publicar contraseñas en GitHub.

## 6. Configuración del backend

El backend está desarrollado con Java y Spring Boot.

### Paso 1. Ingresar a la carpeta del backend

Desde PowerShell, ejecutar:

```powershell
cd C:\PROYECTOS\essalud-api\essalud-api
```

Si el proyecto está ubicado en otra ruta, reemplazarla por la ubicación correspondiente.

### Paso 2. Configurar las variables de entorno

La aplicación utiliza dos variables de entorno para proteger las credenciales:

| Variable | Descripción |
|---|---|
| ESSALUD_DB_PASSWORD | Contraseña del usuario de SQL Server. |
| ESSALUD_JWT_SECRET | Clave secreta utilizada para generar y validar tokens JWT. |

Configurar ambas variables en Windows.

Ejecutar en PowerShell:

```powershell
[Environment]::SetEnvironmentVariable("ESSALUD_DB_PASSWORD", "TU_CLAVE_SQL", "User")

[Environment]::SetEnvironmentVariable("ESSALUD_JWT_SECRET", "TU_CLAVE_JWT_SEGURA", "User")
```

Reemplazar los valores de ejemplo por las credenciales correspondientes.

La clave JWT debe ser suficientemente robusta y compatible con el algoritmo utilizado por la aplicación.

**Seguridad:** No publicar contraseñas, claves JWT ni archivos que contengan credenciales en GitHub.

### Paso 3. Cargar las variables de entorno

Cerrar y abrir una nueva terminal de PowerShell.

Ejecutar:

```powershell
$env:ESSALUD_DB_PASSWORD = [Environment]::GetEnvironmentVariable("ESSALUD_DB_PASSWORD", "User")

$env:ESSALUD_JWT_SECRET = [Environment]::GetEnvironmentVariable("ESSALUD_JWT_SECRET", "User")
```

Estas instrucciones cargan las variables configuradas en Windows dentro de la sesión actual de PowerShell.

### Paso 4. Compilar el backend

Ejecutar:

```powershell
.\mvnw.cmd clean compile
```

Este comando descarga las dependencias necesarias y compila el código fuente.

Si la compilación finaliza correctamente, el backend estará listo para ejecutarse.

### Paso 5. Levantar el backend

Ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

Spring Boot iniciará el servidor en el puerto 8080.

Dirección del backend:

```text
http://localhost:8080
```

**Importante:** Mantener esta terminal abierta mientras se utiliza la aplicación.

## 7. Configuración del frontend

El frontend está desarrollado con Angular.

Para levantarlo, se debe utilizar una segunda terminal de PowerShell.

### Paso 1. Ingresar a la carpeta del frontend

```powershell
cd C:\PROYECTOS\essalud-api\essalud-frontend
```

### Paso 2. Instalar las dependencias

Ejecutar:

```powershell
npm install
```

Este comando instala las dependencias definidas en `package.json`.

### Paso 3. Levantar Angular

Ejecutar:

```powershell
npx ng serve
```

Angular iniciará el servidor de desarrollo en el puerto 4200.

Dirección del frontend:

```text
http://localhost:4200
```

**Importante:** Mantener esta segunda terminal abierta mientras se utiliza el sistema.

## 8. Endpoints principales del backend

El backend proporciona servicios REST para consultar y administrar la información.

| Método | Endpoint | Función |
|---|---|---|
| GET | `/api/establecimientos` | Listar establecimientos. |
| GET | `/api/especialidades` | Listar especialidades médicas. |
| GET | `/api/medicamentos` | Consultar medicamentos. |
| GET | `/api/disponibilidad/medicamentos` | Consultar disponibilidad de medicamentos. |
| POST | `/api/auth/login` | Iniciar sesión. |
| GET | `/api/citas` | Consultar disponibilidad de citas. |
| POST | `/api/citas` | Publicar disponibilidad de citas. |
| PUT | `/api/citas/{id}` | Actualizar disponibilidad de citas. |
| PATCH | `/api/citas/{id}/estado` | Cambiar el estado de una disponibilidad. |

### Parámetros de consulta

Algunos endpoints permiten utilizar parámetros para filtrar la información.

Ejemplo de consulta de disponibilidad de medicamentos:

```text
GET /api/disponibilidad/medicamentos?establecimientoId=1
```

Ejemplo de consulta de citas:

```text
GET /api/citas?especialidadId=1&establecimientoId=1
```

Las operaciones administrativas requieren autenticación mediante JWT y permisos según el rol del usuario.

## 9. Autenticación y control de acceso

El sistema utiliza Spring Security y JSON Web Token (JWT) para proteger las operaciones administrativas.

### Funcionamiento general

1. El usuario envía sus credenciales al endpoint de autenticación.
2. El backend valida las credenciales.
3. Si son correctas, genera un token JWT.
4. El frontend almacena el token para las solicitudes autenticadas.
5. El backend valida el token y los permisos del usuario.

### Roles implementados

- `PROGRAMADOR_CITAS`: administra la disponibilidad de citas.
- `FARMACIA`: administra medicamentos e inventarios.
- `ADMINISTRADOR_SISTEMA`: dispone de permisos administrativos.

Los endpoints de consulta pública no requieren autenticación, mientras que las operaciones de modificación están protegidas.

## 10. Orden correcto de ejecución

Para iniciar correctamente el sistema, seguir este orden:

1. Iniciar el servicio de SQL Server.
2. Verificar que exista la base de datos `EsSaludDisponibilidadDB`.
3. Verificar que las tablas y vistas estén creadas.
4. Configurar las variables de entorno.
5. Abrir una terminal para iniciar el backend Spring Boot.
6. Ejecutar el backend en el puerto 8080.
7. Abrir una segunda terminal para iniciar Angular.
8. Ejecutar el frontend en el puerto 4200.
9. Abrir el navegador e ingresar a:

```text
http://localhost:4200
```

### Resumen de los servicios

| Componente | Puerto | Dirección |
|---|---:|---|
| Backend Spring Boot | 8080 | http://localhost:8080 |
| Frontend Angular | 4200 | http://localhost:4200 |
| SQL Server | 1433 | 127.0.0.1:1433 |

## 11. Consideraciones importantes

- No modificar el esquema de la base de datos sin coordinar previamente con el equipo.
- No publicar contraseñas ni claves JWT.
- No subir carpetas `node_modules`, `target` o `dist` al repositorio.
- Cada integrante debe configurar sus propias credenciales locales.
- El backend y el frontend deben ejecutarse simultáneamente.
- La base de datos debe estar disponible antes de iniciar el backend.
- Utilizar `npm install` para instalar las dependencias del frontend.
- Utilizar Maven Wrapper para compilar y ejecutar el backend.
- No realizar reservas médicas reales mediante este sistema.

## 12. Finalidad académica

El proyecto tiene como finalidad aplicar conocimientos de ingeniería de software, arquitectura cliente-servidor, programación web, seguridad informática, gestión de bases de datos y control de acceso mediante roles.

Asimismo, permite desarrollar habilidades de integración entre tecnologías backend y frontend, diseño de servicios REST, persistencia de datos y administración de sistemas de información.

---

