package IFO.Views;

import IFO.Handler;
import IFO.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class LogicSearchController implements Initializable {
    @FXML
    private GridPane baseGridPane;
    @FXML
    private TextField tf11;
    @FXML
    private TextField tf21;
    @FXML
    private TextField tf12;
    @FXML
    private TextField tf22;
    @FXML
    private TextField tf13;
    @FXML
    private TextField tf23;

    private Handler handler;
    private Stage stage;

    @FXML
    private ListView<String> tagsView;

    private HashMap<TextField, TextField> fieldsSet = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(Handler handler) {
        this.handler = handler;
        fieldsSet.put(tf11, tf21);
        fieldsSet.put(tf12, tf22);
        fieldsSet.put(tf13, tf23);

        ObservableList<String> tagsData = FXCollections.observableArrayList();
        tagsData.addAll(handler.allTags);

        tagsView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tagsView.setItems(tagsData);
        tagsView.setFocusTraversable(false);
    }

    public void setSearch() {
        handler.logicSearchCore(fieldsSet);
        stage.close();
    }

//    public void showAllTags() {
//        try {
//            initializeShowAllTagsController();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void initializeShowAllTagsController() throws Exception {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(Main.class.getResource("Views/ShowAllTags.fxml"));
//        Parent page = loader.load();
//        Stage dialogStage = new Stage();
//        dialogStage.setTitle("");
//        dialogStage.initModality(Modality.WINDOW_MODAL);
//        dialogStage.initOwner(stage);
//        Scene scene = new Scene(page);
//        dialogStage.setScene(scene);
//
//        ShowAllTagsController satController = loader.getController();
//        satController.init(handler, this);
//
//        dialogStage.showAndWait();
//    }
}
