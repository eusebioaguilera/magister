package magister.model;

public class CriterioEvaluacion {
    private int id;
    private int idResultado;
    private String codigo;
    private String descripcion;

    public CriterioEvaluacion(int id, int idResultado, String codigo, String descripcion) {
        this.id = id;
        this.idResultado = idResultado;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdResultado() { return idResultado; }
    public void setIdResultado(int idResultado) { this.idResultado = idResultado; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return codigo + " - " + descripcion;
    }
}
