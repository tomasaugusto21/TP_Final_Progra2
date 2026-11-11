package Clases;

import Enums.EstadoReserva;
import Enums.EstadoVehiculo;
import Excepciones.ObjetoInvalidoEx;
import Excepciones.ObjetoNoEncontradoEx;
import Excepciones.ValidacionEx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class GestorAlquileres {

    private final ArrayList<Entrega> entregas = new ArrayList<>();
    private final ArrayList<Devolucion> devoluciones = new ArrayList<>();
    private final GestorVehiculos gestorVehiculos;
    private final GestorReservas gestorReservas;
    private final GestorUsuarios gestorUsuarios;

    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public GestorAlquileres(GestorVehiculos gestorVehiculos, GestorReservas gestorReservas, GestorUsuarios gestorUsuarios) {
        this.gestorVehiculos = gestorVehiculos;
        this.gestorReservas = gestorReservas;
        this.gestorUsuarios = gestorUsuarios;
    }


    public boolean hacerEntrega (int idCliente, int idVehiculo, int idEmpleado, int idReserva, String fechaDevolucionPrevista, double kmEntrega, double kgTransportados) throws ObjetoNoEncontradoEx, ValidacionEx, ObjetoInvalidoEx {

        gestorReservas.actualizarReservasExpiradas();

        gestorUsuarios.buscarCliente(idCliente);
        gestorUsuarios.buscarEmpleado(idEmpleado);
        Vehiculo v = gestorVehiculos.buscarPorId(idVehiculo);

        if (fechaDevolucionPrevista == null || fechaDevolucionPrevista.isBlank()){
            throw new ValidacionEx("La fecha de devolucion prevista no puede ser nula o estar en blanco");
        }

        LocalDateTime fechaDevolucion;
        try {
            fechaDevolucion = LocalDateTime.parse(fechaDevolucionPrevista, FORMATO_FECHA_HORA);
        } catch (DateTimeParseException e) {
            throw new ValidacionEx("El formato de la fecha y hora es inválido. El formato correcto es: yyyy-MM-dd HH:mm");
        }
        if (kgTransportados < 0){
            throw new ValidacionEx("No se puede transportar un numero negativo de kg");
        }
        if (v instanceof VehiculoDeCarga){
            if (((VehiculoDeCarga) v).getCapacidadDeCargaKg() < kgTransportados){
                throw new ValidacionEx("No se pueden transportar mas kg de los que indica la capacidad del vehiculo");
            }
        }
        if (!fechaDevolucion.isAfter(LocalDateTime.now())) {
            throw new ValidacionEx("La fecha de devolución debe ser posterior al momento actual");
        }
        if (v.getEstadoVehiculo() != EstadoVehiculo.DISPONIBLE) {
            throw new ObjetoInvalidoEx("El vehículo no está disponible para realizar la entrega (estado: " + v.getEstadoVehiculo() + ")");
        }

        Reserva reserva = null;
        if (idReserva != 0) {
            reserva = gestorReservas.buscarReservaPorId(idReserva);
            if (reserva.getEstado() != EstadoReserva.VIGENTE) {
                throw new ObjetoInvalidoEx("La reserva no está vigente (estado: " + reserva.getEstado() + ")");
            }
            if (reserva.getIdCliente() != idCliente) {
                throw new ObjetoInvalidoEx("La reserva pertenece a otro cliente");
            }
            if (reserva.getIdVehiculo() != idVehiculo) {
                throw new ObjetoInvalidoEx("La reserva no corresponde a ese vehículo");
            }
        }

        if (gestorReservas.buscarCruceDeFechas(v, LocalDateTime.now(), fechaDevolucion, idReserva) != null) {
            throw new ValidacionEx("El período del alquiler se pisa con otra reserva del vehículo");
        }

        Entrega entrega = new Entrega(idCliente, idVehiculo, idEmpleado, idReserva, fechaDevolucion, v.getKilometraje(), kgTransportados);

        int id = DBHandler.crear_entrega(entrega);
        if (id == -1) {
            return false;
        }

        entrega.setId(id);
        entregas.add(entrega);
        v.setEstadoVehiculo(EstadoVehiculo.ALQUILADO);

        if (reserva != null) {
            reserva.finalizarReserva();
        }

        return true;
    }

    //solo permite actualizar la fecha moviendola para más adelante
    public boolean actualizarFechaDevolucionPrevista (int id, String nuevaFecha) throws ValidacionEx, ObjetoInvalidoEx, ObjetoNoEncontradoEx {

        gestorReservas.actualizarReservasExpiradas();

        Entrega e = buscarEntregaPorId(id);
        if (e == null) {
            throw new ObjetoNoEncontradoEx("No existe una entrega con ese id");
        }
        if (!e.isActiva()) {
            throw new ObjetoInvalidoEx("La entrega ya fue devuelta y no se puede modificar su fecha de devolucion");
        }

        if (nuevaFecha == null || nuevaFecha.isBlank()) {
            throw new ValidacionEx("La nueva fecha de devolución es obligatoria");
        }
        LocalDateTime nuevaFechaDevolucion;
        try {
            nuevaFechaDevolucion = LocalDateTime.parse(nuevaFecha.trim(), FORMATO_FECHA_HORA);
        } catch (DateTimeParseException ex) {
            throw new ValidacionEx("El formato de la fecha y hora es invalido. El formato correcto es: yyyy-MM-dd HH:mm");
        }
        if (!nuevaFechaDevolucion.isAfter(e.getFechaDevolucionPrevista())) {
            throw new ValidacionEx("La nueva fecha debe ser posterior a la devolución prevista actual");
        }

        Vehiculo v = gestorVehiculos.buscarPorId(e.getIdVehiculo());
        if (gestorReservas.buscarCruceDeFechas(v, e.getFechaDevolucionPrevista(), nuevaFechaDevolucion) != null) {
            throw new ObjetoInvalidoEx("El vehículo tiene una reserva en el tiempo que se quiere extender esta entrega");
        }

        if (DBHandler.actualizar_entrega_fecha_devolucion(id, nuevaFechaDevolucion) == 0) {
            return false;
        }

        e.setFechaDevolucionPrevista(nuevaFechaDevolucion);
        return true;

    }

    public boolean actualizarEmpleadoDeEntrega(int idEntrega, int nuevoIdEmpleado) throws ObjetoNoEncontradoEx, ObjetoInvalidoEx, ValidacionEx {

        gestorUsuarios.buscarEmpleado(nuevoIdEmpleado);

        Entrega entrega = buscarEntregaPorId(idEntrega);
        if (entrega == null) {
            throw new ObjetoNoEncontradoEx("No existe una entrega con id " + idEntrega);
        }
        if (entrega.getIdEmpleado() == nuevoIdEmpleado) {
            throw new ObjetoInvalidoEx("La entrega ya tiene asignado a ese empleado");
        }


        if (DBHandler.actualizar_entrega_cambio_empleado(idEntrega, nuevoIdEmpleado) == 0) {
            return false;
        }

        entrega.setIdEmpleado(nuevoIdEmpleado);
        return true;
    }

    public boolean hacerDevolucion (int idEntrega, int idEmpleado, double kmFinal) throws ObjetoNoEncontradoEx, ObjetoInvalidoEx, ValidacionEx {

        gestorUsuarios.buscarEmpleado(idEmpleado);

        Entrega entrega = buscarEntregaPorId(idEntrega);
        if (entrega == null) {
            throw new ObjetoNoEncontradoEx("No existe una entrega con ese id");
        }
        if (!entrega.isActiva()) {
            throw new ObjetoInvalidoEx("La entrega ya fue devuelta");
        }
        if (kmFinal < entrega.getKmEntrega()) {
            throw new ValidacionEx("El kilometraje final no puede ser menor al de la entrega");
        }

        Vehiculo v = gestorVehiculos.buscarPorId(entrega.getIdVehiculo());
        double kmRecorridos = kmFinal - entrega.getKmEntrega();
        double costoTotal = v.calcularCostoAlquiler(entrega);

        Devolucion devolucion = new Devolucion(idEntrega, idEmpleado, LocalDateTime.now(), kmRecorridos, costoTotal);

        int id = DBHandler.cargar_devolucion(devolucion, entrega.getIdVehiculo(), kmFinal);
        if (id == -1) {
            return false;
        }


        devolucion.setId(id);
        devoluciones.add(devolucion);
        entrega.finalizarEntrega();
        v.setKilometraje(kmFinal);
        v.setEstadoVehiculo(EstadoVehiculo.DISPONIBLE);

        return true;
    }

    public boolean actualizarEmpleadoDeDevolucion(int idDevolucion, int nuevoIdEmpleado) throws ObjetoNoEncontradoEx, ObjetoInvalidoEx, ValidacionEx {

        gestorUsuarios.buscarEmpleado(nuevoIdEmpleado);

        Devolucion devolucion = buscarDevolucionPorId(idDevolucion);
        if (devolucion == null) {
            throw new ObjetoNoEncontradoEx("No existe una devolución con ese id ");
        }
        if (devolucion.getIdEmpleado() == nuevoIdEmpleado) {
            throw new ObjetoInvalidoEx("La devolución ya tiene asignado a ese empleado");
        }

        if (DBHandler.actualizar_devolucion_cambio_empleado(idDevolucion, nuevoIdEmpleado) == 0) {
            return false;
        }

        devolucion.setIdEmpleado(nuevoIdEmpleado);
        return true;
    }

    public Entrega buscarEntregaPorId (int id){
        for (Entrega e : entregas){
            if (e.getId() == id){
                return e;
            }
        }
        return null;
    }

    public Devolucion buscarDevolucionPorId(int id) {
        for (Devolucion d : devoluciones) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
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
