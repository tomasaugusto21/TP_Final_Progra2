package Clases;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Entrega {

    private int id;
    private int idCliente;
    private int idVehiculo;
    private int idEmpleado;
    private int idReserva; // Puede ser null si alquila sin reserva previa
    private LocalDateTime fechaHoraEntrega;
    private LocalDateTime fechaDevolucionPrevista;
    private double kmEntrega;
    private double kgTransportados;
    private boolean activa;

    public Entrega(int id, int idCliente, int idVehiculo, int idEmpleado, int idReserva, LocalDateTime fechaHoraEntrega, LocalDateTime fechaDevolucionPrevista, double kmEntrega, double kgTransportados, boolean activa) {
        this.id = id;
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.idEmpleado = idEmpleado;
        this.idReserva = idReserva;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.kmEntrega = kmEntrega;
        this.kgTransportados = kgTransportados;
        this.activa = activa;
    }

    public Entrega(int idCliente, int idVehiculo, int idEmpleado, int idReserva, LocalDateTime fechaDevolucionPrevista, double kmEntrega, double kgTransportados) {
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.idEmpleado = idEmpleado;
        this.idReserva = idReserva;
        this.fechaHoraEntrega = LocalDateTime.now();
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.kmEntrega = kmEntrega;
        this.kgTransportados = kgTransportados;
        this.activa = true;
    }

    public int getId() {
        return id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public LocalDateTime getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public LocalDateTime getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public double getKmEntrega() {
        return kmEntrega;
    }

    public double getKgTransportados() {
        return kgTransportados;
    }

    public boolean isActiva() {
        return activa;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setFechaDevolucionPrevista(LocalDateTime fechaDevolucionPrevista) {
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    //Métodopara mostrar que el auto ya fue devuelto
    public void finalizarEntrega() {
        this.activa = false;
    }

    @Override
    public String toString() {
        return "Entrega: " +
                "ID: " + id +
                " | Cliente: " + idCliente +
                " | Vehículo: " + idVehiculo +
                " | Empleado: " + idEmpleado +
                " | Reserva: " + (idReserva == 0 ? "Sin reserva" : idReserva) +
                " | Fecha y hora de entrega: " + fechaHoraEntrega.toLocalDate() + " " + fechaHoraEntrega.toLocalTime() +
                " | Devolución prevista: " + fechaDevolucionPrevista.toLocalDate() + " " + fechaDevolucionPrevista.toLocalTime() +
                " | KM iniciales: " + kmEntrega +
                " | KG transportados: " + kgTransportados +
                " | Activa: " + (activa ? "Sí" : "No");
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entrega entrega = (Entrega) o;
        return id == entrega.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    //metodo para calcular las horas del alquiler
    public double calcularHoras() {
        double minutos = Duration.between(fechaHoraEntrega, fechaDevolucionPrevista).toMinutes();
        double horas = Math.ceil(minutos / 60.0);
        return Math.max(horas, 1.0);
    }
}
