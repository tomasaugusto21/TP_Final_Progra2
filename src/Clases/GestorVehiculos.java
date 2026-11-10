package Clases;

import Enums.EstadoVehiculo;
import Excepciones.ObjetoDuplicadoEx;
import Excepciones.ObjetoInvalidoEx;
import Excepciones.ObjetoNoEncontradoEx;
import Excepciones.ValidacionEx;

import java.util.ArrayList;
import java.util.List;

public class GestorVehiculos {

    private final Almacen<Vehiculo> vehiculos = new Almacen<>();


    public boolean agregarAuto (String patente, String marca, String modelo, int anio, double kilometraje, double precioPorHora, EstadoVehiculo estadoVehiculo, int cantidadDePuertas, boolean ruedaDeAuxilio) throws ValidacionEx, ObjetoInvalidoEx, ObjetoDuplicadoEx {

        if (patente == null || patente.isBlank()){
            throw new ValidacionEx("La patente no puede ser nula ni estar en blanco");
        }
        if (marca == null || marca.isBlank()){
            throw new ValidacionEx("La marca no puede ser nula ni estar en blanco");
        }
        if (modelo == null || modelo.isBlank()){
            throw new ValidacionEx("El modelo no puede ser nulo ni estar en blanco");
        }
        if (anio <=0){
            throw new ValidacionEx("El año del vehiculo debe ser mayor a cero");
        }
        if (kilometraje <= 0){
            throw new ValidacionEx("El kilometraje debe ser mayor a cero");
        }
        if (precioPorHora <= 0){
            throw new ValidacionEx("El precio por hora debe ser mayor a cero");
        }
        if (estadoVehiculo == null){
            throw new ValidacionEx("El estado del vehiculo no puede ser nulo");
        }
        if (cantidadDePuertas <= 0){
            throw new ValidacionEx("El vehiculo debe tener un numero de puertas mayor a cero");
        }

        Auto auto = new Auto(patente, marca, modelo, anio, kilometraje, precioPorHora, estadoVehiculo, cantidadDePuertas, ruedaDeAuxilio);

        if (vehiculos.verificarExistencia(auto)){
            throw new ObjetoDuplicadoEx("El Auto a agregar ya existe en el almacen de vehiculos");
        }

        int id = DBHandler.agregar_vehiculo(auto);

        if (id == -1){
            return false;
        }

        auto.setId(id);
        vehiculos.agregar(auto);

        return true;
    }

    public boolean agregarVehiculoDeCarga (String patente, String marca, String modelo, int anio, double kilometraje, double precioPorHora, EstadoVehiculo estadoVehiculo, double capacidadDeCargaKg, double precioPorKg) throws ValidacionEx, ObjetoDuplicadoEx, ObjetoInvalidoEx {

        if (patente == null || patente.isBlank()){
            throw new ValidacionEx("La patente no puede ser nula ni estar en blanco");
        }
        if (marca == null || marca.isBlank()){
            throw new ValidacionEx("La marca no puede ser nula ni estar en blanco");
        }
        if (modelo == null || modelo.isBlank()){
            throw new ValidacionEx("El modelo no puede ser nulo ni estar en blanco");
        }
        if (anio <=0){
            throw new ValidacionEx("El año del vehiculo debe ser mayor a cero");
        }
        if (kilometraje <= 0){
            throw new ValidacionEx("El kilometraje debe ser mayor a cero");
        }
        if (precioPorHora <= 0){
            throw new ValidacionEx("El precio por hora debe ser mayor a cero");
        }
        if (estadoVehiculo == null){
            throw new ValidacionEx("El estado del vehiculo no puede ser nulo");
        }
        if (capacidadDeCargaKg <= 0){
            throw new ValidacionEx("La capacidad de carga debe ser mayor a cero");
        }
        if (precioPorKg <= 0){
            throw new ValidacionEx("El precio por KG debe ser mayor a cero");
        }

        VehiculoDeCarga vehiculoDeCarga = new VehiculoDeCarga(patente, marca, modelo, anio, kilometraje, precioPorHora, estadoVehiculo, capacidadDeCargaKg, precioPorKg);

        if (vehiculos.verificarExistencia(vehiculoDeCarga)){
            throw new ObjetoDuplicadoEx("El vehiculo de carga a agregar ya existe en el almacen de vehiculos");
        }

        int id = DBHandler.agregar_vehiculo(vehiculoDeCarga);

        if (id == -1){
            return false;
        }

        vehiculoDeCarga.setId(id);
        vehiculos.agregar(vehiculoDeCarga);

        return true;
    }


    public boolean actualizarPrecioPorHora (String patente, double nuevoPrecio) throws ObjetoNoEncontradoEx, ValidacionEx {

        Vehiculo buscado = buscarVehiculoPorPatente(patente);

        int idBuscada = buscado.getID();

        if (DBHandler.actualizar_precio_x_hora(idBuscada, nuevoPrecio)==1){
            buscado.setPrecioPorHora(nuevoPrecio);
            return true;
        }
        return false;
    }

    public boolean actualizarRuedaDeAuxilio (String patente, boolean ruedaAuxilio) throws ObjetoNoEncontradoEx, ValidacionEx {

        Vehiculo buscado = buscarVehiculoPorPatente(patente);
        Auto a = (Auto) buscado;

        int idBuscada = buscado.getID();

        if (DBHandler.actualizar_rueda_auxilio(idBuscada, ruedaAuxilio)==1){
            a.setRuedaDeAuxilio(ruedaAuxilio);
            return true;
        }
        return false;
    }

    public boolean actualizarPrecioPorKg (String patente, double nuevoPrecioPorKg) throws ObjetoNoEncontradoEx, ValidacionEx {

        Vehiculo buscado = buscarVehiculoPorPatente(patente);
        VehiculoDeCarga v = (VehiculoDeCarga) buscado;

        int idBuscada = buscado.getID();

        if (DBHandler.actualizar_precio_x_kg(idBuscada, nuevoPrecioPorKg)==1){
            v.setPrecioPorKg(nuevoPrecioPorKg);
            return true;
        }
        return false;
    }

    public boolean actualizarCapacidadDeCarga (String patente, double nuevaCapacidad) throws ObjetoNoEncontradoEx, ValidacionEx {

        Vehiculo buscado = buscarVehiculoPorPatente(patente);
        VehiculoDeCarga v = (VehiculoDeCarga) buscado;

        int idBuscada = buscado.getID();

        if (DBHandler.actualizar_capacidad_carga(idBuscada, nuevaCapacidad)==1){
            v.setPrecioPorKg(nuevaCapacidad);
            return true;
        }
        return false;
    }

    public boolean cambiarEstadoVehiculo (String patente, EstadoVehiculo estadoNuevo) throws ObjetoNoEncontradoEx, ValidacionEx {

        Vehiculo buscado = buscarVehiculoPorPatente(patente);

        int idBuscada = buscado.getID();

        if (DBHandler.cambiar_estado(idBuscada,estadoNuevo)==1){
            buscado.setEstadoVehiculo(estadoNuevo);
            return true;
        }
        return false;
    }

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
