package IFO;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private ObservableList<Ifocol> collectionsData;
    private ObservableList<Ifofile> filesData;
    Object mainController;
    Handler handler;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeRoot(primaryStage);
        startTheJob();
        populateCollectionsListView();
    }

    public void initializeRoot(Stage primaryStage) {
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
            handler.go();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void populateCollectionsListView() {
        collectionsData = FXCollections.observableArrayList(handler.collections.values());

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
