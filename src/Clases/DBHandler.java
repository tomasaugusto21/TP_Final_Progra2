package Clases;

import Enums.EstadoVehiculo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBHandler {
    private static final String url ="jdbc:sqlite:alquiler";

    public void agregar_vehiculo(String patente, String marca, String modelo, int anio, double kilometraje, double precioPorHora, EstadoVehiculo estadoVehiculo){
        int filas_agregadas = 0;
        String INSERT_QUERY = "INSERT INTO vehiculo(patente,marca,modelo,anio,kilometraje,precioPorHora,estadoVehiculo)VALUES(?,?,?,?,?,?,?)";


        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)){

            statement.setString(1,patente);
            statement.setString(2,marca);
            statement.setString(3,modelo);
            statement.setInt(4,anio);
            statement.setDouble(5,kilometraje);
            statement.setDouble(6,precioPorHora);
            statement.setString(7, estadoVehiculo.name());

            filas_agregadas = statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
