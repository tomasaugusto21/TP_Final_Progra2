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



    protected void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                ", patente='" + patente + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", año=" + anio +
                ", kilometraje=" + kilometraje +
                ", precioPorHora=" + precioPorHora +
                ", estadoVehiculo=" + estadoVehiculo +
                '}';
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

    //Metodo para actualizar el kilometraje
    public void actualizarKilometraje(double kmRecorridos) {
        if (kmRecorridos > 0) {
            this.kilometraje += kmRecorridos;
        }
    }

}
