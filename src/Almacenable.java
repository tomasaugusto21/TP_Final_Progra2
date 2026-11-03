public interface Almacenable <T> {
    void guardarEnBDD();
    T obtenerXId(int id);
}
