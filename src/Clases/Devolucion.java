package Clases;
import java.time.LocalDateTime;
public class Devolucion implements Identificador{

    private int id;
    private int idEntrega;
    private int idEmpleado;
    private LocalDateTime fechaHoraDevolucion;
    private double kmRecorridos;
    private double costoTotal;

    // Constructor
    public Devolucion(int id, int idEntrega, int idEmpleado, double kmFinales, double kmInicial, double costoTotal) {
        this.id = id;
        this.idEntrega = idEntrega;
        this.idEmpleado = idEmpleado;
        this.fechaHoraDevolucion = LocalDateTime.now(); // Hora exacta de devolución
        this.kmRecorridos = calcularKmRecorridos(kmFinales, kmInicial);
        this.costoTotal = costoTotal;
    }

    @Override
    public int getID() {
        return 0;
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public LocalDateTime getFechaHoraDevolucion() {
        return fechaHoraDevolucion;
    }

    public double getKmRecorridos() {
        return kmRecorridos;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    //metodo para calcular los km que recorrio el cliente
    public double calcularKmRecorridos(double kmFinales, double kmInicial) {
        double recorridos = kmFinales - kmInicial;

        if (recorridos < 0) {
            recorridos = 0;
        }

        return recorridos;
    }
}
