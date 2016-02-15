package IFO;

import java.io.File;
import java.util.*;

public class Ifofile {

    private String description = "";
    //TreeMap<Category, tags> - keys su kategorie (napr krajina, ludia...)
    private TreeMap<String, HashSet<String>> tags = new TreeMap<>();
    private Integer popularity = 0;

    //--------------------

    File absoluteFile;
    File parentFile;
    String absolutePath;
    String path;
    String name;
    String parent;
    boolean canExecute;
    boolean canRead;
    boolean canWrite;
    boolean isAbsolute;
    boolean isDirectory;
    boolean isFile;
    boolean isHidden;
    Date lastModified;
    long length;

    public Ifofile (String path) {
        File workingFile = new File(path);
        this.absoluteFile = workingFile.getAbsoluteFile();
        this.parentFile = workingFile.getParentFile();
        this.absolutePath = workingFile.getAbsolutePath();
        this.path = workingFile.getPath();
        this.name = workingFile.getName();
        this.parent = workingFile.getParent();
        this.canExecute = workingFile.canExecute();
        this.canRead = workingFile.canRead();
        this.canWrite = workingFile.canWrite();
        this.isAbsolute = workingFile.isAbsolute();
        this.isDirectory = workingFile.isDirectory();
        this.isFile = workingFile.isFile();
        this.isHidden = workingFile.isHidden();
        this.lastModified = new Date(workingFile.lastModified());
        this.length = workingFile.length();
    }

    void setDescription(String desc) {
        this.description = desc;
    }

    String getDescription() {
        return this.description;
    }

    HashSet<String> getCategories() {
        return new HashSet<>(this.tags.keySet());
    }

    HashSet<String> removeCategory(String category) {
        return this.tags.remove(category);
    }

    boolean addCategory(String category) {
        if (!this.tags.containsKey(category)) {
            this.tags.put(category, new HashSet<String>());
            return true;
        }
        return false;
    }

    boolean changeCategoryName(String oldCategoryName, String newCategoryName) {
        if (this.tags.containsKey(oldCategoryName)) {
            HashSet<String> values = removeCategory(oldCategoryName);
            this.tags.put(newCategoryName, values);
            return true;
        }
        return false;
    }

    boolean addTags(String category, HashSet<String> tags) {
        if (!this.tags.containsKey(category))
            addCategory(category);
        return this.tags.get(category).addAll(tags);
    }

    boolean addTag(String category, String tag) {
        if (!this.tags.containsKey(category))
            addCategory(category);
        return this.tags.get(category).add(tag);
    }

    void removeTag(String category, String tag) {
        if (this.tags.containsKey(category))
            this.tags.get(category).remove(tag);
    }

    boolean changeTagName(String category, String oldTagName, String newTagName) {
        removeTag(category, oldTagName);
        return addTag(category, newTagName);
    }

    HashSet<String> getTagsFromCategory(String category) {
        return this.tags.get(category);
    }

    HashSet<String> getAllTags() {
        HashSet<String> returnValues = new HashSet<>();
        Iterator it = this.tags.values().iterator();
        while (it.hasNext())
            returnValues.addAll((HashSet<String>) it.next());
        return returnValues;
    }

    Integer getPopularity() {
        return this.popularity;
    }

    void incrementPopularity() {
        this.popularity++;
    }

    void zeroPopularity() {
        this.popularity = Integer.valueOf(0);
    }

    public String toString() {
        return this.name + String.valueOf(this.tags.entrySet());
    }

    boolean exists() {
        File fileToCheck = new File(path);
        return fileToCheck.exists();
    }


    //unsafe methods, use with caution -- pouzivanim hrozi prepisanie dat
    void setRawTags(TreeMap<String, HashSet<String>> newTags) {
        this.tags = newTags;
    }

    TreeMap<String, HashSet<String>> getRawTags() {
        return this.tags;
    }

    void setRawPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    void setNewRawCustomAttributes(TreeMap<String, HashSet<String>> newTags, String description, Integer popularity) {
        setRawTags(newTags);
        setDescription(description);
        setRawPopularity(popularity);
    }
}
