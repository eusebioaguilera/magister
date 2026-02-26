package magister.dao;

import magister.db.Database;
import magister.model.Alumno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {
    private final Database db;

    public AlumnoDAO() {
        this.db = Database.getInstance();
    }

    public List<Alumno> getAll() {
        List<Alumno> list = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, email FROM alumnos ORDER BY id";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Alumno(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Alumno a) {
        String sql = "INSERT INTO alumnos (id, nombre, apellido, email) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, a.getId());
            ps.setString(2, a.getNombre());
            ps.setString(3, a.getApellido());
            ps.setString(4, a.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM alumnos WHERE id = ?";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNextId() {
        try {
            return db.getNextId("alumnos");
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }
}
