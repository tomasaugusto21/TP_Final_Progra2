package Clases;

import Enums.EstadoVehiculo;
import Enums.RolDeUsuario;
import Excepciones.*;

import java.util.List;

public class Sistema {

    private final GestorVehiculos gestorVehiculos = new GestorVehiculos();
    private final GestorUsuarios gestorUsuarios = new GestorUsuarios();
    private final GestorReservas gestorReservas = new GestorReservas(gestorVehiculos, gestorUsuarios);
    private final GestorAlquileres gestorAlquileres = new GestorAlquileres(gestorVehiculos, gestorReservas, gestorUsuarios);
    private Usuario logueado;


    public void descargarBDD (){
        try{
            gestorVehiculos.descargarDatosBDD();
            gestorUsuarios.descargarDatosBDD();
            gestorReservas.descargarDatosBDD();
            gestorAlquileres.descargarDatosBDD();
        } catch (ObjetoDuplicadoEx | ValidacionEx | ObjetoInvalidoEx e) {
            System.out.println(e.getMessage());
        }
    }

    //metodos del gestor vehiculos

    public boolean agregarAutoAlSistema (String patente, String marca, String modelo, int anio, double kilometraje, double precioPorHora, int cantidadDePuertas, boolean ruedaDeAuxilio) throws ObjetoInvalidoEx, ObjetoDuplicadoEx, ValidacionEx, PermisosEx {

        if (this.logueado.getRol() == RolDeUsuario.CLIENTE){
            throw new PermisosEx("El usuario logueado no tiene permisos para agregar un auto al sistema");
        }

        return gestorVehiculos.agregarAuto(patente, marca, modelo, anio, kilometraje, precioPorHora, cantidadDePuertas, ruedaDeAuxilio);
    }

    public boolean agregarVehiculoDeCargaAlSistema (String patente, String marca, String modelo, int anio, double kilometraje, double precioPorHora, double capacidadDeCargaKg, double precioPorKg) throws ObjetoInvalidoEx, ObjetoDuplicadoEx, ValidacionEx, PermisosEx {

        if (this.logueado.getRol() == RolDeUsuario.CLIENTE){
            throw new PermisosEx("El usuario logueado no tiene permisos para agregar un vehiculo de carga al sistema");
        }

        return gestorVehiculos.agregarVehiculoDeCarga(patente, marca, modelo, anio, kilometraje, precioPorHora, capacidadDeCargaKg, precioPorKg);
    }

    public boolean actualizarPrecioPorHora (String patente, double nuevoPrecio) throws PermisosEx, ObjetoNoEncontradoEx, ValidacionEx {

        if (this.logueado.getRol() == RolDeUsuario.CLIENTE){
            throw new PermisosEx("El usuario logueado no tiene permisos para actualizar precios de vehiculos");
        }

        return gestorVehiculos.actualizarPrecioPorHora(patente, nuevoPrecio);
    }

    public boolean actualizarRuedaDeAuxilio (String patente, boolean ruedaAuxilio) throws PermisosEx, ObjetoNoEncontradoEx, ObjetoInvalidoEx, ValidacionEx {

        if (this.logueado.getRol() == RolDeUsuario.CLIENTE){
            throw new PermisosEx("El usuario logueado no tiene permisos para actualizar la rueda de auxilio de los autos");
        }

        return gestorVehiculos.actualizarRuedaDeAuxilio(patente, ruedaAuxilio);
    }

    public boolean actualizarPrecioPorKg (String patente, double nuevoPrecioPorKg) throws PermisosEx, ObjetoNoEncontradoEx, ObjetoInvalidoEx, ValidacionEx {

        if (this.logueado.getRol() == RolDeUsuario.CLIENTE){
            throw new PermisosEx("El usuario logueado no tiene permisos para actualizar el precio por kg de los vehiculos de carga");
        }

        return gestorVehiculos.actualizarPrecioPorKg(patente, nuevoPrecioPorKg);
    }

    public boolean actualizarCapacidadDeCarga (String patente, double nuevaCapacidad) throws PermisosEx, ObjetoNoEncontradoEx, ObjetoInvalidoEx, ValidacionEx {

        if (this.logueado.getRol() == RolDeUsuario.CLIENTE){
            throw new PermisosEx("El usuario logueado no tiene permisos para actualizar la capacidad de los vehiculos de carga");
        }

        return gestorVehiculos.actualizarCapacidadDeCarga(patente,nuevaCapacidad);
    }

    public boolean cambiarEstadoVehiculo(String patente, EstadoVehiculo estadoNuevo) throws PermisosEx, ObjetoNoEncontradoEx, ObjetoInvalidoEx, ValidacionEx {

        if (this.logueado.getRol() == RolDeUsuario.CLIENTE){
            throw new PermisosEx("El usuario logueado no tiene permisos para cambiar el estado de los vehiculos");
        }

        return gestorVehiculos.cambiarEstadoVehiculo(patente, estadoNuevo);
    }

    public String obtenerVehiculoPorPatente (String patente) throws ObjetoNoEncontradoEx, ValidacionEx {

        Vehiculo v = gestorVehiculos.buscarVehiculoPorPatente(patente);

        return v.toString();
    }

    public String consultarEstadoVehiculo (String patente) throws ObjetoNoEncontradoEx, ValidacionEx {

        EstadoVehiculo estado = gestorVehiculos.consultarEstado(patente);

        return estado.toString();
    }

    public String verVehiculos (){

        StringBuilder sb = new StringBuilder();

        List<Vehiculo> vehiculos = gestorVehiculos.listarVehiculos();

        for (Vehiculo v : vehiculos){
            sb.append(v.toString()).append("\n");
        }

        return sb.toString();
    }

    public String verAutos (){

        StringBuilder sb = new StringBuilder();

        List<Auto> autos = gestorVehiculos.listarAutos();

        for (Auto a : autos){
            sb.append(a.toString()).append("\n");
        }

        return sb.toString();
    }

    public String verVehiculosDeCarga (){

        StringBuilder sb = new StringBuilder();

        List<VehiculoDeCarga> vehiculosDeCarga = gestorVehiculos.listarVehiculosDeCarga();

        for (VehiculoDeCarga v : vehiculosDeCarga){
            sb.append(v.toString()).append("\n");
        }

        return sb.toString();
    }

    public String listarDisponibles (){

        StringBuilder sb = new StringBuilder();

        List<Vehiculo> disponibles = gestorVehiculos.listarDisponibles();

        for (Vehiculo v : disponibles){
            sb.append(v.toString()).append("\n");
        }

        return sb.toString();
    }

    public String listarNoDisponibles (){

        StringBuilder sb = new StringBuilder();

        List<Vehiculo> noDisponibles = gestorVehiculos.listarNoDisponibles();

        for (Vehiculo v : noDisponibles){
            sb.append(v.toString()).append("\n");
        }

        return sb.toString();
    }

    










}
