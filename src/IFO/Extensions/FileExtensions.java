package IFO.Extensions;


import java.util.*;

public class FileExtensions {
    public static final Map<String, String> EXTENSIONS_MAP;
    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put("zip", "Archives");
        aMap.put("7z", "Archives");
        aMap.put("rar", "Archives");
        aMap.put("tar", "Archives");
        aMap.put("gz", "Archives");
        
        aMap.put("docx", "Documents");
        aMap.put("doc", "Documents");
        aMap.put("txt", "Documents");
        aMap.put("rtf", "Documents");
        aMap.put("odt", "Documents");
        
        aMap.put("mp3", "Audio");
        aMap.put("wav", "Audio");
        aMap.put("aac", "Audio");
        aMap.put("wma", "Audio");
        aMap.put("m4a", "Audio");
        aMap.put("flac", "Audio");
        
        aMap.put("avi", "Video");
        aMap.put("mp4", "Video");
        aMap.put("mov", "Video");
        aMap.put("flv", "Video");
        aMap.put("mpg", "Video");
        
        aMap.put("pdf", "Data");
        aMap.put("xls", "Data");
        aMap.put("csv", "Data");
        aMap.put("ini", "Data");
        aMap.put("html", "Data");
        
        aMap.put("exe", "Executables");
        aMap.put("msi", "Executables");
        aMap.put("bin", "Executables");
        aMap.put("app", "Executables");
        aMap.put("dmg", "Executables");

        EXTENSIONS_MAP = Collections.unmodifiableMap(aMap);
    }
}
