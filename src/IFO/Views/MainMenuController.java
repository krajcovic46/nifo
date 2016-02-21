package IFO.Views;

import IFO.Handler;
import IFO.Ifocol;
import IFO.Ifofile;
import IFO.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private ListView<Ifocol> collectionsView;
    @FXML
    private ListView<Ifofile> filesView;
    @FXML
    private MenuItem dbExport;
    @FXML
    private MenuItem dbImport;
    @FXML
    private TextField filterField;
    @FXML
    private Label stateLabel;

    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        primaryStage = stage;
        stateLabel.setText("Welcome to \"Inteligentný organizátor súborov\".");
    }

    public void setupTheMenu(Handler handler, String pathToDB) {
        dbImport.setOnAction(t -> {
            try {
                handler.deserialize(pathToDB);
                stateLabel.setText("Import successful.");
            } catch (IOException e) {
                stateLabel.setText("Import unsuccessful, please try again.");
                e.printStackTrace();
            }
        });

        dbExport.setOnAction(t -> {
            try {
                handler.export(pathToDB);
                stateLabel.setText("Export successful.");
            } catch (IOException e) {
                stateLabel.setText("Export unsucessful, please try again.");
                e.printStackTrace();
            }
        });
    }

    public void populateCollectionsListView(Handler handler) {
        ObservableList<Ifocol> collectionsData =
                FXCollections.observableArrayList(handler.collections.values()).sorted();

        collectionsView.setItems(collectionsData);

        collectionsView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showFilesInCollections(handler, newValue));
    }

    public void showFilesInCollections(Handler handler, Ifocol col) {
        ObservableList<Ifofile> filesData = FXCollections.observableArrayList();
        for (Integer id : col.getFilesInside())
            filesData.add(handler.files.get(id));

        FilteredList<Ifofile> filteredData = new FilteredList<>(filesData, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(file -> {
                return newValue == null || newValue.isEmpty() || file.getName().toLowerCase().contains(newValue.toLowerCase());
            });
        });
        filesView.setItems(filteredData);

        filesView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Ifofile currentItemSelected = filesView.getSelectionModel()
                        .getSelectedItem();
                try {
                    initializeFileDialogController(currentItemSelected);
                } catch (Exception d) {
                    d.printStackTrace();
                }
            }
        });
    }

    private void initializeFileDialogController(Ifofile file) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Views/FileDialog.fxml"));
        Parent page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Show Info");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        FileDialogController fdController = loader.getController();
        fdController.showInfo(file);

        dialogStage.showAndWait();
    }
}
