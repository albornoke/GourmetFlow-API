-- Versión 1.0: Creación inicial de esquemas para GourmetFlow API
-- Fecha: 2026-11-10

CREATE TABLE IF NOT EXISTS tb_platos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    precio NUMERIC(10, 2) NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_plato_nombre ON tb_platos(nombre);

CREATE TABLE IF NOT EXISTS tb_mesas (
    id BIGSERIAL PRIMARY KEY,
    numero_mesa INT NOT NULL UNIQUE,
    capacidad INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'LIBRE'
);

CREATE TABLE IF NOT EXISTS tb_pedidos (
    id BIGSERIAL PRIMARY KEY,
    numero_mesa INT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);