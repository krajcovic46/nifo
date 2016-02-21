package IFO;

import IFO.Views.FileDialogController;
import IFO.Views.MainMenuController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
            mainController.setPrimaryStage(this.primaryStage);
            mainController.setupTheMenu(handler, pathToDB);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Inteligentný organizátor súborov");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception d) {
            d.printStackTrace();
        }
    }

    private void startTheJob() {
        try {
            handler.deserialize(pathToDB);
        }
        catch (IOException e) {
            Utility.createBeginningAlert(handler, primaryStage, nonExistentFiles);
        }
        mainController.populateCollectionsListView(handler);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
