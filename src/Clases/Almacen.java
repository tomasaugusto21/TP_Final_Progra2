package Clases;

import Excepciones.ObjetoInvalidoEx;
import Excepciones.ValidacionEx;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Almacen <T extends Identificable>{

    private Map<Integer, T> elementos;

    public Almacen(){
        this.elementos = new HashMap<>();
    }

    public void agregar(T elemento) throws ObjetoInvalidoEx, ValidacionEx {
        if (elemento == null) {
            throw new ObjetoInvalidoEx("El elemento no puede ser nulo.");
        }

        for (T e : elementos.values()) {
            if (e.getID() == elemento.getID()) {
                throw new ValidacionEx("Ya existe un registro con el ID: " + elemento.getID());
            }
        }

        elementos.put(elemento.getID(), elemento);
    }

    public void eliminar(int id) throws ValidacionEx {
        boolean encontrado = false;
        for (T e : elementos.values()) {
            if (e.getID() == id) {
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new ValidacionEx("No se encontró ningún elemento con el ID: " + id);
        }

        elementos.remove(id);
    }

    public T buscarPorId(int id) throws ValidacionEx {
        for (T e : elementos.values()) {
            if (e.getID() == id) {
                return e;
            }
        }
        throw new ValidacionEx("No se encontró ningún elemento con el ID: " + id);
    }

    public List<T> listarTodos() {
        return new ArrayList<>(elementos.values());
    }

    public <E extends T> List<E> listarDeTipo(Class<E> tipo) {
        List<E> resultado = new ArrayList<>();
        for (T elemento : elementos.values()) {
            if (tipo.isInstance(elemento)) {
                resultado.add(tipo.cast(elemento));
            }
        }
        return resultado;
    }

    public Map<Integer, T> getElementos() {
        return elementos;
    }
}
