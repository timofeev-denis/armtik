/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.voskhod.loc;

import java.util.Locale;
import javax.swing.JOptionPane;

public class i18n {
    private static loc uik_loc;
    private static String s_pathlocale;
    private static String s_locale;
    private static String s_err;
    
    // инициализация
    public static boolean init(String s_path, String s_loc) {
        try {            
            uik_loc = new loc("MessagesBundle", new Locale(s_loc.toLowerCase(), ""), s_path);
            return true;
        } catch( Exception e ) {
            s_err = e.getMessage().toString();
            return false;
        }        
    }
    
    // перевод
    public static String getString(String s_param) {
        try {
            s_err = "";
            String s_text = uik_loc.getString(s_param);            
            return s_text;
        } catch( Exception e ) {
            s_err = "Ошибка при локализации по параметру: " + s_param + "\n" + 
                    "Текст ошибки: " + e.getMessage().toString();
            //JOptionPane.showMessageDialog( null, s_err, "Ошибка", JOptionPane.ERROR_MESSAGE);            
            System.err.println( s_err );
            return "Error";
        }            
    }
    
    // текст ошибки
    public static String getError() {
        return s_err;
    }
    
    // путь к файлам локализации
    public static String getLocalePath() {
        return s_pathlocale;
    }
    
    // язык локализации
    public static String getLocale() {
        return s_locale;
    }
    
}
