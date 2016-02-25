package IFO.Views;


import IFO.Ifofile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class FileDialogController implements Initializable {
    @FXML
    private Label nameInfo;
    @FXML
    private Label pathInfo;
    @FXML
    private Label folderInfo;
    @FXML
    private Label descInfo;
    @FXML
    private Label tagsInfo;
    @FXML
    private GridPane gridPaneCUNT;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridPaneCUNT.setPadding(new Insets(10, 10, 10, 10));
    }

    public void showInfo(Ifofile file) {
        nameInfo.setText(file.getName());
        pathInfo.setText(file.getPath());
        folderInfo.setText(file.getParent());
        descInfo.setText(file.getDescription());
        tagsInfo.setText(file.getAllTags().toString());
    }
}
