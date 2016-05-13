package IFO;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import IFO.Extensions.FileExtensions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.TextField;

public class Handler {

    private HashMap<Integer, Ifofile> files = new HashMap<>();
    Integer lastID = 0;
    public HashMap<String, Ifocol> collections = new HashMap<>();
    public HashSet<String> allTags = new HashSet<>();
    public HashSet<Integer> logicFound = new HashSet<>();


    public HashMap<Integer, Ifofile> getFiles() {
        return this.files;
    }

    public void fillInternalStructures(String path, boolean searchRecursively) {
        File[] directory = new File(path).listFiles();
        if (directory != null)
            for (File f : directory)
                if (f.isFile()) {
                    addFile(f);
                } else
                    if (searchRecursively)
                        fillInternalStructures(f.getAbsolutePath(), true);
    }

    public void addFile(File f) {
        files.put(++lastID, new Ifofile(f.getAbsolutePath(), lastID));
        String extension = f.getName().substring(f.getName().lastIndexOf(".") + 1);
        String col = FileExtensions.EXTENSIONS_MAP.get(extension);
        if (col == null) col = "Miscellaneous";
        addFilesToCollection(col, lastID);
        addFilesToCollection("All", lastID);
    }

    public void removeFile(Integer id) {
        files.remove(id);
    }

    private String serialize() {
        Gson gson = new Gson();
        return gson.toJson(files) + System.lineSeparator() + gson.toJson(collections) +
                System.lineSeparator() + lastID;
    }

