package IFO.Views;


import IFO.Ifofile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class FileDialogController {
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
    private Button cancelButton;

    private Stage stage;

    public void showInfo(Ifofile file) {
        nameInfo.setText(file.getName());
        pathInfo.setText(file.getPath());
        folderInfo.setText(file.getParent());
        descInfo.setText(file.getDescription());
        tagsInfo.setText(file.getAllTags().toString());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }
}
