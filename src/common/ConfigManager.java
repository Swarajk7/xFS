package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
Singleton class for handling server side config read and write.
ConfigurationManger is also readonly and hence won't cause any conflicts or concurrency issue.
 */
public class ConfigManager {
    public static final String SERVER_BINDING_NAME = "serverbindingname";
    public static final String SERVER_PORT_NUMBER = "serverportnumber";
    public static final String SERVER_IP_ADDRESS = "serveripaddress";
    public static final String THRESHOLD_TIMEOUT_FOR_CLIENT = "thresholdtimeoutforclient";
    public static final String NUMBER_OF_UPLOAD_THREADS = "numberofuploadthreads";
    public static final String BASE_PATH = "basepath";
    public static final String CLIENT_BINDING_NAME = "bindingnameforclient";

    //fix below line for better lookup.. don't hardcode
    private static String filename = "C:\\Users\\sk111\\IdeaProjects\\xFS\\src\\server_config.properties";
    private Properties prop;
    private static ConfigManager obj = null;

    private ConfigManager() {
    }

    public static ConfigManager create() throws IOException {
        if (obj != null) return obj;
        obj = new ConfigManager();
        obj.prop = new Properties();
        InputStream input = new FileInputStream(filename);
        obj.prop.load(input);
        return obj;
    }

    public String getValue(String key) {
        return this.prop.getProperty(key);
    }

    public int getIntegerValue(String key) {
        return Integer.parseInt(this.getValue(key));
    }
}
