package Clases;

import Enums.EstadoReserva;
import Enums.EstadoVehiculo;
import Excepciones.ObjetoDuplicadoEx;
import Excepciones.ObjetoInvalidoEx;
import Excepciones.ObjetoNoEncontradoEx;
import Excepciones.ValidacionEx;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

public class GestorReservas {

    private final Almacen<Reserva> reservas = new Almacen<>();
    private final GestorVehiculos gestorVehiculos;
    private final GestorUsuarios gestorUsuarios;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm");

    public GestorReservas(GestorVehiculos gestorVehiculos, GestorUsuarios gestorUsuarios) {
        this.gestorVehiculos = gestorVehiculos;
        this.gestorUsuarios = gestorUsuarios;
    }



    public void descargarDatosBDD () throws ObjetoInvalidoEx, ObjetoDuplicadoEx, ValidacionEx {

        List<Reserva> obtenidas = DBHandler.obtenerTodasLasReservas();

        for (Reserva r : obtenidas){
            reservas.agregar(r);
        }
    }


    public boolean crearReserva (int idCliente, int idVehiculo, String horaInicio, String horaFin, String fechaInicio, String fechaFin) throws ValidacionEx, ObjetoInvalidoEx, ObjetoDuplicadoEx, ObjetoNoEncontradoEx {

        actualizarReservasExpiradas();

        gestorUsuarios.buscarCliente(idCliente);
        Vehiculo vehiculo = gestorVehiculos.buscarPorId(idVehiculo);

        LocalDateTime momentoInicio = leerFechaHora(fechaInicio, horaInicio);
        LocalDateTime momentoFin = leerFechaHora(fechaFin, horaFin);

        if (momentoInicio.isBefore(LocalDateTime.now())) {
            throw new ValidacionEx("La fecha de inicio de la reserva no puede estar en el pasado");
        }
        if (!momentoFin.isAfter(momentoInicio)) {
            throw new ValidacionEx("La fecha de fin de la reserva debe ser posterior a la de inicio");
        }

        if (vehiculo.getEstadoVehiculo() == EstadoVehiculo.FUERA_DE_SERVICIO) {
            throw new ObjetoInvalidoEx("El vehículo " + vehiculo.getPatente() + " está fuera de servicio");
        }

        Reserva chequeo = buscarCruceDeFechas(vehiculo, momentoInicio, momentoFin);
        if (chequeo != null) {
            throw new ObjetoInvalidoEx("El vehículo ya está reservado del para el momento elegido");
        }

        LocalDateTime soloDiaInicio = momentoInicio.toLocalDate().atStartOfDay();
        LocalDateTime soloDiaFin = momentoFin.toLocalDate().atStartOfDay();
        Reserva reserva = new Reserva(idCliente, idVehiculo, momentoInicio, momentoFin, soloDiaInicio, soloDiaFin);

        int id = DBHandler.cargar_reserva(reserva);

        if (id == -1){
            return false;
        }

        reserva.setId(id);
        reservas.agregar(reserva);

        return true;
    }

    public boolean actualizarHoraInicioReserva (int id, String nuevaHora) throws ValidacionEx, ObjetoInvalidoEx {

        actualizarReservasExpiradas();

        Reserva reserva = reservas.buscarPorId(id);
        if (reserva.getEstado() != EstadoReserva.VIGENTE) {
            throw new ObjetoInvalidoEx("Solo se puede modificar una reserva cuyo estado sea VIGENTE");
        }

        LocalTime hora = leerHora(nuevaHora);
        LocalDateTime nuevoInicio = LocalDateTime.of(reserva.getHoraInicio().toLocalDate(), hora);

        if (nuevoInicio.isBefore(LocalDateTime.now())) {
            throw new ValidacionEx("El nuevo inicio no puede estar en el pasado");
        }
        if (!reserva.getHoraFin().isAfter(nuevoInicio)) {
            throw new ValidacionEx("El nuevo inicio debe ser anterior al fin de la reserva");
        }

        Vehiculo vehiculo = gestorVehiculos.buscarPorId(reserva.getIdVehiculo());
        if (buscarCruceDeFechas(vehiculo, nuevoInicio, reserva.getHoraFin(), id) != null) {
            throw new ObjetoInvalidoEx("El nuevo horario se pisa con otra reserva del vehículo");
        }

        if (DBHandler.actualizar_reserva_hora_inicio(id, nuevoInicio) == 0){
            return false;
        }

        reserva.setHoraInicio(nuevoInicio);

        return true;
    }

