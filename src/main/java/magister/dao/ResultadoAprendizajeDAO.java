package magister.dao;

import magister.db.Database;
import magister.model.ResultadoAprendizaje;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultadoAprendizajeDAO {
    private final Database db;

    public ResultadoAprendizajeDAO() {
        this.db = Database.getInstance();
    }

    public List<ResultadoAprendizaje> getByAsignatura(int idAsignatura) {
        List<ResultadoAprendizaje> list = new ArrayList<>();
        String sql = "SELECT id, id_asignatura, codigo, descripcion, ponderacion FROM resultados_aprendizaje WHERE id_asignatura = ? ORDER BY codigo";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idAsignatura);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ResultadoAprendizaje(
                        rs.getInt("id"),
                        rs.getInt("id_asignatura"),
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("ponderacion")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(ResultadoAprendizaje ra) {
        String sql = "INSERT INTO resultados_aprendizaje (id, id_asignatura, codigo, descripcion, ponderacion) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, ra.getId());
            ps.setInt(2, ra.getIdAsignatura());
            ps.setString(3, ra.getCodigo());
            ps.setString(4, ra.getDescripcion());
            ps.setDouble(5, ra.getPonderacion());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ResultadoAprendizaje ra) {
        String sql = "UPDATE resultados_aprendizaje SET codigo = ?, descripcion = ?, ponderacion = ? WHERE id = ?";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, ra.getCodigo());
            ps.setString(2, ra.getDescripcion());
            ps.setDouble(3, ra.getPonderacion());
            ps.setInt(4, ra.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM resultados_aprendizaje WHERE id = ?";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNextId() {
        try {
            return db.getNextId("resultados_aprendizaje");
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }
}
