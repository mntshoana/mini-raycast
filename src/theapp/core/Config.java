package theapp.core;

import java.io.*;
import java.util.Properties;

public class Config {
    private static Properties properties;
    private static File file;
    static {
        properties = new Properties();
        file = new File ("res/settings/config.xml");
        try {
            load();
        } catch (Exception e){
            System.out.println(" [Error] Failed to load configuration settings. Path: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }
    public static void save(String key, int value){
        save(key, value, null);
    }
    public static void save(String key, int value, String comment){
        try {
            File dir = new File("res/settings");
            if (!dir.exists())
                dir.mkdirs();
            if (!file.exists())
                file.createNewFile();
            OutputStream stream = new FileOutputStream(file.getPath());

            properties.setProperty(key, Integer.toString(value));
            properties.storeToXML(stream, (comment == "") ? null : comment);
        } catch (Exception e){
            System.out.println(" [Error] Failed to save configuration settings. Path: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    private static void load () throws IOException {
        if (!file.exists())
            return;
        InputStream stream = new FileInputStream(file.getPath());
        properties.loadFromXML(stream);
    }

    public static int load (String key) throws NoSuchFieldException {
        if (!file.exists())
            throw new NoSuchFieldException("Config file does not exist (it has not been created yet.");

        String value = properties.getProperty(key);
        if (value == null)
            throw new NoSuchFieldException("Key " + key + " does not exist");
        return Integer.parseInt(value);
    }
}