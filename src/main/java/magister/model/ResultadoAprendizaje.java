package magister.model;

public class ResultadoAprendizaje {
    private int id;
    private int idAsignatura;
    private String codigo;
    private String descripcion;
    private double ponderacion;

    public ResultadoAprendizaje(int id, int idAsignatura, String codigo, String descripcion, double ponderacion) {
        this.id = id;
        this.idAsignatura = idAsignatura;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.ponderacion = ponderacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdAsignatura() { return idAsignatura; }
    public void setIdAsignatura(int idAsignatura) { this.idAsignatura = idAsignatura; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPonderacion() { return ponderacion; }
    public void setPonderacion(double ponderacion) { this.ponderacion = ponderacion; }

    @Override
    public String toString() {
        return codigo + " - " + descripcion;
    }
}
