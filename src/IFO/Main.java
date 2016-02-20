package IFO;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

public class Main extends Application {

    ObservableList<Ifocol> collectionsData;
    ObservableList<Ifofile> filesData;
    Stage primaryStage;
    Object mainController;
    Handler handler;
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
            loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
            Parent root  = loader.load();

            mainController = loader.getController();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Intelingentý organizátor súborov");

            Utility.setupTheMenu(mainController, handler, pathToDB);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startTheJob() {
        handler = new Handler();
        try {
            handler.deserialize(pathToDB);
        }
        catch (IOException e) {
            Utility.createBeginningAlert(handler, primaryStage, nonExistentFiles);
        }
        Utility.populateCollectionsListView(handler, mainController, collectionsData, filesData);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
