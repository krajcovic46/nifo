package IFO;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.util.*;

public class Ifofile {


    private Integer id;
    private String description = "";
    //TreeMap<Category, tags> - keys su kategorie (napr krajina, ludia...)
    private TreeMap<String, HashSet<String>> tags = new TreeMap<>();
    private Integer popularity = 0;

    //--------------------

    String absolutePath;
    String name;
    String parent;
    boolean isAbsolute;
    boolean isDirectory;
    boolean isFile;
    boolean linked;

    public Ifofile (String path, Integer id) {
        this.id = id;

        File workingFile = new File(path);
        newValues(workingFile);
    }

    public void newValues(File f) {
        this.absolutePath = f.getAbsolutePath();
        this.name = f.getName();
        this.parent = f.getParent();
        this.isAbsolute = f.isAbsolute();
        this.isDirectory = f.isDirectory();
        this.isFile = f.isFile();
        this.linked = true;
    }

    public Integer getId() {
        return this.id;
    }

    public boolean isLinked() {
        return this.linked;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getName() {
        return this.name;
    }

    public String getAbsolutePath() {
        return this.absolutePath;
    }

    public String getParent() {
        return this.parent;
    }

    public String getDescription() {
        return this.description;
    }

    public HashSet<String> getCategories() {
        return new HashSet<>(this.tags.keySet());
    }

    public HashSet<String> removeCategory(String category) {
        return this.tags.remove(category);
    }

    public boolean addCategory(String category) {
        if (!this.tags.containsKey(category)) {
            this.tags.put(category, new HashSet<String>());
            return true;
        }
        return false;
    }

    public boolean changeCategoryName(String oldCategoryName, String newCategoryName) {
        if (this.tags.containsKey(oldCategoryName)) {
            HashSet<String> values = removeCategory(oldCategoryName);
            this.tags.put(newCategoryName, values);
            return true;
        }
        return false;
    }

    public boolean addTags(String category, HashSet<String> tags) {
        if (!this.tags.containsKey(category))
            addCategory(category);
        return this.tags.get(category).addAll(tags);
    }

    public boolean addTag(String category, String tag) {
        if (!this.tags.containsKey(category))
            addCategory(category);
        return this.tags.get(category).add(tag);
    }

    public void removeTag(String category, String tag) {
        if (this.tags.containsKey(category))
            this.tags.get(category).remove(tag);
    }

    public boolean changeTagName(String category, String oldTagName, String newTagName) {
        removeTag(category, oldTagName);
        return addTag(category, newTagName);
    }

    public HashSet<String> getTagsFromCategory(String category) {
        return this.tags.get(category);
    }

    public HashSet<String> getAllTags() {
        HashSet<String> returnValues = new HashSet<>();
        Iterator it = this.tags.values().iterator();
        while (it.hasNext())
            returnValues.addAll((HashSet<String>) it.next());
        return returnValues;
    }

    public TreeMap<String, HashSet<String>> getTagStructure() {
        return this.tags;
    }

    public Integer getPopularity() {
        return this.popularity;
    }

    public void incrementPopularity() {
        this.popularity++;
    }

    public void zeroPopularity() {
        this.popularity = Integer.valueOf(0);
    }

    public String toString() {
        if (Utility.withPath)
            return this.name + " - " + this.absolutePath;
        else
            return this.name;
    }

    public boolean exists() {
        File fileToCheck = new File(absolutePath);
        return fileToCheck.exists();
    }


    //unsafe methods, use with caution -- pouzivanim hrozi prepisanie dat
    public void setRawTags(TreeMap<String, HashSet<String>> newTags) {
        this.tags = newTags;
    }

    public TreeMap<String, HashSet<String>> getRawTags() {
        return this.tags;
    }

    public void setRawPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public void setNewRawCustomAttributes(TreeMap<String, HashSet<String>> newTags, String description, Integer popularity) {
        setRawTags(newTags);
        setDescription(description);
        setRawPopularity(popularity);
    }
}
