/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.voskhod.loc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

/**
 *
 * @author andreenko
 */
public class loc extends ResourceBundle {
    
    protected static final String BUNDLE_EXTENSION = "lang";
    protected static final Control UTF8_CONTROL = new UTF8Control();
    //protected static final String i18n_PATH = 
    //        new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().getAbsolutePath()
    //        + File.separator
    //        + "i18n";
    private static String i18n_PATH;
    
    public loc(String resourceName, Locale locale, String s_path) {
        i18n_PATH = s_path;
        setParent(ResourceBundle.getBundle(resourceName, locale, UTF8_CONTROL)); 
    }
    
    @Override
    protected Object handleGetObject(String key) {
        return parent.getObject(key);
    }
    
    @Override
    public Enumeration getKeys() {
        return parent.getKeys();
    }
    
    protected static class UTF8Control extends Control {
        @Override
        public ResourceBundle newBundle
            (String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException
        {
            // The below code is copied from default Control#newBundle() implementation.
            // Only the PropertyResourceBundle line is changed to read the file as UTF-8.
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);
            ResourceBundle bundle = null;
            InputStream stream = null;
            try {
                //stream = new FileInputStream(System.getProperty("user.dir") + File.separator + resourceName);
                stream = new FileInputStream(i18n_PATH + File.separator + resourceName);
            } catch( Exception ex ) {
                
            }
            
            /*
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            */
            
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }
            
            return bundle;
        }
    }
    
}
