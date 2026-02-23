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
import magister.model.Asignatura;

import java.util.ArrayList;
import java.util.List;

public class AsignaturasView extends VBox {
    private TableView<Asignatura> table;
    private ObservableList<Asignatura> asignaturas;
    private TableColumn<Asignatura, String> colCodigo;
    private TableColumn<Asignatura, String> colNombre;
    private TableColumn<Asignatura, Integer> colCreditos;

    public AsignaturasView() {
        asignaturas = FXCollections.observableArrayList(new ArrayList<>());
        
        setSpacing(10);
        setPadding(new Insets(10));
        
        Label title = new Label("Gestión de Asignaturas");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 18));
        
        colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        
        colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        colCreditos = new TableColumn<>("Créditos");
        colCreditos.setCellValueFactory(new PropertyValueFactory<>("creditos"));
        
        table = new TableView<>(asignaturas);
        table.getColumns().addAll(colCodigo, colNombre, colCreditos);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        HBox buttonBox = new HBox(10);
        Button btnAgregar = new Button("Agregar");
        Button btnEliminar = new Button("Eliminar");
        
        btnAgregar.setOnAction(e -> agregarAsignatura());
        btnEliminar.setOnAction(e -> eliminarAsignatura());
        
        buttonBox.getChildren().addAll(btnAgregar, btnEliminar);
        
        getChildren().addAll(title, table, buttonBox);
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);
    }

    private void agregarAsignatura() {
        TextField codigoField = new TextField();
        TextField nombreField = new TextField();
        TextField creditosField = new TextField();
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        grid.add(new Label("Código:"), 0, 0);
        grid.add(codigoField, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(nombreField, 1, 1);
        grid.add(new Label("Créditos:"), 0, 2);
        grid.add(creditosField, 1, 2);
        
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nueva Asignatura");
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String codigo = codigoField.getText().trim();
                String nombre = nombreField.getText().trim();
                String creditosStr = creditosField.getText().trim();
                
                if (!codigo.isEmpty() && !nombre.isEmpty() && !creditosStr.isEmpty()) {
                    try {
                        int creditos = Integer.parseInt(creditosStr);
                        asignaturas.add(new Asignatura(codigo, nombre, creditos));
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Los créditos deben ser un número");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Todos los campos son obligatorios");
                    alert.showAndWait();
                }
            }
        });
    }

    private void eliminarAsignatura() {
        Asignatura selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            asignaturas.remove(selected);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setContentText("Seleccione una fila para eliminar");
            alert.showAndWait();
        }
    }
}
