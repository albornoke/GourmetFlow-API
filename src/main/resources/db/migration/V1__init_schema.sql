-- Versión 1.0: Núcleo estructural completo para GourmetFlow Enterprise API
-- Incluye: Platos, Mesas, Usuarios/Roles, Clientes y Comandas operativas con trazabilidad.
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

-- 3. Tabla de Usuarios y Roles (Seguridad, control de acceso y regla anti-bloqueo)
CREATE TABLE IF NOT EXISTS tb_usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    correo_electronico TEXT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_usuario_username ON tb_usuarios(username);

-- 4. Tabla de Clientes (Fidelización y comprador final)
CREATE TABLE IF NOT EXISTS tb_clientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100)
);

CREATE INDEX IF NOT EXISTS idx_cliente_telefono ON tb_clientes(telefono);

-- 5. Tabla de Pedidos / Comandas (Acoplada con Mesa, Cliente y Mesero/Usuario)
CREATE TABLE IF NOT EXISTS tb_pedidos (
    id BIGSERIAL PRIMARY KEY,
    numero_mesa INT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cliente_id BIGINT,
    usuario_id BIGINT,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES tb_clientes(id),
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(id)
);