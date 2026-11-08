package Clases;

import Excepciones.ObjetoNoEncontradoEx;
import Excepciones.ValidacionEx;

import java.util.List;

public class GestorUsuarios {

    private final Almacen<Usuario> usuarios = new Almacen<>();



    public Usuario buscarPorId (int id){
        return usuarios.buscarPorId(id);
    }

    public Cliente buscarCliente(int id) throws ObjetoNoEncontradoEx {
        Usuario usuario = usuarios.buscarPorId(id);
        if (!(usuario instanceof Cliente)) {
            throw new ObjetoNoEncontradoEx("No existe un cliente con id " + id);
        }
        return (Cliente) usuario;
    }

    public List<Cliente> listarClientes (){
        return usuarios.listarDeTipo(Cliente.class);
    }

    public List<Empleado> listarEmpleados (){
        return usuarios.listarDeTipo(Empleado.class);
    }





}
