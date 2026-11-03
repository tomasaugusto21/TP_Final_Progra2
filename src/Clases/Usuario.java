package Clases;

import Enums.RolDeUsuario;

public abstract class Usuario {

    private int id;
    private String nombre;
    private String apellido;
    private int dni;
    private String email;
    private String username;
    private RolDeUsuario rol;

    public Usuario(int id, String nombre, String apellido, int dni, String email, String username, RolDeUsuario rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.username = username;
        this.rol = rol;
    }
}
