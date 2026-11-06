package Clases;

import Enums.EstadoVehiculo;

public class Auto extends Vehiculo{

    private int cantidadDePuertas;
    private boolean ruedaDeAuxilio;

    public Auto(int id, String patente, String marca, String modelo, int año, double kilometraje, double precioPorHora, EstadoVehiculo estadoVehiculo, int cantidadDePuertas, boolean ruedaDeAuxilio) {
        super(id, patente, marca, modelo, año, kilometraje, precioPorHora, estadoVehiculo);
        this.cantidadDePuertas = cantidadDePuertas;
        this.ruedaDeAuxilio = ruedaDeAuxilio;
    }

    //falta sobreescritura del metodo de vehiculo para calcular alquiler

}
