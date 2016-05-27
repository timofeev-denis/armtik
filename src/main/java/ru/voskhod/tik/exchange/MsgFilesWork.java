
package ru.voskhod.tik.exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import ru.voskhod.loc.i18n;
import ru.voskhod.tik.Settings;

/**
 *
 * @author bormotov
 */
public class MsgFilesWork 
{
    private static ArrayList<String> listFilesFromUik;
    
    public static void listFilesForFolder(final File folder, String extention)
    {
        if(!existsPath(folder)) return;
        
        if ( null == listFilesFromUik) 
        {
            listFilesFromUik = new ArrayList<>();
        }
        else
        {
            listFilesFromUik.clear();
        }
        
        for (final File fileEntry : folder.listFiles())
        {
            if (fileEntry.isDirectory())
            {
                listFilesForFolder(fileEntry, extention);
            }
            else if ((fileEntry.getName().indexOf("#") > 0) ? fileEntry.getName().substring(0, fileEntry.getName().lastIndexOf("#")).endsWith(extention) : fileEntry.getName().endsWith(extention))
            {
                switch (extention.toLowerCase())
                {
                    case ".uik":
                        listFilesFromUik.add(fileEntry.getName());
                        break;
                    case ".csv":
                        convertToUtf8(fileEntry.getAbsolutePath(), fileEntry.getAbsolutePath() + "utf");
                {
//                    try {
//                        Files.move(Paths.get(fileEntry.getAbsolutePath() + "utf"), Paths.get(fileEntry.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
//                    } catch (IOException ex) {
//                        Logger.getLogger(MsgFilesWork.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }
                        break;
                }
            }
        }
    }
    
    public static void listDirForFolder(final File folder)
    {
        if(!existsPath(folder)) return;
        
        if ( null == listFilesFromUik) 
        {
            listFilesFromUik = new ArrayList<>();
        }
        else
        {
            listFilesFromUik.clear();
        }
        
        for (final File fileEntry : folder.listFiles())
        {
            if (fileEntry.isDirectory())
            {
                listFilesFromUik.add(fileEntry.getName());
            }
        }
    }
    
    public static String tplDocVerify(String document_text, String document_id) throws SQLException
    {
        String addDivPrint1,addDivPrint2 = ""; 
        addDivPrint1 =
                "<div style=\"font-size:14px; width:90%; text-align:right;\">"+
//                "<input type=\"button\" value=\"Печать\" onclick=\"printBlock('print1');\" />"+
                "</div>\n" +
                "<div id=\"print1\">";
        PgSqlJdbc.PgExecuteN(
        "SELECT	uds.office office, uds.signer_name signer_name, TO_CHAR(uds.date_add,'dd.MM.yyyy hh24:mi:ss') date_add, \n" +
        "CASE WHEN (udv.verify_result = 262144 OR uds.sign_mark IS NULL) THEN (select COALESCE(subscription,'" + i18n.getString("tikNoSignature") + "') from infobj.uik_document_signers where document_id = uds.document_id and vrn = udv.signer_id)\n" +
        "else udv.verify_description\n" +
        "end verify_description,\n" +
        "TO_CHAR(ud.date_add,'dd.MM.yyyy hh24:mi:ss') sign_dt,\n" +
        "ud.doc_name doc_name\n" +
        "\n" +
        "FROM	infobj.uik_documents ud, infobj.uik_document_signers uds, infobj.uik_document_verify udv\n" +
        "\n" +
        "WHERE \n" +
        "	uds.document_id = '" + document_id +"'\n" +
        "	AND udv.document_id = '" + document_id +"'\n" +
        "	AND ud.id = '" + document_id +"'\n" +
        "	AND udv.signer_id = uds.vrn\n" +
        "	AND udv.document_id = ud.id\n" +
        "\n" +
        "ORDER BY uds.signer_rank");
        String doc_name = "", sign_dt = "";
        int i = PgSqlJdbc.getCurrentResultSet();
        while (PgSqlJdbc.getResultSet().get(i).next())
        {
            addDivPrint2 += 
                    "<tr>\n" +
                    "	<td width=\"15%\">" + PgSqlJdbc.getResultSet().get(i).getString("OFFICE") +"</td>"
                    + " <td width=\"50%\">" + PgSqlJdbc.getResultSet().get(i).getString("SIGNER_NAME") + "</td>"
                    + " <td width=\"7%\">" + ((PgSqlJdbc.getResultSet().get(i).getString("DATE_ADD") == null ) ? "" : PgSqlJdbc.getResultSet().get(i).getString("DATE_ADD")) + "</td>"
                    + " <td>" + PgSqlJdbc.getResultSet().get(i).getString("VERIFY_DESCRIPTION") + "</td>\n" +
                    "</tr>";
            doc_name = PgSqlJdbc.getResultSet().get(i).getString("DOC_NAME");
            sign_dt = PgSqlJdbc.getResultSet().get(i).getString("SIGN_DT");
        }
        doc_name = ("".equals(doc_name)) ? i18n.getString("tikAreaNumber") + ": 1488" : doc_name;
        
        addDivPrint2 = 
                "</div>\n" +
                "<div style=\"font-size:14px; width:90%; text-align:right;\">" +
//                "<input type=\"button\" value=\"Печать\" onclick=\"printBlock('print2');\" />" +
                "</div>\n" +
                "<div id=\"otchet\" style=\"font-size:14px; width:90%; margin-left:50px;\">\n" +
                "	</br>\n" +
                "	<div id=\"print2\">\n" +
                "		<div id=\"otchet_head\" style=\"font-size:14px; width:90%\">\n" +
                "			<p align=\"center\" style=\"font-size:18px\">\n" +
                "				<strong>" + doc_name + "</strong>\n" +
                "			</p>\n" +
                "			<p align=\"center\" style=\"font-size:16px\">\n" +
                "				<strong>" + i18n.getString("tikESCheckProtocol") + "</strong>\n" +
                "			</p>\n" +
                "			<p align=\"right\" style=\"font-size:14px\">\n" +
                "				<strong id=\"currdt\"></strong>\n" +
                "			</p>\n" +
                "		</div>\n" +
                "		<div id=\"otchet_body\" style=\"font-size:14px; width:90%\">\n" +
                "			<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" align=\"center\" border=\"1\">\n" +
                "				<tr style=\"font-weight:bold; text-align:center;\">\n" +
                "					<td width=\"15%\">" + i18n.getString("tikPosition") + "</td><td width=\"50%\">" + i18n.getString("tikFIO") + "</td><td width=\"7%\">" + i18n.getString("tikDate") + "</td><td>" + i18n.getString("tikResult") + "</td>\n" +
                "				</tr>" + addDivPrint2 + "\n" +
                "</table>\n" +
                "		</div>\n" +
                "		</br>\n" +
                "		<div id=\"otchet_footer\" style=\"font-size:14px; width:90%; text-align:center;\">\n" +
                "			<p align=\"right\" style=\"font-size:16px\">\n" +
                "				<strong>" + i18n.getString("tikGroupOfControlMembers") + ":</strong>\n" +
                "			</p>\n" +
                "			<p align=\"right\">\n" +
                "                           <strong>\"__\" _________________ 20__ г.&nbsp&nbsp&nbsp&nbsp&nbsp" + i18n.getString("tikSignature") + " ____________</strong>\n" +
                "                       </p>\n" +
                "			<p align=\"right\">\n" +
                "                           <strong>\"__\" _________________ 20__ г.&nbsp&nbsp&nbsp&nbsp&nbsp" + i18n.getString("tikSignature") + " ____________</strong>\n" +
                "                       </p>\n" +
                "			<p align=\"right\">\n" +
                "                           <strong>\"__\" _________________ 20__ г.&nbsp&nbsp&nbsp&nbsp&nbsp" + i18n.getString("tikSignature") + " ____________</strong>\n" +
                "                       </p>\n" +
                "			<p align=\"right\">\n" +
                "                           <strong>\"__\" _________________ 20__ г.&nbsp&nbsp&nbsp&nbsp&nbsp" + i18n.getString("tikSignature") + " ____________</strong>\n" +
                "                       </p>\n" +
                "			<p align=\"right\">\n" +
                "                           <strong>\"__\" _________________ 20__ г.&nbsp&nbsp&nbsp&nbsp&nbsp" + i18n.getString("tikSignature") + " ____________</strong>\n" +
                "                       </p>\n" +
                "			<p align=\"right\">\n" +
                "                           <strong>\"__\" _________________ 20__ г.&nbsp&nbsp&nbsp&nbsp&nbsp" + i18n.getString("tikSignature") + " ____________</strong>\n" +
                "                       </p>\n" +
                "			<p align=\"right\">\n" +
                "                           <strong>\"__\" _________________ 20__ г.&nbsp&nbsp&nbsp&nbsp&nbsp" + i18n.getString("tikSignature") + " ____________</strong>\n" +
                "                       </p>\n" +
                "			<p align=\"right\">\n" +
                "                           <strong>\"__\" _________________ 20__ г.&nbsp&nbsp&nbsp&nbsp&nbsp" + i18n.getString("tikSignature") + " ____________</strong>\n" +
                "                       </p>\n" +
                "			<p align=\"right\">\n" +
                "                           <strong>\"__\" _________________ 20__ г.&nbsp&nbsp&nbsp&nbsp&nbsp" + i18n.getString("tikSignature") + " ____________</strong>\n" +
                "                       </p>\n" +
                "		</div>\n" +
                "	</div>\n" +
                "</div>";
        
        String tpl = document_text.replace("<body>", "<div id=\"text_doc1\">" + addDivPrint1).replace("#SignDateTime#", sign_dt).replace("</body>", addDivPrint2 + "</div>" );
        return tpl;
    }
    public static void moveCsvToArchive(String pathTmpCSV, String archive)
    {
        if(!existsPath(new File(pathTmpCSV))) return;
        try
        {
            File folder = new File(archive);
            if(!folder.exists())
            {
                folder.mkdir();
            }
            folder = new File(pathTmpCSV);
            String extention = ".csv";
            for (final File fileEntry : folder.listFiles())
            {
                if ((fileEntry.getName().indexOf("#") > 0) ? fileEntry.getName().substring(0, fileEntry.getName().lastIndexOf("#")).endsWith(extention) : fileEntry.getName().endsWith(extention))
                {
                    switch (extention.toLowerCase())
                    {
                        case ".csv":
                            Path original = Paths.get(pathTmpCSV + File.separator + fileEntry.getName());
                            Path destination = Paths.get(archive + File.separator + fileEntry.getName());
                            //
                            System.out.println("Старт перемещения csv файлов в архив");
                            Files.move(original, destination, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("Файл " + fileEntry.getName() + " перемещён из " + original.toString() + " в " + destination.toString());
                            break;
                    }
                }
            }
            //
            folder.delete();
        }
        catch(IOException ex)
        {
            System.err.println("ТИК. Ошибка пермещения csv файлов.\nОшибка: " + ex.getMessage());
        }
    }
    private static boolean existsPath(File path)
    {
        if (!path.exists())
        {
            System.err.println("По указанному пути: " + path.toString() + ", не найден каталог");
            return false;
        }
        return true;
    }
    public static void convertToUtf8(String input, String output)
    {
        try 
        {
            try (FileInputStream fis = new FileInputStream(input)) 
            {
                byte[] contents = new byte[fis.available()];
                fis.read(contents, 0, contents.length);
                String asString = new String(contents, Settings.getProperty("DB_ENCODING"));
                byte[] newBytes = asString.getBytes("UTF-8");
                try (FileOutputStream fos = new FileOutputStream(output))
                {
                    fos.write(newBytes);
                }
            }
        } 
        catch(Exception e) 
        {
            e.getMessage();
        }
    }
    /**
     * @return the listFilesFromUik
     */
    public static ArrayList<String> getListFilesFromUik() 
    {
        return listFilesFromUik;
    }    
}
