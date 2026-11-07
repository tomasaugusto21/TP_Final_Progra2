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

    //devuelve todo
    public Map<Integer, T> getElementos() {
        return elementos;
    }
}
