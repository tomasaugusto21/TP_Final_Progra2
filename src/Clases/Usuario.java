package Clases;

import Enums.RolDeUsuario;

import java.util.Objects;

public abstract class Usuario implements Identificable {

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

    public Usuario(String nombre, String apellido, int dni, String email, String username, RolDeUsuario rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.username = username;

        this.rol = rol;
    }

    protected void setId(int id) {
        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public RolDeUsuario getRol() {
        return rol;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(username, usuario.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni=" + dni +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", rol=" + rol +
                '}';
    }
}
