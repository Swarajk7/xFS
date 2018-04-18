package client;

import java.io.File;

public class FileHandler {
    public static String[] getFileNames(String filePath) {
        return new File(filePath).list();
    }
}
