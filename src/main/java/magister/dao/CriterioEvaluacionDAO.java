package magister.dao;

import magister.db.Database;
import magister.model.CriterioEvaluacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CriterioEvaluacionDAO {
    private final Database db;

    public CriterioEvaluacionDAO() {
        this.db = Database.getInstance();
    }

    public List<CriterioEvaluacion> getByResultado(int idResultado) {
        List<CriterioEvaluacion> list = new ArrayList<>();
        String sql = "SELECT id, id_resultado, codigo, descripcion, ponderacion FROM criterios_evaluacion WHERE id_resultado = ? ORDER BY codigo";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idResultado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CriterioEvaluacion(
                        rs.getInt("id"),
                        rs.getInt("id_resultado"),
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

    public void insert(CriterioEvaluacion ce) {
        String sql = "INSERT INTO criterios_evaluacion (id, id_resultado, codigo, descripcion, ponderacion) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, ce.getId());
            ps.setInt(2, ce.getIdResultado());
            ps.setString(3, ce.getCodigo());
            ps.setString(4, ce.getDescripcion());
            ps.setDouble(5, ce.getPonderacion());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(CriterioEvaluacion ce) {
        String sql = "UPDATE criterios_evaluacion SET codigo = ?, descripcion = ?, ponderacion = ? WHERE id = ?";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, ce.getCodigo());
            ps.setString(2, ce.getDescripcion());
            ps.setDouble(3, ce.getPonderacion());
            ps.setInt(4, ce.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM criterios_evaluacion WHERE id = ?";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteByResultado(int idResultado) {
        String sql = "DELETE FROM criterios_evaluacion WHERE id_resultado = ?";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idResultado);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNextId() {
        try {
            return db.getNextId("criterios_evaluacion");
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }
}
