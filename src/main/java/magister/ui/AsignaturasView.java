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
import magister.dao.AsignaturaDAO;
import magister.model.Asignatura;

public class AsignaturasView extends VBox {
    private TableView<Asignatura> table;
    private ObservableList<Asignatura> asignaturas;
    private TableColumn<Asignatura, Integer> colId;
    private TableColumn<Asignatura, String> colNombre;
    private TableColumn<Asignatura, Integer> colCreditos;
    private final AsignaturaDAO dao;

    public AsignaturasView() {
        dao = new AsignaturaDAO();
        asignaturas = FXCollections.observableArrayList(dao.getAll());
        
        setSpacing(10);
        setPadding(new Insets(10));
        
        Label title = new Label("Gestión de Asignaturas");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 18));
        
        colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        colCreditos = new TableColumn<>("Créditos");
        colCreditos.setCellValueFactory(new PropertyValueFactory<>("creditos"));
        
        table = new TableView<>(asignaturas);
        table.getColumns().addAll(colId, colNombre, colCreditos);
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
    
    public void refresh() {
        asignaturas.clear();
        asignaturas.addAll(dao.getAll());
    }

    private void agregarAsignatura() {
        int nextId = dao.getNextId();
        
        TextField nombreField = new TextField();
        TextField creditosField = new TextField();
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        grid.add(new Label("ID:"), 0, 0);
        grid.add(new Label(String.valueOf(nextId)), 1, 0);
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
                String nombre = nombreField.getText().trim();
                String creditosStr = creditosField.getText().trim();
                
                if (!nombre.isEmpty() && !creditosStr.isEmpty()) {
                    try {
                        int creditos = Integer.parseInt(creditosStr);
                        Asignatura a = new Asignatura(nextId, nombre, creditos);
                        dao.insert(a);
                        asignaturas.add(a);
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
            dao.delete(selected.getId());
            asignaturas.remove(selected);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setContentText("Seleccione una fila para eliminar");
            alert.showAndWait();
        }
    }
}
