package Clases;

import java.time.LocalDateTime;
import java.util.Objects;

public class Devolucion {

    private int id;
    private int idEntrega;
    private int idEmpleado;
    private LocalDateTime fechaHoraDevolucion;
    private double kmRecorridos;
    private double costoTotal;

    // Constructor
    public Devolucion(int id, int idEntrega, int idEmpleado, LocalDateTime fechaHoraDevolucion, double kmFinales, double kmInicial, double costoTotal) {
        this.id = id;
        this.idEntrega = idEntrega;
        this.idEmpleado = idEmpleado;
        this.fechaHoraDevolucion = fechaHoraDevolucion;
        this.kmRecorridos = calcularKmRecorridos(kmFinales, kmInicial);
        this.costoTotal = costoTotal;
    }

    public Devolucion(int idEntrega, int idEmpleado, LocalDateTime fechaHoraDevolucion, double kmRecorridos, double costoTotal) {
        this.idEntrega = idEntrega;
        this.idEmpleado = idEmpleado;
        this.fechaHoraDevolucion = fechaHoraDevolucion;
        this.kmRecorridos = kmRecorridos;
        this.costoTotal = costoTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public LocalDateTime getFechaHoraDevolucion() {
        return fechaHoraDevolucion;
    }

    public void setFechaHoraDevolucion(LocalDateTime fechaHoraDevolucion) {
        this.fechaHoraDevolucion = fechaHoraDevolucion;
    }

    public double getKmRecorridos() {
        return kmRecorridos;
    }

    public void setKmRecorridos(double kmRecorridos) {
        this.kmRecorridos = kmRecorridos;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }


    @Override
    public String toString() {
        return "Devolucion{" +
                "id=" + id +
                ", idEntrega=" + idEntrega +
                ", idEmpleado=" + idEmpleado +
                ", fechaHoraDevolucion=" + fechaHoraDevolucion +
                ", kmRecorridos=" + kmRecorridos +
                ", costoTotal=" + costoTotal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Devolucion that = (Devolucion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



}