    public void deserialize(String path) throws IOException  {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));
        String filesToBe, collectionsToBe;
        filesToBe = bufferedReader.readLine();
        collectionsToBe = bufferedReader.readLine();
        lastID = Integer.valueOf(bufferedReader.readLine());
        bufferedReader.close();
        Gson gson = new Gson();
        files = gson.fromJson(filesToBe, new TypeToken<HashMap<Integer, Ifofile>>(){}.getType());
        collections = gson.fromJson(collectionsToBe, new TypeToken<HashMap<String, Ifocol>>(){}.getType());
        fillAllTags();
        Utility.nonExistentFiles = checkFilesExistence();
    }

    private void fillAllTags() {
        for (Ifofile ifo : files.values())
            allTags.addAll(ifo.getAllTags());
    }

    public void export(String path) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(new File(path)));
        bufferedWriter.write(serialize());
        bufferedWriter.close();
    }

    HashSet<Integer> checkFilesExistence() {
        HashSet<Integer> filesWhichDontExist = new HashSet<>();
        Thread checker = new Thread() {
            public void run() {
                for (Ifofile f : files.values())
                    if (!f.exists()) {
                        Integer fid = f.getId();
                        filesWhichDontExist.add(fid);
                        files.get(fid).linked = false;
                    }
            }
        };
        checker.start();
        return filesWhichDontExist;
    }

    public void addTagToFiles(String tag, HashSet<Integer> keys) {
        for (Integer key : keys)
            files.get(key).addTag("", tag);
        allTags.add(tag);
    }

    public void addTagsToFiles(HashSet<String> tags, HashSet<Integer> keys) {
        for (Integer key : keys)
            files.get(key).addTags("", tags);
        allTags.addAll(tags);
    }

    public void addDescriptionToFiles(String description, HashSet<Integer> keys) {
        for (Integer key : keys)
            files.get(key).setDescription(description);
    }

    public void removeDescriptionFromFiles(HashSet<Integer> keys) {
        for (Integer key : keys)
            files.get(key).setDescription("");
    }

    boolean copyFile(String colName, Integer key, String toPath, boolean preserveCustomAttributes) {
        Ifofile workingFile = files.get(key);
        Path from = Paths.get(workingFile.absolutePath);
        toPath += "\\"+workingFile.getName();
        Path to = Paths.get(toPath);
        try {
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Ifofile newFile = new Ifofile(toPath, ++lastID);
        if (preserveCustomAttributes)
            newFile.setNewRawCustomAttributes(workingFile.getRawTags(), workingFile.getDescription(),
                    workingFile.getPopularity());
        addFilesToCollection(colName+" - new", lastID);
        addFilesToCollection("All", lastID);
        files.put(lastID, newFile);
        return true;
    }

    public boolean createAnEmptyCollection(String colName) {
        if (!collections.containsKey(colName)) {
            collections.put(colName, new Ifocol(colName));
            return true;
        }
        return false;
    }

    public boolean copyOnlyCollection(String colName) {
        if (collections.containsKey(colName)) {
            Ifocol newCol = new Ifocol(colName + " - copy");
            for (Integer id : collections.get(colName).getFilesInside())
                newCol.add(id);
            collections.put(newCol.name, newCol);
            return true;
        }
        return false;
    }

    public boolean deleteACollection(String colName) {
        if (collections.containsKey(colName))
            collections.remove(colName);
        return true;
    }

    public boolean renameACollection(String oldColName, String newColName) {
        if (collections.containsKey(newColName))
            return false;
        Ifocol oldCol = collections.remove(oldColName);
        oldCol.name = newColName;
        collections.put(newColName, oldCol);
        return true;
    }

    public boolean addFilesToCollection(String colName, HashSet<Integer> keys) {
        Ifocol col = collections.get(colName);
        if (col == null) {
            col = new Ifocol(colName);
            collections.put(colName, col);
        }
        for (Integer k : keys)
            col.add(k);
        return true;
    }

    public boolean removeFilesFromCollection(String colName, HashSet<Integer> keys) {
        Ifocol col = collections.get(colName);
        if (col == null)
            return false;
        for (Integer k : keys)
            col.remove(k);
        return true;
    }

    public boolean moveFilesBetweenCollections(String fromCol, String toCol, HashSet<Integer> keys) {
        Ifocol fcol = collections.get(fromCol);
        Ifocol tcol = collections.get(toCol);
        if (fcol == null || tcol == null)
            return false;
        for (Integer k : keys) {
            tcol.add(k);
            fcol.remove(k);
        }
        return true;
    }

    private HashMap<String, Ifocol> deepCopyCollections() {
        HashMap<String, Ifocol> temp;
        temp = (HashMap<String, Ifocol>) collections.clone();
        for (Map.Entry<String, Ifocol> entry : collections.entrySet()) {
            temp.put(entry.getKey(), entry.getValue().clone());
        }
        return temp;
    }

    public boolean addFilesToCollection(String colName, Integer key) {
        Ifocol col = collections.get(colName);
        if (col == null) {
            col = new Ifocol(colName);
            collections.put(colName, col);
        }
        col.add(key);
        return true;
    }

    public boolean removeFilesFromCollection(String colName, Integer key) {
        Ifocol col = collections.get(colName);
        if (col == null)
            return false;
        col.remove(key);
        return true;
    }

    public boolean moveFilesInCollectionOnDisk(String colName, String toPath) {
        HashMap<String, Ifocol> copy = deepCopyCollections();
        Ifocol col = copy.get(colName);
        if (col == null)
            return false;
        for (Integer key : col.getFilesInside())
            moveFile(colName, key, toPath);
        return true;
    }

    boolean moveFile(String colName, Integer key, String toPath) {
        Ifofile workingFile = files.get(key);
        Path from = Paths.get(workingFile.absolutePath);
        toPath += "\\"+workingFile.getName();
        Path to = Paths.get(toPath);
        try {
            Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Ifofile newFile = new Ifofile(toPath, ++lastID);
        newFile.setNewRawCustomAttributes(workingFile.getRawTags(),
                workingFile.getDescription(), workingFile.getPopularity());
        files.put(lastID, newFile);
        addFilesToCollection(colName, lastID);
        addFilesToCollection("All", lastID);
        removeFilesFromCollection(colName, key);
        removeFilesFromCollection("All", key);
        files.remove(key);
        return true;
    }

    public boolean copyFilesInCollectionOnDisk(String colName, String toPath) {
        Ifocol col = collections.get(colName);
        if (col == null)
            return false;
        for (Integer key : col.getFilesInside()) {
            copyFile(colName, key, toPath, true);
        }
        return true;
    }

    boolean removeFilesInCollectionOnDisk(String colName) {
        Ifocol col = collections.get(colName);
        if (col != null) {
            for (Integer k : col.getFilesInside())
                removeFile(k);
            return true;
        }
        return false;
    }

    boolean removeFileOnDisk(Integer key) {
        Ifofile file = files.get(key);
        Path path = Paths.get(file.absolutePath);
        try {
            Files.delete(path);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public HashSet<Integer> fullTextSearch(String what) {
        HashSet<Integer> candidates = new HashSet<>();
        for(Ifofile f : files.values()) {
            if (f.name.contains(what) || f.getDescription().contains(what)
                    || tagsContainSearchedWord(f, what)) {
                candidates.add(f.getId());
            }
        }
        return candidates;
    }

    private boolean tagsContainSearchedWord(Ifofile file, String what) {
        for (String s : file.getAllTags())
            if (s.contains(what))
                return true;
        return false;
    }

    public void logicSearchCore(HashMap<TextField, TextField> fieldsSet) {
        for (Map.Entry<TextField, TextField> entry : fieldsSet.entrySet()) {
            HashSet<String> tags = new HashSet<>();
            HashSet<String> nots = new HashSet<>();
            tags.addAll(Arrays.asList(entry.getKey().getText().split(",")));
            nots.addAll(Arrays.asList(entry.getValue().getText().split(",")));
            logicFound.addAll(ifoAnd(tags, nots));
        }
    }

    private HashSet<Integer> ifoAnd(HashSet<String> tags, HashSet<String> nots) {
        HashSet<Integer> filesSearchedFor = new HashSet<>();
        for (Ifofile file : files.values())
            if ((fileContainsTags(file, tags) && (!fileContainsTags(file, nots))))
                filesSearchedFor.add(file.getId());
        return filesSearchedFor;
    }

    private boolean fileContainsTags(Ifofile file, HashSet<String> tags) {
        for (String tag : tags)
            if (!file.getAllTags().contains(tag))
                return false;
        return true;
    }


}
