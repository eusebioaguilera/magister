package magister.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:magister.db";
    private static Database instance;
    private Connection connection;

    private Database() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            initTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initTables() throws SQLException {
        String createAsignaturas = """
            CREATE TABLE IF NOT EXISTS asignaturas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                creditos INTEGER NOT NULL
            )
            """;
        
        String createAlumnos = """
            CREATE TABLE IF NOT EXISTS alumnos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                email TEXT
            )
            """;
        
        try (PreparedStatement ps1 = connection.prepareStatement(createAsignaturas);
             PreparedStatement ps2 = connection.prepareStatement(createAlumnos)) {
            ps1.execute();
            ps2.execute();
        }
    }

    public int getNextId(String table) throws SQLException {
        String sql = "SELECT MAX(id) as max_id FROM " + table;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                return maxId == 0 ? 1 : maxId + 1;
            }
            return 1;
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
