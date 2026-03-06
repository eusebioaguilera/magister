package magister.model;

import java.util.ArrayList;
import java.util.List;

public class Asignatura {
    private int id;
    private String nombre;
    private int creditos;
    private List<ResultadoAprendizaje> resultadosAprendizaje;

    public Asignatura(int id, String nombre, int creditos) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
        this.resultadosAprendizaje = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }
    public List<ResultadoAprendizaje> getResultadosAprendizaje() { return resultadosAprendizaje; }
    public void setResultadosAprendizaje(List<ResultadoAprendizaje> resultadosAprendizaje) { this.resultadosAprendizaje = resultadosAprendizaje; }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + creditos + " créditos)";
    }
}
