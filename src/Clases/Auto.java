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

    public Auto(String patente, String marca, String modelo, int anio, double kilometraje, double precioPorHora, EstadoVehiculo estadoVehiculo, int cantidadDePuertas, boolean ruedaDeAuxilio) {
        super(patente, marca, modelo, anio, kilometraje, precioPorHora, estadoVehiculo);
        this.cantidadDePuertas = cantidadDePuertas;
        this.ruedaDeAuxilio = ruedaDeAuxilio;
    }

    public int getCantidadDePuertas() {
        return cantidadDePuertas;
    }

    public boolean isRuedaDeAuxilio() {
        return ruedaDeAuxilio;
    }

    public void setRuedaDeAuxilio(boolean ruedaDeAuxilio) {
        this.ruedaDeAuxilio = ruedaDeAuxilio;
    }



    @Override
    public double calcularCostoAlquiler(Entrega entrega) {
        double horas = entrega.calcularHoras();
        double subtotal = horas * getPrecioPorHora();

        if (ruedaDeAuxilio) {
            subtotal *= 1.05;
        }
        return subtotal;
    }

    @Override
    public String toString() {
        return super.toString().replace("Vehículo:", "Auto:") +
                " | Puertas: " + cantidadDePuertas +
                " | Rueda de auxilio: " + (ruedaDeAuxilio ? "Sí" : "No");
    }
}
