package Clases;

import Enums.EstadoReserva;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reserva implements Identificable {
    private int id;
    private int idCliente;
    private int idVehiculo;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private EstadoReserva estado;
    private LocalDateTime fechaCreacion;


    public Reserva(int id, int idCliente, int idVehiculo, LocalDateTime horaInicio, LocalDateTime horaFin,LocalDateTime fechaInicio, LocalDateTime fechaFin){
        this.id = id;
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = EstadoReserva.VIGENTE; // Toda reserva nace activa
        this.fechaCreacion = LocalDateTime.now(); // Guarda cuándo se creó en el sistema
    }
    public Reserva(int idCliente, int idVehiculo, LocalDateTime horaInicio, LocalDateTime horaFin,LocalDateTime fechaInicio, LocalDateTime fechaFin){
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = EstadoReserva.VIGENTE; // Toda reserva nace activa
        this.fechaCreacion = LocalDateTime.now(); // Guarda cuándo se creó en el sistema
    }

    public Reserva(int id, int idCliente, int idVehiculo, LocalDateTime horaInicio, LocalDateTime horaFin, LocalDateTime fechaInicio, LocalDateTime fechaFin, EstadoReserva estado, LocalDateTime fechaCreacion) {
        this.id = id;
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public int getID() {
        return id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(LocalDateTime horaFin) {
        this.horaFin = horaFin;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }


    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", idCliente=" + idCliente +
                ", idVehiculo=" + idVehiculo +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return id == reserva.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    //METODOS
    //para cancelar una reserva
    public void cancelarReserva(){
        this.estado = EstadoReserva.ANULADA;
    }

    //para terminarla
    public void finalizarReserva(){
        this.estado = EstadoReserva.FINALIZADA;
    }

    public void vencerReserva (){
        this.estado = EstadoReserva.EXPIRADA;
    }

    //metodo para controlar que dos reservas no se solapen, devuelve true si se solapan
    public boolean seSolapaCon(LocalDateTime otroInicio, LocalDateTime otroFin) {
        return horaInicio.isBefore(otroFin) && otroInicio.isBefore(horaFin);
    }



}
