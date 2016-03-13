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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
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
    private Button exportButton;
    @FXML
    private Button addEmptyColButton;
    @FXML
    private Button addColFromSelectionButton;
    @FXML
    private Button deleteColButton;
    @FXML
    private Button renameColButton;
    @FXML
    private Button addTagsToFileButton;
    @FXML
    private Button moveFileToCollectionButton;
    @FXML
    private Button addDescription;
    @FXML
    private Button removeTags;

    private String pathToDB;
    private Stage primaryStage;
    private Handler handler;
    private HashSet<Integer> selectedFiles;
    private Ifocol selectedCollection;
    private ObservableList<Ifocol> collectionsData;

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

    private void _updateCollectionsView() {
        collectionsData = FXCollections.observableArrayList(handler.collections.values()).sorted();
        collectionsView.setItems(collectionsData);
    }

    private void customizeToolbarButtons() {
        customizeButton(refreshButton, "Images/refresh.png", "Refresh", false);
        customizeButton(addFilesButton, "Images/addfiles.png", "Add new files to the database", false);
        customizeButton(exportButton, "Images/export.png", "Export/Save", false);
        customizeButton(addEmptyColButton, "Images/newempcol.png", "Create an empty collection", false);
        customizeButton(addColFromSelectionButton, "Images/newselcol.png", "Create a collection from selected files", true);
        customizeButton(deleteColButton, "Images/deletecol.png", "Delete a collection", true);
        customizeButton(renameColButton, "Images/renamecol.png", "Rename a collection", true);
        customizeButton(addTagsToFileButton, "Images/addtags.png", "Add tags to the selected file.", true);
        customizeButton(moveFileToCollectionButton, "Images/movefiletocol.png", "Move file to another collection",
                true);
        customizeButton(addDescription, "Images/adddescription.png", "Add description to file", true);
        customizeButton(removeTags, "Images/removefile.png", "Remove tags from a file", true);
    }

    private void customizeButton(Button button, String pathToImage, String tooltip, boolean disabled) {
        Image image = new Image(getClass().getResourceAsStream(pathToImage));
        button.setGraphic(new ImageView(image));
        button.setTooltip(new Tooltip(tooltip));
        button.setDisable(disabled);
    }

    public void populateCollectionsListView() {
        collectionsData =
                FXCollections.observableArrayList(handler.collections.values()).sorted();
        collectionsView.setItems(collectionsData);

        collectionsView.getSelectionModel().selectedItemProperty().addListener(t -> {
            _refresh();

            selectedCollection = collectionsView.getSelectionModel().getSelectedItem();

            boolean isSelectedButNotAll = selectedCollection != null && !selectedCollection.name.equals("All");
            deleteColButton.setDisable(!isSelectedButNotAll);
            renameColButton.setDisable(!isSelectedButNotAll);
            if (isSelectedButNotAll) {
                boolean isEmpty;
                isEmpty = selectedFiles == null || selectedFiles.size() == 0 ||
                        selectedCollection.getFilesInside() == null || selectedCollection.getFilesInside().size() == 0;
                disableChosenButtons(isEmpty, addColFromSelectionButton, addTagsToFileButton,
                        moveFileToCollectionButton, addDescription, removeTags);
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
            filteredData.setPredicate(file -> newValue == null ||
                    newValue.isEmpty() || file.getName().toLowerCase().contains(newValue.toLowerCase()));
        });
        filesView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        filesView.setItems(filteredData);

        filesView.setOnMouseClicked(e -> {
            _refresh();
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
            selectedFiles = new HashSet<>();
            for (Ifofile selectedItem : selectedItems) {
                selectedFiles.add(selectedItem.getId());
                System.out.println("selectedFiles: " + selectedFiles + " selectedItems: " + selectedItems + " selectedItem: " + selectedItem);
            }
            boolean disable = selectedItems.size()==0;
            disableChosenButtons(disable, addColFromSelectionButton, addTagsToFileButton,
                    moveFileToCollectionButton, addDescription, removeTags);
        });
    }

    private void disableChosenButtons(boolean value, Button... buttons) {
        for (Button b : buttons)
            b.setDisable(value);
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

    private void initializeMoveFileToColDialogController() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Views/MoveFileToColDialog.fxml"));
        Parent page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Move a file to another collection");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        MoveFileToColDialogController mftcdController = loader.getController();
        mftcdController.init(handler, selectedCollection, selectedFiles);
        mftcdController.setStage(dialogStage);

        dialogStage.showAndWait();
    }

    private void initializeAddTagsDialogController() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Views/AddTagsDialog.fxml"));
        Parent page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add tags to files");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AddTagsDialogController atdcController = loader.getController();
        atdcController.init(handler, selectedFiles);
        atdcController.setStage(dialogStage);

        dialogStage.showAndWait();
    }

    private void initializeAddDescriptionDialogController() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Views/AddDescriptionDialog.fxml"));
        Parent page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add description to file");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AddDescriptionDialogController addcController = loader.getController();
        addcController.init(handler, selectedFiles);
        addcController.setStage(dialogStage);

        dialogStage.showAndWait();
    }

    private void initializeRemoveTagDialogController() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Views/RemoveTagDialog.fxml"));
        Parent page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Remove tags from files");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        RemoveTagDialogController rtdcController = loader.getController();
        rtdcController.init(handler, selectedFiles);
        rtdcController.setStage(dialogStage);

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
        long startTime = System.nanoTime();
        try {
            handler.fillInternalStructures(Utility.directoryChooser("Add files", primaryStage), true);
            _refresh();
            handler.export(pathToDB);
        } catch (Exception e) {
            stateLabel.setText("Could not add files, please try again.");
        }
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println(estimatedTime);
        _updateCollectionsView();
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
        _updateCollectionsView();
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
        _updateCollectionsView();
        stateLabel.setText("Refreshed");
    }

    @FXML
    public void setAddEmptyColButton(ActionEvent event) {
        try {
            String newCollectionName = Utility.textInput("Enter name for new collection", "New Collection");
            if (handler.createAnEmptyCollection(newCollectionName)) {
                _refresh();
                stateLabel.setText("\"" + newCollectionName + "\" collection has been added successfully.");
            } else
                stateLabel.setText("A collection with name "+ "\"" + newCollectionName + "\" already exists.");
        } catch (NoSuchElementException ignored) {}
        _updateCollectionsView();
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
        _updateCollectionsView();
    }

    @FXML
    public void setDeleteColButton(ActionEvent event) {
        if (!selectedCollection.isEmpty()) {
            if (Utility.deletionWarning("Warning")) {
                handler.deleteACollection(selectedCollection.name);
                filesView.setItems(null);
            }
        }
        else
            handler.deleteACollection(selectedCollection.name);
        _updateCollectionsView();
    }

    @FXML
    public void setRenameColButton(ActionEvent event) {
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
    public void setAddTagsToFileButton(ActionEvent event) {
        try {
            initializeAddTagsDialogController();
        }
        catch (Exception e) {
            stateLabel.setText("Tags have not been added, please try again.");
        }
        stateLabel.setText("Tags have been added successfully.");
    }

    @FXML
    public void setAddDescriptionButton(ActionEvent event) {
        try {
            initializeAddDescriptionDialogController();
        } catch (Exception e) {
            stateLabel.setText("Description has not been added, please try again.");
        }
    }

    @FXML
    public void setRemoveTags(ActionEvent event) {
        try {
            initializeRemoveTagDialogController();
        } catch (Exception e) {
            e.printStackTrace();
            /*TODO - lepsi popisok bro*/
            stateLabel.setText("Couldn't :(");
        }
    }

    @FXML
    public void setMoveFileToCollectionButton(ActionEvent event) {
        try {
            initializeMoveFileToColDialogController();
        } catch (Exception e) {
            e.printStackTrace();
        }
        _updateCollectionsView();
    }
}
