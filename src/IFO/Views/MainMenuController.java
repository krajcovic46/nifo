package IFO.Views;

import IFO.Handler;
import IFO.Ifocol;
import IFO.Ifofile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class MainMenuController {

    @FXML
    public ListView<Ifocol> collectionsView;

    @FXML
    public ListView<Ifofile> filesView;

    @FXML
    public MenuItem dbExport;

    @FXML
    public MenuItem dbImport;

    public void setupTheMenu(Handler handler, String pathToDB) {
        dbImport.setOnAction(t -> {
            try {
                handler.deserialize(pathToDB);
            } catch (IOException e) {
                /*TODO - treba ozmanit pouzivatelovi ak sa nepodaril import - nejaky popup?
                * DOLU DAME STAVOVY RIADOK, TO BUDE TOP*/
                e.printStackTrace();
            }
        });

        dbExport.setOnAction(t -> {
            try {
                handler.export(pathToDB);
            } catch (IOException e) {
                /*TODO - treba ozmanit pouzivatelovi ak sa nepodaril export - nejaky popup?
                * DOLU DAME STAVOVY RIADOK, TO BUDE TOP*/
                e.printStackTrace();
            }
        });
    }

    public void populateCollectionsListView(Handler handler, ObservableList<Ifocol> collectionsData,
                                                   ObservableList<Ifofile> filesData) {
        collectionsData = FXCollections.observableArrayList(handler.collections.values()).sorted();

        collectionsView.setItems(collectionsData);

        collectionsView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showFilesInCollections(handler,filesData, newValue));
    }

    public void showFilesInCollections(Handler handler,ObservableList<Ifofile> filesData, Ifocol col) {
        filesData = FXCollections.observableArrayList();
        for (Integer id : col.getFilesInside())
            filesData.add(handler.files.get(id));
        filesView.setItems(filesData);
        filesView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Ifofile currentItemSelected = filesView.getSelectionModel()
                        .getSelectedItem();

            }
        });
    }
}
