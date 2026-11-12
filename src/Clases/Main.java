package Clases;

import Enums.EstadoReserva;
import Enums.EstadoVehiculo;
import Enums.RolDeUsuario;
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

        RolDeUsuario rol = sistema.getRolLogueado(); //revisar
        if (rol == null) {
            System.out.println("No hay un rol válido asociado a la sesión.");
            return;
        }

        switch (rol) {
            case CLIENTE:
                menuCliente(scanner, sistema);
                break;
            case EMPLEADO:
                menuEmpleado(scanner, sistema);
                break;
            case ADMINISTRADOR:
                menuAdministrador(scanner, sistema);
                break;
            default:
                System.out.println("Rol desconocido.");
        }

        sistema.cerrarSesion();//revisar
        System.out.println("Sesión cerrada correctamente.");
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

                        boolean creada = sistema.crearReserva(idCliente,idVehiculo,horaInicio,horaFin,fechaInicio,fechaFin);

                        if (creada) {
                            System.out.println("Reserva creada con éxito.");
                        } else {
                            System.out.println("No se pudo crear la reserva (fecha u hora invalidas o auto no disponible)");
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

                        boolean modificacionOK = false;

                        switch (opcionAModificar) {
                            case 1:
                                System.out.print("Ingrese la nueva hora de inicio (HH:mm): ");
                                String nuevaHoraInicio = scanner.nextLine();
                                modificacionOK = sistema.actualizarHoraInicioReserva(idReserva, nuevaHoraInicio);
                                break;
                            case 2:
                                System.out.print("Ingrese la nueva hora de fin (HH:mm): ");
                                String nuevaHoraFin = scanner.nextLine();
                                modificacionOK = sistema.actualizarHoraFinReserva(idReserva, nuevaHoraFin);
                                break;
                            case 3:
                                System.out.print("Ingrese la nueva fecha de inicio (dd/MM/yyyy): ");
                                String nuevaFechaInicio = scanner.nextLine();
                                modificacionOK = sistema.actualizarFechaInicioReserva(idReserva, nuevaFechaInicio);
                                break;
                            case 4:
                                System.out.print("Ingrese la nueva fecha de fin (dd/MM/yyyy): ");
                                String nuevaFechaFin = scanner.nextLine();
                                modificacionOK = sistema.actualizarFechaFinReserva(idReserva, nuevaFechaFin);
                                break;
                            default:
                                System.out.println("Opción no válida.");
                        }

                        if (modificacionOK) {
                            System.out.println("Modificación realizada con éxito.");
                        } else {
                            System.out.println("No se pudo realizar la modificación.");
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
                        boolean telefonoNuevoOK = sistema.actualizarTelefonoCliente(idClienteDatos, nuevoTel);

                        System.out.print("Nuevo domicilio: ");
                        String nuevoDomicilio = scanner.nextLine();
                        boolean domicilioNuevoOK = sistema.actualizarDomicilioCliente(idClienteDatos, nuevoDomicilio);

                        if (telefonoNuevoOK || domicilioNuevoOK) {
                            System.out.println("Datos actualizados con éxito.");
                        } else {
                            System.out.println("No se pudieron actualizar los datos.");
                        }
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
        int opcionEmpleado;
        do {
            System.out.println("\n========== MENÚ EMPLEADO ==========");
            System.out.println("1. Ver vehículos");
            System.out.println("2. Ver vehículos disponibles");
            System.out.println("3. Ver vehículos no disponibles");
            System.out.println("4. Consultar vehículo");
            System.out.println("5. Ver clientes");
            System.out.println("6. Buscar cliente");
            System.out.println("7. Registrar un nuevo cliente");
            System.out.println("8. Crear reserva para un cliente");
            System.out.println("9. Ver reservas y actualizar expiradas");
            System.out.println("10. Buscar cruce de fechas en vehículo");
            System.out.println("11. Actualizar estado de reserva");
            System.out.println("12. Registrar entrega");
            System.out.println("13. Registrar devolución");
            System.out.println("14. Ver entregas activas");
            System.out.println("15. Ver todas las entregas");
            System.out.println("16. Ver todas las devoluciones");
            System.out.println("17. Modificar fecha de devolución prevista (Entrega)");
            System.out.println("18. Actualizar empleado de entrega");
            System.out.println("19. Actualizar empleado de devolución");
            System.out.println("20. Cambiar estado de vehículo");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            opcionEmpleado = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcionEmpleado) {
                    case 1:
                        System.out.println(sistema.verVehiculos());
                        break;
                    case 2:
                        System.out.println(sistema.listarDisponibles());
                        break;
                    case 3:
                        System.out.println(sistema.listarNoDisponibles());
                    case 4:
                        System.out.print("Patente: ");
                        String pat = scanner.nextLine();
                        System.out.println(sistema.obtenerVehiculoPorPatente(pat));
                        break;
                    case 5:
                        System.out.println(sistema.verClientes());
                        break;
                    case 6:
                        System.out.print("ID del cliente a buscar: ");
                        int idClienteBuscado = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println(sistema.buscarCliente(idClienteBuscado));
                        break;
                    case 7:
                        System.out.println("\n--- REGISTRAR NUEVO CLIENTE ---");
                        System.out.print("Nombre: ");
                        String nom = scanner.nextLine();
                        System.out.print("Apellido: ");
                        String ape = scanner.nextLine();
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
                        String tel = scanner.nextLine();
                        System.out.print("Domicilio: ");
                        String dom = scanner.nextLine();
                        System.out.print("Contraseña: ");
                        String contra = scanner.nextLine();

                    case 8:
                        System.out.print("ID Cliente: ");
                        int idC = scanner.nextInt();
                        System.out.print("ID Vehículo: ");
                        int idV = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Fecha Inicio (dd/MM/yyyy): ");
                        String fIni = scanner.nextLine();
                        System.out.print("Hora Inicio (HH:mm): ");
                        String hIni = scanner.nextLine();
                        System.out.print("Fecha Fin (dd/MM/yyyy): ");
                        String fFin = scanner.nextLine();
                        System.out.print("Hora Fin (HH:mm): ");
                        String hFin = scanner.nextLine();

                        boolean creadaEmp = sistema.crearReserva(idC, idV, hIni, hFin, fIni, fFin);
                        if (creadaEmp) {
                            System.out.println("Reserva creada con éxito para el cliente.");
                        } else {
                            System.out.println("No se pudo crear la reserva.");
                        }
                        break;
                    case 9:
                        int cantExpiradas = sistema.actualizarReservasExpiradas();
                        System.out.println("Se actualizaron " + cantExpiradas + " reservas expiradas.");
                        break;
                    case 10:
                        System.out.print("Patente del vehículo: ");
                        String pVeh = scanner.nextLine();
                        System.out.println(sistema.consultarEstadoVehiculo(pVeh));
                        break;
                    case 11:
                        System.out.print("ID Reserva: ");
                        int idR = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Nuevo estado (VIGENTE, ANULADA, FINALIZADA, EXPIRADA): ");
                        String estStr = scanner.nextLine().toUpperCase();//para que sea si o si mayuscula
                        boolean okEstRes = sistema.actualizarEstadoDeReserva(idR, EstadoReserva.valueOf(estStr));
                        if (okEstRes){
                            System.out.println("Estado de reserva actualizado.");
                        } else {
                            System.out.println("No se pudo actualizar el estado de la reserva.");
                        }
                        break;
                    case 12:
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

                        boolean okEntrega = sistema.registrarEntrega(cliEnt, vehEnt, empEnt, resEnt, fDevPrev, kgTrans);
                        if (okEntrega) {
                            System.out.println("Entrega registrada con éxito.");
                        } else {
                            System.out.println("Error al registrar entrega.");
                        }
                        break;
                    case 13:
                        System.out.print("ID Entrega: ");
                        int idEntDev = scanner.nextInt();
                        System.out.print("ID Empleado: ");
                        int idEmpDev = scanner.nextInt();
                        System.out.print("Kilometraje final: ");
                        double kmFin = scanner.nextDouble();
                        scanner.nextLine();

                        boolean okDevolucion = sistema.registrarDevolucion(idEntDev, idEmpDev, kmFin);
                        if (okDevolucion) {
                            System.out.println("Devolución registrada con éxito.");
                        } else {
                            System.out.println("Error al registrar devolución.");
                        }
                        break;
                    case 14:
                        System.out.println(sistema.listarEntregasActivas());
                        break;
                    case 15:
                        System.out.println(sistema.verEntregas());
                        break;
                    case 16:
                        System.out.println(sistema.verDevoluciones());
                        break;
                    case 17:
                        System.out.print("ID Entrega: ");
                        int idEntMod = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Nueva fecha prevista (yyyy-MM-dd HH:mm): ");
                        String nuevaF = scanner.nextLine();
                        boolean okFechaPrevista = sistema.actualizarFechaDevolucionPrevistaEntrega(idEntMod, nuevaF);
                        if (okFechaPrevista) {
                            System.out.println("Fecha prevista actualizada.");
                        }else {
                            System.out.println("No se pudo actualizar la fecha.");
                        }
                        break;
                    case 18:
                        System.out.print("ID Entrega: ");
                        int idEntEmp = scanner.nextInt();
                        System.out.print("Nuevo ID Empleado: ");
                        int nuevoEmp = scanner.nextInt();
                        scanner.nextLine();
                        boolean okEmpEnt = sistema.actualizarEmpleadoDeEntrega(idEntEmp, nuevoEmp);
                        if (okEmpEnt){
                            System.out.println("Empleado de entrega actualizado.");
                        } else {
                            System.out.println("Error al actualizar empleado.");
                        }
                        break;
                    case 19:
                        System.out.print("ID Devolución: ");
                        int idDevEmp = scanner.nextInt();
                        System.out.print("Nuevo ID Empleado: ");
                        int nuevoEmpDev = scanner.nextInt();
                        scanner.nextLine();
                        boolean okEmpDev = sistema.actualizarEmpleadoDeDevolucion(idDevEmp, nuevoEmpDev);
                        if (okEmpDev){
                            System.out.println("Empleado de devolución actualizado.");
                        } else {
                            System.out.println("Error al actualizar empleado.");
                        }
                        break;
                    case 20:
                        System.out.print("Patente del vehículo: ");
                        String patEst = scanner.nextLine();
                        System.out.print("Nuevo estado (DISPONIBLE, EN_REPARACION, EN_LIMPIEZA, FUERA_DE_SERVICIO): ");
                        String estVeh = scanner.nextLine().toUpperCase();
                        boolean okCambioEst = sistema.cambiarEstadoVehiculo(patEst, EstadoVehiculo.valueOf(estVeh));
                        if (okCambioEst){
                            System.out.println("Estado del vehículo modificado.");
                        } else{
                            System.out.println("No se pudo cambiar el estado del vehículo.");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error de ejecución: " + e.getMessage());
            }
        } while (opcionEmpleado != 0);
    }

    public static void menuAdministrador(Scanner scanner, Sistema sistema) {
        int opcionAdministrador;
        do {
            System.out.println("\n========== MENÚ ADMINISTRADOR ==========");
            System.out.println("1. Gestionar vehículos (Agregar autos/carga, actualizar precios)");
            System.out.println("2. Gestionar usuarios (Crear empleado, ver empleados/clientes)");
            System.out.println("3. Ver reportes generales de reservas y entregas");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            opcionAdministrador = scanner.nextInt();
            scanner.nextLine();

            switch (opcionAdministrador) {
                case 1:
                    menuGestionVehiculos(scanner, sistema);
                    break;
                case 2:
                    try {
                        System.out.println("\n--- GESTIÓN DE USUARIOS ---");
                        System.out.println("1. Crear Empleado");
                        System.out.println("2. Ver Empleados");
                        System.out.println("3. Ver Clientes");
                        int opcionUsuario = scanner.nextInt();
                        scanner.nextLine();

                        if (opcionUsuario == 1) {
                            System.out.print("Nombre: ");
                            String name = scanner.nextLine();
                            System.out.print("Apellido: ");
                            String lastName = scanner.nextLine();
                            System.out.print("DNI: ");
                            int dni = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Username: ");
                            String username = scanner.nextLine();
                            System.out.print("Legajo: ");
                            String legajo = scanner.nextLine();
                            System.out.print("Contraseña: ");
                            String password = scanner.nextLine();

                            sistema.crearEmpleado(name,lastName,dni,email,username,legajo,password);
                            System.out.println("¡Empleado creado con éxito!");
                        } else if (opcionUsuario == 2) {
                            System.out.println(sistema.verEmpleados());
                        } else if (opcionUsuario == 3) {
                            System.out.println(sistema.verClientes());
                        }
                    } catch (Exception e) {
                        System.out.println("Error en gestión de usuarios: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        System.out.println("\n--- REPORTES GENERALES ---");
                        System.out.println("Entregas registradas: " + sistema.verEntregas());
                        System.out.println("Devoluciones registradas: " + sistema.verDevoluciones());
                    } catch (Exception e) {
                        System.out.println("Error al obtener reportes: " + e.getMessage());
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcionAdministrador != 0);
    }

    public static void menuGestionVehiculos(Scanner scanner, Sistema sistema) {
        int opcionVehiculo;
        do {
            System.out.println("\n--- GESTIONAR VEHÍCULOS ---");
            System.out.println("1. Agregar Auto al sistema");
            System.out.println("2. Agregar Vehículo de Carga al sistema");
            System.out.println("3. Actualizar precio por hora");
            System.out.println("4. Actualizar rueda de auxilio (Auto)");
            System.out.println("5. Actualizar precio por kg (Carga)");
            System.out.println("6. Actualizar capacidad de carga");
            System.out.println("7. Cambiar estado de vehículo");
            System.out.println("8. Ver todos los vehículos");
            System.out.println("9. Ver autos");
            System.out.println("10. Ver vehículos de carga");
            System.out.println("11. Listar disponibles");
            System.out.println("12. Listar no disponibles");
            System.out.println("13. Obtener vehículo por patente");
            System.out.println("14. Consultar estado de vehículo");
            System.out.println("0. Volver al menú anterior");
            System.out.print("Seleccione una opción: ");

            opcionVehiculo = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcionVehiculo) {
                    case 1:
                        System.out.print("Patente: ");
                        String patA = scanner.nextLine();
                        System.out.print("Marca: ");
                        String marA = scanner.nextLine();
                        System.out.print("Modelo: ");
                        String modA = scanner.nextLine();
                        System.out.print("Año: ");
                        int anioA = scanner.nextInt();
                        System.out.print("Kilometraje: ");
                        double kmA = scanner.nextDouble();
                        System.out.print("Precio por hora: ");
                        double precA = scanner.nextDouble();
                        System.out.print("Cantidad de puertas: ");
                        int ptoA = scanner.nextInt();
                        System.out.print("¿Tiene rueda de auxilio? (true/false): ");
                        boolean auxA = scanner.nextBoolean();
                        scanner.nextLine();

                        sistema.agregarAutoAlSistema(patA, marA, modA, anioA, kmA, precA, ptoA, auxA);
                        System.out.println("Auto agregado con exito.");
                        break;
                    case 2:
                        System.out.print("Patente: ");
                        String patCarga = scanner.nextLine();
                        System.out.print("Marca: ");
                        String marCarga = scanner.nextLine();
                        System.out.print("Modelo: ");
                        String modCarga = scanner.nextLine();
                        System.out.print("Año: ");
                        int anioCarga = scanner.nextInt();
                        System.out.print("Kilometraje: ");
                        double kmCarga = scanner.nextDouble();
                        System.out.print("Precio por hora: ");
                        double precCarga = scanner.nextDouble();
                        System.out.print("Capacidad de carga (kg): ");
                        double capCarga = scanner.nextDouble();
                        System.out.print("Precio por kg: ");
                        double precKgCarga = scanner.nextDouble();
                        scanner.nextLine();

                        boolean okCarga = sistema.agregarVehiculoDeCargaAlSistema(patCarga,marCarga,modCarga,anioCarga,kmCarga,precCarga,capCarga,precKgCarga);
                        if (okCarga){
                            System.out.println("¡Vehículo de carga agregado con éxito!");
                        } else {
                            System.out.println("No se pudo agregar el vehículo de carga.");
                        }
                        break;
                    case 3:
                        System.out.print("Patente del vehículo: ");
                        String patP = scanner.nextLine();
                        System.out.print("Nuevo precio por hora: ");
                        double nuevoPrecio = scanner.nextDouble();
                        scanner.nextLine();
                        boolean okPrecio = sistema.actualizarPrecioPorHora(patP,nuevoPrecio);
                        if(okPrecio){
                            System.out.println("Precio actualizado con exito.");
                        }else{
                            System.out.println(" no se pudo actualizar el precio.");
                        }
                        break;
                    case 4:
                        System.out.print("Patente del auto: ");
                        String patAux = scanner.nextLine();
                        System.out.print("Tiene rueda de auxilio (true/false): ");
                        boolean valAux = scanner.nextBoolean();
                        scanner.nextLine();
                        boolean okAuxiliar = sistema.actualizarRuedaDeAuxilio(patAux,valAux);
                        if(okAuxiliar){
                            System.out.println("Rueda de auxilio actualizada");
                        }else{
                            System.out.println("No se pudo actualizar.");
                        }
                        break;
                    case 5:
                        System.out.print("Patente vehículo de carga: ");
                        String patKg = scanner.nextLine();
                        System.out.print("Nuevo precio por kg: ");
                        double nuevoPrecKg = scanner.nextDouble();
                        scanner.nextLine();
                        boolean okPrecioCarga = sistema.actualizarPrecioPorKg(patKg,nuevoPrecKg);
                        if(okPrecioCarga){
                            System.out.println("Precio por carga actualizado.");
                        }else{
                            System.out.println("No se pudo actualizar.");
                        }
                        break;
                    case 6:
                        System.out.print("Patente vehículo de carga: ");
                        String patCap = scanner.nextLine();
                        System.out.print("Nueva capacidad (kg): ");
                        double nuevaCap = scanner.nextDouble();
                        scanner.nextLine();
                        boolean okCapacidad = sistema.actualizarCapacidadDeCarga(patCap,nuevaCap);
                        if(okCapacidad){
                            System.out.println("Capacidad de carga actualizada");
                        }else{
                            System.out.println("No se pudo actualizar.");
                        }
                        break;
                    case 7:
                        System.out.print("Patente: ");
                        String patEstado = scanner.nextLine();
                        System.out.print("Nuevo estado (DISPONIBLE, EN_REPARACION, EN_LIMPIEZA, FUERA_DE_SERVICIO): ");
                        String estadoNew = scanner.nextLine().toUpperCase();//para que lo transforme a mayuscula si lo necesita
                       boolean okCambioEstado = sistema.cambiarEstadoVehiculo(patEstado,EstadoVehiculo.valueOf(estadoNew));
                       if(okCambioEstado){
                           System.out.println("Estado cambiado con exito.");
                       }else{
                           System.out.println("No se pudo cambiar el estado.");
                       }
                        break;
                    case 8:
                        System.out.println(sistema.verVehiculos());
                        break;
                    case 9:
                        System.out.println(sistema.verAutos());
                        break;
                    case 10:
                        System.out.println(sistema.verVehiculosDeCarga());
                        break;
                    case 11:
                        System.out.println(sistema.listarDisponibles());
                        break;
                    case 12:
                        System.out.println(sistema.listarNoDisponibles());
                        break;
                    case 13:
                        System.out.print("Patente a buscar: ");
                        String patBuscada = scanner.nextLine();
                        System.out.println(sistema.obtenerVehiculoPorPatente(patBuscada));
                        break;
                    case 14:
                        System.out.print("Patente a consultar: ");
                        String patConsulta = scanner.nextLine();
                        System.out.println("Estado: " + sistema.consultarEstadoVehiculo(patConsulta));
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error en gestión de vehículos: " + e.getMessage());
            }
        } while (opcionVehiculo != 0);
    }
}
