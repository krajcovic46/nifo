package IFO.Views;

import IFO.Handler;
import IFO.Ifofile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class ShowAllTagsController implements Initializable {
    @FXML
    private ListView<String> tagsView;;

    ObservableList<String> tagsData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

//    public void setStage(Stage stage) {
//        this.stage = stage;
//    }

    public void init(Handler handler, LogicSearchController lsController) {
        ObservableList<String> tagsData = FXCollections.observableArrayList();
        tagsData.addAll(handler.allTags);

        tagsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tagsView.setItems(tagsData);
    }
}
