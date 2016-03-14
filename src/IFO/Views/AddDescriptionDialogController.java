package IFO.Views;

import IFO.Handler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class AddDescriptionDialogController implements Initializable {
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button addDescription;

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

    public void setAddDescription(ActionEvent event) {
        handler.addDescriptionToFiles(descriptionTextArea.getText(), keys);
        descriptionTextArea.setText("");
        stage.close();
    }
}
