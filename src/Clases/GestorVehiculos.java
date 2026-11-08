package Clases;

import Enums.EstadoVehiculo;
import Excepciones.ObjetoNoEncontradoEx;
import Excepciones.ValidacionEx;

import java.util.ArrayList;
import java.util.List;

public class GestorVehiculos {

    private final Almacen<Vehiculo> vehiculos = new Almacen<>();




    public Vehiculo buscarVehiculoPorPatente (String patente) throws ObjetoNoEncontradoEx, ValidacionEx {

        if (patente == null || patente.isBlank()){
            throw new ValidacionEx("La patente a buscar no puede ser nula o estar en blanco");
        }

        for (Vehiculo v : vehiculos.listarTodos()){
            if (v.getPatente().equalsIgnoreCase(patente)){
                return v;
            }
        }
        throw new ObjetoNoEncontradoEx("No se pudo encontrar un vehículo con la patente buscada");
    }

    public EstadoVehiculo consultarEstado(String patente) throws ObjetoNoEncontradoEx, ValidacionEx {
        return buscarVehiculoPorPatente(patente).getEstadoVehiculo();
    }


    public List<Vehiculo> listarVehiculos (){
        return vehiculos.listarTodos();
    }

    public List<Auto> listarAutos (){
        return vehiculos.listarDeTipo(Auto.class);
    }

    public List<VehiculoDeCarga> listarVehiculosDeCarga (){
        return vehiculos.listarDeTipo(VehiculoDeCarga.class);
    }

    public List<Vehiculo> listarDisponibles() {

        List<Vehiculo> resultado = new ArrayList<>();

        for (Vehiculo v : vehiculos.listarTodos()) {
            if (v.getEstadoVehiculo() == EstadoVehiculo.DISPONIBLE) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public List<Vehiculo> listarNoDisponibles() {

        List<Vehiculo> resultado = new ArrayList<>();

        for (Vehiculo v : vehiculos.listarTodos()) {
            EstadoVehiculo e = v.getEstadoVehiculo();
            if (e == EstadoVehiculo.EN_REPARACION || e == EstadoVehiculo.EN_LIMPIEZA
                    || e == EstadoVehiculo.FUERA_DE_SERVICIO) {
                resultado.add(v);
            }
        }
        return resultado;
    }



}
