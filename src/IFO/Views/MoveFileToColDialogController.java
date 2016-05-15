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

    private Handler handler;
    private Ifocol selectedIfocol;
    private Stage stage;
    private HashSet<Integer> keys;

    private boolean copy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(Handler handler, Ifocol selectedIfocol, HashSet<Integer> keys, boolean copy) {
        this.handler = handler;
        this.copy = copy;
        this.selectedIfocol = selectedIfocol;
        this.keys = keys;
        Collection<Ifocol> values = handler.collections.values();
        ObservableList<Ifocol> choiceCollections = FXCollections.observableArrayList(values);
        removeAllFromCols(choiceCollections);
        choiceCollections.remove(selectedIfocol);
        choiceBox.setItems(choiceCollections);
    }

    public void getSelected() {
        if (!copy)
            handler.moveFilesBetweenCollections(selectedIfocol.name, choiceBox.getValue().name, keys);
        else
            handler.copyFilesFromColToCol(selectedIfocol.name, choiceBox.getValue().name, keys);
        stage.close();
    }

    private void removeAllFromCols(Collection<Ifocol> values) {
        for (Ifocol col : values) {
            if (col.name.equals("All")) {
                values.remove(col);
                break;
            }
        }
    }
}
