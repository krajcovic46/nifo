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
    
    static {
        Map<String, ArrayList<String>> aMap = new HashMap<>();
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
        
        //"pdf", "xls", "csv", "ini", "html"
        aMap.put("pdf", "Data");
        aMap.put("xls", "Data");
        aMap.put("csv", "Data");
        aMap.put("ini", "Data");
        aMap.put("html", "Data");
        
    }


}
