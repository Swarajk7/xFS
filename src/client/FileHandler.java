package client;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class FileHandler {
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

    public static String getFilePathForFileName(String filename) {
        // return file path by locating the file.
        return filename;
    }
}
