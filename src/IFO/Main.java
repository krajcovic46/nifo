package IFO;

import IFO.Views.FileDialogController;
import IFO.Views.MainMenuController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sun.plugin.javascript.navig.Anchor;

import java.io.IOException;
import java.util.HashSet;

public class Main extends Application {

    ObservableList<Ifocol> collectionsData;
    ObservableList<Ifofile> filesData;
    Stage primaryStage;
    MainMenuController mainController;
    Handler handler = new Handler();
    HashSet<Ifofile> nonExistentFiles;
    String pathToDB = System.getProperty("user.dir")+"\\dbexport.ifo";

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeRoot(primaryStage);
        startTheJob();

    }

    public void initializeRoot(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("Views/GUI.fxml"));
            Parent root  = loader.load();

            mainController = loader.getController();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Intelingentý organizátor súborov");

            mainController.setupTheMenu(handler, pathToDB);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception d) {
            d.printStackTrace();
        }
    }

    private void initializeFileDialogController() throws Exception {
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
        fdController.setStage(dialogStage);

    }

    private void startTheJob() {
        try {
            handler.deserialize(pathToDB);
        }
        catch (IOException e) {
            Utility.createBeginningAlert(handler, primaryStage, nonExistentFiles);
        }
        mainController.populateCollectionsListView(handler, collectionsData, filesData);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
