package Clases;

import Enums.RolDeUsuario;

public class Cliente extends Usuario{

    private int numeroDeLicencia;
    private int telefono;
    private String domicilio;

    public Cliente(int id, String nombre, String apellido, int dni, String email, String username, RolDeUsuario rol, int numeroDeLicencia, int telefono, String domicilio) {
        super(id, nombre, apellido, dni, email, username, rol);
        this.numeroDeLicencia = numeroDeLicencia;
        this.telefono = telefono;
        this.domicilio = domicilio;
    }
}
