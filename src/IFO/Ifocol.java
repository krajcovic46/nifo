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

    public boolean add(Integer key) {
        return filesInside.add(key);
    }

    public boolean remove(Integer key) {
        return filesInside.remove(key);
    }

    public HashSet<Integer> getFilesInside () {
        return filesInside;
    }

    public String toString() {
        return name;
    }
}
