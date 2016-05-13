package IFO.Views;

import IFO.Handler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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

    private HashMap<TextField, TextField> fieldsSet = new HashMap<>();
    private Integer lastRow;

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
        lastRow = 3;
    }

//    public void setAddLine() {
//        TextField newTF = new TextField();
//        TextField newTFnegation = new TextField();
//        baseGridPane.addRow(++lastRow);//
//        baseGridPane.add(newTF, 1, lastRow);
//        baseGridPane.add(newTFnegation, 2, lastRow);
//        fieldsSet.put(newTF, newTFnegation);
//    }

    public void setSearch() {
        handler.logicSearchCore(fieldsSet);
        stage.close();
    }
}
