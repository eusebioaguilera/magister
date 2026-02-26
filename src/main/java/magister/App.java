package magister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import magister.db.Database;
import magister.ui.AlumnosView;
import magister.ui.AsignaturasView;

public class App extends Application {
    private AsignaturasView asignaturasView;
    private AlumnosView alumnosView;
    
    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();
        
        asignaturasView = new AsignaturasView();
        Tab tabAsignaturas = new Tab("Asignaturas");
        tabAsignaturas.setClosable(false);
        tabAsignaturas.setContent(asignaturasView);
        
        alumnosView = new AlumnosView();
        Tab tabAlumnos = new Tab("Alumnos");
        tabAlumnos.setClosable(false);
        tabAlumnos.setContent(alumnosView);
        
        tabPane.getTabs().addAll(tabAsignaturas, tabAlumnos);
        
        Menu menuDatos = new Menu("Datos");
        
        MenuItem itemAbrir = new MenuItem("Abrir archivo");
        itemAbrir.setOnAction(e -> abrirArchivo(stage));
        
        MenuItem itemExportar = new MenuItem("Exportar archivo");
        itemExportar.setOnAction(e -> exportarArchivo(stage));
        
        menuDatos.getItems().addAll(itemAbrir, itemExportar);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menuDatos);
        
        VBox root = new VBox(menuBar, tabPane);
        
        Scene scene = new Scene(root, 800, 600);
        
        stage.setTitle("Magister - Gestión Académica");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
    
    private void abrirArchivo(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir archivo de base de datos");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos SQLite", "*.db", "*.sqlite", "*.sqlite3")
        );
        
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Database.getInstance().changeDatabase(file.getAbsolutePath());
                asignaturasView.refresh();
                alumnosView.refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void exportarArchivo(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar archivo de base de datos");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos SQLite", "*.db", "*.sqlite", "*.sqlite3")
        );
        
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                String currentDb = Database.getCurrentDbPath();
                File sourceFile = new File(currentDb);
                File destFile = file;
                
                try (FileInputStream fis = new FileInputStream(sourceFile);
                     FileOutputStream fos = new FileOutputStream(destFile)) {
                    fis.getChannel().transferTo(0, fis.getChannel().size(), fos.getChannel());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
