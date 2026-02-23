package magister;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import magister.ui.AlumnosView;
import magister.ui.AsignaturasView;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();
        
        Tab tabAsignaturas = new Tab("Asignaturas");
        tabAsignaturas.setContent(new AsignaturasView());
        
        Tab tabAlumnos = new Tab("Alumnos");
        tabAlumnos.setContent(new AlumnosView());
        
        tabPane.getTabs().addAll(tabAsignaturas, tabAlumnos);
        
        VBox root = new VBox(tabPane);
        
        Scene scene = new Scene(root, 800, 600);
        
        stage.setTitle("Magister - Gestión Académica");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
