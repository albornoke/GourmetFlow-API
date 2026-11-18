# 🍽️ GourmetFlow Enterprise API

<p align="center">
  <b>Backend de Nivel Empresarial para la Gestión Integral de Restaurantes y Hostelería</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-Enterprise-brightgreen?style=for-the-badge&logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge&logo=postgresql" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Flyway-Migrations-red?style=for-the-badge&logo=flyway" alt="Flyway">
  <img src="https://img.shields.io/badge/Security-AES%20%2F%20JWE-yellow?style=for-the-badge&logo=jsonwebtokens" alt="Security JWE">
</p>

---

## 🚀 Sobre el Proyecto

**GourmetFlow Enterprise** es una API REST backend robusta, escalable y altamente segura diseñada bajo estrictos principios de arquitectura limpia (SOLID) y Domain-Driven Design (DDD). Está pensada para optimizar y centralizar toda la operación de restaurantes y cadenas gastronómicas: desde el control multisucuersal y turnos del personal, hasta la gestión de comandas en tiempo real, menús, mesas y trazabilidad de clientes.

---

## ✨ Características Principales (Versión 1.0)

* 🏢 **Multisucursal (Sedes):** Capacidad para operar múltiples sedes o puntos físicos de manera centralizada.
* 👥 **Control de Personal y Roles:** Gestión de usuarios segmentada mediante Enum de Roles (`SUPER_ADMIN`, `ADMIN`, `SUPERVISOR`, `CAJERO`, `MESERO`) con reglas de negocio y control de turnos laborales.
* 📦 **Menús y Comandas en Tiempo Real:** Administración de platos, control de stock/disponibilidad y gestión de pedidos (asociados a mesas, clientes y meseros).
* 💳 **Medios de Pago:** Integración lista para múltiples pasarelas y métodos de cobro (Efectivo, Tarjeta, Transferencia, etc.).
* 🛡️ **Seguridad y Cifrado Avanzado:** 
  * Cifrado simétrico AES-256 para datos sensibles en base de datos.
  * Preparado con servicio JWE (JSON Web Encryption con Nimbus JOSE + JWT y AES-256-GCM) para blindaje de tokens.
* 🔄 **Migraciones Automatizadas:** Control de esquemas de base de datos profesional mediante **Flyway**.
* 📋 **Manejo Global de Errores:** Respuestas HTTP uniformes y limpias mediante `@RestControllerAdvice`.
* 🧪 **Alta Testabilidad:** Suite completa de pruebas unitarias (JUnit 5 + Mockito) cubriendo controladores, servicios y validaciones.

---

## 🛠️ Tecnologías y Stack

* **Lenguaje:** Java 21 (Modern releases & Records)
* **Framework:** Spring Boot 4.x / Spring Data JPA / Spring Security
* **Base de Datos:** PostgreSQL
* **Control de Migraciones:** Flyway
* **Seguridad / Cifrado:** Nimbus JOSE + JWT (JWE) & JCA (AES-256)
* **Documentación:** Springdoc OpenAPI (Swagger UI)
* **Pruebas:** JUnit 5, Mockito, Spring Boot Test

---

## ⚙️ Estructura de la Base de Datos (Flyway V1)

El sistema cuenta con un esquema relacional optimizado e íntegro que conecta las 8 entidades clave del negocio:
1. `tb_sedes` (Sucursales)
2. `tb_platos` (Menú)
3. `tb_mesas` (Salón)
4. `tb_turnos` (Jornadas laborales)
5. `tb_usuarios` (Personal y accesos)
6. `tb_clientes` (Fidelización)
7. `tb_medios_pago` (Formas de cobro)
8. `tb_pedidos` (Comandas operativas con trazabilidad total)

---

## 🚀 Cómo Ejecutar el Proyecto Localmente

### Prerrequisitos
* JDK 21 instalado.
* Maven.
* PostgreSQL configurado localmente.

### 1. Clonar el repositorio
```bash
git clone [https://github.com/tu-usuario/GourmetFlow-API.git](https://github.com/tu-usuario/GourmetFlow-API.git)
cd GourmetFlow-API
