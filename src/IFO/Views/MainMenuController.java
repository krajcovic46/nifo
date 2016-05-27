package IFO.Views;

import IFO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainMenuController implements Initializable {

    @FXML
    private ListView<Ifocol> collectionsView;
    @FXML
    private ListView<Ifofile> filesView;
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
    private Button addDescriptionButton;
    @FXML
    private Button removeTags;
    @FXML
    private Button removeFileFromACol;
    @FXML
    private Button removeDescription;
    @FXML
    private Button copyColContentButton;
    @FXML
    private Button moveColContentButton;
    @FXML
    private Button copyOnlyColButton;
    @FXML
    private Button copyFileToCollectionButton;

    @FXML
    private MenuItem addColFromSelectionMenu;
    @FXML
    private MenuItem copySelectedCollectionMenu;
    @FXML
    private MenuItem deleteSelectedCollectionMenu;
    @FXML
    private MenuItem renameSelectedCollectionMenu;

    @FXML
    private MenuItem addTagsToFileMenu;
    @FXML
    private MenuItem addDescriptionToFileMenu;
    @FXML
    private MenuItem removeDescriptionMenu;
    @FXML
    private MenuItem removeTagsMenu;

    private MenuItem addTagsMenuItem;
    private MenuItem addDescriptionMenuItem;
    private MenuItem removeTagsMenuItem;
    private MenuItem removeDescriptionMenuItem;
    private MenuItem moveFilesToAnotherColMenuItem;
    private MenuItem removeFilesFromColMenuItem;
    private MenuItem copyOnlyColMenuItem;
    private MenuItem linkUnlinkedFilesMenuItem;
    private MenuItem addColFromSelectionMenuItem;
    private MenuItem addFilesToColMenuItem;

    private MenuItem renameColMenuItem;
    private MenuItem deleteColMenuItem;

    private ContextMenu filesContextMenu;
    private ContextMenu colContextMenu;

    private String pathToDB;
    private Stage primaryStage;
    private Handler handler;
    private HashSet<Integer> selectedFiles;
    private Ifocol selectedCollection;
    private ObservableList<Ifocol> collectionsData;

    private HashMap<ListCell<Ifofile>, Ifofile> cells = new HashMap<>();

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
        setupFilesContextMenu();
        setupColContextMenu();
    }

    private void _refresh() {
        UpdateableListViewSkin.cast(collectionsView.getSkin()).refresh();
        UpdateableListViewSkin.cast(filesView.getSkin()).refresh();
    }

    private void _updateCollectionsView() {
        collectionsData = FXCollections.observableArrayList(handler.collections.values()).sorted();
        collectionsView.setItems(collectionsData);
    }

    private void setupColContextMenu() {
        colContextMenu = new ContextMenu();

        renameColMenuItem = new MenuItem("Rename collection...");
        renameColMenuItem.setOnAction(e -> setRenameColButton());

        deleteColMenuItem = new MenuItem("Delete collection...");
        deleteColMenuItem.setOnAction(e -> setDeleteColButton());

        copyOnlyColMenuItem = new MenuItem("Copy collection");
        copyOnlyColMenuItem.setOnAction(e -> setCopyOnlyColButton());

        colContextMenu.getItems().addAll(renameColMenuItem, deleteColMenuItem, copyOnlyColMenuItem);

        addColFromSelectionMenu.setDisable(true);
        copySelectedCollectionMenu.setDisable(true);
        deleteSelectedCollectionMenu.setDisable(true);
        renameSelectedCollectionMenu.setDisable(true);

        addTagsToFileMenu.setDisable(true);
        addDescriptionToFileMenu.setDisable(true);
        removeDescriptionMenu.setDisable(true);
        removeTagsMenu.setDisable(true);
    }

    private void setupFilesContextMenu() {
        filesContextMenu = new ContextMenu();

        SeparatorMenuItem smi = new SeparatorMenuItem();
        SeparatorMenuItem smi2 = new SeparatorMenuItem();

        addTagsMenuItem = new MenuItem("Add tag(s)...");
        addTagsMenuItem.setOnAction(e -> setAddTagsToFileButton());

        addDescriptionMenuItem = new MenuItem("Add description...");
        addDescriptionMenuItem.setOnAction(e -> setAddDescriptionButton());

        removeTagsMenuItem = new MenuItem("Remove tag(s)...");
        removeTagsMenuItem.setOnAction(e -> setRemoveTagsButton());

        removeDescriptionMenuItem = new MenuItem("Remove description");
        removeDescriptionMenuItem.setOnAction(e -> setRemoveDescriptionButton());

        moveFilesToAnotherColMenuItem = new MenuItem("Move files to a collection...");
        moveFilesToAnotherColMenuItem.setOnAction(e -> setMoveFileToCollectionButton());

        removeFilesFromColMenuItem = new MenuItem("Remove files from a collection...");
        removeFilesFromColMenuItem.setOnAction(e -> setRemoveFileFromACollectionButton());

        linkUnlinkedFilesMenuItem = new MenuItem("Link unlinked file...");
        linkUnlinkedFilesMenuItem.setOnAction(e -> setLinkUnlinkedFiles());

        addColFromSelectionMenuItem = new MenuItem("Create collection from selection...");
        addColFromSelectionMenuItem.setOnAction(e -> setAddColFromSelection());

        addFilesToColMenuItem = new MenuItem("Add files to another collection...");
        addFilesToColMenuItem.setOnAction(e -> setCopyFileToCollectionButton());

        filesContextMenu.getItems().addAll(addColFromSelectionMenuItem, addFilesToColMenuItem,
                moveFilesToAnotherColMenuItem, smi, addTagsMenuItem, addDescriptionMenuItem, removeTagsMenuItem,
                removeDescriptionMenuItem, removeFilesFromColMenuItem, smi2, linkUnlinkedFilesMenuItem);
    }

    private void customizeToolbarButtons() {
        customizeButton(refreshButton, "Images/refresh.png", "Refresh", false);
        customizeButton(addFilesButton, "Images/addfiles.png", "Add new files to the database", false);
        customizeButton(exportButton, "Images/export.png", "Export/Save", false);
        customizeButton(addEmptyColButton, "Images/newempcol.png", "Create an empty collection", false);
        customizeButton(addColFromSelectionButton, "Images/newselcol.png", "Create a collection from selected files",
                true);
        customizeButton(copyOnlyColButton, "Images/copyonlycol.png", "Copy an entire collection", true);
        customizeButton(deleteColButton, "Images/deletecol.png", "Delete a collection", true);
        customizeButton(renameColButton, "Images/renamecol.png", "Rename a collection", true);
        customizeButton(addTagsToFileButton, "Images/addtags.png", "Add tags to the selected file.", true);
        customizeButton(copyFileToCollectionButton, "Images/movefiletocol.png", "Add file(s) to another collection",
                true);
        customizeButton(moveFileToCollectionButton, "Images/movefiletocol.png", "Move file(s) to another collection",
                true);
        customizeButton(addDescriptionButton, "Images/adddescription.png", "Add description to file(s)", true);
        customizeButton(removeTags, "Images/removefile.png", "Remove tags from a file", true);
        customizeButton(removeFileFromACol, "Images/removefilefromcol.png", "Remove files from the collection",
                true);
        customizeButton(removeDescription, "Images/removefile.png", "Remove description from selected files", true);
        customizeButton(copyColContentButton, "Images/copycol.png", "Copy the entire content of a collection on HDD", true);
        customizeButton(moveColContentButton, "Images/movecol.png", "Move the entire content of a collection on HDD", true);
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

//        addColFromSelectionMenu;
//        copySelectedCollectionMenu;
//        deleteSelectedCollectionMenu;
//        renameSelectedCollectionMenu;

        collectionsView.getSelectionModel().selectedItemProperty().addListener(t -> {
            _refresh();

            selectedCollection = collectionsView.getSelectionModel().getSelectedItem();

            boolean isSelectedButNotAll = selectedCollection != null && !selectedCollection.name.equals("All");
            disableChosenButtons(!isSelectedButNotAll, deleteColButton, copyOnlyColButton, renameColButton,
                    copyColContentButton, moveColContentButton);
            disableChosenMenuItems(!isSelectedButNotAll, deleteColMenuItem, renameSelectedCollectionMenu);
            if (isSelectedButNotAll) {
                boolean isEmpty;
                isEmpty = selectedFiles == null || selectedFiles.size() == 0 ||
                        selectedCollection.getFilesInside() == null || selectedCollection.getFilesInside().size() == 0;
                disableChosenButtons(isEmpty, addColFromSelectionButton, addTagsToFileButton,
                        addDescriptionButton, removeTags, removeDescription);
            }
        });

        collectionsView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY && selectedCollection != null) {
                colContextMenu.show(primaryStage, e.getScreenX(), e.getScreenY());
                boolean allColSelected = selectedCollection.name.equals("All");
                renameColMenuItem.setDisable(allColSelected);;
                deleteColMenuItem.setDisable(allColSelected);
            }
        });

        collectionsView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        ObservableList<Ifofile> filesData = FXCollections.observableArrayList();
                        for (Integer id : newValue.getFilesInside())
                            filesData.add(handler.getFiles().get(id));
                        showFilesInCollections(filesData);
                    }
                });
    }

    public void showFilesInCollections(ObservableList<Ifofile> filesData) {
        FilteredList<Ifofile> filteredData = new FilteredList<>(filesData, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(file -> newValue == null ||
                    newValue.isEmpty() || file.getName().toLowerCase().contains(newValue.toLowerCase()));
        });
        filesView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        filesView.setItems(filteredData);

        ObservableList<Ifofile> selectedItems = filesView.getSelectionModel().getSelectedItems();

        filesView.setOnMouseClicked(e -> {
            selectedFiles = new HashSet<>();
            for (Ifofile selectedItem : selectedItems)
                selectedFiles.add(selectedItem.getId());
            boolean disable = selectedItems.size()==0;
            disableChosenMenuItems(disable, addColFromSelectionMenu, copySelectedCollectionMenu,
                    deleteSelectedCollectionMenu, renameSelectedCollectionMenu, addTagsToFileMenu, addDescriptionToFileMenu,
                    removeDescriptionMenu, removeTagsMenu);
            disableChosenButtons(disable, addColFromSelectionButton, addTagsToFileButton,
                    addDescriptionButton, removeTags, removeDescription);
            try {
                boolean disableSpecific = selectedItems.size() == 0 || selectedCollection.name.equals("All");
                disableChosenButtons(disableSpecific, removeFileFromACol, copyFileToCollectionButton,
                        moveFileToCollectionButton);
            }
            catch (NullPointerException ignored) {}

            if (e.getButton() == MouseButton.SECONDARY && selectedFiles.size() != 0) {
                filesContextMenu.show(primaryStage, e.getScreenX(), e.getScreenY());
                boolean allColSelected = selectedCollection.name.equals("All");
                moveFilesToAnotherColMenuItem.setDisable(allColSelected);
                removeFilesFromColMenuItem.setDisable(allColSelected);
                if (selectedFiles.size() == 1) {
                    ArrayList<Integer> list = new ArrayList<>(selectedFiles);
                    boolean unlinkedFileSelected = handler.getFiles().get(list.get(0)).isLinked();
                    linkUnlinkedFilesMenuItem.setDisable(unlinkedFileSelected);
                }

            }
            else if (e.getButton() == MouseButton.PRIMARY) {
                if (e.getClickCount() == 2) {
                    Ifofile currentItemSelected = filesView.getSelectionModel().getSelectedItem();
                    try {
                        initializeFileDialogController(currentItemSelected);
                    } catch (Exception d) {
                        d.printStackTrace();
                    }
                }
            }
        });

        filesView.setCellFactory(new Callback<ListView<Ifofile>, ListCell<Ifofile>>() {
            @Override
            public ListCell<Ifofile> call(ListView<Ifofile> stringListView) {
                return new ListCell<Ifofile>(){
                    @Override
                    protected void updateItem(Ifofile ifofile, boolean b) {
                        super.updateItem(ifofile, b);
                        if (isEmpty()) {
                            setText(null);
                        } else {
                            if (ifofile != null) {
                                String tags = (ifofile.getAllTags().size() == 0) ? "no tags" : ifofile.getAllTags().toString();
                                String description = (ifofile.getDescription().equals("")) ? "no description"
                                        : ifofile.getDescription();
                                if (Utility.nonExistentFiles != null && Utility.nonExistentFiles.contains(ifofile.getId())) {
                                    setStyle("-fx-background-color: rgba(205, 0, 8, 0.61);");
                                    setText(ifofile.getName() + " -- " + tags + ", " + description);
                                } else {
                                    setStyle(null);
                                    setText(ifofile.getName() + " -- " + tags + ", " + description);
                                }
                                cells.put(this, ifofile);
                            }
                        }
                    }
                };
            }
        });
    }

    private void disableChosenButtons(boolean value, Button... buttons) {
        for (Button b : buttons)
            b.setDisable(value);
    }

    private void disableChosenMenuItems(boolean value, MenuItem... items) {
        for (MenuItem m : items)
            m.setDisable(value);
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

    private void initializeMoveFileToColDialogController(boolean copy) throws Exception {
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
        mftcdController.init(handler, selectedCollection, selectedFiles, copy);
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

    private void initializeLogicSearchController() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Views/LogicSearch.fxml"));
        Parent page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Search files using logic(tm)");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        LogicSearchController lsController = loader.getController();
        lsController.init(handler);
        lsController.setStage(dialogStage);

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
    public void setAddFiles() {
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
    public void setDbExport() {
        try {
            handler.export(pathToDB);
            stateLabel.setText("Export/Save successful.");
        } catch (IOException e) {
            stateLabel.setText("Export/Save unsucessful, please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    public void refresh() {
        _refresh();
        _updateCollectionsView();
        stateLabel.setText("Refreshed");
    }

    @FXML
    public void setAddEmptyColButton() {
        try {
            String newCollectionName = Utility.textInput("Add collection", "Enter name for new collection", "Name",
                    "New Collection");
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
            String newCollectionName = Utility.textInput("Add collection", "Enter name for new collection", "Name",
                    "New Collection");
            if (handler.addFilesToCollection(newCollectionName, selectedFiles)) {
                _refresh();
                stateLabel.setText("\"" + newCollectionName + "\" collection has been added successfully.");
            } else
                stateLabel.setText("A collection with name "+ "\"" + newCollectionName + "\" already exists.");
        } catch (NoSuchElementException ignored) {}
        _updateCollectionsView();
    }

    @FXML
    public void setCopyOnlyColButton() {
        handler.copyOnlyCollection(selectedCollection.name);
        refresh();
        stateLabel.setText("Collection has been copied.");
    }

    @FXML
    public void setDeleteColButton() {
        if (!selectedCollection.isEmpty()) {
            if (Utility.deletionWarning("Warning", "You are trying to delete a collection which is not empty",
                    "Are you sure?")) {
                handler.deleteACollection(selectedCollection.name);
                filesView.setItems(null);
            }
        }
        else
            handler.deleteACollection(selectedCollection.name);
        _updateCollectionsView();
    }

    @FXML
    public void setRenameColButton() {
        try {
            if (handler.renameACollection(selectedCollection.name,
                    Utility.textInput("Rename", "Enter new name to rename", "Name", "New Name")))
                stateLabel.setText("Collection successfully renamed.");
            else
                stateLabel.setText("A collection with the same name already exists.");
        } catch (Exception ignored) {}
        _refresh();
    }

    @FXML
    public void setAddTagsToFileButton() {
        try {
            initializeAddTagsDialogController();
        }
        catch (Exception e) {
            stateLabel.setText("Tags have not been added, please try again.");
        }
        _refresh();
        stateLabel.setText("Tags have been added successfully.");
    }

    @FXML
    public void setAddDescriptionButton() {
        try {
            initializeAddDescriptionDialogController();
        } catch (Exception e) {
            stateLabel.setText("Description has not been added, please try again.");
        }
        _refresh();
        stateLabel.setText("Description has been added successfully.");
    }

    @FXML
    public void setRemoveTagsButton() {
        try {
            initializeRemoveTagDialogController();
        } catch (Exception e) {
            e.printStackTrace();
            /*TODO - lepsi popisok bro*/
            stateLabel.setText("Couldn't :(");
        }
        _refresh();
    }

    @FXML
    public void setCopyFileToCollectionButton() {
        try {
            initializeMoveFileToColDialogController(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _updateCollectionsView();
    }

    @FXML
    public void setMoveFileToCollectionButton() {
        try {
            initializeMoveFileToColDialogController(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _updateCollectionsView();
    }

    @FXML
    public void setRemoveFileFromACollectionButton() {
        if (Utility.deletionWarning("Warning", "You are trying to delete files from a collection", "Are you sure?"))
            handler.removeFilesFromCollection(selectedCollection.name, selectedFiles);
        refresh();
    }

    @FXML
    public void setRemoveDescriptionButton() {
        if (Utility.deletionWarning("Warning", "You are trying to remove description from selected files",
                "Are you sure?"))
            handler.removeDescriptionFromFiles(selectedFiles);
    }

    @FXML
    public void setCopyColContent() {
        if (Utility.deletionWarning("Warning", "You are trying to copy the contents of an entire collection to" +
                "another place on your HDD", "Are you sure you wish to proceed?")) {
            String directory = Utility.directoryChooser("Pick a new location", primaryStage);
            handler.copyFilesInCollectionOnDisk(selectedCollection.name, directory);
        }
        refresh();
    }

    public void setMoveColContent() {
        if (Utility.deletionWarning("Warning", "You are trying to move the contents of an entire collection to" +
                "another place on your HDD", "Are you sure you wish to proceed?")) {
            String directory = Utility.directoryChooser("Pick a new location", primaryStage);
            handler.moveFilesInCollectionOnDisk(selectedCollection.name, directory);
        }
        refresh();
    }

    public void setLinkUnlinkedFiles() {
        try {
            String pathToFile = Utility.fileChooser("Choose a file to link", primaryStage);
            File f = new File(pathToFile);
            ArrayList<Integer> nieco = new ArrayList<Integer>(selectedFiles);
            Ifofile subor = handler.getFiles().get(nieco.get(0));
            subor.newValues(f);
        } catch (NullPointerException ignored) {}
    }

    public void setViewPaths() {
        Utility.withPath = !Utility.withPath;
        _refresh();
    }

    public void setSearch() {
        collectionsView.getSelectionModel().clearSelection();
        HashSet<Integer> found = handler.fullTextSearch(Utility.textInput("Search", "Enter a term to search", "Query", ""));
        ObservableList<Ifofile> filesData = FXCollections.observableArrayList();
        for (Integer id : found)
            filesData.add(handler.getFiles().get(id));
        showFilesInCollections(filesData);
        _refresh();
    }

    public void setLogicSearch() {
        try {
            initializeLogicSearchController();
            collectionsView.getSelectionModel().clearSelection();
            ObservableList<Ifofile> filesData = FXCollections.observableArrayList();
            for (Integer id : handler.logicFound)
                filesData.add(handler.getFiles().get(id));
            showFilesInCollections(filesData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _refresh();
    }
}
