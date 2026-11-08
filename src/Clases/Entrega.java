package Clases;
import java.time.LocalDateTime;
import java.util.Objects;

public class Entrega {

    private int id;
    private int idCliente;
    private int idVehiculo;
    private int idEmpleado;
    private Reserva reserva; // Puede ser null si alquila sin reserva previa
    private LocalDateTime fechaHoraEntrega;
    private LocalDateTime fechaDevolucionPrevista;
    private double kmInicial;
    private double kgTransportados;
    private boolean activa;

    public Entrega(int id,int idCliente,int idVehiculo,int idEmpleado,Reserva reserva,LocalDateTime fechaHoraEntrega,LocalDateTime fechaDevolucionPrevista,double kmInicial,double kgTransportados,boolean activa){

        this.id = id;
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.idEmpleado = idEmpleado;
        this.reserva = reserva;
        this.fechaHoraEntrega = LocalDateTime.now(); // Se toma la hora actual del sistema
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.kmInicial = kmInicial;
        this.kgTransportados = kgTransportados;
        this.activa = true; // Arranca activa porque se acaba de entregar
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

    public Reserva getReserva() {
        return reserva;
    }

    public LocalDateTime getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public LocalDateTime getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public double getKmInicial() {
        return kmInicial;
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

    public void setKmInicial(double kmInicial) {
        this.kmInicial = kmInicial;
    }

    //Métodopara mostrar que el auto ya fue devuelto
    public void finalizarEntrega() {
        this.activa = false;
    }

    @Override
    public String toString() {
        return "Entrega{" +
                "id=" + id +
                ", idCliente=" + idCliente +
                ", idVehiculo=" + idVehiculo +
                ", idEmpleado=" + idEmpleado +
                ", reserva=" + reserva +
                ", fechaHoraEntrega=" + fechaHoraEntrega +
                ", fechaDevolucionPrevista=" + fechaDevolucionPrevista +
                ", kmInicial=" + kmInicial +
                ", kgTransportados=" + kgTransportados +
                ", activa=" + activa +
                '}';
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
        int horaEntrega = fechaHoraEntrega.getHour();
        int horaDevolucion = fechaDevolucionPrevista.getHour();

        double horas = horaDevolucion - horaEntrega;

        if (horas <= 0) {
            horas = 1.0; // Mínimo 1 hora de cobro
        }
        return horas;
    }
}
