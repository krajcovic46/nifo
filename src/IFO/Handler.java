package IFO;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import IFO.Extensions.FileExtensions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Handler {

    HashMap<Integer, Ifofile> files;
    /*TODO - change path*/
    String path = "C:\\Users\\Stanlezz\\Desktop\\asd";
    Integer lastID = 0;
    HashMap<String, Ifocol> collections;

<<<<<<< HEAD:src/IFO/Temp.java
    String devString = "";

    //dummy metoda - naplni pole zatial dummy datami
    public static void fill (String path, boolean searchRecursively) {
=======
    void fillInternalStructures(String path, boolean searchRecursively) {
>>>>>>> the-great-refactor:src/IFO/Handler.java
        File[] directory = new File(path).listFiles();
        if (directory != null)
            for (File f : directory)
                if (f.isFile()) {
                    files.put(++lastID, new Ifofile(f.getAbsolutePath()));
                    String extension = f.getName().substring(f.getName().lastIndexOf(".") + 1);
                    String where = "Miscellaneous";
                    for (String category : FileExtensions.EXTENSIONS_MAP.keySet())
                        if (FileExtensions.EXTENSIONS_MAP.get(category).contains(extension.toLowerCase()))
                            where = category;
                    addFilesToCollection(where, new Integer[]{lastID});
                } else
                    if (searchRecursively)
                        fillInternalStructures(f.getAbsolutePath(), true);
    }

<<<<<<< HEAD:src/IFO/Temp.java
    //serializuje pole - vytvori z neho json string
    public static String serialize () {
=======
    String serialize () {
>>>>>>> the-great-refactor:src/IFO/Handler.java
        Gson gson = new Gson();
        return gson.toJson(files) + System.lineSeparator() + gson.toJson(collections);
    }

<<<<<<< HEAD:src/IFO/Temp.java
    //deserializuje - vytvori z jsonu objekty do pola
    public static void deserialize () throws IOException  {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("C:\\Users\\Stanlezz\\Desktop\\stranka bakalarka\\dbexport.txt")));
=======
    void deserialize () throws IOException  {
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(new File("C:\\Users\\Stanlezz\\Desktop\\stranka bakalarka\\dbexport.txt")));
>>>>>>> the-great-refactor:src/IFO/Handler.java
        String filesToBe, collectionsToBe;
        filesToBe = bufferedReader.readLine();
        collectionsToBe = bufferedReader.readLine();
        bufferedReader.close();
        Gson gson = new Gson();
        files = gson.fromJson(filesToBe, new TypeToken<HashMap<Integer, Ifofile>>(){}.getType());
        collections = gson.fromJson(collectionsToBe, new TypeToken<HashMap<String, Ifocol>>(){}.getType());
    }

<<<<<<< HEAD:src/IFO/Temp.java
    public static void export() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("C:\\Users\\Stanlezz\\Desktop\\stranka bakalarka\\dbexport.txt")));
        bufferedWriter.write(this.serialize());
        bufferedWriter.close();
    }

    void go() throws IOException {
        fill(this.path, true);
        //System.out.println(files);
        export();
        files = null;
        //System.out.println(files);
        deserialize();
        //System.out.println(files);
        checkFilesExistence();
    }

    public static HashSet<Ifofile> checkFilesExistence() {
=======
    void export() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(new File("C:\\Users\\Stanlezz\\Desktop\\stranka bakalarka\\dbexport.txt")));
        bufferedWriter.write(serialize());
        bufferedWriter.close();
    }

    HashSet<Ifofile> checkFilesExistence() {
>>>>>>> the-great-refactor:src/IFO/Handler.java
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

    public static boolean copyFile (Integer key, String toPath, boolean preserveCustomAttributes) {
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
        Ifofile newFile = new Ifofile(toPath);
        if (preserveCustomAttributes) {
            newFile.setNewRawCustomAttributes(workingFile.getRawTags(),
                    workingFile.getDescription(), workingFile.getPopularity());
        }
        files.put(++lastID, newFile);
        return true;
    }

    public static boolean moveFile (Integer key, String toPath) {
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
        Ifofile newFile = new Ifofile(toPath);
        newFile.setNewRawCustomAttributes(workingFile.getRawTags(),
                workingFile.getDescription(), workingFile.getPopularity());
        files.put(key, newFile);
        return true;
    }

    public static boolean addFilesToCollection(String colName, Integer[] keys) {
        Ifocol col = collections.get(colName);
        if (col == null) {
            col = new Ifocol(colName);
            collections.put(colName, col);
        }
        for (Integer k : keys)
            col.add(k);
        return true;
    }

    public static boolean removeFilesFromCollection(String colName, Integer[] keys) {
        Ifocol col = collections.get(colName);
        if (col == null)
            return false;
        for (Integer k : keys)
            col.remove(k);
        return true;
    }

    public static boolean moveFilesFromCollectionToCollection(String fromCol, String toCol, Integer[] keys) {
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

    public static boolean moveFilesInCollectionOnDisk(String colName, String toPath) {
        Ifocol col = collections.get(colName);
        if (col == null)
            return false;
        for (Integer key : col.getFilesInside())
            moveFile(key, toPath);
        return true;
    }

    public static boolean copyFilesInCollectionOnDisk(String colName, String toPath) {
        Ifocol col = collections.get(colName);
        if (col == null)
            return false;
        for (Integer key : col.getFilesInside())
            copyFile(key, toPath, true);
        return true;
    }

    public static boolean removeFilesInCollectionOnDisk(String colName) {
        Ifocol col = collections.get(colName);
        if (col != null) {
            for (Integer k : col.getFilesInside())
                removeFile(k);
            return true;
        }
        return false;
    }

    public static boolean removeFile(Integer key) {
        Ifofile file = files.get(key);
        Path path = Paths.get(file.absolutePath);
        try {
            Files.delete(path);
        } catch (java.io.IOException e) {
            return false;
        }
        return true;
    }

    public static HashSet<Integer> fullTextSearch(String what) {
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

    public static private boolean tagsContainSearchedWord(Ifofile file, String what) {
        for (String s : file.getAllTags()) {
            if (s.contains(what))
                return true;
        }
        return false;
    }
}
