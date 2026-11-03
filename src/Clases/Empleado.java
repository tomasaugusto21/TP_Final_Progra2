package Clases;

import Enums.RolDeUsuario;

public class Empleado extends Usuario{

    private int legajo;

    public Empleado(int id, String nombre, String apellido, int dni, String email, String username, RolDeUsuario rol, int legajo) {
        super(id, nombre, apellido, dni, email, username, rol);
        this.legajo = legajo;
    }

    public int getLegajo() {
        return legajo;
    }

    //faltarian los metodos para registrar entrega o devolucion pero por ahora no sabemos si son necesarios

}
