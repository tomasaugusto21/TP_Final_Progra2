package Clases;

import Enums.EstadoVehiculo;

public class Auto extends Vehiculo{

    private int cantidadDePuertas;

    public Auto(int id, String patente, String marca, String modelo, int año, double kilometraje, double precioPorHora, EstadoVehiculo estadoVehiculo, int cantidadDePuertas) {
        super(id, patente, marca, modelo, año, kilometraje, precioPorHora, estadoVehiculo);
        this.cantidadDePuertas = cantidadDePuertas;
    }


}
