package IFO.Views;

import IFO.Handler;
import IFO.Ifocol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    }

    public void setAddTag(ActionEvent event) {
        handler.addTagToFiles(tagTextField.getText(), keys);
        tagTextField.setText("");
    }

    public void setAddTags(ActionEvent event) {
        HashSet<String> tags = new HashSet<>(Arrays.asList(tagTextArea.getText().split(",")));
        handler.addTagsToFiles(tags, keys);
        tagTextArea.setText("");
    }


}
