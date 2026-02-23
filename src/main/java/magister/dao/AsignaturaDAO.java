package magister.dao;

import magister.db.Database;
import magister.model.Asignatura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsignaturaDAO {
    private final Database db;

    public AsignaturaDAO() {
        this.db = Database.getInstance();
    }

    public List<Asignatura> getAll() {
        List<Asignatura> list = new ArrayList<>();
        String sql = "SELECT id, nombre, creditos FROM asignaturas ORDER BY id";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Asignatura(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("creditos")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Asignatura a) {
        String sql = "INSERT INTO asignaturas (id, nombre, creditos) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, a.getId());
            ps.setString(2, a.getNombre());
            ps.setInt(3, a.getCreditos());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM asignaturas WHERE id = ?";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNextId() {
        try {
            return db.getNextId("asignaturas");
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }
}
