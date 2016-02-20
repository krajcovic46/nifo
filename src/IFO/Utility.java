package IFO;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;

import java.io.File;

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
}
