package Clases;

import Enums.EstadoReserva;
import Enums.EstadoVehiculo;
import Enums.RolDeUsuario;
import Excepciones.ErrorDeCargaEx;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBHandler {
    private static final String url = "jdbc:sqlite:alquiler.db?foreign_keys=on";



    public static List<Vehiculo> obtenerTodosLosVehiculos() {
        List<Vehiculo> vehiculos = new ArrayList<>();

        String SELECT_QUERY = "SELECT v.id, v.patente, v.marca, v.modelo, v.anio, v.kilometraje, "
                + "v.precio_por_hora, v.estado, "
                + "a.id AS id_auto, a.cantidad_puertas, a.tiene_rueda_auxilio, "
                + "c.id AS id_carga, c.precio_por_kg, c.capacidad_carga "
                + "FROM vehiculo v "
                + "LEFT JOIN auto a ON a.id = v.id "
                + "LEFT JOIN vehiculo_de_carga c ON c.id = v.id";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(SELECT_QUERY);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String patente = rs.getString("patente");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                int anio = rs.getInt("anio");
                double kilometraje = rs.getDouble("kilometraje");
                double precioPorHora = rs.getDouble("precio_por_hora");
                EstadoVehiculo estado = EstadoVehiculo.valueOf(rs.getString("estado"));

                Vehiculo vehiculo = null;

                if (rs.getObject("id_auto") != null) {
                    vehiculo = new Auto(patente, marca, modelo, anio, kilometraje, precioPorHora, estado,
                            rs.getInt("cantidad_puertas"),
                            rs.getInt("tiene_rueda_auxilio") == 1);
                } else if (rs.getObject("id_carga") != null) {
                    vehiculo = new VehiculoDeCarga(patente, marca, modelo, anio, kilometraje, precioPorHora, estado,
                            rs.getDouble("precio_por_kg"),
                            rs.getDouble("capacidad_carga"));
                }

                if (vehiculo != null) {
                    vehiculo.setId(id);
                    vehiculos.add(vehiculo);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehiculos;
    }








    public static int agregar_vehiculo(Vehiculo vehiculo) {

        int idGenerada = -1;

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
                    int id = result.getInt(1);

                    if (vehiculo instanceof Auto) {
                        Auto auto = (Auto) vehiculo;

                        try (PreparedStatement statementAuto = connection.prepareStatement(INSERT_QUERY_AUTO)) {
                            statementAuto.setInt(1, id);
                            statementAuto.setInt(2, auto.getCantidadDePuertas());
                            statementAuto.setInt(3, auto.isRuedaDeAuxilio() ? 1 : 0);

                            int filasAfectadas = statementAuto.executeUpdate();

                            if (filasAfectadas == 1) {
                                idGenerada = id;
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
                                idGenerada = id;
                            }
                        }
                    }

                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idGenerada;
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
        String UPDATE_QUERY = "UPDATE vehiculo SET precio_por_hora = ? WHERE id = ?";

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

    public static int agregar_usuario(Usuario usuario, String password) {

        int idGenerada = -1;

        String INSERT_QUERY = "INSERT INTO usuario(nombre,apellido,dni,email,username,password,rol)VALUES(?,?,?,?,?,?,?)";
        String INSERT_QUERY_EMPLEADO = "INSERT INTO empleado(id,legajo)VALUES(?,?)";
        String INSERT_QUERY_CLIENTE = "INSERT INTO cliente(id,nro_licencia,telefono,domicilio)VALUES(?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getApellido());
            statement.setString(3, String.valueOf(usuario.getDni()));
            statement.setString(4, usuario.getEmail());
            statement.setString(5, usuario.getUsername());
            statement.setString(6, password);
            statement.setString(7, usuario.getRol().name());

            statement.executeUpdate();

            try (ResultSet result = statement.getGeneratedKeys()) {
                if (result.next()) {
                    int id = result.getInt(1);

                    if (usuario instanceof Empleado) {
                        Empleado empleado = (Empleado) usuario;

                        try (PreparedStatement statement_empleado = connection.prepareStatement(INSERT_QUERY_EMPLEADO)) {

                            statement_empleado.setInt(1, id);
                            statement_empleado.setString(2, empleado.getLegajo());
                            int filas_agregadas = statement_empleado.executeUpdate();
                            if (filas_agregadas >= 1) {
                                idGenerada = id;
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
                                idGenerada = id;
                            }
                        }
                    }
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idGenerada;
    }

    public static Usuario obtenerUsuario(String usernameBuscado, String contraseñaBuscada) {

        Usuario buscado = null;

        String SELECT_QUERY = "SELECT u.id, u.nombre, u.apellido, u.dni, u.email, u.username, u.rol, "
                + "e.legajo, c.nro_licencia, c.telefono, c.domicilio "
                + "FROM usuario u "
                + "LEFT JOIN empleado e ON e.id = u.id "
                + "LEFT JOIN cliente c ON c.id = u.id "
                + "WHERE u.username = ? AND u.password = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {

            statement.setString(1, usernameBuscado);
            statement.setString(2, contraseñaBuscada);

            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    return buscado;
                }

                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                int dni = Integer.parseInt(rs.getString("dni"));
                String email = rs.getString("email");
                String username = rs.getString("username");
                RolDeUsuario rol = RolDeUsuario.valueOf(rs.getString("rol"));


                if (rol == RolDeUsuario.ADMINISTRADOR) {
                    buscado = new Administrador(nombre, apellido, dni, email, username, rol);
                } else if (rol == RolDeUsuario.EMPLEADO) {
                    buscado = new Empleado(nombre, apellido, dni, email, username, rol,
                            rs.getString("legajo"));
                } else if (rol == RolDeUsuario.CLIENTE) {
                    buscado = new Cliente(nombre, apellido, dni, email, username, rol,
                            rs.getString("nro_licencia"), rs.getString("telefono"),
                            rs.getString("domicilio"));
                }

                buscado.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buscado;
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

    public static int actualizar_usuario_apellido(int id_actualizar, String nuevo_apellido) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE usuario SET apellido = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, nuevo_apellido);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int actualizar_usuario_email(int id_actualizar, String nuevo_email) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE usuario SET email = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, nuevo_email);
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

            statement.setString(1, nuevo_rol.name());
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int actualizar_usuario_username(int id_actualizar, String nuevo_username) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE usuario SET username = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, nuevo_username);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int actualizar_empleado_legajo(int id_actualizar, String nuevo_legajo) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE empleado SET legajo = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, nuevo_legajo);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int actualizar_cliente_telefono(int id_actualizar, String nuevo_telefono) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE cliente SET telefono = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, nuevo_telefono);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int actualizar_cliente_domicilio(int id_actualizar, String nuevo_domicilio) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE cliente SET domicilio = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, nuevo_domicilio);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int cargar_reserva(Reserva reserva) {
        int id = -1;
        String INSERT_QUERY = "INSERT INTO reserva(id_cliente,id_vehiculo,fecha_inicio,hora_inicio,fecha_fin,hora_fin,estado,fecha_creacion)VALUES (?,?,?,?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, reserva.getIdCliente());
            statement.setInt(2, reserva.getIdVehiculo());
            statement.setString(3, reserva.getFechaInicio().toString());
            statement.setString(4, reserva.getHoraInicio().toString());
            statement.setString(5, reserva.getFechaFin().toString());
            statement.setString(6, reserva.getHoraFin().toString());
            statement.setString(7, reserva.getEstado().name());
            statement.setString(8, reserva.getFechaCreacion().toString());

            statement.executeUpdate();

            try (ResultSet result = statement.getGeneratedKeys()) {
                if (result.next()) {
                    id = result.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public static int actualizar_reserva_auto(int id_actualizar,int nuevo_id_vehiculo) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE reserva SET id_vehiculo = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setInt(1,nuevo_id_vehiculo);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_reserva_hora_inicio(int id_actualizar, LocalDateTime nueva_hora_inicio) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE reserva SET hora_inicio = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, nueva_hora_inicio.toString());
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_reserva_hora_fin(int id_actualizar, LocalDateTime nueva_hora_fin) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE reserva SET hora_fin = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nueva_hora_fin.toString());
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_reserva_fecha_inicio(int id_actualizar, LocalDateTime nueva_fecha_inicio) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE reserva SET fecha_inicio = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, nueva_fecha_inicio.toLocalDate().atStartOfDay().toString());
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int actualizar_reserva_fecha_fin(int id_actualizar, LocalDateTime nueva_fecha_fin) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE reserva SET fecha_fin = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, nueva_fecha_fin.toLocalDate().atStartOfDay().toString());
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_reserva_estado(int id_actualizar, EstadoReserva nuevo_estado) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE reserva SET estado = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nuevo_estado.toString());
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_reserva_fecha_creacion(int id_actualizar,LocalDateTime nueva_fecha_creacion) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE reserva SET fecha_creacion = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nueva_fecha_creacion.toString());
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }


    public static int crear_entrega (Entrega entrega) {
        int id = -1;
        String INSERT_QUERY = "INSERT INTO entrega(id_cliente,id_vehiculo,id_empleado,id_reserva,fecha_hora_entrega,fecha_devolucion_prevista,km_entrega,kg_transportados,activa)VALUES (?,?,?,?,?,?,?,?,?)";
        String UPDATE_VEHICULO = "UPDATE vehiculo SET estado = ? WHERE id = ?";
        String UPDATE_RESERVA = "UPDATE reserva SET estado = ? WHERE id = ?";


        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, entrega.getIdCliente());
            statement.setInt(2, entrega.getIdVehiculo());
            statement.setInt(3, entrega.getIdEmpleado());
            if (entrega.getIdReserva() == 0) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, entrega.getIdReserva());
            }
            statement.setString(5, entrega.getFechaHoraEntrega().toString());
            statement.setString(6, entrega.getFechaDevolucionPrevista().toString());
            statement.setDouble(7, entrega.getKmEntrega());
            statement.setDouble(8, entrega.getKgTransportados());
            statement.setInt(9,entrega.isActiva() ? 1 : 0);

            statement.executeUpdate();

            try (ResultSet result = statement.getGeneratedKeys()) {
                if (result.next()) {
                    id = result.getInt(1);
                }
            }

            try (PreparedStatement statementDos = connection.prepareStatement(UPDATE_VEHICULO)) {
                statementDos.setString(1, EstadoVehiculo.ALQUILADO.name());
                statementDos.setInt(2, entrega.getIdVehiculo());
                statementDos.executeUpdate();
            }

            if (entrega.getIdReserva() != 0) {
                try (PreparedStatement statementTres = connection.prepareStatement(UPDATE_RESERVA)) {
                    statementTres.setString(1, EstadoReserva.FINALIZADA.name());
                    statementTres.setInt(2, entrega.getIdReserva());
                    statementTres.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public static int actualizar_entrega_nuevo_vehiculo(int id_actualizar,int nuevo_id_vehiculo) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE entrega SET id_vehiculo = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setInt(1,nuevo_id_vehiculo);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_entrega_cambio_empleado(int id_actualizar,int nuevo_id_empleado) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE entrega SET id_empleado = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setInt(1,nuevo_id_empleado);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_entrega_reserva(int id_actualizar,int nueva_id_reserva) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE entrega SET id_reserva = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setInt(1,nueva_id_reserva);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_entrega_fecha_hora(int id_actualizar,LocalDateTime nueva_fecha_hora) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE entrega SET fecha_hora_entrega = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nueva_fecha_hora.toString());
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_entrega_fecha_devolucion(int id_actualizar,LocalDateTime nueva_fecha_entrega) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE entrega SET fecha_devolucion_prevista = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nueva_fecha_entrega.toString());
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_entrega_km(int id_actualizar,double nuevo_kilometraje) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE entrega SET km_entrega = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setDouble(1,nuevo_kilometraje);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_entrega_kg_transportados(int id_actualizar,double nuevo_kg_transportados) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE entrega SET kg_transportados = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setDouble(1,nuevo_kg_transportados);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_entrega_estado_activa(int id_actualizar,boolean nuevo_estado_activo) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE entrega SET activa = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setInt(1,nuevo_estado_activo ? 1:0);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public static int cargar_devolucion (Devolucion devolucion, int idVehiculo, double kmFinal) {
        int id = -1;
        String INSERT_QUERY = "INSERT INTO devolucion(id_entrega,id_empleado,fecha_hora_devolucion,km_recorridos,costo_total)VALUES (?,?,?,?,?)";
        String UPDATE_ENTREGA = "UPDATE entrega SET activa = 0 WHERE id = ?";
        String UPDATE_VEHICULO = "UPDATE vehiculo SET kilometraje = ?, estado = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, devolucion.getIdEntrega());
            statement.setInt(2, devolucion.getIdEmpleado());
            statement.setString(3, devolucion.getFechaHoraDevolucion().toString());
            statement.setDouble(4, devolucion.getKmRecorridos());
            statement.setDouble(5, devolucion.getCostoTotal());

            statement.executeUpdate();

            try (ResultSet result = statement.getGeneratedKeys()) {
                if (result.next()) {
                    id = result.getInt(1);
                }
            }

            try (PreparedStatement statementDos = connection.prepareStatement(UPDATE_ENTREGA)) {
                statementDos.setInt(1, devolucion.getIdEntrega());
                statementDos.executeUpdate();
            }

            try (PreparedStatement statementTres = connection.prepareStatement(UPDATE_VEHICULO)) {
                statementTres.setDouble(1, kmFinal);
                statementTres.setString(2, EstadoVehiculo.DISPONIBLE.name());
                statementTres.setInt(3, idVehiculo);
                statementTres.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }
    public static int actualizar_devolucion_cambio_empleado(int id_actualizar,int nuevo_id_empleado) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE devolucion SET id_empleado = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setInt(1,nuevo_id_empleado);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_devolucion_fecha_hora(int id_actualizar,LocalDateTime nueva_fecha_hora_devolucion) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE devolucion SET fecha_hora_devolucion = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1,nueva_fecha_hora_devolucion.toString());
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_devolucion_km_recorridos(int id_actualizar,double nuevo_km_recorridos) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE devolucion SET km_recorridos = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setDouble(1,nuevo_km_recorridos);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public static int actualizar_devolucion_costo_total(int id_actualizar,double nuevo_costo_total) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE devolucion SET costo_total = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setDouble(1,nuevo_costo_total);
            statement.setInt(2, id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

}
