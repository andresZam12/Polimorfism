# Sistema de Notificaciones — Universidad

> API REST para la gestión y envío de notificaciones universitarias.
> **Proyecto:** `A_Programa_Orientada_a_Objetos_2610_10205`

Construido con **Java 17 + Spring Boot 3 + Spring Data JPA + PostgreSQL**, siguiendo principios de POO (herencia, polimorfismo) y SOLID (especialmente Open/Closed Principle: se pueden agregar nuevos medios de notificación sin modificar los existentes).

---

## Tabla de contenido
1. [Arquitectura y diseño](#arquitectura-y-diseño)
2. [Diagrama de clases](#diagrama-de-clases)
3. [Requisitos previos](#requisitos-previos)
4. [Configuración de PostgreSQL](#configuración-de-postgresql)
5. [Ejecución](#ejecución)
6. [Documentación interactiva (Swagger)](#documentación-interactiva-swagger)
7. [Endpoints principales](#endpoints-principales)
8. [Ejemplos con `curl`](#ejemplos-con-curl)
9. [Cómo agregar un nuevo medio de notificación](#cómo-agregar-un-nuevo-medio-de-notificación)
10. [Subir el proyecto a GitHub](#subir-el-proyecto-a-github)

---

## Arquitectura y diseño

| Capa | Responsabilidad | Clases |
|------|------------------|--------|
| **Modelo** | Dominio del problema — entidades JPA | `Notificacion` (abstract), `NotificacionEmail`, `NotificacionSMS`, `NotificacionAppMovil`, `ResultadoEnvio`, enums |
| **Repositorio** | Acceso a datos | `NotificacionRepository` (Spring Data JPA) |
| **Servicio** | Reglas de negocio + orquestación | `NotificacionService` |
| **Controlador REST** | Exposición HTTP | `NotificacionController` |
| **DTO** | Contratos de entrada/salida | `EmailRequest`, `SmsRequest`, `AppMovilRequest`, `NotificacionResponse`, `ResultadoEnvioResponse` |
| **Excepciones** | Manejo uniforme de errores | `GlobalExceptionHandler` + excepciones custom |

### Por qué herencia (y no un `if/else` por medio)

- `Notificacion` es una clase **abstracta** que encapsula los atributos comunes (`codigo`, `destinatario`, `mensaje`, `fechaEnvio`, `estado`, `tipoSituacion`) y declara el método `enviar()` como **abstracto**.
- Cada subclase (`NotificacionEmail`, `NotificacionSMS`, `NotificacionAppMovil`) implementa `enviar()` a su manera y agrega los atributos particulares de su medio.
- El servicio invoca `n.enviar()` **sin saber** el medio concreto → polimorfismo puro.
- Para agregar un nuevo medio (p. ej., WhatsApp): basta con crear una subclase — **no se modifica nada del código existente**.

La estrategia JPA es `InheritanceType.JOINED`: una tabla común (`notificaciones`) + una tabla por cada subclase con sus atributos específicos.

---

## Diagrama de clases

Ver [`docs/diagrama-clases.png`](docs/diagrama-clases.png).

También se incluye la fuente editable en PlantUML: [`docs/diagrama-clases.puml`](docs/diagrama-clases.puml).
Para regenerar el PNG con PlantUML (requiere `plantuml` instalado):

```bash
plantuml docs/diagrama-clases.puml
```

---

## Requisitos previos

| Herramienta | Versión |
|-------------|---------|
| Java        | 17 o superior |
| Maven       | 3.8+ (o usa el wrapper `./mvnw` si lo agregas) |
| PostgreSQL  | 13+ |
| Git         | 2+ |

Comprobación rápida:

```bash
java -version
mvn -version
psql --version
```

---

## Configuración de PostgreSQL

Crea la base de datos y (opcionalmente) un usuario dedicado:

```sql
-- conéctate como superusuario, p. ej. postgres
CREATE DATABASE notificaciones_db;
-- el usuario 'postgres' con contraseña 'postgres' ya sirve por defecto;
-- si usas otro, ajústalo en application.properties
```

Luego edita `src/main/resources/application.properties` si tus credenciales son distintas:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/notificaciones_db
spring.datasource.username=<tu_usuario>
spring.datasource.password=<tu_contraseña>
```

> **Tip:** Si no quieres instalar PostgreSQL de inmediato, puedes arrancar con el perfil `h2` (base en memoria) — ver siguiente sección.

---

## Ejecución

### Opción A — Con PostgreSQL (perfil `dev`, por defecto)

```bash
mvn spring-boot:run
```

### Opción B — Con H2 en memoria (perfil `h2`, sin instalar nada)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

Console H2 (solo perfil `h2`): http://localhost:8080/h2-console
(JDBC URL: `jdbc:h2:mem:notificacionesdb`, user `sa`, sin contraseña).

### Construir JAR ejecutable

```bash
mvn clean package
java -jar target/notificaciones-1.0.0.jar
```

### Ejecutar tests

```bash
mvn test
```

---

## Documentación interactiva (Swagger)

Una vez arriba la app:

- Swagger UI: **http://localhost:8080/swagger-ui.html**
- OpenAPI JSON: **http://localhost:8080/api-docs**

---

## Endpoints principales

Base URL: `http://localhost:8080/api/v1/notificaciones`

| Método | Ruta                | Descripción |
|-------:|---------------------|-------------|
| POST   | `/email`            | Crea una notificación por correo electrónico |
| POST   | `/sms`              | Crea una notificación por SMS |
| POST   | `/app-movil`        | Crea una notificación push para la app móvil |
| POST   | `/{id}/enviar`      | Envía la notificación (despacho polimórfico) |
| GET    | `/`                 | Lista todas las notificaciones (filtrable por `?estado=`) |
| GET    | `/{id}`             | Obtiene una notificación por id |

---

## Ejemplos con `curl`

### Crear notificación por **correo electrónico**

```bash
curl -X POST http://localhost:8080/api/v1/notificaciones/email \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "NOT-001",
    "destinatario": "Juan Pérez",
    "mensaje": "Sus calificaciones del semestre 2026-1 ya están disponibles en el portal.",
    "tipoSituacion": "PUBLICACION_CALIFICACIONES",
    "asunto": "Publicación de calificaciones",
    "correoDestinatario": "juan.perez@uni.edu",
    "copiaOculta": "archivo@uni.edu"
  }'
```

### Crear notificación por **SMS**

```bash
curl -X POST http://localhost:8080/api/v1/notificaciones/sms \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "NOT-002",
    "destinatario": "María López",
    "mensaje": "Recordatorio: el pago de matrícula vence el 30 de abril.",
    "tipoSituacion": "RECORDATORIO_PAGO_MATRICULA",
    "numeroTelefono": "+573001112233",
    "proveedorSMS": "Twilio"
  }'
```

### Crear notificación para **app móvil**

```bash
curl -X POST http://localhost:8080/api/v1/notificaciones/app-movil \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "NOT-003",
    "destinatario": "Carlos Ruiz",
    "mensaje": "La clase de Algoritmos del jueves fue cancelada.",
    "tipoSituacion": "CANCELACION_CLASE",
    "tokenDispositivo": "fcm-token-abc123",
    "prioridad": "ALTA",
    "sonidoAlerta": true
  }'
```

### Disparar el envío (polimórfico)

```bash
curl -X POST http://localhost:8080/api/v1/notificaciones/1/enviar
```

### Listar

```bash
curl http://localhost:8080/api/v1/notificaciones
curl "http://localhost:8080/api/v1/notificaciones?estado=ENVIADA"
```

---

## Cómo agregar un nuevo medio de notificación

Digamos que la universidad pide agregar WhatsApp:

1. Crear la subclase:

   ```java
   @Entity
   @Table(name = "notificaciones_whatsapp")
   @DiscriminatorValue("WHATSAPP")
   public class NotificacionWhatsApp extends Notificacion {
       private String numeroWhatsApp;
       private String plantilla;

       @Override
       public ResultadoEnvio enviar() {
           // lógica específica de WhatsApp
           marcarEnviada();
           return ResultadoEnvio.ok("Entregado a WhatsApp Business API");
       }

       @Override public String getMedio() { return "WHATSAPP"; }
   }
   ```

2. Crear `WhatsAppRequest` (DTO) + un endpoint `POST /whatsapp` en el controlador + un método `crearWhatsApp(...)` en el servicio.

**Nada de la lógica existente cambia.** Los endpoints ya creados siguen funcionando y la misma llamada a `POST /{id}/enviar` despachará correctamente al nuevo medio.

---

## Subir el proyecto a GitHub

Desde la raíz del proyecto (`notificaciones-universidad/`):

```bash
# 1. Inicializar repositorio local
git init
git branch -M main

# 2. Añadir y commitear todo
git add .
git commit -m "Proyecto inicial: sistema de notificaciones universidad (POO + Spring Boot)"

# 3. Crear el repo remoto en GitHub (UI web: https://github.com/new)
#    Nombre sugerido: notificaciones-universidad
#    NO inicialices el repo con README (ya tenemos uno)

# 4. Vincular remoto y subir
git remote add origin https://github.com/<TU_USUARIO>/notificaciones-universidad.git
git push -u origin main
```

Reemplaza `<TU_USUARIO>` con tu nombre de usuario de GitHub.
La URL final (para entregar) queda:

```
https://github.com/<TU_USUARIO>/notificaciones-universidad
```

### Alternativa con GitHub CLI (más rápido)

Si tienes `gh` instalado:

```bash
gh repo create notificaciones-universidad --public --source=. --remote=origin --push
```

---

## Estructura del proyecto

```
notificaciones-universidad/
├── pom.xml
├── README.md
├── .gitignore
├── docs/
│   ├── diagrama-clases.puml
│   ├── diagrama-clases.png
│   ├── diagrama-clases.svg
│   └── diagrama-clases.dot
└── src/
    ├── main/
    │   ├── java/edu/universidad/notificaciones/
    │   │   ├── NotificacionesApplication.java
    │   │   ├── controller/NotificacionController.java
    │   │   ├── service/NotificacionService.java
    │   │   ├── repository/NotificacionRepository.java
    │   │   ├── model/
    │   │   │   ├── Notificacion.java        (abstract)
    │   │   │   ├── NotificacionEmail.java
    │   │   │   ├── NotificacionSMS.java
    │   │   │   ├── NotificacionAppMovil.java
    │   │   │   ├── ResultadoEnvio.java
    │   │   │   └── enums/
    │   │   │       ├── EstadoNotificacion.java
    │   │   │       └── TipoSituacion.java
    │   │   ├── dto/
    │   │   │   ├── EmailRequest.java
    │   │   │   ├── SmsRequest.java
    │   │   │   ├── AppMovilRequest.java
    │   │   │   ├── NotificacionResponse.java
    │   │   │   └── ResultadoEnvioResponse.java
    │   │   └── exception/
    │   │       ├── NotificacionNotFoundException.java
    │   │       ├── CodigoDuplicadoException.java
    │   │       └── GlobalExceptionHandler.java
    │   └── resources/
    │       ├── application.properties
    │       └── application-h2.properties
    └── test/
        └── java/edu/universidad/notificaciones/
            └── NotificacionesApplicationTests.java
```

---

## Autor

**Andrés** — andresze2001@gmail.com
