package client;

import java.io.File;
import java.util.ArrayList;

public class FileHandler {
    public static String[] getFileNames(String filepath) {
        ArrayList<String> filenames = new ArrayList<>();
        for (File file : new File(filepath).listFiles()) {
            if (!file.isDirectory()) filenames.add(file.getName());
        }
        return filenames.toArray(new String[0]);
    }
    public static int calculateCheckSum(String filepath) {
        return 0;
    }
}
