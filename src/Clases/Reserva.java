package Clases;

import Enums.EstadoReserva;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Reserva implements Identificador{
    private int id;
    private int idCliente;
    private int idVehiculo;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private EstadoReserva estado;
    private LocalDateTime fechaCreacion;


    public Reserva(int id, int idCliente, int idVehiculo, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin){
        this.id = id;
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.estado = EstadoReserva.VIGENTE; // Toda reserva nace activa
        this.fechaCreacion = LocalDateTime.now(); // Guarda cuándo se creó en el sistema
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

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
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

    //metodo para calcular los dias q se lo llevo
    public long calcularDias(){
        int diaInicio = fechaHoraInicio.getDayOfMonth();
        int diaFin = fechaHoraFin.getDayOfMonth();

        long dias = diaFin - diaInicio;

        if (dias <= 0) {
            dias = 1;
        }
        return dias;
    }

}
