package IFO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Main extends Application {

    private ObservableList<Ifocol> collectionsData;
    private ObservableList<Ifofile> filesData;
    Stage primaryStage;
    Object mainController;
    Handler handler;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeRoot(primaryStage);
        startTheJob();

    }

    public void initializeRoot(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
            Parent root  = loader.load();

            mainController = loader.getController();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Intelingentý organizátor súborov");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startTheJob() {
        handler = new Handler();
        try {
            handler.deserialize("");
        }
        catch (IOException e) {
            createBeginningAlert();
        }
    }

    void createBeginningAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("No files");
        alert.setHeaderText("There are no files loaded in the database");
        alert.setContentText("Please select appropriate action.");
        ButtonType importFiles = new ButtonType("Import files");
        ButtonType importJSON = new ButtonType("Import DB file");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(importFiles, importJSON, cancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == importFiles){
            try {
                handler.fillInternalStructures(Utility.directoryChooser("Point to a directory", primaryStage), true);
            } catch (Exception e) {
                createBeginningAlert();
            }
        } else if (result.get() == importJSON) {
            try {
                handler.deserialize(Utility.fileChooser("Point to the DB file", primaryStage));
                /*TODO - treba skontrolovat ci vsetky fajly naozaj existuju
                * handler.checkFilesExistence();
                * zabalit do nejakej metodky ktora potom aj vyznaci ktore subory nenaslo
                * pouzit popup z Utility
                * */
            } catch (Exception e) {
                try {
                    handler.fillInternalStructures(
                            Utility.directoryChooser(
                                    "Import of the DB failed, point to a directory", primaryStage), true);
                } catch (Exception d) {
                    createBeginningAlert();
                }
            }
        }
        else System.exit(0);

        populateCollectionsListView();
    }

    void populateCollectionsListView() {
        collectionsData = FXCollections.observableArrayList(handler.collections.values()).sorted();

        ListView<Ifocol> colView = ((FXMLController) mainController).collectionsView;
        colView.setItems(collectionsData);

        colView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showFilesInCollections(newValue));
    }

    void showFilesInCollections(Ifocol col) {
        filesData = FXCollections.observableArrayList();
        for (Integer id : col.getFilesInside())
            filesData.add(handler.files.get(id));
        ListView<Ifofile> filView = ((FXMLController) mainController).filesView;
        filView.setItems(filesData);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
