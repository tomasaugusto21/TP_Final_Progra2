package Clases;

import Enums.RolDeUsuario;

public class Administrador extends Usuario{


    public Administrador(int id, String nombre, String apellido, int dni, String email, String username, RolDeUsuario rol) {
        super(id, nombre, apellido, dni, email, username, rol);
    }


    public void crearEmpleado() {
        System.out.println("Administrador creando nuevo empleado.");
    }

}
