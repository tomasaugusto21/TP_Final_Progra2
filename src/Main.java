package Clases;

import Enums.EstadoReserva;
import Enums.EstadoVehiculo;
import Excepciones.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Sistema sistema = new Sistema();

        sistema.descargarBDD();

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===============================");
            System.out.println("      SISTEMA DE ALQUILER");
            System.out.println("===============================");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarse (Cliente)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese su username: ");
                        String user = scanner.nextLine();
                        System.out.print("Ingrese su contraseña: ");
                        String password = scanner.nextLine();

                        try {
                            sistema.loguearse(user, password);
                            System.out.println("¡Inicio de sesión exitoso!");


                            menuSegunRol(scanner, sistema);
                        } catch (LoginEx | ValidacionEx e) {
                            System.out.println("Error de login: " + e.getMessage());
                        }
                        break;

                    case 2:
                        try {
                            System.out.println("\n--- REGISTRO DE CLIENTE ---");
                            System.out.print("Nombre: ");
                            String nombre = scanner.nextLine();
                            System.out.print("Apellido: ");
                            String apellido = scanner.nextLine();
                            System.out.print("DNI: ");
                            int dni = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Username: ");
                            String username = scanner.nextLine();
                            System.out.print("Nro de Licencia: ");
                            String licencia = scanner.nextLine();
                            System.out.print("Teléfono: ");
                            String telefono = scanner.nextLine();
                            System.out.print("Domicilio: ");
                            String domicilio = scanner.nextLine();
                            System.out.print("Contraseña: ");
                            String contra = scanner.nextLine();

                            sistema.registrarse(nombre, apellido, dni, email, username, licencia, telefono, domicilio, contra);
                            System.out.println("¡Cliente registrado con éxito!");
                        } catch (Exception e) {
                            System.out.println("Error al registrarse: " + e.getMessage());
                        }
                        break;

                    case 0:
                        System.out.println("Saliendo del sistema...");
                        break;

                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida. Ingrese un número.");
                scanner.nextLine();
                opcion = -1;
            }
        } while (opcion != 0);

        scanner.close();
    }

    public static void menuSegunRol(Scanner scanner, Sistema sistema) {

        System.out.println("\n¿Qué menú desea desplegar?");
        System.out.println("1. Menú Cliente");
        System.out.println("2. Menú Empleado");
        System.out.println("3. Menú Administrador");
        System.out.print("Seleccione: ");
        int tipo = scanner.nextInt();
        scanner.nextLine();

        switch (tipo) {
            case 1:
                menuCliente(scanner, sistema);
                break;
            case 2:
                menuEmpleado(scanner, sistema);
                break;
            case 3:
                menuAdministrador(scanner, sistema);
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    public static void menuCliente(Scanner scanner, Sistema sistema) {
        int opcionCliente;
        do {
            System.out.println("\n========== MENÚ CLIENTE ==========");
            System.out.println("1. Ver vehículos");
            System.out.println("2. Ver autos");
            System.out.println("3. Ver vehículos de carga");
            System.out.println("4. Ver vehículos disponibles");
            System.out.println("5. Consultar vehículo");
            System.out.println("6. Crear reserva");
            System.out.println("7. Ver mis reservas");
            System.out.println("8. Modificar una reserva (Hora/Fecha)");
            System.out.println("9. Ver mi historial de alquileres");
            System.out.println("10. Modificar mis datos (Teléfono/Domicilio)");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            opcionCliente = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcionCliente) {
                    case 1:
                        System.out.println(sistema.verVehiculos());
                        break;
                    case 2:
                        System.out.println(sistema.verAutos());
                        break;
                    case 3:
                        System.out.println(sistema.verVehiculosDeCarga());
                        break;
                    case 4:
                        System.out.println(sistema.listarDisponibles());
                        break;
                    case 5:
                        System.out.print("Ingrese la patente del vehículo a consultar: ");
                        String patente = scanner.nextLine();
                        System.out.println("Estado: " + sistema.consultarEstadoVehiculo(patente));
                        System.out.println(sistema.obtenerVehiculoPorPatente(patente));
                        break;
                    case 6:
                        System.out.print("ID Cliente: ");
                        int idCliente = scanner.nextInt();
                        System.out.print("ID Vehículo: ");
                        int idVehiculo = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Fecha Inicio (dd/MM/yyyy): ");
                        String fechaInicio = scanner.nextLine();
                        System.out.print("Hora Inicio (HH:mm): ");
                        String horaInicio = scanner.nextLine();
                        System.out.print("Fecha Fin (dd/MM/yyyy): ");
                        String fechaFin = scanner.nextLine();
                        System.out.print("Hora Fin (HH:mm): ");
                        String horaFin = scanner.nextLine();

                        boolean creada = sistema.crearReserva(idCliente,idVehiculo,fechaInicio,horaInicio,fechaFin,horaFin);

                        if (creada) {
                            System.out.println("Reserva creada con éxito.");
                        } else {
                            System.out.println("No se pudo crear la reserva.");
                        }
                        break;
                    case 7:
                        System.out.print("Ingrese su ID de cliente: ");
                        int idClienteReserva = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println(sistema.listarReservaDeCliente(idClienteReserva));
                        break;
                    case 8:
                        System.out.print("ID de la reserva a modificar: ");
                        int idReserva = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("¿Qué desea modificar?");
                        System.out.println("1. Hora de inicio");
                        System.out.println("2. Hora de fin");
                        System.out.println("3. Fecha de inicio");
                        System.out.println("4. Fecha de fin");
                        System.out.print("Seleccione una opción: ");
                        int opcionAModificar = scanner.nextInt();
                        scanner.nextLine();

                        if (opcionAModificar == 1) {
                            System.out.print("Ingrese la nueva hora de inicio (HH:mm): ");
                            String nuevaHoraInicio = scanner.nextLine();
                            sistema.actualizarHoraInicioReserva(idReserva, nuevaHoraInicio);
                            System.out.println("Modificación de hora de inicio intentada.");
                        } else {
                            if (opcionAModificar == 2) {
                                System.out.print("Ingrese la nueva hora de fin (HH:mm): ");
                                String nuevaHoraFin = scanner.nextLine();
                                sistema.actualizarHoraFinReserva(idReserva, nuevaHoraFin);
                                System.out.println("Modificación de hora de fin intentada.");
                            } else {
                                if (opcionAModificar == 3) {
                                    System.out.print("Ingrese la nueva fecha de inicio (dd/MM/yyyy): ");
                                    String nuevaFechaInicio = scanner.nextLine();
                                    sistema.actualizarFechaInicioReserva(idReserva, nuevaFechaInicio);
                                    System.out.println("Modificación de fecha de inicio intentada.");
                                } else {
                                    if (opcionAModificar == 4) {
                                        System.out.print("Ingrese la nueva fecha de fin (dd/MM/yyyy): ");
                                        String nuevaFechaFin = scanner.nextLine();
                                        sistema.actualizarFechaFinReserva(idReserva, nuevaFechaFin);
                                        System.out.println("Modificación de fecha de fin intentada.");
                                    } else {
                                        System.out.println("Opción no válida.");
                                    }
                                }
                            }
                        }
                        break;
                    case 9:
                        System.out.print("Ingrese su ID de cliente: ");
                        int idClienteHistorial = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println(sistema.verHistorialDeClienteEntregas(idClienteHistorial));
                        break;
                    case 10:
                        System.out.print("Ingrese su ID de cliente: ");
                        int idClienteDatos = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Nuevo teléfono: ");
                        String nuevoTel = scanner.nextLine();
                        sistema.actualizarTelefonoCliente(idClienteDatos, nuevoTel);
                        System.out.print("Nuevo domicilio: ");
                        String nuevoDom = scanner.nextLine();
                        sistema.actualizarDomicilioCliente(idClienteDatos, nuevoDom);
                        System.out.println("Datos actualizados.");
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error de ejecución: " + e.getMessage());
            }
        } while (opcionCliente != 0);
    }
    public static void menuEmpleado(Scanner scanner, Sistema sistema) {
        int opcion;
        do {
            System.out.println("\n========== MENÚ EMPLEADO ==========");
            System.out.println("1. Ver vehículos");
            System.out.println("2. Ver vehículos disponibles");
            System.out.println("3. Consultar vehículo");
            System.out.println("4. Ver clientes");
            System.out.println("5. Buscar cliente");
            System.out.println("6. Ver reservas (Actualizar expiradas)");
            System.out.println("7. Buscar cruce de fechas en vehículo");
            System.out.println("8. Actualizar estado de reserva");
            System.out.println("9. Actualizar reservas expiradas manualmente");
            System.out.println("10. Registrar entrega");
            System.out.println("11. Registrar devolución");
            System.out.println("12. Ver entregas activas");
            System.out.println("13. Ver todas las entregas");
            System.out.println("14. Ver todas las devoluciones");
            System.out.println("15. Modificar fecha de devolución prevista (Entrega)");
            System.out.println("16. Actualizar empleado de entrega");
            System.out.println("17. Actualizar empleado de devolución");
            System.out.println("18. Cambiar estado de vehículo");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcion) {
                    case 1:
                        System.out.println(sistema.verVehiculos());
                        break;
                    case 2:
                        System.out.println(sistema.listarDisponibles());
                        break;
                    case 3:
                        System.out.print("Patente: ");
                        String pat = scanner.nextLine();
                        System.out.println(sistema.obtenerVehiculoPorPatente(pat));
                        break;
                    case 4:
                        System.out.println(sistema.verClientes());
                        break;
                    case 5:
                        System.out.print("ID del cliente a buscar: ");
                        int idClienteBuscado = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println(sistema.buscarCliente(idClienteBuscado));
                        break;
                    case 6:
                        int cant = sistema.actualizarReservasExpiradas();
                        System.out.println("Se actualizaron " + cant + " reservas expiradas.");
                        break;
                    case 7:
                        System.out.print("Patente del vehículo: ");
                        String patVehiculo = scanner.nextLine();
                        break;
                    case 8:
                        System.out.print("ID Reserva: ");
                        int idReserv = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Nuevo estado (VIGENTE, ANULADA, FINALIZADA, EXPIRADA): ");
                        String estado = scanner.nextLine().toUpperCase();
                        sistema.actualizarEstadoDeReserva(idReserv, EstadoReserva.valueOf(estado));
                        System.out.println("Estado de reserva actualizado.");
                        break;
                    //me comi el case 9 lo hago desp
                        case 10:
                        System.out.print("ID Cliente: ");
                        int cliEnt = scanner.nextInt();
                        System.out.print("ID Vehículo: ");
                        int vehEnt = scanner.nextInt();
                        System.out.print("ID Empleado: ");
                        int empEnt = scanner.nextInt();
                        System.out.print("ID Reserva (0 si no tiene): ");
                        int resEnt = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Fecha devolución prevista (yyyy-MM-dd HH:mm): ");
                        String fDevPrev = scanner.nextLine();
                        System.out.print("Kg transportados (0 si es auto): ");
                        double kgTrans = scanner.nextDouble();
                        scanner.nextLine();

                        //if else
                    case 11:
                        System.out.print("ID Entrega: ");
                        int idEntDev = scanner.nextInt();
                        System.out.print("ID Empleado: ");
                        int idEmpDev = scanner.nextInt();
                        System.out.print("Kilometraje final: ");
                        double kmFin = scanner.nextDouble();
                        scanner.nextLine();

                       //if else
                    case 12:
                        System.out.println(sistema.listarEntregasActivas());
                        break;
                    case 13:
                        System.out.println(sistema.verEntregas());
                        break;
                    case 14:
                        System.out.println(sistema.verDevoluciones());
                        break;
                    case 15:
                        System.out.print("ID Entrega: ");
                        int idEntMod = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Nueva fecha prevista (yyyy-MM-dd HH:mm): ");
                        String nuevaF = scanner.nextLine();
                        sistema.actualizarFechaDevolucionPrevistaEntrega(idEntMod, nuevaF);
                        System.out.println("Fecha prevista actualizada.");
                        break;
                    case 16:
                        System.out.print("ID Entrega: ");
                        int idEntEmp = scanner.nextInt();
                        System.out.print("Nuevo ID Empleado: ");
                        int nuevoEmp = scanner.nextInt();
                        scanner.nextLine();
                        sistema.actualizarEmpleadoDeEntrega(idEntEmp, nuevoEmp);
                        System.out.println("Empleado de entrega actualizado.");
                        break;
                    case 17:
                        System.out.print("ID Devolución: ");
                        int idDevEmp = scanner.nextInt();
                        System.out.print("Nuevo ID Empleado: ");
                        int nuevoEmpDev = scanner.nextInt();
                        scanner.nextLine();
                        sistema.actualizarEmpleadoDeDevolucion(idDevEmp, nuevoEmpDev);
                        System.out.println("Empleado de devolución actualizado.");
                        break;
                    case 18:
                        System.out.print("Patente del vehículo: ");
                        String patEst = scanner.nextLine();
                        System.out.print("Nuevo estado (DISPONIBLE, EN_REPARACION, EN_LIMPIEZA, FUERA_DE_SERVICIO): ");
                        String estVeh = scanner.nextLine().toUpperCase();
                        sistema.cambiarEstadoVehiculo(patEst, EstadoVehiculo.valueOf(estVeh));
                        System.out.println("Estado del vehículo modificado.");
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error de ejecución: " + e.getMessage());
            }
        } while (opcion != 0);
    }
}