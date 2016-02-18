package IFO.Extensions;


import java.util.*;

public class FileExtensions {
    public static final Map<String, ArrayList<String>> EXTENSIONS_MAP;
    static {
        Map<String, ArrayList<String>> aMap = new HashMap<>();
        aMap.put("Archives", new ArrayList<>(Arrays.asList(
                "zip", "7z", "rar", "tar", "gz"
        )));
        aMap.put("Documents", new ArrayList<>(Arrays.asList(
                "docx", "doc", "txt", "rtf", "odt"
        )));
        aMap.put("Audio", new ArrayList<>(Arrays.asList(
                "mp3", "wav", "aac", "wma", "m4a", "flac"
        )));
        aMap.put("Video", new ArrayList<>(Arrays.asList(
                "avi", "mp4", "mov", "flv", "mpg"
        )));
        aMap.put("Data", new ArrayList<>(Arrays.asList(
                "pdf", "xls", "csv", "ini", "html"
        )));
        aMap.put("Executables", new ArrayList<>(Arrays.asList(
                "exe", "msi", "bin", "app", "dmg"
        )));
        EXTENSIONS_MAP = Collections.unmodifiableMap(aMap);
    }


}
