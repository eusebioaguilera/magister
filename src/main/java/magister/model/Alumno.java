package magister.model;

public class Alumno {
    private String nia;
    private String nombre;
    private String apellido;
    private String email;

    public Alumno(String nia, String nombre, String apellido, String email) {
        this.nia = nia;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    public String getNia() { return nia; }
    public void setNia(String nia) { this.nia = nia; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return nia + " - " + nombre + " " + apellido;
    }
}
