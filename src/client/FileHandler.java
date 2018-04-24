package client;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.security.MessageDigest;
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

    // Taken from https://howtodoinjava.com/core-java/io/how-to-generate-sha-or-md5-file-checksum-hash-in-java/
    public static String calculateCheckSum(String filepath) throws Exception{
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            //Get file input stream for reading the file content
            FileInputStream fis = new FileInputStream(filepath);

            //Create byte array to read data in chunks
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            //Read file data and update in message digest
            while ((bytesCount = fis.read(byteArray)) != -1) {
                md5Digest.update(byteArray, 0, bytesCount);
            }
            ;

            //close the stream; We don't need it now.
            fis.close();

            //Get the hash's bytes
            byte[] bytes = md5Digest.digest();

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            //return complete hash
            return sb.toString();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
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
