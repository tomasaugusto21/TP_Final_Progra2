package Clases;

import Enums.RolDeUsuario;

public class Empleado extends Usuario{

    private String legajo;

    public Empleado(int id, String nombre, String apellido, int dni, String email, String username, RolDeUsuario rol, String legajo) {
        super(id, nombre, apellido, dni, email, username, rol);
        this.legajo = legajo;
    }

    public Empleado(String nombre, String apellido, int dni, String email, String username, RolDeUsuario rol, String legajo) {
        super(nombre, apellido, dni, email, username, rol);
        this.legajo = legajo;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }


}
