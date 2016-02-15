package IFO;


import java.util.HashSet;

//contains Integers which point to the main database
public class Ifocol {
    public String name;
    private HashSet<Integer> filesInside = new HashSet<>();

    public Ifocol() {
    }

    public Ifocol(String name) {
        this.name = name;
    }

    /*public Ifocol(HashSet<Integer> setToCopy) {
        filesInside = new HashSet<>(setToCopy);
    }*/

    boolean add(Integer key) {
        return filesInside.add(key);
    }

    boolean remove(Integer key) {
        return filesInside.remove(key);
    }

    HashSet<Integer> getFilesInside () {
        return filesInside;
    }
}
