package com.lautaro.cliente_service.constantes;

public final class ClienteConstants {

    // Prevenir instanciación
    private ClienteConstants() {
        throw new UnsupportedOperationException("Clase de constantes - no instanciar");
    }

    // Nombres de tabla
    public static final String TABLE_NAME = "clientes";

    // Nombres de columnas
    public static final String COL_ID = "id";
    public static final String COL_NOMBRE = "nombre";
    public static final String COL_APELLIDO = "apellido";
    public static final String COL_EMAIL = "email";
    public static final String COL_TELEFONO = "telefono";
    public static final String COL_CIUDAD = "ciudad";
    public static final String COL_DIRECCION = "direccion";
    public static final String COL_ACTIVO = "activo";
    public static final String COL_USER_ID = "user_id";

    // Acciones de auditoría
    public static final String ACCION_CREAR = "CREAR";
    public static final String ACCION_ACTUALIZAR = "ACTUALIZAR";
    public static final String ACCION_ELIMINAR = "ELIMINAR";

    // Valores por defecto
    public static final String VALOR_ACTIVO_TRUE = "true";
    public static final String VALOR_ACTIVO_FALSE = "false";
}
