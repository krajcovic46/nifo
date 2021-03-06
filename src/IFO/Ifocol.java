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

    public boolean isEmpty() {
        return filesInside.isEmpty();
    }

    @Override
    public Ifocol clone() {
        Ifocol result = new Ifocol(name);
        result.filesInside = (HashSet<Integer>) filesInside.clone();
        return result;
    }
}
