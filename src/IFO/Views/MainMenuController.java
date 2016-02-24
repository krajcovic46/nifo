package IFO.Views;

import IFO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private ListView<Ifocol> collectionsView;
    @FXML
    private ListView<Ifofile> filesView;
    @FXML
    private MenuItem addFiles;
    @FXML
    private MenuItem dbImport;
    @FXML
    private MenuItem dbExport;
    @FXML
    private TextField filterField;
    @FXML
    private Label stateLabel;
    @FXML
    private Button refreshButton;
    @FXML
    private Button addFilesButton;
    @FXML
    private Button addEmptyCol;
    @FXML
    private Button addColFromSelection;
    @FXML
    private Button deleteCol;
    @FXML
    private Button renameCol;
    @FXML
    private Button addTagsToFile;
    @FXML
    private Button moveFileToCollection;

    private String pathToDB;
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
        this.pathToDB = pathToDB;
        customizeToolbarButtons();
    }

    private void _refresh() {
        UpdateableListViewSkin.cast(collectionsView.getSkin()).refresh();
        UpdateableListViewSkin.cast(filesView.getSkin()).refresh();
    }

    private void customizeToolbarButtons() {
        customizeRefreshButton();

        customizeAddEmptyColButton();

        customizeAddColFromSelectionButton();

        customizeDeleteColButton();

        customizeRenameColButton();

        customizeAddTagsToFileButton();
    }

    private void customizeRefreshButton() {
        Image refreshButtImg = new Image(getClass().getResourceAsStream(""));
        refreshButton.setGraphic(new ImageView(refreshButtImg));
        refreshButton.setTooltip(new Tooltip("Refresh"));
    }

    private void customizeAddEmptyColButton() {
        Image emptyColImg = new Image(getClass().getResourceAsStream("Images/newempcol.png"));
        addEmptyCol.setGraphic(new ImageView(emptyColImg));
        addEmptyCol.setTooltip(new Tooltip("Create an empty collection."));
    }

    private void customizeAddColFromSelectionButton(){
        Image selectionColImg = new Image(getClass().getResourceAsStream("Images/newselcol.png"));
        addColFromSelection.setGraphic(new ImageView(selectionColImg));
        addColFromSelection.setTooltip(new Tooltip("Create a collection from selected files."));
        addColFromSelection.setDisable(true);
    }

    private void customizeDeleteColButton() {
        Image deleteColImg = new Image(getClass().getResourceAsStream("Images/deletecol.png"));
        deleteCol.setGraphic(new ImageView(deleteColImg));
        deleteCol.setTooltip(new Tooltip("Delete a collection."));
        deleteCol.setDisable(true);
    }

    private void customizeRenameColButton() {
        Image renameColImg = new Image(getClass().getResourceAsStream("Images/renamecol.png"));
        renameCol.setGraphic(new ImageView(renameColImg));
        renameCol.setTooltip(new Tooltip("Rename a collection."));
        renameCol.setDisable(true);
    }

    private void customizeAddTagsToFileButton() {
        Image addTagsToFileImg = new Image(getClass().getResourceAsStream("Images/renamecol.png"));
        addTagsToFile.setGraphic(new ImageView(addTagsToFileImg));
        addTagsToFile.setTooltip(new Tooltip("Add tags to the selected file."));
    }

    public void populateCollectionsListView() {
        ObservableList<Ifocol> collectionsData =
                FXCollections.observableArrayList(handler.collections.values()).sorted();
        collectionsView.setItems(collectionsData);

        collectionsView.getSelectionModel().selectedItemProperty().addListener(t -> {
            //System.out.println("selected: " + collectionsView.getSelectionModel().getSelectedItem());
            selectedCollection = collectionsView.getSelectionModel().getSelectedItem();

            System.out.println(Arrays.toString(selectedFiles));
            boolean isSelectedButNotAll = selectedCollection != null && !selectedCollection.name.equals("All");
            deleteCol.setDisable(!isSelectedButNotAll);
            renameCol.setDisable(!isSelectedButNotAll);
            if (isSelectedButNotAll) {
                boolean isEmpty = selectedCollection.getFilesInside() == null ||
                        selectedCollection.getFilesInside().size() == 0 || selectedFiles == null;
                addColFromSelection.setDisable(isEmpty);
            }
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
            /*TODO - shift-click stale nefunguje ffs*/
            selectedFiles = new Integer[selectedItems.size()];
            for (int i = 0; i < selectedItems.size(); i++)
                /*TODO - sem dat asi inu datovu strukturu pretoze je to retardovane*/
                selectedFiles[i] = selectedItems.get(i).getId();
            System.out.println(Arrays.toString(selectedFiles));
            addColFromSelection.setDisable(false);
        });
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UpdateableListViewSkin<Ifocol> colSkin = new UpdateableListViewSkin<>(collectionsView);
        UpdateableListViewSkin<Ifofile> filSkin = new UpdateableListViewSkin<>(filesView);
        collectionsView.setSkin(colSkin);
        filesView.setSkin(filSkin);
    }

    @FXML
    public void setAddFiles(ActionEvent actionEvent) {
        try {
            handler.fillInternalStructures(Utility.directoryChooser("Add files", primaryStage), true);
            _refresh();
            handler.export(pathToDB);
        } catch (Exception e) {
            stateLabel.setText("Could not add files, please try again.");
        }
    }

    @FXML
    public void setDbImport() {
        try {
            handler.deserialize(pathToDB);
            stateLabel.setText("Import successful.");
        } catch (IOException e) {
            stateLabel.setText("Import unsuccessful, please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    public void setDbExport(ActionEvent event) {
        try {
            handler.export(pathToDB);
            stateLabel.setText("Export/Save successful.");
        } catch (IOException e) {
            stateLabel.setText("Export/Save unsucessful, please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    public void refresh(ActionEvent event) {
        _refresh();
    }

    @FXML
    public void setAddEmptyCol(ActionEvent event) {
        try {
            String newCollectionName = Utility.textInput("Enter name for new collection", "New Collection");
            if (handler.createAnEmptyCollection(newCollectionName)) {
                _refresh();
                stateLabel.setText("\"" + newCollectionName + "\" collection has been added successfully.");
            } else
                stateLabel.setText("A collection with name "+ "\"" + newCollectionName + "\" already exists.");
        } catch (NoSuchElementException ignored) {}
    }

    @FXML
    public void setAddColFromSelection() {
        try {
            String newCollectionName = Utility.textInput("Enter name for new collection", "New Collection");
            if (handler.addFilesToCollection(newCollectionName, selectedFiles)) {
                _refresh();
                stateLabel.setText("\"" + newCollectionName + "\" collection has been added successfully.");
            } else
                stateLabel.setText("A collection with name "+ "\"" + newCollectionName + "\" already exists.");
        } catch (NoSuchElementException ignored) {}
    }

    @FXML
    public void setDeleteCol(ActionEvent event) {
        if (!selectedCollection.isEmpty()) {
            if (Utility.deletionWarning("Warning")) {
                handler.deleteACollection(selectedCollection.name);
                filesView.setItems(null);
            }
        }
        else
            handler.deleteACollection(selectedCollection.name);
        _refresh();
    }

    @FXML
    public void setRenameCol(ActionEvent event) {
        try {
            if (handler.renameACollection(selectedCollection.name,
                    Utility.textInput("Rename a collection", "New Name")))
                stateLabel.setText("Collection successfuly renamed.");
            else
                stateLabel.setText("A collection with the same name already exists.");
        } catch (Exception ignored) {}
        _refresh();
    }

    @FXML
    public void setAddTagsToFile(ActionEvent event) {

    }
}