    public boolean actualizarHoraFinReserva (int id, String nuevaHora) throws ObjetoInvalidoEx, ValidacionEx {

        actualizarReservasExpiradas();

        Reserva reserva = reservas.buscarPorId(id);
        if (reserva.getEstado() != EstadoReserva.VIGENTE) {
            throw new ObjetoInvalidoEx("Solo se puede modificar una reserva cuyo estado sea VIGENTE");
        }

        LocalTime hora = leerHora(nuevaHora);
        LocalDateTime nuevoFin = LocalDateTime.of(reserva.getHoraFin().toLocalDate(), hora);

        if (!nuevoFin.isAfter(LocalDateTime.now())) {
            throw new ValidacionEx("El nuevo fin no puede estar en el pasado");
        }
        if (!nuevoFin.isAfter(reserva.getHoraInicio())) {
            throw new ValidacionEx("El nuevo fin debe ser posterior al inicio de la reserva");
        }

        Vehiculo vehiculo = gestorVehiculos.buscarPorId(reserva.getIdVehiculo());
        if (buscarCruceDeFechas(vehiculo, reserva.getHoraInicio(), nuevoFin, reserva.getID()) != null) {
            throw new ObjetoInvalidoEx("El nuevo horario se pisa con otra reserva del vehículo");
        }

        if (DBHandler.actualizar_reserva_hora_fin(id, nuevoFin) == 0) {
            return false;
        }

        reserva.setHoraFin(nuevoFin);

        return true;
    }

    public boolean actualizarFechaInicioReserva (int id, String nuevaFecha) throws ValidacionEx, ObjetoInvalidoEx {

        actualizarReservasExpiradas();

        Reserva reserva = reservas.buscarPorId(id);
        if (reserva.getEstado() != EstadoReserva.VIGENTE) {
            throw new ObjetoInvalidoEx("Solo se puede modificar una reserva cuyo estado sea VIGENTE");
        }

        if (nuevaFecha == null || nuevaFecha.isBlank()) {
            throw new ValidacionEx("La fecha no puede ser nula o estar vacia");
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(nuevaFecha.trim(), FORMATO_FECHA);
        } catch (DateTimeException e) {
            throw new ValidacionEx("Fecha inválida. Use dd/MM/aaaa");
        }

        LocalDateTime nuevoInicio = LocalDateTime.of(fecha, reserva.getHoraInicio().toLocalTime());

        if (nuevoInicio.isBefore(LocalDateTime.now())) {
            throw new ValidacionEx("El nuevo inicio no puede estar en el pasado");
        }
        if (!reserva.getHoraFin().isAfter(nuevoInicio)) {
            throw new ValidacionEx("El nuevo inicio debe ser anterior al fin de la reserva");
        }

        Vehiculo vehiculo = gestorVehiculos.buscarPorId(reserva.getIdVehiculo());
        if (buscarCruceDeFechas(vehiculo, nuevoInicio, reserva.getHoraFin(), reserva.getID()) != null) {
            throw new ObjetoInvalidoEx("El nuevo horario se pisa con otra reserva del vehículo");
        }


        if (DBHandler.actualizar_reserva_fecha_inicio(id, nuevoInicio) == 0) {
            return false;
        }

        reserva.setFechaInicio(nuevoInicio.toLocalDate().atStartOfDay());
        reserva.setHoraInicio(nuevoInicio);

        return true;
    }

    public boolean actualizarFechaFinReserva (int id, String nuevaFecha) throws ObjetoInvalidoEx, ValidacionEx {

        actualizarReservasExpiradas();

        Reserva reserva = reservas.buscarPorId(id);
        if (reserva.getEstado() != EstadoReserva.VIGENTE) {
            throw new ObjetoInvalidoEx("Solo se puede modificar una reserva cuyo estado sea VIGENTE");
        }

        if (nuevaFecha == null || nuevaFecha.isBlank()) {
            throw new ValidacionEx("La fecha es obligatoria");
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(nuevaFecha.trim(), FORMATO_FECHA);
        } catch (DateTimeException e) {
            throw new ValidacionEx("Fecha inválida. Use dd/MM/aaaa");
        }

        LocalDateTime nuevoFin = LocalDateTime.of(fecha, reserva.getHoraFin().toLocalTime());

        if (!nuevoFin.isAfter(LocalDateTime.now())) {
            throw new ValidacionEx("El nuevo fin no puede estar en el pasado");
        }
        if (!nuevoFin.isAfter(reserva.getHoraInicio())) {
            throw new ValidacionEx("El nuevo fin debe ser posterior al inicio de la reserva");
        }

        Vehiculo vehiculo = gestorVehiculos.buscarPorId(reserva.getIdVehiculo());
        if (buscarCruceDeFechas(vehiculo, reserva.getHoraInicio(), nuevoFin, reserva.getID()) != null) {
            throw new ObjetoInvalidoEx("El nuevo horario se pisa con otra reserva del vehículo");
        }

        if (DBHandler.actualizar_reserva_fecha_fin(id, nuevoFin) == 0) {
            return false;
        }

        reserva.setFechaFin(nuevoFin.toLocalDate().atStartOfDay());
        reserva.setHoraFin(nuevoFin);

        return true;
    }

