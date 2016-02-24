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

    Stage primaryStage;
    MainMenuController mainController;
    Handler handler;
    HashSet<Ifofile> nonExistentFiles;
    String pathToDB = System.getProperty("user.dir")+"\\dbexport.ifo";

    @Override
    public void start(Stage primaryStage) throws Exception {
        handler = new Handler();
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
            mainController.passHandler(handler);
            mainController.init(pathToDB);

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
        mainController.populateCollectionsListView();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stopping");
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
