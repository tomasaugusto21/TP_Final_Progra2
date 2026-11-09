package Clases;

import Enums.EstadoVehiculo;
import Excepciones.ErrorDeCargaEx;

import java.sql.*;

public class DBHandler {
    private static final String url = "jdbc:sqlite:alquiler";

    public void agregar_vehiculo(Vehiculo vehiculo) {

        String INSERT_QUERY = "INSERT INTO vehiculo(patente,marca,modelo,anio,kilometraje,precioPorHora,estadoVehiculo)VALUES(?,?,?,?,?,?,?)";
        String INSERT_QUERY_AUTO = "INSERT INTO auto(id,cantidad_puertas,tiene_rueda_auxilio)VALUES(?,?,?)";
        String INSERT_QUERY_VEHICULO_CARGA = "INSERT INTO vehiculo_de_carga(id,precio_por_kg,capacidad_carga)VALUES(?,?,?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {

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

                            statementAuto.executeUpdate();
                        }
                    }
                    if (vehiculo instanceof VehiculoDeCarga) {
                        VehiculoDeCarga vehiculoDeCarga = (VehiculoDeCarga) vehiculo;

                        try (PreparedStatement statementVehiculoCarga = connection.prepareStatement(INSERT_QUERY_VEHICULO_CARGA)) {

                            statementVehiculoCarga.setInt(1, id);
                            statementVehiculoCarga.setDouble(2, vehiculoDeCarga.getPrecioPorKg());
                            statementVehiculoCarga.setDouble(3, vehiculoDeCarga.getCapacidadDeCargaKg());

                            statementVehiculoCarga.executeUpdate();
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
    }

    public int borrar_vehiculo_id(int id_eliminar) {
        int filas_alteradas = 0;
        String DELETE_QUERY = "DELETE FROM vehiculo WHERE id = ?";
        String DELETE_QUERY_AUTO = "DELETE FROM auto WHERE id = ?";
        String DELETE_QUERY_VEHICULO_CARGA = "DELETE FROM vehiculo_de_carga WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement statement_borrar_hija_auto = connection.prepareStatement(DELETE_QUERY_AUTO)) {
                statement_borrar_hija_auto.setInt(1, id_eliminar);
                statement_borrar_hija_auto.executeUpdate();
            }
            try (PreparedStatement statement_borrar_hija_VCarga = connection.prepareStatement(DELETE_QUERY_VEHICULO_CARGA)) {
                statement_borrar_hija_VCarga.setInt(1, id_eliminar);
                statement_borrar_hija_VCarga.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
                statement.setInt(1, id_eliminar);
                filas_alteradas = statement.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public int actualizar_kilometraje(int id_actualizar, double kilometraje_nuevo) {
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

    public int actualizar_precio_x_hora(int id_actualizar, double precio_por_hora_nuevo) {

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

    public int actualizar_rueda_auxilio(int id_actualizar, boolean rueda_auxilio) {
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE auto SET tiene_rueda_auxilio = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setInt(1,rueda_auxilio ? 1:0);
            statement.setInt(2,id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }

    public int actualizar_precio_x_kg(int id_actualizar ,double precio_nuevo){
        int filas_alteradas =0;
        String UPDATE_QUERY = "UPDATE vehiculo_de_carga  SET precio_por_kg = ? WHERE id = ?";

        try(Connection connection = DriverManager.getConnection(url);
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)){

            statement.setDouble(1,precio_nuevo);
            statement.setInt(2,id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }
    public int actualizar_capacidad_carga (int id_actualizar, double capacidad_nueva){
        int filas_alteradas = 0;
        String UPDATE_QUERY = "UPDATE vehiculo_de_carga SET capacidad_carga = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)){

            statement.setDouble(1,capacidad_nueva);
            statement.setInt(2,id_actualizar);

            filas_alteradas = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas_alteradas;
    }


    


}
