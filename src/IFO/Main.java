package IFO;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;

public class Main extends Application {

    private ObservableList<Ifocol> collectionsData;
    private ObservableList<Ifofile> filesData;
    Object mainController;
    Temp t;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeRoot(primaryStage);
        startTheJob();
        populateCollectionsListView();
    }

    public void initializeRoot(Stage primaryStage) {
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("sample.fxml"));
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
        /*TODO - needs changing, cant have main control methods in Temp.java (duh)*/
        t = new Temp();
        try {
            t.go();
        }
        catch (IOException e) {
        }
    }

    void populateCollectionsListView() {
        t.addFilesToCollection("Neviem", new Integer[]{1,2,3,4,5,6,7,8});
        t.addFilesToCollection("Ultra", new Integer[]{9,10,11,12});
        collectionsData = FXCollections.observableArrayList(t.collections.values());

        ListView<Ifocol> colView = ((FXMLController) mainController).collectionsView;
        colView.setItems(collectionsData);

        colView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showFilesInCollections(newValue));
    }

    void showFilesInCollections(Ifocol col) {
        filesData = FXCollections.observableArrayList();
        for (Integer id : col.getFilesInside())
            filesData.add(t.files.get(id));
        ListView<Ifofile> filView = ((FXMLController) mainController).filesView;
        filView.setItems(filesData);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
