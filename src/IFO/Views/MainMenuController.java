package IFO.Views;

import IFO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.NoSuchElementException;

public class MainMenuController {

    @FXML
    private ListView<Ifocol> collectionsView;
    @FXML
    private ListView<Ifofile> filesView;
    @FXML
    private MenuItem importFiles;
    @FXML
    private MenuItem dbImport;
    @FXML
    private MenuItem dbExport;
    @FXML
    private TextField filterField;
    @FXML
    private Label stateLabel;
    @FXML
    private Button addEmptyCol;
    @FXML
    private Button addColFromSelection;
    @FXML
    private Button deleteCol;
    @FXML
    private Button renameCol;

    private Stage primaryStage;
    private Handler handler;
    private Integer[] selectedFiles;
    private Ifocol selectedCollection;


    public void setPrimaryStage(Stage stage) {
        primaryStage = stage;
        stateLabel.setText("Welcome to \"Inteligentný organizátor súborov\".");
    }

    public void passHandler(Handler handler) {
        this.handler = handler;
    }

    public void init(String pathToDB) {
        setupTheMenu(pathToDB);
        createToolbarButtons();
    }

    private void createToolbarButtons() {
        Image emptyColImg = new Image(getClass().getResourceAsStream("Images/newempcol.png"));
        addEmptyCol.setGraphic(new ImageView(emptyColImg));
        addEmptyCol.setOnAction(t -> {
            try {
                String newCollectionName = Utility.textInput("Enter name for new collection", "New Collection");
                if (handler.createAnEmptyCollection(newCollectionName)) {
                    addDataToView();
                    stateLabel.setText("\"" + newCollectionName + "\" collection has been added successfully.");
                } else
                    stateLabel.setText("A collection with name "+ "\"" + newCollectionName + "\" already exists.");
            } catch (NoSuchElementException ignored) {}
        });
        addEmptyCol.setTooltip(new Tooltip("Create an empty collection."));

        Image selectionColImg = new Image(getClass().getResourceAsStream("Images/newselcol.png"));
        addColFromSelection.setGraphic(new ImageView(selectionColImg));
        addColFromSelection.setOnAction(t -> {
            try {
                String newCollectionName = Utility.textInput("Enter name for new collection", "New Collection");
                if (handler.addFilesToCollection(newCollectionName, selectedFiles)) {
                    addDataToView();
                    stateLabel.setText("\"" + newCollectionName + "\" collection has been added successfully.");
                } else
                    stateLabel.setText("A collection with name "+ "\"" + newCollectionName + "\" already exists.");
            } catch (NoSuchElementException ignored) {}
        });
        addColFromSelection.setTooltip(new Tooltip("Create a collection from selected files."));

        Image deleteColImg = new Image(getClass().getResourceAsStream("Images/deletecol.png"));
        deleteCol.setGraphic(new ImageView(deleteColImg));
        deleteCol.setOnAction(event -> {
            if (!selectedCollection.isEmpty()) {
                if (Utility.deletionWarning("Warning"))
                    handler.deleteACollection(selectedCollection.name);
            }
            else
                handler.deleteACollection(selectedCollection.name);
            addDataToView();
        });
        deleteCol.setTooltip(new Tooltip("Delete a collection."));
        deleteCol.setDisable(true);

        Image renameColImg = new Image(getClass().getResourceAsStream("Images/renamecol.png"));
        renameCol.setGraphic(new ImageView(renameColImg));
        renameCol.setOnAction(event -> {
            handler.renameACollection(selectedCollection.name, Utility.textInput("Rename a colleciton", "New Name"));
            addDataToView();
        });
        renameCol.setTooltip(new Tooltip("Rename a collection."));
        renameCol.setDisable(true);

    }

    private void setupTheMenu(String pathToDB) {
        importFiles.setOnAction(t -> {
            try {
                handler.fillInternalStructures(Utility.directoryChooser("Add files", primaryStage), true);
                addDataToView();
                handler.export(pathToDB);
            } catch (Exception e) {
                stateLabel.setText("Could not add files, please try again.");
            }
        });

        dbImport.setOnAction(t -> {
            try {
                handler.deserialize(pathToDB);
                stateLabel.setText("Import successful.");
            } catch (IOException e) {
                stateLabel.setText("Import unsuccessful, please try again.");
                e.printStackTrace();
            }
        });

        dbExport.setOnAction(t -> {
            try {
                handler.export(pathToDB);
                stateLabel.setText("Export/Save successful.");
            } catch (IOException e) {
                stateLabel.setText("Export/Save unsucessful, please try again.");
                e.printStackTrace();
            }
        });
    }

    public void populateCollectionsListView() {

        addDataToView();

        collectionsView.getSelectionModel().selectedItemProperty().addListener(t -> {
            selectedCollection = collectionsView.getSelectionModel().getSelectedItem();
            deleteCol.setDisable(false);
            renameCol.setDisable(false);
        });

        collectionsView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null)
                        showFilesInCollections(newValue);
                });
    }

    public void showFilesInCollections(Ifocol col) {
        ObservableList<Ifofile> filesData = FXCollections.observableArrayList();
        for (Integer id : col.getFilesInside())
            filesData.add(handler.getFiles().get(id));

        FilteredList<Ifofile> filteredData = new FilteredList<>(filesData, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            // kind of magic - vracia to true ak to splna podmienky medzi ORmi
            filteredData.setPredicate(file -> newValue == null ||
                    newValue.isEmpty() || file.getName().toLowerCase().contains(newValue.toLowerCase()));
        });
        filesView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        filesView.setItems(filteredData);

        filesView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Ifofile currentItemSelected = filesView.getSelectionModel().getSelectedItem();
                try {
                    initializeFileDialogController(currentItemSelected);
                } catch (Exception d) {
                    d.printStackTrace();
                }
            }
        });

        filesView.getSelectionModel().selectedItemProperty().addListener(t -> {
            ObservableList<Ifofile> selectedItems = filesView.getSelectionModel().getSelectedItems();
//            System.out.println(selectedItems);
            /*TODO - shift-click stale nefunguje ffs*/
            selectedFiles = new Integer[selectedItems.size()];
            for (int i = 0; i < selectedItems.size(); i++) {
                selectedFiles[i] = selectedItems.get(i).getId();
            }
        });
    }

    private void addDataToView() {
        /*TODO - optimalize this -> called too many times, sorted and copied each time, could be
        * catastrophic in bigger DBs*/
        ObservableList<Ifocol> collectionsData =
                FXCollections.observableArrayList(handler.collections.values()).sorted();

        collectionsView.setItems(collectionsData);
    }

    private void initializeFileDialogController(Ifofile file) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Views/FileDialog.fxml"));
        Parent page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Show Info");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        FileDialogController fdController = loader.getController();
        fdController.showInfo(file);

        dialogStage.showAndWait();
    }
}
