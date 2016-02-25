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
import javafx.stage.Stage;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;

public class MoveFileToColDialogController implements Initializable {
    @FXML
    private ChoiceBox<Ifocol> choiceBox;
    @FXML
    private Button confirmButton;

    private Handler handler;
    private Ifocol selectedIfocol;
    private Stage stage;
    private HashSet<Integer> keys;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(Handler handler, Ifocol selectedIfocol, HashSet<Integer> keys) {
        this.handler = handler;
        this.selectedIfocol = selectedIfocol;
        this.keys = keys;
        Collection<Ifocol> values = handler.collections.values();
        ObservableList<Ifocol> choiceCollections = FXCollections.observableArrayList(values);
        choiceCollections.remove(selectedIfocol);
        choiceBox.setItems(choiceCollections);
    }

    public void getSelected(ActionEvent actionEvent) {
        handler.moveFilesBetweenCollections(selectedIfocol.name, choiceBox.getValue().name, keys);
        stage.close();
    }
}