    public boolean actualizarEstadoDeReserva (int id, EstadoReserva nuevoEstado) throws ValidacionEx, ObjetoInvalidoEx {

        actualizarReservasExpiradas();

        if (nuevoEstado == null){
            throw new ValidacionEx("El nuevo estado de la reserva no puede ser nulo");
        }

        Reserva reserva = buscarReservaPorId(id);

        if (reserva.getEstado() != EstadoReserva.VIGENTE){
            throw new ObjetoInvalidoEx("Solo se puede cambiar el estado de una reserva vigente");
        }

        if (DBHandler.actualizar_reserva_estado(id, nuevoEstado) == 0){
            return false;
        }

        if (nuevoEstado == EstadoReserva.ANULADA) {
            reserva.cancelarReserva();
        } else if (nuevoEstado == EstadoReserva.FINALIZADA) {
            reserva.finalizarReserva();
        } else if (nuevoEstado == EstadoReserva.EXPIRADA) {
            reserva.vencerReserva();
        }

        return true;
    }

    public int actualizarReservasExpiradas() {

        int cantidad = 0;

        for (Reserva r : reservas.listarTodos()) {

            if (r.getEstado() == EstadoReserva.VIGENTE && r.getHoraFin().isBefore(LocalDateTime.now())) {

                if (DBHandler.actualizar_reserva_estado(r.getID(), EstadoReserva.EXPIRADA) > 0) {
                    r.vencerReserva();
                    cantidad++;
                }
            }
        }
        return cantidad;
    }


    public Reserva buscarReservaPorId (int id) throws ValidacionEx {
        return reservas.buscarPorId(id);
    }

    public Reserva buscarCruceDeFechas(Vehiculo vehiculo, LocalDateTime inicio, LocalDateTime fin) throws ObjetoInvalidoEx {

        if (vehiculo == null) {
            throw new ObjetoInvalidoEx("El vehículo no puede ser nulo");
        }
        for (Reserva r : reservas.listarTodos()) {
            if (r.getEstado() == EstadoReserva.VIGENTE && r.getIdVehiculo() == vehiculo.getID() && r.seSolapaCon(inicio, fin)) {
                return r;
            }
        }
        return null;
    }

    public Reserva buscarCruceDeFechas(Vehiculo vehiculo, LocalDateTime inicio, LocalDateTime fin, int idReservaAIgnorar) throws ObjetoInvalidoEx {
        if (vehiculo == null) {
            throw new ObjetoInvalidoEx("El vehículo no puede ser nulo");
        }
        for (Reserva r : reservas.listarTodos()) {
            if (r.getID() != idReservaAIgnorar && r.getEstado() == EstadoReserva.VIGENTE && r.getIdVehiculo() == vehiculo.getID() && r.seSolapaCon(inicio, fin)) {
                return r;
            }
        }
        return null;
    }

    public List<Reserva> listarDeCliente(Cliente cliente) throws ObjetoInvalidoEx {

        actualizarReservasExpiradas();

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


    private LocalDateTime leerFechaHora(String fecha, String hora) throws ValidacionEx {

        if (fecha == null || fecha.isBlank()){
            throw new ValidacionEx("La fecha no puede ser nula o estar en blanco");
        }
        if (hora == null || hora.isBlank()){
            throw new ValidacionEx("La hoa no puede ser nula o estar en blanco");
        }

        try {
            LocalDate f = LocalDate.parse(fecha, FORMATO_FECHA);
            LocalTime h = LocalTime.parse(hora, FORMATO_HORA);
            return LocalDateTime.of(f, h);
        } catch (DateTimeParseException e) {
            throw new ValidacionEx("Fecha u hora inválida. Use dd/MM/aaaa y HH:mm");
        }
    }

    private LocalTime leerHora(String hora) throws ValidacionEx {

        if (hora == null || hora.isBlank()) {
            throw new ValidacionEx("La hora no puede ser nula o estar en blanco");
        }
        try {
            return LocalTime.parse(hora, FORMATO_HORA);
        } catch (DateTimeException e) {
            throw new ValidacionEx("Hora inválida. El formato correcto es: HH:mm");
        }
    }


}
