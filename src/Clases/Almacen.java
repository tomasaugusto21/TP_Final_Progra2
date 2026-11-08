package Clases;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Almacen <T extends Identificador>{

    private Map<Integer, T> elementos;

    public Almacen(){
        this.elementos = new HashMap<>();
    }


    public void agregar(T elemento){
        elementos.put(elemento.getID(),elemento);
    }

    public void eliminar(int id){
        elementos.remove(id);
    }

    public T buscarPorId(int id){
        return elementos.get(id);
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

    //devuelve todo
    public Map<Integer, T> getElementos() {
        return elementos;
    }
}
