package IFO;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import IFO.Extensions.FileExtensions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Handler {

    private HashMap<Integer, Ifofile> files = new HashMap<>();

    Integer lastID = 0;
    public HashMap<String, Ifocol> collections = new HashMap<>();

    public HashMap<Integer, Ifofile> getFiles() {
        return this.files;
    }

    public void fillInternalStructures(String path, boolean searchRecursively) {
        File[] directory = new File(path).listFiles();
        if (directory != null)
            for (File f : directory)
                if (f.isFile()) {
                    files.put(++lastID, new Ifofile(f.getAbsolutePath(), lastID));
                    String extension = f.getName().substring(f.getName().lastIndexOf(".") + 1);
                    String col = FileExtensions.EXTENSIONS_MAP.get(extension);
                    if (col == null) col = "Miscellaneous";
                    addFilesToCollection(col, lastID);
                    addFilesToCollection("All", lastID);
                } else
                    if (searchRecursively)
                        fillInternalStructures(f.getAbsolutePath(), true);
    }

    private String serialize() {
        Gson gson = new Gson();
        return gson.toJson(files) + System.lineSeparator() + gson.toJson(collections) +
                System.lineSeparator() + lastID;
    }

    public void deserialize(String path) throws IOException  {
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(new File(path)));
        String filesToBe, collectionsToBe;
        filesToBe = bufferedReader.readLine();
        collectionsToBe = bufferedReader.readLine();
        lastID = Integer.valueOf(bufferedReader.readLine());
        bufferedReader.close();
        Gson gson = new Gson();
        files = gson.fromJson(filesToBe, new TypeToken<HashMap<Integer, Ifofile>>(){}.getType());
        collections = gson.fromJson(collectionsToBe, new TypeToken<HashMap<String, Ifocol>>(){}.getType());
    }

    public void export(String path) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(new File(path)));
        bufferedWriter.write(serialize());
        bufferedWriter.close();
    }

    HashSet<Ifofile> checkFilesExistence() {
        HashSet<Ifofile> filesWhichDontExist = new HashSet<>();
        Thread checker = new Thread() {
            public void run() {
                for (Ifofile f : files.values())
                    if (!f.exists())
                        filesWhichDontExist.add(f);
            }
        };
        checker.start();
        return filesWhichDontExist;
    }

    public void addTagToFiles(String tag, HashSet<Integer> keys) {
        for (Integer key : keys)
            files.get(key).addTag("", tag);
    }

    public void addTagsToFiles(HashSet<String> tags, HashSet<Integer> keys) {
        for (Integer key : keys)
            files.get(key).addTags("", tags);
    }

    public void addDescriptionToFiles(String description, HashSet<Integer> keys) {
        for (Integer key : keys)
            files.get(key).setDescription(description);
    }

    boolean copyFile(Integer key, String toPath, boolean preserveCustomAttributes) {
        Ifofile workingFile = files.get(key);
        Path from = Paths.get(workingFile.absolutePath);
        Path to = Paths.get(toPath);
        CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING };
        try {
            Files.copy(from, to, options);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Ifofile newFile = new Ifofile(toPath, key);
        if (preserveCustomAttributes) {
            newFile.setNewRawCustomAttributes(workingFile.getRawTags(),
                    workingFile.getDescription(), workingFile.getPopularity());
        }
        files.put(++lastID, newFile);
        return true;
    }

    public boolean createAnEmptyCollection(String colName) {
        if (!collections.containsKey(colName)) {
            collections.put(colName, new Ifocol(colName));
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

    public boolean addFilesToCollection(String colName, Integer key) {
        Ifocol col = collections.get(colName);
        if (col == null) {
            col = new Ifocol(colName);
            collections.put(colName, col);
        }
        col.add(key);
        return true;
    }

    boolean removeFilesFromCollection(String colName, Integer[] keys) {
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

    boolean moveFilesInCollectionOnDisk(String colName, String toPath) {
        Ifocol col = collections.get(colName);
        if (col == null)
            return false;
        for (Integer key : col.getFilesInside())
            moveFile(key, toPath);
        return true;
    }

    boolean copyFilesInCollectionOnDisk(String colName, String toPath) {
        Ifocol col = collections.get(colName);
        if (col == null)
            return false;
        for (Integer key : col.getFilesInside())
            copyFile(key, toPath, true);
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

    boolean removeFile(Integer key) {
        Ifofile file = files.get(key);
        Path path = Paths.get(file.absolutePath);
        try {
            Files.delete(path);
        } catch (java.io.IOException e) {
            return false;
        }
        return true;
    }

    HashSet<Integer> fullTextSearch(String what) {
        HashSet<Integer> candidates = new HashSet<>();
        for (int i = 0; i < files.size(); i++) {
            Ifofile workFile = files.get(i);
            if (workFile.name.contains(what) || workFile.getDescription().contains(what)
                    || tagsContainSearchedWord(workFile, what)) {
                candidates.add(i);
            }
        }
        return candidates;
    }

    private boolean tagsContainSearchedWord(Ifofile file, String what) {
        for (String s : file.getAllTags()) {
            if (s.contains(what))
                return true;
        }
        return false;
    }

    boolean moveFile(Integer key, String toPath) {
        Ifofile workingFile = files.get(key);
        Path from = Paths.get(workingFile.absolutePath);
        Path to = Paths.get(toPath);
        CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING };
        try {
            Files.move(from, to, options);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Ifofile newFile = new Ifofile(toPath, key);
        newFile.setNewRawCustomAttributes(workingFile.getRawTags(),
                workingFile.getDescription(), workingFile.getPopularity());
        files.put(key, newFile);
        return true;
    }
}
