package client;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class FileHandler {
    private static String basepath = null;

    public static String[] getFileNames(String filepath) {
        ArrayList<String> filenames = new ArrayList<>();
        for (File file : Objects.requireNonNull(new File(filepath).listFiles())) {
            if (!file.isDirectory()) filenames.add(file.getName());
        }
        return filenames.toArray(new String[0]);
    }

    public static int calculateCheckSum(String filepath) {
        return 0;
    }

    public static String getFilePathForFileName(String filename) throws Exception {
        // return file path by locating the file.
        if (basepath == null) throw new Exception("Set BasePath First!!");
        return basepath + "/" + filename;
    }

    public static void setBasepath(String path) {
        basepath = path;
    }

    public static String getBasePath() {
        return basepath;
    }
}
