package Clases;

import Enums.RolDeUsuario;
import Excepciones.*;

import java.util.List;

public class GestorUsuarios {

    private final Almacen<Usuario> usuarios = new Almacen<>();



    public void descargarDatosBDD () throws ObjetoInvalidoEx, ObjetoDuplicadoEx, ValidacionEx {

        List<Usuario> obtenidos = DBHandler.obtenerTodosLosUsuarios();

        for (Usuario u : obtenidos){
            usuarios.agregar(u);
        }
    }

    public Cliente signup (String nombre, String apellido, int dni, String email, String username, String numeroDeLicencia, String telefono, String domicilio, String contraseña) throws ValidacionEx, ObjetoDuplicadoEx, ObjetoInvalidoEx, ErrorDeCargaEx {

        validarPassword(contraseña);

        if (nombre == null || nombre.isBlank()){
            throw new ValidacionEx("El nombre del cliente no puede ser nulo o estar en blanco");
        }
        if (apellido == null || apellido.isBlank()){
            throw new ValidacionEx("El apellido del cliente no puede ser nulo o estar en blanco");
        }
        if (dni < 1000000 || dni > 99999999){
            throw new ValidacionEx("El dni del cliente debe tener 7 u 8 dígitos");
        }
        if (email == null || email.isBlank()){
            throw new ValidacionEx("El email del cliente no puede ser nulo o estar en blanco");
        }
        if (username == null || username.isBlank()){
            throw new ValidacionEx("El username del cliente no puede ser nulo o estar en blanco");
        }
        if (numeroDeLicencia == null || numeroDeLicencia.isBlank()){
            throw new ValidacionEx("El numero de licencia del cliente no puede ser nulo o estar en blanco");
        }
        if (telefono == null || telefono.isBlank()){
            throw new ValidacionEx("El telefono del cliente no puede ser nulo o estar en blanco");
        }
        if (!telefono.matches("\\d{10,11}")) {
            throw new ValidacionEx("El teléfono debe tener entre 10 y 11 dígitos.");
        }
        if (domicilio == null || domicilio.isBlank()){
            throw new ValidacionEx("El domicilio del cliente no puede ser nulo o estar en blanco");
        }

        Cliente nuevo = new Cliente(nombre, apellido, dni, email, username, RolDeUsuario.CLIENTE, numeroDeLicencia, telefono, domicilio);

        validarUnico(nuevo);

        int id = DBHandler.agregar_usuario(nuevo, contraseña);

        if (id == -1){
            throw new ErrorDeCargaEx("Ocurrió un error al crear el signup en la base de datos");
        }

        nuevo.setId(id);
        usuarios.agregar(nuevo);

        return nuevo;
    }

    public Usuario login (String username, String contraseña) throws ValidacionEx, LoginEx {

        if (username == null || username.isBlank()){
            throw new ValidacionEx("El username no puede ser nulo ni estar en blanco");
        }
        if (contraseña == null || contraseña.isBlank()){
            throw new ValidacionEx("La contraseña no puede ser nula o estar en blanco");
        }

        Usuario usuario = DBHandler.obtenerUsuario(username, contraseña);

        if (usuario == null){
            throw new LoginEx("El username o la contraseña son incorrectos");
        }
        return usuario;
    }

    public Empleado crearEmpleado (String nombre, String apellido, int dni, String email, String username, String legajo, String contraseña) throws ValidacionEx, ObjetoDuplicadoEx, ObjetoInvalidoEx, ErrorDeCargaEx {

        validarPassword(contraseña);

        if (nombre == null || nombre.isBlank()){
            throw new ValidacionEx("El nombre del empleado no puede ser nulo o estar en blanco");
        }
        if (apellido == null || apellido.isBlank()){
            throw new ValidacionEx("El apellido del empleado no puede ser nulo o estar en blanco");
        }
        if (dni < 1000000 || dni > 99999999){
            throw new ValidacionEx("El dni del empleado debe tener 7 u 8 dígitos");
        }
        if (email == null || email.isBlank()){
            throw new ValidacionEx("El email del empleado no puede ser nulo o estar en blanco");
        }
        if (username == null || username.isBlank()){
            throw new ValidacionEx("El username del empleado no puede ser nulo o estar en blanco");
        }
        if (legajo == null || legajo.isBlank()){
            throw new ValidacionEx("El legajo del empleado no puede ser nulo o estar en blanco");
        }

        Empleado nuevo = new Empleado(nombre, apellido, dni, email, username, RolDeUsuario.EMPLEADO , legajo);

        validarUnico(nuevo);

        int id = DBHandler.agregar_usuario(nuevo, contraseña);

        if (id == -1){
            throw new ErrorDeCargaEx("Ocurrió un error al crear el empleado en la base de datos");
        }

        nuevo.setId(id);
        usuarios.agregar(nuevo);

        return nuevo;
    }


    public boolean actualizarNombre (int id, String nuevoNombre) throws ValidacionEx {

        if (nuevoNombre == null || nuevoNombre.isBlank()){
            throw new ValidacionEx("El nuevo nombre no puede ser nulo ni estar en blanco");
        }

        Usuario buscado = buscarPorId(id);

        if (DBHandler.actualizar_usuario_nombre(id, nuevoNombre) == 1){
            buscado.setNombre(nuevoNombre);
            return true;
        }
        return false;
    }

    public boolean actualizarApellido (int id, String nuevoApellido) throws ValidacionEx {

        if (nuevoApellido == null || nuevoApellido.isBlank()){
            throw new ValidacionEx("El nuevo apellido no puede ser nulo ni estar en blanco");
        }

        Usuario buscado = buscarPorId(id);

        if (DBHandler.actualizar_usuario_apellido(id, nuevoApellido) == 1){
            buscado.setApellido(nuevoApellido);
            return true;
        }
        return false;
    }

