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

                            statementVehiculoCarga.setInt(1,id);
                            statementVehiculoCarga.setDouble(2,vehiculoDeCarga.getPrecioPorKg());
                            statementVehiculoCarga.setDouble(3,vehiculoDeCarga.getCapacidadDeCargaKg());

                            statementVehiculoCarga.executeUpdate();
                        }
                    }

                }else{
                    throw new ErrorDeCargaEx("ERROR: No se logro obtener el identificador del vehiculo");
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
