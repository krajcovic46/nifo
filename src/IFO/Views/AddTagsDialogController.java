package IFO.Views;

import IFO.Handler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;

public class AddTagsDialogController implements Initializable {
    @FXML
    private TextField tagTextField;
    @FXML
    private TextArea tagTextArea;
    @FXML
    private Button addTag;
    @FXML
    private Button addTags;
    @FXML
    private javafx.scene.control.Label tagsListLabel;
    @FXML
    private ListView<String> tagsView;

    private Handler handler;
    private Stage stage;
    private HashSet<Integer> keys;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(Handler handler, HashSet<Integer> keys) {
        this.handler = handler;
        this.keys = keys;

        tagTextField.requestFocus();

        HashSet<String> tags = new HashSet<>();

        for (Integer k : this.keys) {
            tags.addAll(handler.getFiles().get(k).getAllTags());
        }

        tagsListLabel.setText((tags.size() == 0) ? "no tags in this file" : tags.toString());

        ObservableList<String> tagsData = FXCollections.observableArrayList();
        tagsData.addAll(handler.allTags);

        tagsView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tagsView.setItems(tagsData);
        tagsView.setFocusTraversable(false);

    }

    public void setAddTag() {
        handler.addTagToFiles(tagTextField.getText(), keys);
        tagTextField.setText("");
        init(handler, keys);
    }

    public void setAddTags() {
        HashSet<String> tags = new HashSet<>(Arrays.asList(tagTextArea.getText().trim().split(",")));
        handler.addTagsToFiles(tags, keys);
        tagTextArea.setText("");
        init(handler, keys);
    }


}
