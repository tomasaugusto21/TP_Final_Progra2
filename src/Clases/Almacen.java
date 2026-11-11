package Clases;

import Excepciones.ObjetoDuplicadoEx;
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

    public void agregar(T elemento) throws ObjetoInvalidoEx, ValidacionEx, ObjetoDuplicadoEx {
        if (elemento == null) {
            throw new ObjetoInvalidoEx("El elemento no puede ser nulo.");
        }

        if (elementos.containsValue(elemento)){
            throw new ObjetoDuplicadoEx("El elemento que se intentó agregar ya existe en el almacen");
        }

        if (elementos.containsKey(elemento.getID())){
            throw new ObjetoDuplicadoEx("El ID del elemento a agregar ya existe en el almacen");
        }

        elementos.put(elemento.getID(), elemento);

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

    public boolean verificarExistencia (T obj){
        return elementos.containsValue(obj);
    }

}
