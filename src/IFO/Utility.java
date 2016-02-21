package IFO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class Utility {

    public static String directoryChooser(String title, Stage primaryStage) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        File defaultDirectory = new File("C:");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(primaryStage);
        return selectedDirectory.getAbsolutePath();
    }

    public static String fileChooser(String title, Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        return selectedFile.getAbsolutePath();
    }

    public static Popup createPopup(String message) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(message);
        label.setOnMouseReleased(e -> popup.hide());
        popup.getContent().add(label);
        return popup;
    }

    public static void showPopup(String message, Stage primaryStage) {
        final Popup popup = createPopup(message);
        popup.setOnShown(e -> {
            popup.setX(primaryStage.getX() + primaryStage.getWidth()/2 - popup.getWidth()/2);
            popup.setY(primaryStage.getY() + primaryStage.getHeight()/2 - popup.getHeight()/2);
        });
        popup.show(primaryStage);
    }

    public static void setupTheMenu(Object mainController, Handler handler, String pathToDB) {
        MenuItem dbImport = ((FXMLController) mainController).dbImport;
        dbImport.setOnAction(t -> {
            try {
                handler.deserialize(pathToDB);
            } catch (IOException e) {
                /*TODO - treba ozmanit pouzivatelovi ak sa nepodaril import - nejaky popup?
                * DOLU DAME STAVOVY RIADOK, TO BUDE TOP*/
                e.printStackTrace();
            }
        });

        MenuItem dbExport = ((FXMLController) mainController).dbExport;
        dbExport.setOnAction(t -> {
            try {
                handler.export(pathToDB);
            } catch (IOException e) {
                /*TODO - treba ozmanit pouzivatelovi ak sa nepodaril export - nejaky popup?
                * DOLU DAME STAVOVY RIADOK, TO BUDE TOP*/
                e.printStackTrace();
            }
        });
    }

    public static void createBeginningAlert(Handler handler, Stage primaryStage, Set<Ifofile> nonExistentFiles) {
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
                createBeginningAlert(handler, primaryStage, nonExistentFiles);
            }
        } else if (result.get() == importJSON) {
            try {
                handler.deserialize(Utility.fileChooser("Point to the DB file", primaryStage));
                /*TODO - treba skontrolovat ci vsetky fajly naozaj existuju
                * zistit, ci toto funguje
                * */
                nonExistentFiles = handler.checkFilesExistence();
                Utility.showPopup("Couldn't find " +
                        String.valueOf(nonExistentFiles.size() + " files"), primaryStage);

            } catch (Exception e) {
                createBeginningAlert(handler, primaryStage, nonExistentFiles);
            }
        }
        else System.exit(0);
    }

    public static void populateCollectionsListView(Handler handler, Object mainController,
                                                   ObservableList<Ifocol> collectionsData,
                                                   ObservableList<Ifofile> filesData) {
        collectionsData = FXCollections.observableArrayList(handler.collections.values()).sorted();

        ListView<Ifocol> colView = ((FXMLController) mainController).collectionsView;
        colView.setItems(collectionsData);

        colView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showFilesInCollections(handler,
                        mainController, filesData, newValue));
    }

    public static void showFilesInCollections(Handler handler, Object mainController,
                                              ObservableList<Ifofile> filesData, Ifocol col) {
        filesData = FXCollections.observableArrayList();
        for (Integer id : col.getFilesInside())
            filesData.add(handler.files.get(id));
        ListView<Ifofile> filView = ((FXMLController) mainController).filesView;
        filView.setItems(filesData);
        filView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Ifofile currentItemSelected = filView.getSelectionModel()
                        .getSelectedItem();
            }
        });
    }
}
