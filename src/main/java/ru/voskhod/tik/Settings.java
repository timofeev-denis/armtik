package ru.voskhod.tik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.servlet.ServletContext;
import ru.voskhod.loc.*;

public class Settings {
    private static String settingsFileName = "";
    private static String filePath = "";
    private static String dbHost = "";
    private static String pathMsg = "";
    private static String pathMsgArchive = "";
    private static String language = "";
    private static String electionsName = "";
    private static String vrnvibref = "";
    private static String dbEncoding = "";

    public static void init(ServletContext context) {
        if(isUnix()) {
            settingsFileName = "tik-linux.properties";
        } else {
            settingsFileName = "tik-win32.properties";
        }
        try {
            filePath = context.getRealPath("/WEB-INF/" + settingsFileName).toString();
        } catch(Exception ex) {
            System.out.println("Error reading absolute path of " + settingsFileName + ": " + ex.getMessage());
            filePath = context.getRealPath("/WEB-INF").toString() + File.separator + settingsFileName;
        }
        
        try {
            Properties prop = new Properties();
            File settingsFile = new File(filePath);
            if (settingsFile.exists() == false && settingsFile.createNewFile() == false) {
                System.out.println("Error creating " + settingsFileName);
            }
            if (new File(filePath).length() == 0) {
                System.err.println("EMPTY CONFIGURATION FILE");
                prop.setProperty("DB_HOST", "localhost");
                prop.setProperty("PATH_MSG", "/opt/uik_data/tik/inbox");
                prop.setProperty("PATH_MSG_ARCHIVE", "/opt/uik_data/tik/archive");
                prop.setProperty("LANGUAGE", "ru");
                prop.setProperty("VRNVIBREF", "462401515484839");
                prop.setProperty("DB_ENCODING", "");
                prop.store(new FileOutputStream(filePath), "TIK settings");
            }
            prop.load(new FileInputStream(filePath));
            //prop.setProperty( "FILE_PATH", filePath );
            //System.out.println( ">>>> " + settingsFile.getPath() );
            
            i18n.init( getProperty( "WEB_INF_PATH" ), prop.getProperty( "LANGUAGE" ));

            dbHost = prop.getProperty("DB_HOST");
            pathMsg = prop.getProperty("PATH_MSG");
            pathMsgArchive = prop.getProperty("PATH_MSG_ARCHIVE");
            language = prop.getProperty("LANGUAGE");
            electionsName = prop.getProperty("ELECTIONS_NAME");
            vrnvibref = prop.getProperty("VRNVIBREF");
            dbEncoding = prop.getProperty("DB_ENCODING");
        } catch (Exception ex) {
            System.out.println("Error reading " + settingsFileName + ": " + ex.getMessage());
        }
        
    }
    public static String getProperty(String propertyName) {
        switch(propertyName) {
            case "DB_HOST":
                return dbHost;
            case "PATH_MSG":
                return pathMsg;
            case "PATH_MSG_ARCHIVE":
                return pathMsgArchive;
            case "WEB_INF_PATH":
                Path p = Paths.get( filePath ).getParent();
                return p.toString();
            case "LANGUAGE":
                return language;
            case "ELECTIONS_NAME":
                return electionsName;
            case "VRNVIBREF":
                return vrnvibref;
            case "DB_ENCODING":
                return dbEncoding;
            default:
                return "";
        }
    }
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }
    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }
}
