package Clases;

import Enums.RolDeUsuario;

public class Cliente extends Usuario{

    private String numeroDeLicencia;
    private String telefono;
    private String domicilio;

    public Cliente(int id, String nombre, String apellido, int dni, String email, String username, RolDeUsuario rol, String numeroDeLicencia, String telefono, String domicilio) {
        super(id, nombre, apellido, dni, email, username, rol);
        this.numeroDeLicencia = numeroDeLicencia;
        this.telefono = telefono;
        this.domicilio = domicilio;
    }

    public Cliente(String nombre, String apellido, int dni, String email, String username, RolDeUsuario rol, String numeroDeLicencia, String telefono, String domicilio) {
        super(nombre, apellido, dni, email, username, rol);
        this.numeroDeLicencia = numeroDeLicencia;
        this.telefono = telefono;
        this.domicilio = domicilio;
    }

    public String getNumeroDeLicencia() {
        return numeroDeLicencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    @Override
    public String toString() {
        return super.toString() +"Cliente{" +
                "numeroDeLicencia='" + numeroDeLicencia + '\'' +
                ", telefono='" + telefono + '\'' +
                ", domicilio='" + domicilio + '\'' +
                '}';
    }
}
