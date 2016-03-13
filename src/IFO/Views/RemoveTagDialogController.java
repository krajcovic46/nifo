package IFO.Views;

import IFO.Handler;
import IFO.Ifofile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class RemoveTagDialogController implements Initializable {
    @FXML
    private ListView<String> tagsView;
    @FXML
    private Button removeTags;

    private Handler handler;
    private Stage stage;
    private HashSet<Integer> keys;
    private ObservableList<String> selectedItems;

    ObservableList<String> tagsData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(Handler handler, HashSet<Integer> keys) {
        this.handler = handler;
        this.keys = keys;

        HashSet<String> tags = new HashSet<>();

        for (Integer k : keys)
            tags.addAll(handler.getFiles().get(k).getAllTags());

        tagsData = FXCollections.observableArrayList();
        tagsData.addAll(tags);

        tagsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tagsView.setItems(tagsData);
        tagsView.getSelectionModel().selectedItemProperty().addListener(t -> {
            selectedItems = tagsView.getSelectionModel().getSelectedItems();
        });
    }

    public void setRemoveTags(ActionEvent event) {
        for (String tag : selectedItems)
            for (Integer k : keys) {
                handler.getFiles().get(k).removeTag("", tag);
                tagsData.remove(tag);
            }
        tagsView.setItems(tagsData);
    }
}