    public boolean actualizarEmail (int id, String nuevoEmail) throws ValidacionEx {

        if (nuevoEmail == null || nuevoEmail.isBlank()){
            throw new ValidacionEx("El nuevo email no puede ser nulo ni estar en blanco");
        }

        Usuario buscado = buscarPorId(id);

        if (DBHandler.actualizar_usuario_email(id, nuevoEmail) == 1){
            buscado.setEmail(nuevoEmail);
            return true;
        }
        return false;
    }

    public boolean actualizarUsername (int id, String nuevoUsername) throws ValidacionEx, ObjetoDuplicadoEx {

        if (nuevoUsername == null || nuevoUsername.isBlank()) {
            throw new ValidacionEx("El nuevo username no puede ser nulo ni estar en blanco");
        }

        for (Usuario u : usuarios.listarTodos()){
            if (u.getID() != id && u.getUsername().equalsIgnoreCase(nuevoUsername)){
                throw new ObjetoDuplicadoEx("Ya existe un usuario con ese username");
            }
        }

        Usuario buscado = buscarPorId(id);

        if (DBHandler.actualizar_usuario_username(id, nuevoUsername) == 1){
            buscado.setUsername(nuevoUsername);
            return true;
        }
        return false;
    }

    public boolean actualizarLegajo (int id, String nuevoLegajo) throws ValidacionEx, ObjetoInvalidoEx {

        if (nuevoLegajo == null || nuevoLegajo.isBlank()){
            throw new ValidacionEx("El nuevo legajo no puede ser nulo ni estar en blanco");
        }

        Usuario buscado = buscarPorId(id);

        if (!(buscado instanceof Empleado)){
            throw new ObjetoInvalidoEx("No se puede actualizar el legajo de un Usuario que no es Empleado");
        }

        if (DBHandler.actualizar_empleado_legajo(id, nuevoLegajo)==1){
            ((Empleado) buscado).setLegajo(nuevoLegajo);
            return true;
        }
        return false;
    }

    public boolean actualizarTelefonoCliente (int id, String nuevoTelefono) throws ValidacionEx, ObjetoInvalidoEx {

        if (nuevoTelefono == null || nuevoTelefono.isBlank()){
            throw new ValidacionEx("El nuevo telefono no puede ser nulo ni estar en blanco");
        }

        if (!nuevoTelefono.matches("\\d{10,11}")) {
            throw new ValidacionEx("El teléfono debe tener entre 10 y 11 dígitos.");
        }

        Usuario buscado = buscarPorId(id);

        if (!(buscado instanceof Cliente)){
            throw new ObjetoInvalidoEx("No se puede actualizar el telefono de un Usuario que no es Cliente");
        }

        if (DBHandler.actualizar_cliente_telefono(id, nuevoTelefono) == 1){
            ((Cliente) buscado).setTelefono(nuevoTelefono);
            return true;
        }
        return false;
    }

    public boolean actualizarDomicilioCliente (int id, String nuevoDomicilio) throws ObjetoInvalidoEx, ValidacionEx {

        if (nuevoDomicilio == null || nuevoDomicilio.isBlank()){
            throw new ValidacionEx("El nuevo domicilio no puede ser nulo ni estar en blanco");
        }

        Usuario buscado = buscarPorId(id);

        if (!(buscado instanceof Cliente)){
            throw new ObjetoInvalidoEx("No se puede actualizar el domicilio de un Usuario que no es Cliente");
        }

        if (DBHandler.actualizar_cliente_domicilio(id, nuevoDomicilio) == 1){
            ((Cliente) buscado).setDomicilio(nuevoDomicilio);
            return true;
        }
        return false;
    }


    public Usuario buscarPorId (int id) throws ValidacionEx {
        return usuarios.buscarPorId(id);
    }

    public Cliente buscarCliente(int id) throws ObjetoNoEncontradoEx, ValidacionEx {
        Usuario usuario = usuarios.buscarPorId(id);
        if (!(usuario instanceof Cliente)) {
            throw new ObjetoNoEncontradoEx("No existe un cliente con id " + id);
        }
        return (Cliente) usuario;
    }

    public Empleado buscarEmpleado (int id) throws ValidacionEx, ObjetoNoEncontradoEx {
        Usuario usuario = usuarios.buscarPorId(id);
        if (!(usuario instanceof Empleado)) {
            throw new ObjetoNoEncontradoEx("No existe un empleado con id " + id);
        }
        return (Empleado) usuario;
    }

    public List<Cliente> listarClientes (){
        return usuarios.listarDeTipo(Cliente.class);
    }

    public List<Empleado> listarEmpleados (){
        return usuarios.listarDeTipo(Empleado.class);
    }



    private void validarUnico (Usuario nuevo) throws ObjetoDuplicadoEx {

        for (Usuario u : usuarios.listarTodos()) {
            if (u.getUsername().equalsIgnoreCase(nuevo.getUsername())) {
                throw new ObjetoDuplicadoEx("Ya existe un usuario con ese username");
            }
        }

        for (Usuario u : usuarios.listarTodos()){
            if (u.getDni() == nuevo.getDni()){
                throw new ObjetoDuplicadoEx("Ya existe un usuario registrado con ese DNI");
            }
        }
    }

    private void validarPassword(String password) throws ValidacionEx {
        if (password == null || password.isEmpty()) {
            throw new ValidacionEx("La contraseña es obligatoria y no puede ser nula ni estar en blanco");
        }
        if (password.length() < 5){
            throw new ValidacionEx("La contraseña debe tener una extension minima de 5 caracteres");
        }
    }

}
