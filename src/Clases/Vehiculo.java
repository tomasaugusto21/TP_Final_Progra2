package Clases;

import Enums.EstadoVehiculo;

public abstract class Vehiculo {

    private int id;
    private String patente;
    private String marca;
    private String modelo;
    private int año;
    private double kilometraje;
    private double precioPorHora;
    private EstadoVehiculo estadoVehiculo;

    public Vehiculo(int id, String patente, String marca, String modelo, int año, double kilometraje, double precioPorHora, EstadoVehiculo estadoVehiculo) {
        this.id = id;
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.kilometraje = kilometraje;
        this.precioPorHora = precioPorHora;
        this.estadoVehiculo = estadoVehiculo;
    }


}
