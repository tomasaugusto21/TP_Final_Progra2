package Clases;

import Enums.RolDeUsuario;

public class Empleado extends Usuario{

    private String legajo;

    public Empleado(int id, String nombre, String apellido, int dni, String email, String username, RolDeUsuario rol, String legajo) {
        super(id, nombre, apellido, dni, email, username, rol);
        this.legajo = legajo;
    }

    public String getLegajo() {
        return legajo;
    }

    //faltarian los metodos para registrar entrega o devolucion pero por ahora no sabemos si son necesarios

    public void registrarEntrega() {
        System.out.println("Empleado registrando entrega de vehículo.");
    }

    public void registrarDevolucion() {
        System.out.println("Empleado registrando devolución de vehículo.");
    }
}
