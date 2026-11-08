package Clases;

import Excepciones.ObjetoInvalidoEx;

import java.util.ArrayList;
import java.util.List;

public class GestorAlquileres {

    private final ArrayList<Entrega> entregas = new ArrayList<>();
    private final ArrayList<Devolucion> devoluciones = new ArrayList<>();
    private final GestorVehiculos gestorVehiculos;
    private final GestorReservas gestorReservas;

    public GestorAlquileres(GestorVehiculos gestorVehiculos, GestorReservas gestorReservas) {
        this.gestorVehiculos = gestorVehiculos;
        this.gestorReservas = gestorReservas;
    }


    public List<Entrega> listarActualmenteAlquilados() {
        List<Entrega> resultado = new ArrayList<>();
        for (Entrega e : entregas) {
            if (e.isActiva()) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public List<Entrega> historialDeCliente(Cliente cliente) throws ObjetoInvalidoEx {

        if (cliente == null){
            throw new ObjetoInvalidoEx("El cliente del cual se busca listar su historial no puede ser nulo");
        }

        List<Entrega> resultado = new ArrayList<>();
        for (Entrega e : entregas) {
            if (e.getIdCliente() == cliente.getID()) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public List<Entrega> listarEntregas(){
        return new ArrayList<>(entregas);
    }

    public List<Devolucion> listarDevoluciones() {
        return new ArrayList<>(devoluciones);
    }


}
