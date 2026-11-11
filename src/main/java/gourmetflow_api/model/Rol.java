package gourmetflow_api.model;

public enum Rol {
    SUPER_ADMIN("Super Administrador - Desarrollador (Acceso global y rescate 2FA)"),
    ADMIN("Administrador del Restaurante (Gestión total del local)"),
    SUPERVISOR("Supervisor (Apagafuegos operativo y corrección de pedidos)"),
    CAJERO("Cajero (Cobros, cierres de caja y facturación)"),
    MESERO("Mesero (Toma de pedidos y gestión de mesas)");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}