package IFO;

import javafx.scene.control.*;
import javafx.stage.*;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Utility {

    public static boolean withPath;
    public static HashSet<Integer> nonExistentFiles;

    public static String directoryChooser(String title, Stage primaryStage) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        File defaultDirectory = new File("C:");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(primaryStage);
        return selectedDirectory.getAbsolutePath();
    }

    public static String textInput(String title, String header, String contentText, String what) {
        TextInputDialog dialog = new TextInputDialog(what);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(contentText);

        Optional<String> result = dialog.showAndWait();
        //result.ifPresent(name -> System.out.println("Your name: " + name));
        System.out.println(" result.get: " + result.get() +" what: " + what);
        return (result.isPresent()) ? result.get() : what;
    }

    public static String fileChooser(String title, Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        return selectedFile.getAbsolutePath();
    }

    public static boolean deletionWarning(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        Optional<ButtonType> result = alert.showAndWait();
        return (result.get() == ButtonType.OK);
    }

    public static void createBeginningAlert(Handler handler, Stage primaryStage) {
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
                e.printStackTrace();
                createBeginningAlert(handler, primaryStage);
            }
        } else if (result.get() == importJSON) {
            try {
                handler.deserialize(Utility.fileChooser("Point to the DB file", primaryStage));
                /*TODO - treba skontrolovat ci vsetky fajly naozaj existuju
                * presunieme sa do status baru dolu
                * */
                nonExistentFiles = handler.checkFilesExistence();
            } catch (Exception e) {
                e.printStackTrace();
                createBeginningAlert(handler, primaryStage);
            }
        }
        else System.exit(0);
    }


}
