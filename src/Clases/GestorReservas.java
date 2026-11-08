package Clases;

import Enums.EstadoReserva;
import Excepciones.ObjetoInvalidoEx;
import Excepciones.ObjetoNoEncontradoEx;

import java.util.ArrayList;
import java.util.List;

public class GestorReservas {

    private final Almacen<Reserva> reservas = new Almacen<>();
    private final GestorVehiculos gestorVehiculos;

    public GestorReservas(GestorVehiculos gestorVehiculos) {
        this.gestorVehiculos = gestorVehiculos;
    }




    public Reserva buscarVigenteDeVehiculo(Vehiculo vehiculo) throws ObjetoInvalidoEx {

        if (vehiculo == null){
            throw new ObjetoInvalidoEx("El vehiculo del que se quieren obtener sus reservas vigentes no puede ser nulo");
        }

        for (Reserva r : reservas.listarTodos()) {
            if (r.getEstado() == EstadoReserva.VIGENTE && r.getIdVehiculo() == vehiculo.getID()) {
                return r;
            }
        }
        return null;
    }

    public List<Reserva> listarDeCliente(Cliente cliente) throws ObjetoInvalidoEx {

        if (cliente == null){
            throw new ObjetoInvalidoEx("El cliente del cual se quieren listar sus reservas no puede ser nulo");
        }

        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : reservas.listarTodos()) {
            if (r.getIdCliente() == cliente.getID()) {
                resultado.add(r);
            }
        }
        return resultado;
    }


}
