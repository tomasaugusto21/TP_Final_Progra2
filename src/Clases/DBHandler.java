package Clases;

import Enums.EstadoVehiculo;
import Enums.RolDeUsuario;
import Excepciones.ErrorDeCargaEx;

import java.sql.*;

public class DBHandler {
    private static final String url = "jdbc:sqlite:alquiler.db?foreign_keys=on";

    public static boolean agregar_vehiculo(Vehiculo vehiculo) {

        boolean agregado = false;

        String INSERT_QUERY = "INSERT INTO vehiculo(patente,marca,modelo,anio,kilometraje,precio_por_hora,estado)VALUES(?,?,?,?,?,?,?)";
        String INSERT_QUERY_AUTO = "INSERT INTO auto(id,cantidad_puertas,tiene_rueda_auxilio)VALUES(?,?,?)";
        String INSERT_QUERY_VEHICULO_CARGA = "INSERT INTO vehiculo_de_carga(id,precio_por_kg,capacidad_carga)VALUES(?,?,?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, vehiculo.getPatente());
            statement.setString(2, vehiculo.getMarca());
            statement.setString(3, vehiculo.getModelo());
            statement.setInt(4, vehiculo.getAnio());
            statement.setDouble(5, vehiculo.getKilometraje());
            statement.setDouble(6, vehiculo.getPrecioPorHora());
            statement.setString(7, vehiculo.getEstadoVehiculo().name());

            statement.executeUpdate();

            try (ResultSet result = statement.getGeneratedKeys()) {
                if (result.next()) {
                    int id = result.getInt("id");

                    if (vehiculo instanceof Auto) {
                        Auto auto = (Auto) vehiculo;

                        try (PreparedStatement statementAuto = connection.prepareStatement(INSERT_QUERY_AUTO)) {
                            statementAuto.setInt(1, id);
                            statementAuto.setInt(2, auto.getCantidadDePuertas());
                            statementAuto.setInt(3, auto.isRuedaDeAuxilio() ? 1 : 0);

                            int filasAfectadas = statementAuto.executeUpdate();

                            if (filasAfectadas == 1) {
                                agregado = true;
                            }
                        }
                    }
                    if (vehiculo instanceof VehiculoDeCarga) {
                        VehiculoDeCarga vehiculoDeCarga = (VehiculoDeCarga) vehiculo;

                        try (PreparedStatement statementVehiculoCarga = connection.prepareStatement(INSERT_QUERY_VEHICULO_CARGA)) {

                            statementVehiculoCarga.setInt(1, id);
                            statementVehiculoCarga.setDouble(2, vehiculoDeCarga.getPrecioPorKg());
                            statementVehiculoCarga.setDouble(3, vehiculoDeCarga.getCapacidadDeCargaKg());

                            int filasAfectadas = statementVehiculoCarga.executeUpdate();

                            if (filasAfectadas == 1) {
                                agregado = true;
                            }
                        }
                    }

                } else {
                    //falta agarrarlo por eso tira error.
                    throw new ErrorDeCargaEx("ERROR: No se logro obtener el identificador del vehiculo");
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agregado;
    }

    /// la idea no es que se eliminen los vehiculos porque se perderian datos en los registros, sino que los damos de baja
    public static int cambiar_estado(int id_cambiar, EstadoVehiculo estado_nuevo) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE vehiculo SET estado = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, estado_nuevo.name());
            statement.setInt(2, id_cambiar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }


    public static int actualizar_kilometraje(int id_actualizar, double kilometraje_nuevo) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE vehiculo SET kilometraje = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setDouble(1, kilometraje_nuevo);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int actualizar_precio_x_hora(int id_actualizar, double precio_por_hora_nuevo) {

        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE vehiculos SET precio_por_hora = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setDouble(1, precio_por_hora_nuevo);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int actualizar_rueda_auxilio(int id_actualizar, boolean rueda_auxilio) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE auto SET tiene_rueda_auxilio = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setInt(1, rueda_auxilio ? 1 : 0);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int actualizar_precio_x_kg(int id_actualizar, double precio_nuevo) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE vehiculo_de_carga  SET precio_por_kg = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setDouble(1, precio_nuevo);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int actualizar_capacidad_carga(int id_actualizar, double capacidad_nueva) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE vehiculo_de_carga SET capacidad_carga = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setDouble(1, capacidad_nueva);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static boolean agregar_usuario(Usuario usuario, String password) {

        boolean agregado = false;

        String INSERT_QUERY = "INSERT INTO usuario(nombre,apellido,dni,email,username,password,rol)VALUES(?,?,?,?,?,?,?)";
        String INSERT_QUERY_EMPLEADO = "INSERT INTO empleado(id,legajo,)VALUES(?,?)";
        String INSERT_QUERY_CLIENTE = "INSERT INTO cliente(id,nro_licencia,telefono,domicilio)VALUES(?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getApellido());
            statement.setInt(3, usuario.getDni());
            statement.setString(4, usuario.getEmail());
            statement.setString(5, usuario.getUsername());
            statement.setString(6, password);
            statement.setString(7, usuario.getRol().name());

            statement.executeUpdate();

            try (ResultSet result = statement.getGeneratedKeys()) {
                if (result.next()) {
                    int id = result.getInt("id");

                    if (usuario instanceof Empleado) {
                        Empleado empleado = (Empleado) usuario;

                        try (PreparedStatement statement_empleado = connection.prepareStatement(INSERT_QUERY_EMPLEADO)) {

                            statement_empleado.setInt(1, id);
                            statement_empleado.setString(2, empleado.getLegajo());
                            int filas_agregadas = statement_empleado.executeUpdate();
                            if (filas_agregadas >= 1) {
                                agregado = true;
                            }
                        }
                    }
                    if (usuario instanceof Cliente) {
                        Cliente cliente = (Cliente) usuario;

                        try (PreparedStatement statement_cliente = connection.prepareStatement(INSERT_QUERY_CLIENTE)) {

                            statement_cliente.setInt(1, id);
                            statement_cliente.setString(2, cliente.getNumeroDeLicencia());
                            statement_cliente.setString(3, cliente.getTelefono());
                            statement_cliente.setString(4, cliente.getDomicilio());


                            int filas_agregadas = statement_cliente.executeUpdate();
                            if (filas_agregadas >= 1) {
                                agregado = true;
                            }
                        }
                    }
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agregado;
    }


    public static int actualizar_usuario_nombre(int id_actualizar, String nuevo_nombre) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE usuario SET nombre = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, nuevo_nombre);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_usuario_apellido(int id_actualizar,String nuevo_apellido) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE usuario SET apellido = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nuevo_apellido);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_usuario_email(int id_actualizar,String nuevo_email) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE usuario SET email = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nuevo_email);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_usuario_rol(int id_actualizar, RolDeUsuario nuevo_rol) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE usuario SET rol = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nuevo_rol);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_usuario_username(int id_actualizar,String nuevo_username) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE usuario SET username = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nuevo_username);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_empleado_legajo(int id_actualizar,String nuevo_legajo) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE empleado SET legajo = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nuevo_legajo);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int actualizar_cliente_telegono(int id_actualizar,String nuevo_telefono) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE usuario SET telefono = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nuevo_telefono);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_cliente_domicilio(int id_actualizar,String nuevo_domicilio) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE usuario SET  = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nuevo_domicilio);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

}
