-- Versión 1.0: Creación inicial de esquemas para GourmetFlow Enterprise API
-- Fecha: 2026-11-11

-- 1. Tabla de Platos (Menú del restaurante)
CREATE TABLE IF NOT EXISTS tb_platos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    precio NUMERIC(10, 2) NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_plato_nombre ON tb_platos(nombre);

-- 2. Tabla de Mesas (Gestión de salón)
CREATE TABLE IF NOT EXISTS tb_mesas (
    id BIGSERIAL PRIMARY KEY,
    numero_mesa INT NOT NULL UNIQUE,
    capacidad INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'LIBRE'
);

-- 3. Tabla de Pedidos (Comandas operativas)
CREATE TABLE IF NOT EXISTS tb_pedidos (
    id BIGSERIAL PRIMARY KEY,
    numero_mesa INT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabla de Usuarios y Roles (Seguridad, control de acceso y regla anti-bloqueo)
CREATE TABLE IF NOT EXISTS tb_usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    correo_electronico TEXT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_usuario_username ON tb_usuarios(username);