package magister.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import magister.dao.AsignaturaDAO;
import magister.dao.CriterioEvaluacionDAO;
import magister.dao.ResultadoAprendizajeDAO;
import magister.model.Asignatura;
import magister.model.CriterioEvaluacion;
import magister.model.ResultadoAprendizaje;

public class AsignaturasView extends VBox {
    private TableView<Asignatura> table;
    private ObservableList<Asignatura> asignaturas;
    private TableColumn<Asignatura, Integer> colId;
    private TableColumn<Asignatura, String> colNombre;
    private TableColumn<Asignatura, Integer> colCreditos;
    private final AsignaturaDAO dao;
    private final ResultadoAprendizajeDAO resultadoDao;
    private final CriterioEvaluacionDAO criterioDao;

    public AsignaturasView() {
        dao = new AsignaturaDAO();
        resultadoDao = new ResultadoAprendizajeDAO();
        criterioDao = new CriterioEvaluacionDAO();
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
        
        table.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                Asignatura selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    mostrarEditorAsignatura(selected);
                }
            }
        });
        
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

    private void mostrarEditorAsignatura(Asignatura asignatura) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar Asignatura");
        dialog.setHeaderText("Editar información de la asignatura");

        TextField nombreField = new TextField(asignatura.getNombre());
        TextField creditosField = new TextField(String.valueOf(asignatura.getCreditos()));

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(10);
        infoGrid.setPadding(new Insets(10));

        infoGrid.add(new Label("ID:"), 0, 0);
        infoGrid.add(new Label(String.valueOf(asignatura.getId())), 1, 0);
        infoGrid.add(new Label("Nombre:"), 0, 1);
        infoGrid.add(nombreField, 1, 1);
        infoGrid.add(new Label("Créditos:"), 0, 2);
        infoGrid.add(creditosField, 1, 2);

        Label resultadosLabel = new Label("Resultados de Aprendizaje");
        resultadosLabel.setFont(Font.font("SansSerif", FontWeight.BOLD, 14));

        ListView<ResultadoAprendizaje> resultadosListView = new ListView<>();
        ObservableList<ResultadoAprendizaje> resultadosList = FXCollections.observableArrayList(
            resultadoDao.getByAsignatura(asignatura.getId())
        );
        resultadosListView.setItems(resultadosList);
        resultadosListView.setCellFactory(lv -> new ListCell<ResultadoAprendizaje>() {
            @Override
            protected void updateItem(ResultadoAprendizaje item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCodigo() + " - " + item.getDescripcion() + " (" + item.getPonderacion() + ")");
                }
            }
        });

        HBox resultadosButtons = new HBox(10);
        Button btnAddResultado = new Button("Añadir RA");
        Button btnEditResultado = new Button("Editar RA");
        Button btnDeleteResultado = new Button("Eliminar RA");
        resultadosButtons.getChildren().addAll(btnAddResultado, btnEditResultado, btnDeleteResultado);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(infoGrid, resultadosLabel, resultadosListView, resultadosButtons);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(600);
        dialog.getDialogPane().setPrefHeight(500);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        btnAddResultado.setOnAction(e -> {
            agregarResultadoAprendizaje(asignatura.getId(), resultadosList);
        });

        btnEditResultado.setOnAction(e -> {
            ResultadoAprendizaje selected = resultadosListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                editarResultadoAprendizaje(selected, resultadosList);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Advertencia");
                alert.setContentText("Seleccione un resultado de aprendizaje para editar");
                alert.showAndWait();
            }
        });

        btnDeleteResultado.setOnAction(e -> {
            ResultadoAprendizaje selected = resultadosListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmar eliminación");
                confirm.setContentText("¿Eliminar este resultado de aprendizaje?");
                confirm.showAndWait().ifPresent(result -> {
                    if (result == ButtonType.OK) {
                        criterioDao.deleteByResultado(selected.getId());
                        resultadoDao.delete(selected.getId());
                        resultadosList.remove(selected);
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Advertencia");
                alert.setContentText("Seleccione un resultado de aprendizaje para eliminar");
                alert.showAndWait();
            }
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String nombre = nombreField.getText().trim();
                String creditosStr = creditosField.getText().trim();
                
                if (!nombre.isEmpty() && !creditosStr.isEmpty()) {
                    try {
                        int creditos = Integer.parseInt(creditosStr);
                        asignatura.setNombre(nombre);
                        asignatura.setCreditos(creditos);
                        dao.update(asignatura);
                        refresh();
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Los créditos deben ser un número");
                        alert.showAndWait();
                    }
                }
            }
        });
    }

    private void agregarResultadoAprendizaje(int idAsignatura, ObservableList<ResultadoAprendizaje> resultadosList) {
        int nextId = resultadoDao.getNextId();

        TextField codigoField = new TextField();
        TextArea descripcionField = new TextArea();
        descripcionField.setPrefRowCount(3);
        TextField ponderacionField = new TextField("1.0");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("ID:"), 0, 0);
        grid.add(new Label(String.valueOf(nextId)), 1, 0);
        grid.add(new Label("Código:"), 0, 1);
        grid.add(codigoField, 1, 1);
        grid.add(new Label("Descripción:"), 0, 2);
        grid.add(descripcionField, 1, 2);
        grid.add(new Label("Ponderación:"), 0, 3);
        grid.add(ponderacionField, 1, 3);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Resultado de Aprendizaje");
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String codigo = codigoField.getText().trim();
                String descripcion = descripcionField.getText().trim();
                String ponderacionStr = ponderacionField.getText().trim();

                if (!codigo.isEmpty() && !descripcion.isEmpty() && !ponderacionStr.isEmpty()) {
                    try {
                        double ponderacion = Double.parseDouble(ponderacionStr);
                        ResultadoAprendizaje ra = new ResultadoAprendizaje(nextId, idAsignatura, codigo, descripcion, ponderacion);
                        resultadoDao.insert(ra);
                        resultadosList.add(ra);
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("La ponderación debe ser un número");
                        alert.showAndWait();
                    }
                }
            }
        });
    }

    private void editarResultadoAprendizaje(ResultadoAprendizaje ra, ObservableList<ResultadoAprendizaje> resultadosList) {
        TextField codigoField = new TextField(ra.getCodigo());
        TextArea descripcionField = new TextArea(ra.getDescripcion());
        descripcionField.setPrefRowCount(3);
        TextField ponderacionField = new TextField(String.valueOf(ra.getPonderacion()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("ID:"), 0, 0);
        grid.add(new Label(String.valueOf(ra.getId())), 1, 0);
        grid.add(new Label("Código:"), 0, 1);
        grid.add(codigoField, 1, 1);
        grid.add(new Label("Descripción:"), 0, 2);
        grid.add(descripcionField, 1, 2);
        grid.add(new Label("Ponderación:"), 0, 3);
        grid.add(ponderacionField, 1, 3);

        Label criteriosLabel = new Label("Criterios de Evaluación");
        criteriosLabel.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));

        ListView<CriterioEvaluacion> criteriosListView = new ListView<>();
        ObservableList<CriterioEvaluacion> criteriosList = FXCollections.observableArrayList(
            criterioDao.getByResultado(ra.getId())
        );
        criteriosListView.setItems(criteriosList);
        criteriosListView.setCellFactory(lv -> new ListCell<CriterioEvaluacion>() {
            @Override
            protected void updateItem(CriterioEvaluacion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCodigo() + " - " + item.getDescripcion() + " (" + item.getPonderacion() + ")");
                }
            }
        });

        HBox criteriosButtons = new HBox(10);
        Button btnAddCriterio = new Button("Añadir Criterio");
        Button btnEditCriterio = new Button("Editar Criterio");
        Button btnDeleteCriterio = new Button("Eliminar Criterio");
        criteriosButtons.getChildren().addAll(btnAddCriterio, btnEditCriterio, btnDeleteCriterio);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(grid, criteriosLabel, criteriosListView, criteriosButtons);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar Resultado de Aprendizaje");
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(500);
        dialog.getDialogPane().setPrefHeight(450);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        btnAddCriterio.setOnAction(e -> {
            agregarCriterioEvaluacion(ra.getId(), criteriosList);
        });

        btnEditCriterio.setOnAction(e -> {
            CriterioEvaluacion selected = criteriosListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                editarCriterioEvaluacion(selected, criteriosList);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Advertencia");
                alert.setContentText("Seleccione un criterio para editar");
                alert.showAndWait();
            }
        });

        btnDeleteCriterio.setOnAction(e -> {
            CriterioEvaluacion selected = criteriosListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                criterioDao.delete(selected.getId());
                criteriosList.remove(selected);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Advertencia");
                alert.setContentText("Seleccione un criterio para eliminar");
                alert.showAndWait();
            }
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String codigo = codigoField.getText().trim();
                String descripcion = descripcionField.getText().trim();
                String ponderacionStr = ponderacionField.getText().trim();

                if (!codigo.isEmpty() && !descripcion.isEmpty() && !ponderacionStr.isEmpty()) {
                    try {
                        double ponderacion = Double.parseDouble(ponderacionStr);
                        ra.setCodigo(codigo);
                        ra.setDescripcion(descripcion);
                        ra.setPonderacion(ponderacion);
                        resultadoDao.update(ra);
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("La ponderación debe ser un número");
                        alert.showAndWait();
                    }
                }
            }
        });
    }

    private void agregarCriterioEvaluacion(int idResultado, ObservableList<CriterioEvaluacion> criteriosList) {
        int nextId = criterioDao.getNextId();

        TextField codigoField = new TextField();
        TextArea descripcionField = new TextArea();
        descripcionField.setPrefRowCount(3);
        TextField ponderacionField = new TextField("1.0");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("ID:"), 0, 0);
        grid.add(new Label(String.valueOf(nextId)), 1, 0);
        grid.add(new Label("Código:"), 0, 1);
        grid.add(codigoField, 1, 1);
        grid.add(new Label("Descripción:"), 0, 2);
        grid.add(descripcionField, 1, 2);
        grid.add(new Label("Ponderación:"), 0, 3);
        grid.add(ponderacionField, 1, 3);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Criterio de Evaluación");
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String codigo = codigoField.getText().trim();
                String descripcion = descripcionField.getText().trim();
                String ponderacionStr = ponderacionField.getText().trim();

                if (!codigo.isEmpty() && !descripcion.isEmpty() && !ponderacionStr.isEmpty()) {
                    try {
                        double ponderacion = Double.parseDouble(ponderacionStr);
                        CriterioEvaluacion ce = new CriterioEvaluacion(nextId, idResultado, codigo, descripcion, ponderacion);
                        criterioDao.insert(ce);
                        criteriosList.add(ce);
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("La ponderación debe ser un número");
                        alert.showAndWait();
                    }
                }
            }
        });
    }

    private void editarCriterioEvaluacion(CriterioEvaluacion ce, ObservableList<CriterioEvaluacion> criteriosList) {
        TextField codigoField = new TextField(ce.getCodigo());
        TextArea descripcionField = new TextArea(ce.getDescripcion());
        descripcionField.setPrefRowCount(3);
        TextField ponderacionField = new TextField(String.valueOf(ce.getPonderacion()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("ID:"), 0, 0);
        grid.add(new Label(String.valueOf(ce.getId())), 1, 0);
        grid.add(new Label("Código:"), 0, 1);
        grid.add(codigoField, 1, 1);
        grid.add(new Label("Descripción:"), 0, 2);
        grid.add(descripcionField, 1, 2);
        grid.add(new Label("Ponderación:"), 0, 3);
        grid.add(ponderacionField, 1, 3);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar Criterio de Evaluación");
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String codigo = codigoField.getText().trim();
                String descripcion = descripcionField.getText().trim();
                String ponderacionStr = ponderacionField.getText().trim();

                if (!codigo.isEmpty() && !descripcion.isEmpty() && !ponderacionStr.isEmpty()) {
                    try {
                        double ponderacion = Double.parseDouble(ponderacionStr);
                        ce.setCodigo(codigo);
                        ce.setDescripcion(descripcion);
                        ce.setPonderacion(ponderacion);
                        criterioDao.update(ce);
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("La ponderación debe ser un número");
                        alert.showAndWait();
                    }
                }
            }
        });
    }
}
