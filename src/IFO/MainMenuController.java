package IFO;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

public class MainMenuController {

    @FXML
    public ListView<Ifocol> collectionsView;

    @FXML
    public ListView<Ifofile> filesView;

    @FXML
    public MenuItem dbExport;

    @FXML
    public MenuItem dbImport;
}
