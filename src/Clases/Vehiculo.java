package Clases;

import Enums.EstadoVehiculo;

import java.util.Objects;

public abstract class Vehiculo implements Identificable {

    private int id;
    private String patente;
    private String marca;
    private String modelo;
    private int anio;
    private double kilometraje;
    private double precioPorHora;
    private EstadoVehiculo estadoVehiculo;

    public Vehiculo(int id, String patente, String marca, String modelo, int anio, double kilometraje, double precioPorHora, EstadoVehiculo estadoVehiculo) {
        this.id = id;
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.kilometraje = kilometraje;
        this.precioPorHora = precioPorHora;
        this.estadoVehiculo = estadoVehiculo;
    }

    public Vehiculo(String patente, String marca, String modelo, int anio, double kilometraje, double precioPorHora, EstadoVehiculo estadoVehiculo) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
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

    public int getAnio() {
        return anio;
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

    public void setPrecioPorHora(double precioPorHora) {
        this.precioPorHora = precioPorHora;
    }

    public void setEstadoVehiculo(EstadoVehiculo estadoVehiculo) {
        this.estadoVehiculo = estadoVehiculo;
    }

    public void setKilometraje(double kilometraje) {
        this.kilometraje = kilometraje;
    }

    protected void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Vehículo: " +
                "ID: " + id +
                " | " + marca + " " + modelo + " (" + anio + ")" +
                " | Patente: " + patente +
                " | KM: " + kilometraje +
                " | Precio: $" + precioPorHora + "/h" +
                " | Estado: " + estadoVehiculo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) o;
        return Objects.equals(patente, vehiculo.patente);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(patente);
    }

    //metodo para calcular el alquiler
    public abstract double calcularCostoAlquiler(Entrega entrega);


}
