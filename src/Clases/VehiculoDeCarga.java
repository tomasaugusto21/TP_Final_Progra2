package Clases;

import Enums.EstadoVehiculo;

public class VehiculoDeCarga extends Vehiculo{

    private double capacidadDeCargaKG;


    public VehiculoDeCarga(int id, String patente, String marca, String modelo, int año, double kilometraje, double precioPorHora, EstadoVehiculo estadoVehiculo, double capacidadDeCargaKG) {
        super(id, patente, marca, modelo, año, kilometraje, precioPorHora, estadoVehiculo);
        this.capacidadDeCargaKG = capacidadDeCargaKG;
    }



}
