package Clases;

import Enums.EstadoVehiculo;

import java.util.Objects;

public abstract class Vehiculo implements Identificador{

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

    @Override
    public int getID() {
        return id;
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAño() {
        return año;
    }

    public double getKilometraje() {
        return kilometraje;
    }

    public double getPrecioPorHora() {
        return precioPorHora;
    }

    public EstadoVehiculo getEstadoVehiculo() {
        return estadoVehiculo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) o;
        return id == vehiculo.id && Objects.equals(patente, vehiculo.patente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patente);
    }

    //metodo para calcular el alquiler
    public abstract double calcularCostoAlquiler(Entrega entrega);

}
