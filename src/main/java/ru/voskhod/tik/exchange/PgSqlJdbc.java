package ru.voskhod.tik.exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import ru.voskhod.tik.Settings;

/**
 *
 * @author bormotov
 */
public class PgSqlJdbc 
{
    private static Connection con = null;
    private static String dbName = "";
    private static String dbUser = "";
    private static String dbPass = "";
    private static String dbHost = "";
    private static String dbPort = "";
    private static String dbConnecString = "";
    private static ArrayList<ResultSet> resultSet;
    private static ArrayList<Statement> st;
    private static Statement stExec;
    private static int currentResultSet;
    
    public static void PgConnect() throws ClassNotFoundException, SQLException, IOException
    {
        Class.forName("org.postgresql.Driver");
        System.out.println("Connecting to DB");
        if ("".equals(dbConnecString))
        {
            if ("".equals(dbName))
            {
                System.err.println("Не указано имя БД");
                return;
            }
            dbConnecString = String.format("jdbc:postgresql://%s:%s/%s", ("".equals(dbHost) ? "localhost" : dbHost), ("".equals(dbPort) ? "5432" : dbPort), dbName.toUpperCase());
            resultSet = new ArrayList<>();
            st = new ArrayList<>();
            currentResultSet = -1;
        }

        con = DriverManager.getConnection(dbConnecString, ("".equals(dbUser) ? "admin" : dbUser), ("".equals(dbPass) ? "admin" : dbPass));
        if (getCon() != null) {
            System.out.println("Connect to DB: Done. User: " + dbUser);
            getCon().setAutoCommit(false);
            stExec = con.createStatement();
            System.out.println("DB - :" + getDbName() + " AutoCommit set false");
            return;
        }
        System.err.println("Connect to DB: FaiL. " + String.format("DB - %s, USER - /%s", dbName, dbUser));
    }
    
    public static void PgDisconnect() throws SQLException
    {
        con.close();
        if (resultSet != null)
        {
            if (resultSet.size() > 0) {
                for (ResultSet rs : resultSet) 
                {
                    rs.close();    
                }
            }
        }
        if (st != null)
        {
            if (st.size() > 0) {
                for (Statement s : st) 
                {
                    s.close();    
                }
            }
        }
        stExec.close();
        System.out.println("Disconnect: Done");
    }
    
    public static void PgCopyCsvToDb(File path) throws ClassNotFoundException, SQLException, IOException
    {
//        FileReader fileReader;
        InputStreamReader fileReader;
        CopyManager copyManager = new CopyManager((BaseConnection) getCon());
        for (final File fileEntry : path.listFiles())
        {
            if (fileEntry.getName().toLowerCase().endsWith(".csv"))
            {
//                fileReader = new FileReader(fileEntry.getAbsoluteFile());
//                fileReader = new InputStreamReader(new FileInputStream(fileEntry.getAbsoluteFile()),"windows-1251");
                fileReader = new InputStreamReader(new FileInputStream(fileEntry.getAbsoluteFile()),Settings.getProperty("DB_ENCODING"));
                System.out.println("Copying text data rows from stdin");
                System.out.println(fileEntry.getAbsoluteFile() + " start.");
                copyManager.copyIn("COPY INFOBJ.UIK_" + fileEntry.getName().substring(0, fileEntry.getName().toLowerCase().lastIndexOf(".csv")).toUpperCase() + " FROM STDIN WITH DELIMITER '^' CSV HEADER NULL 'NULL'", fileReader );
                System.out.println(fileEntry.getAbsoluteFile() + " done.");
            }   
        }
    }
    
    public static void PgExecute1(String query) throws SQLException
    {
        currentResultSet++;
        st.add(con.createStatement());
        st.get(currentResultSet).setFetchSize(1);
        resultSet.add(st.get(currentResultSet).executeQuery(query));
        resultSet.get(currentResultSet).next();
        System.out.print("DB - " + dbName + ".One row was returned.");
    }
    
    public static void PgExecuteN(String query) throws SQLException
    {
        currentResultSet++;
        st.add(con.createStatement());
        st.get(currentResultSet).setFetchSize(0);
        resultSet.add(st.get(currentResultSet).executeQuery(query));
        System.out.print("DB - " + dbName + ". Row was returned to ResultSet.");
    }
    
    public static void PgExecuteUpdate(String query) throws SQLException
    {
        stExec.executeUpdate(query);
        System.out.print("DB - " + dbName + ". Execute query.");
    }
    
    public static void PgCallStoredProc(String query) throws SQLException
    {
        CallableStatement proc;
        proc = con.prepareCall("{ call " + query + " }");
        proc.executeUpdate();
        proc.close();
        System.out.print("DB - " + dbName + ". Call stored procedure: " + query);
    }
    
    public static void PgCommit() throws SQLException
    {
        con.commit();
        System.out.print("DB - " + dbName + ". Commit.");
    }
    
    public static void PgRollback() throws SQLException
    {
        con.rollback();
        System.out.print("DB - " + dbName + ". Commit.");
    }

    /**
     * @return the dbName
     */
    public static String getDbName() 
    {
        return dbName;
    }

    /**
     * @param aDbName the dbName to set
     */
    public static void setDbName(String aDbName) 
    {
        dbName = aDbName;
    }

    /**
     * @return the dbUser
     */
    public static String getDbUser() 
    {
        return dbUser;
    }

    /**
     * @param aDbUser the dbUser to set
     */
    public static void setDbUser(String aDbUser) 
    {
        dbUser = aDbUser;
    }

    /**
     * @param aDbPass the dbPass to set
     */
    public static void setDbPass(String aDbPass) 
    {
        dbPass = aDbPass;
    }

    /**
     * @return the con
     */
    public static Connection getCon() 
    {
        return con;
    }

    /**
     * @return the dbHost
     */
    public static String getDbHost() 
    {
        return dbHost;
    }

    /**
     * @param aDbHost the dbHost to set
     */
    public static void setDbHost(String aDbHost) 
    {
        dbHost = aDbHost;
    }

    /**
     * @return the dbPort
     */
    public static String getDbPort() 
    {
        return dbPort;
    }

    /**
     * @param aDbPort the dbPort to set
     */
    public static void setDbPort(String aDbPort) 
    {
        dbPort = aDbPort;
    }

    /**
     * @return the dbConnecString
     */
    public static String getDbConnecString() 
    {
        return dbConnecString;
    }

    /**
     * @param aDbConnecString the dbConnecString to set
     */
    public static void setDbConnecString(String aDbConnecString) 
    {
        dbConnecString = aDbConnecString;
    }

    /**
     * @return the resultSet
     */
    public static ArrayList<ResultSet> getResultSet() 
    {
        return resultSet;
    }

    /**
     * @return the currentResultSet
     */
    public static int getCurrentResultSet() 
    {
        return currentResultSet;
    }
}
