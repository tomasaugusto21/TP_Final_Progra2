package Clases;

import Enums.EstadoVehiculo;
import Enums.RolDeUsuario;
import Excepciones.*;

import java.util.ArrayList;
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



    //metodos del gestor usuario

    public void registrarse (String nombre, String apellido, int dni, String email, String username, String numeroDeLicencia, String telefono, String domicilio, String contraseña) throws ErrorDeCargaEx, ObjetoInvalidoEx, ObjetoDuplicadoEx, ValidacionEx {

        Cliente registrado = gestorUsuarios.signup(nombre, apellido, dni, email, username, numeroDeLicencia, telefono, domicilio, contraseña);

    }

    public void loguearse (String username, String contraseña) throws LoginEx, ValidacionEx {
        this.logueado = gestorUsuarios.login(username, contraseña);
    }

    public boolean crearEmpleado (String nombre, String apellido, int dni, String email, String username, String legajo, String contraseña) throws PermisosEx, ErrorDeCargaEx, ObjetoInvalidoEx, ObjetoDuplicadoEx, ValidacionEx {
        if (this.logueado.getRol() != RolDeUsuario.ADMINISTRADOR){
            throw new PermisosEx("Solo los administradores pueden crear empleados");
        }

        Empleado e = gestorUsuarios.crearEmpleado(nombre, apellido, dni, email, username, legajo, contraseña);

        if (e == null){
            return false;
        }else{
            return true;
        }
    }

    public boolean actualizarNombre (int id, String nuevoNombre) throws ValidacionEx {
        return gestorUsuarios.actualizarNombre(id, nuevoNombre);
    }

    public boolean actualizarApellido (int id, String nuevoApellido) throws ValidacionEx {
        return gestorUsuarios.actualizarApellido(id, nuevoApellido);
    }

    public boolean actualizarEmail (int id, String nuevoEmail) throws ValidacionEx {
        return gestorUsuarios.actualizarEmail(id, nuevoEmail);
    }

    public boolean actualizarUsername (int id, String nuevoUsername) throws ValidacionEx, ObjetoDuplicadoEx{
        return gestorUsuarios.actualizarUsername(id, nuevoUsername);
    }

    public boolean actualizarLegajo (int id, String nuevoLegajo) throws ValidacionEx, ObjetoInvalidoEx, PermisosEx {
        if (this.logueado.getRol() != RolDeUsuario.ADMINISTRADOR){
            throw new PermisosEx("Solo los administradores pueden actualizar legajos");
        }

        return gestorUsuarios.actualizarLegajo(id, nuevoLegajo);
    }

    public boolean actualizarTelefonoCliente (int id, String nuevoTelefono) throws ValidacionEx, ObjetoInvalidoEx{
        return gestorUsuarios.actualizarTelefonoCliente(id, nuevoTelefono);
    }

    public boolean actualizarDomicilioCliente (int id, String nuevoDomicilio) throws ObjetoInvalidoEx, ValidacionEx{
        return gestorUsuarios.actualizarDomicilioCliente(id, nuevoDomicilio);
    }

    public String buscarCliente (int id) throws PermisosEx, ObjetoNoEncontradoEx, ValidacionEx {

        if (this.logueado.getRol() == RolDeUsuario.CLIENTE){
            throw new PermisosEx("Los clientes no pueden buscar otros clientes");
        }

        Cliente buscado = gestorUsuarios.buscarCliente(id);

        return buscado.toString();
    }

    public String buscarEmpleado (int id) throws PermisosEx, ObjetoNoEncontradoEx, ValidacionEx {

        if (this.logueado.getRol() == RolDeUsuario.CLIENTE){
            throw new PermisosEx("Los clientes no pueden buscar empleados");
        }

        Empleado buscado = gestorUsuarios.buscarEmpleado(id);

        return buscado.toString();
    }

    public String verClientes () throws PermisosEx {

        if (this.logueado.getRol() == RolDeUsuario.CLIENTE){
            throw new PermisosEx("Los clientes no pueden acceder a esa informacion");
        }

        StringBuilder sb = new StringBuilder();
        List<Cliente> clientes = gestorUsuarios.listarClientes();

        for (Cliente c : clientes){
            sb.append(c.toString()).append("\n");
        }

        return sb.toString();
    }

    public String verEmpleados () throws PermisosEx {

        if (this.logueado.getRol() == RolDeUsuario.CLIENTE){
            throw new PermisosEx("Los clientes no pueden acceder a esa informacion");
        }

        StringBuilder sb = new StringBuilder();
        List<Empleado> empleados = gestorUsuarios.listarEmpleados();

        for (Empleado e : empleados){
            sb.append(e.toString()).append("\n");
        }
        return sb.toString();
    }
    





}
