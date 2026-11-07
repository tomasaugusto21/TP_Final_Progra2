package Clases;

import Enums.EstadoVehiculo;

public class VehiculoDeCarga extends Vehiculo{

    private double capacidadDeCargaKg;
    private double precioPorKg;

    public VehiculoDeCarga(int id, String patente, String marca, String modelo, int año, double kilometraje, double precioPorHora, EstadoVehiculo estadoVehiculo, double capacidadDeCargaKg, double precioPorKg) {
        super(id, patente, marca, modelo, año, kilometraje, precioPorHora, estadoVehiculo);
        this.capacidadDeCargaKg = capacidadDeCargaKg;
        this.precioPorKg = precioPorKg;
    }

    //falta sobreescritura del metodo de vehiculo para calcular alquiler


    @Override
    public double calcularCostoAlquiler(Entrega entrega) {
        double horas = entrega.calcularHoras();
        double kgTransportados = entrega.getKgTransportados();

        return (horas * getPrecioPorHora()) + (kgTransportados * precioPorKg);
    }
    }

