package magister.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import magister.model.Alumno;

import java.util.ArrayList;

public class AlumnosView extends VBox {
    private TableView<Alumno> table;
    private ObservableList<Alumno> alumnos;
    private TableColumn<Alumno, String> colNia;
    private TableColumn<Alumno, String> colNombre;
    private TableColumn<Alumno, String> colApellido;
    private TableColumn<Alumno, String> colEmail;

    public AlumnosView() {
        alumnos = FXCollections.observableArrayList(new ArrayList<>());
        
        setSpacing(10);
        setPadding(new Insets(10));
        
        Label title = new Label("Gestión de Alumnos");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 18));
        
        colNia = new TableColumn<>("NIA");
        colNia.setCellValueFactory(new PropertyValueFactory<>("nia"));
        
        colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        table = new TableView<>(alumnos);
        table.getColumns().addAll(colNia, colNombre, colApellido, colEmail);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        HBox buttonBox = new HBox(10);
        Button btnAgregar = new Button("Agregar");
        Button btnEliminar = new Button("Eliminar");
        
        btnAgregar.setOnAction(e -> agregarAlumno());
        btnEliminar.setOnAction(e -> eliminarAlumno());
        
        buttonBox.getChildren().addAll(btnAgregar, btnEliminar);
        
        getChildren().addAll(title, table, buttonBox);
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);
    }

    private void agregarAlumno() {
        TextField niaField = new TextField();
        TextField nombreField = new TextField();
        TextField apellidoField = new TextField();
        TextField emailField = new TextField();
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        grid.add(new Label("NIA:"), 0, 0);
        grid.add(niaField, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(nombreField, 1, 1);
        grid.add(new Label("Apellido:"), 0, 2);
        grid.add(apellidoField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Alumno");
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String nia = niaField.getText().trim();
                String nombre = nombreField.getText().trim();
                String apellido = apellidoField.getText().trim();
                String email = emailField.getText().trim();
                
                if (!nia.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty()) {
                    alumnos.add(new Alumno(nia, nombre, apellido, email));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("NIA, Nombre y Apellido son obligatorios");
                    alert.showAndWait();
                }
            }
        });
    }

    private void eliminarAlumno() {
        Alumno selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            alumnos.remove(selected);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setContentText("Seleccione una fila para eliminar");
            alert.showAndWait();
        }
    }
}
