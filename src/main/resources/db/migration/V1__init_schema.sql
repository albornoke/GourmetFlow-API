-- Versión 1.0: Núcleo estructural completo y comercial para GourmetFlow Enterprise API
-- Incluye: Sedes, Platos, Mesas, Usuarios/Roles, Clientes, Turnos, Medios de Pago y Pedidos.

-- 1. Tabla de Sedes (Multisucursal)
CREATE TABLE IF NOT EXISTS tb_sedes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    direccion VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

-- 2. Tabla de Platos (Menú del restaurante)
CREATE TABLE IF NOT EXISTS tb_platos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    precio NUMERIC(10, 2) NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_plato_nombre ON tb_platos(nombre);

-- 3. Tabla de Mesas (Gestión de salón)
CREATE TABLE IF NOT EXISTS tb_mesas (
    id BIGSERIAL PRIMARY KEY,
    numero_mesa INT NOT NULL UNIQUE,
    capacidad INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'LIBRE',
    sede_id BIGINT,
    CONSTRAINT fk_mesa_sede FOREIGN KEY (sede_id) REFERENCES tb_sedes(id)
);

-- 4. Tabla de Turnos / Horarios del Personal
CREATE TABLE IF NOT EXISTS tb_turnos (
    id BIGSERIAL PRIMARY KEY,
    nombre_turno VARCHAR(50) NOT NULL, -- Ej: 'MAÑANA', 'TARDE', 'NOCHE', 'CONTINUA'
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL
);

-- 5. Tabla de Usuarios y Roles (Personal del restaurante)
CREATE TABLE IF NOT EXISTS tb_usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    correo_electronico TEXT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    turno_id BIGINT,
    CONSTRAINT fk_usuario_turno FOREIGN KEY (turno_id) REFERENCES tb_turnos(id)
);

CREATE INDEX IF NOT EXISTS idx_usuario_username ON tb_usuarios(username);

-- 6. Tabla de Clientes (Fidelización)
CREATE TABLE IF NOT EXISTS tb_clientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100)
);

CREATE INDEX IF NOT EXISTS idx_cliente_telefono ON tb_clientes(telefono);

-- 7. Tabla de Medios de Pago
CREATE TABLE IF NOT EXISTS tb_medios_pago (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE, -- 'EFECTIVO', 'TARJETA', 'TRANSFERENCIA', 'NEQUI'
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- 8. Tabla de Pedidos / Comandas (Núcleo operativo total)
CREATE TABLE IF NOT EXISTS tb_pedidos (
    id BIGSERIAL PRIMARY KEY,
    numero_mesa INT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cliente_id BIGINT,
    usuario_id BIGINT, -- Mesero que atiende
    medio_pago_id BIGINT,
    sede_id BIGINT,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES tb_clientes(id),
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(id),
    CONSTRAINT fk_pedido_medio_pago FOREIGN KEY (medio_pago_id) REFERENCES tb_medios_pago(id),
    CONSTRAINT fk_pedido_sede FOREIGN KEY (sede_id) REFERENCES tb_sedes(id)
);