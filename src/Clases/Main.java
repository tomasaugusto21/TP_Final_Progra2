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

                            if (user.matches(".*\\d.*")) {
                                throw new ValidacionEx("El username no puede contener números");
                            }

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
                            String dni = scanner.nextLine();
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

                            if (nombre.matches(".*\\d.*")) {
                                throw new ValidacionEx("El nombre no puede contener números");
                            }

                            if (apellido.matches(".*\\d.*")) {
                                throw new ValidacionEx("El apellido no puede contener números");
                            }

                            if (username.matches(".*\\d.*")) {
                                throw new ValidacionEx("El username no puede contener números");
                            }

                            if (!dni.matches("\\d+")) {
                                throw new ValidacionEx("El DNI solo puede contener números");
                            }

                            if (!telefono.matches("\\d+")) {
                                throw new ValidacionEx("El teléfono solo puede contener números");
                            }

                            if (!licencia.matches("\\d+")) {
                                throw new ValidacionEx("La licencia solo puede contener números");
                            }

                            int dniNumero = Integer.parseInt(dni);

                            sistema.registrarse(nombre, apellido, dniNumero, email, username, licencia, telefono, domicilio, contra);
                            System.out.println("¡Cliente registrado con éxito!");
                        } catch (ErrorDeCargaEx | ObjetoInvalidoEx | ObjetoDuplicadoEx | ValidacionEx e) {
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
        int opcionCliente = -1;
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

            String opcion = scanner.nextLine();

            try {

                if (!opcion.matches("\\d+")) {
                    throw new ValidacionEx("La opción debe ser un número");
                }

                opcionCliente = Integer.parseInt(opcion);

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
                        System.out.print("ID Vehículo: ");
                        String idV = scanner.nextLine();
                        if (!idV.matches("\\d+")) {
                            throw new ValidacionEx("El ID del vehículo debe contener números");
                        }
                        int idVehiculo = Integer.parseInt(idV);

                        System.out.print("Fecha Inicio (dd/MM/yyyy): ");
                        String fechaInicio = scanner.nextLine();
                        System.out.print("Hora Inicio (HH:mm): ");
                        String horaInicio = scanner.nextLine();
                        System.out.print("Fecha Fin (dd/MM/yyyy): ");
                        String fechaFin = scanner.nextLine();
                        System.out.print("Hora Fin (HH:mm): ");
                        String horaFin = scanner.nextLine();


                        int idCliente = sistema.getIdLogueado();

                        boolean creada = sistema.crearReserva(idCliente,idVehiculo,horaInicio,horaFin,fechaInicio,fechaFin);

                        if (creada) {
                            System.out.println("Reserva creada con éxito.");
                        } else {
                            System.out.println("No se pudo crear la reserva (fecha u hora invalidas o auto no disponible)");
                        }
                        break;
                    case 7:
                        int idClienteReserva = sistema.getIdLogueado();
                        System.out.println(sistema.listarReservaDeCliente(idClienteReserva));
                        break;
                    case 8:
                        System.out.print("ID de la reserva a modificar: ");
                        String idReservaString = scanner.nextLine();

                        if (!idReservaString.matches("\\d+")){
                            throw new ValidacionEx("El id de la reserva no debe contener letras");
                        }

                        int idReserva = Integer.parseInt(idReservaString);

                        System.out.println("¿Qué desea modificar?");
                        System.out.println("1. Hora de inicio");
                        System.out.println("2. Hora de fin");
                        System.out.println("3. Fecha de inicio");
                        System.out.println("4. Fecha de fin");
                        System.out.print("Seleccione una opción: ");
                        String opcionAModificarString = scanner.nextLine();

                        if (!opcionAModificarString.matches("\\d+")){
                            throw new ValidacionEx("Porfavor, ingrese una opcion valida entre (1-2-3-4)");
                        }

                        int opcionAModificar = Integer.parseInt(opcionAModificarString);

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
                        int idClienteHistorial = sistema.getIdLogueado();
                        System.out.println(sistema.verHistorialDeClienteEntregas(idClienteHistorial));
                        break;
                    case 10:

                        System.out.println("Seleccione la opcion que desee cambiar (1.Telefono/2.Domicilio)");
                        String opcionTelDomString = scanner.nextLine();

                        if (!opcionTelDomString.matches("\\d+")){
                            throw new ValidacionEx("Se selecciono una opcion invalida. Por favor seleccione 1.Telefono o 2.Domicilio");
                        }

                        int opcionTelDom = Integer.parseInt(opcionTelDomString);

                        boolean telefonoNuevoOK = false;
                        boolean domicilioNuevoOK = false;

                        int idClienteDatos = sistema.getIdLogueado();

                        if (opcionTelDom == 1){

                            System.out.print("Nuevo teléfono: ");
                            String nuevoTel = scanner.nextLine();

                            if (!nuevoTel.matches("\\d+")){
                                throw new ValidacionEx("El nuevo telefono debe contener solo numeros");
                            }

                            telefonoNuevoOK = sistema.actualizarTelefonoCliente(idClienteDatos, nuevoTel);


                        }else if (opcionTelDom == 2){

                            System.out.print("Nuevo domicilio: ");
                            String nuevoDomicilio = scanner.nextLine();
                            domicilioNuevoOK = sistema.actualizarDomicilioCliente(idClienteDatos, nuevoDomicilio);

                        }

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
            } catch (ObjetoNoEncontradoEx | ObjetoInvalidoEx | ObjetoDuplicadoEx | ValidacionEx | PermisosEx e) {
                System.out.println("Error de ejecución: " + e.getMessage());
            }
        } while (opcionCliente != 0);
    }
    public static void menuEmpleado(Scanner scanner, Sistema sistema) {
        int opcionEmpleado = -1;
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
            String opcionEmpleadoString = scanner.nextLine();

            try {

                if (!opcionEmpleadoString.matches("\\d+")){
                    throw new ValidacionEx("La opcion debe ser un numero.");
                }

                opcionEmpleado = Integer.parseInt(opcionEmpleadoString);

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
                        System.out.println("\n--- REGISTRO NUEVO CLIENTE ---");
                        System.out.print("Nombre: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Apellido: ");
                        String apellido = scanner.nextLine();
                        System.out.print("DNI: ");
                        String dni = scanner.nextLine();
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

                        if (nombre.matches(".*\\d.*")) {
                            throw new ValidacionEx("El nombre no puede contener números");
                        }

                        if (apellido.matches(".*\\d.*")) {
                            throw new ValidacionEx("El apellido no puede contener números");
                        }

                        if (username.matches(".*\\d.*")) {
                            throw new ValidacionEx("El username no puede contener números");
                        }

                        if (!dni.matches("\\d+")) {
                            throw new ValidacionEx("El DNI solo puede contener números");
                        }

                        if (!telefono.matches("\\d+")) {
                            throw new ValidacionEx("El teléfono solo puede contener números");
                        }

                        if (!licencia.matches("\\d+")) {
                            throw new ValidacionEx("La licencia solo puede contener números");
                        }

                        int dniNumero = Integer.parseInt(dni);

                        sistema.registrarse(nombre, apellido, dniNumero, email, username, licencia, telefono, domicilio, contra);
                        System.out.println("¡Cliente registrado con éxito!");

                    case 8:
                        System.out.print("ID Cliente: ");
                        String idCliente = scanner.nextLine();

                        if (!idCliente.matches("\\d+")) {
                            throw new ValidacionEx("El ID del cliente solo puede contener números");
                        }

                        int idC = Integer.parseInt(idCliente);


                        System.out.print("ID Vehículo: ");
                        String idVehiculo = scanner.nextLine();

                        if (!idVehiculo.matches("\\d+")) {
                            throw new ValidacionEx("El ID del vehículo solo puede contener números");
                        }

                        int idV = Integer.parseInt(idVehiculo);


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
                        System.out.print("Nuevo estado (1.VIGENTE 2.ANULADA 3.FINALIZADA 4.EXPIRADA): ");
                        String estStr = scanner.nextLine();

                        if (!estStr.matches("\\d+")) {
                            throw new ValidacionEx("Opcion invalida. Por favor seleccione una entre (1, 2, 3, 4)");
                        }

                        int opcionEstado = Integer.parseInt(estStr);

                        boolean okEstRes = false;

                        if (opcionEstado == 1){
                            okEstRes = sistema.actualizarEstadoDeReserva(idR, EstadoReserva.VIGENTE);
                        }else if (opcionEstado == 2){
                            okEstRes = sistema.actualizarEstadoDeReserva(idR, EstadoReserva.ANULADA);
                        }else if(opcionEstado == 3){
                            okEstRes = sistema.actualizarEstadoDeReserva(idR,EstadoReserva.FINALIZADA);
                        }else if(opcionEstado == 4){
                            okEstRes = sistema.actualizarEstadoDeReserva(idR,EstadoReserva.EXPIRADA);
                        }

                        if (okEstRes){
                            System.out.println("Estado de reserva actualizado.");
                        } else {
                            System.out.println("No se pudo actualizar el estado de la reserva.");
                        }
                        break;
                    case 12:
                        System.out.print("ID Cliente: ");
                        String cli = scanner.nextLine();

                        if (!cli.matches("\\d+")) {
                            throw new ValidacionEx("El ID del cliente solo puede contener números");
                        }

                        int cliEnt = Integer.parseInt(cli);


                        System.out.print("ID Vehículo: ");
                        String veh = scanner.nextLine();

                        if (!veh.matches("\\d+")) {
                            throw new ValidacionEx("El ID del vehículo solo puede contener números");
                        }

                        int vehEnt = Integer.parseInt(veh);


                        System.out.print("ID Empleado: ");
                        String emp = scanner.nextLine();

                        if (!emp.matches("\\d+")) {
                            throw new ValidacionEx("El ID del empleado solo puede contener números");
                        }

                        int empEnt = Integer.parseInt(emp);


                        System.out.print("ID Reserva (0 si no tiene): ");
                        String res = scanner.nextLine();

                        if (!res.matches("\\d+")) {
                            throw new ValidacionEx("El ID de reserva solo puede contener números");
                        }

                        int resEnt = Integer.parseInt(res);


                        System.out.print("Fecha devolución prevista (yyyy-MM-dd HH:mm): ");
                        String fDevPrev = scanner.nextLine();


                        System.out.print("Kg transportados (0 si es auto): ");
                        String kg = scanner.nextLine();

                        if (!kg.matches("\\d+(\\.\\d+)?")) {
                            throw new ValidacionEx("Los kilogramos deben ser un número válido");
                        }

                        double kgTrans = Double.parseDouble(kg);

                        boolean okEntrega = sistema.registrarEntrega(cliEnt, vehEnt, empEnt, resEnt, fDevPrev, kgTrans);
                        if (okEntrega) {
                            System.out.println("Entrega registrada con éxito.");
                        } else {
                            System.out.println("Error al registrar entrega.");
                        }
                        break;
                    case 13:
                        System.out.print("ID Entrega: ");
                        String idEntrega = scanner.nextLine();

                        if (!idEntrega.matches("\\d+")) {
                            throw new ValidacionEx("El ID de entrega solo puede contener números");
                        }

                        int idEntDev = Integer.parseInt(idEntrega);


                        System.out.print("ID Empleado: ");
                        String idEmpleado = scanner.nextLine();

                        if (!idEmpleado.matches("\\d+")) {
                            throw new ValidacionEx("El ID del empleado solo puede contener números");
                        }

                        int idEmpDev = Integer.parseInt(idEmpleado);


                        System.out.print("Kilometraje final: ");
                        String km = scanner.nextLine();

                        if (!km.matches("\\d+(\\.\\d+)?")) {
                            throw new ValidacionEx("El kilometraje debe ser un número válido");
                        }

                        double kmFin = Double.parseDouble(km);

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
                        String idEntModString = scanner.nextLine();

                        if (!idEntModString.matches("\\d+")){
                            throw new ValidacionEx("El id de la entrega debe ser un numero.");
                        }
                        int idEntMod = Integer.parseInt(idEntModString);
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
                        String idEntregaString = scanner.nextLine();

                        if (!idEntregaString.matches("\\d+")) {
                            throw new ValidacionEx("El ID de entrega solo puede contener números");
                        }

                        int idEntEmp = Integer.parseInt(idEntregaString);


                        System.out.print("Nuevo ID Empleado: ");
                        String nuevoEmpleado = scanner.nextLine();

                        if (!nuevoEmpleado.matches("\\d+")) {
                            throw new ValidacionEx("El nuevo ID de empleado solo puede contener números");
                        }

                        int nuevoEmp = Integer.parseInt(nuevoEmpleado);
                        boolean okEmpEnt = sistema.actualizarEmpleadoDeEntrega(idEntEmp, nuevoEmp);
                        if (okEmpEnt){
                            System.out.println("Empleado de entrega actualizado.");
                        } else {
                            System.out.println("Error al actualizar empleado.");
                        }
                        break;
                    case 19:
                        System.out.print("ID Devolución: ");
                        String idDevolucion = scanner.nextLine();

                        if (!idDevolucion.matches("\\d+")) {
                            throw new ValidacionEx("El ID de devolución solo puede contener números");
                        }

                        int idDevEmp = Integer.parseInt(idDevolucion);


                        System.out.print("Nuevo ID Empleado: ");
                        String nuevoEmpleadoString = scanner.nextLine();

                        if (!nuevoEmpleadoString.matches("\\d+")) {
                            throw new ValidacionEx("El nuevo ID de empleado solo puede contener números");
                        }

                        int nuevoEmpDev = Integer.parseInt(nuevoEmpleadoString);
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
                        System.out.print("Nuevo estado (1.DISPONIBLE 2.EN_REPARACION 3.EN_LIMPIEZA 4.FUERA_DE_SERVICIO): ");
                        String estVehString = scanner.nextLine();

                        if(!estVehString.matches("\\d+")){
                            throw new ValidacionEx("Opcion invalida. Porfavor seleccione una opcion entre (1,2,3,4)");
                        }
                        int opcionVeh = Integer.parseInt(estVehString);
                        boolean okCambioEst = false;

                        //TERMINARrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
                        if(opc)

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
