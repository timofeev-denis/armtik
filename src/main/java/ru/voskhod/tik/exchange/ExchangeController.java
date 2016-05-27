package ru.voskhod.tik.exchange;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.voskhod.tik.Settings;
import ru.voskhod.loc.i18n;

/**
 *
 * @author bormotov
 */
@Controller
//@RequestMapping("/exchange/*")
public class ExchangeController 
{
    //String PATH_MSG = "g:\\GAS_M\\POCHTA\\T6215\\IN_R\\UIK\\ARCHIVE\\";

//    private final String PATH_MSG = "c:\\TEMP\\TIK_UNZIP\\";
//    private final String PATH_MSG_ARCHIVE = "c:\\TEMP\\UIK_IMP\\";
    private final String PATH_MSG;// = "/opt/uik_data/tik/inbox";
    private final String PATH_MSG_ARCHIVE;// = "/opt/uik_data/tik/archive";
    
    @Autowired
    public ExchangeController(ServletContext context)
    {
//        PgSqlJdbc.setDbName("ra62t015");
        Settings.init(context);
        PgSqlJdbc.setDbName("ra71t026");
        PgSqlJdbc.setDbHost(Settings.getProperty("DB_HOST"));
        PATH_MSG = Settings.getProperty("PATH_MSG");
        PATH_MSG_ARCHIVE = Settings.getProperty("PATH_MSG_ARCHIVE");
        
        ///////////////
        System.out.println("dbhost " + PgSqlJdbc.getDbHost());
        System.out.println("PATH_MSG " + PATH_MSG);
        System.out.println("PATH_MSG_ARCHIVE " + PATH_MSG_ARCHIVE);
    }

    @RequestMapping
    (
        value = "/Exchange",
        method = RequestMethod.GET
    )
    public ModelAndView view(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //MsgFilesWork.listFilesForFolder(new File(PATH_MSG), ".uik");
        MsgFilesWork.listDirForFolder(new File(PATH_MSG));
        ModelAndView model;
        String str = null;
//        try
//        {
//            PgSqlJdbc.setDbName("ra62t015");
//            PgSqlJdbc.PgConnect();
//            //PgSqlJdbc.PgExecuteUpdate("DELETE FROM INFOBJ.uik_documents");
//            //PgSqlJdbc.PgCallStoredProc("infobj.clearshadowtbl()");
//            //PgSqlJdbc.PgCopyCsvToDb(new File("c:\\TEMP\\TIK_UNZIP\\"));
//            //PgSqlJdbc.PgCommit();
//            PgSqlJdbc.PgExecute1("SELECT * FROM infobj.uik_documents");
//            str = PgSqlJdbc.getResultSet().getString("DOCUMENT");
//            PgSqlJdbc.PgDisconnect();
//        }
//        catch(ClassNotFoundException | SQLException | IOException ex)
//        {
//            System.out.println("Error: " + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()) );
//        }
        ArrayList<String> uikFiles = MsgFilesWork.getListFilesFromUik();
        Collections.sort(uikFiles);
        model = new ModelAndView("view", "msg_name", uikFiles);
        //model = new ModelAndView("view_documents", "text_doc",str);
        return model;
    }
    @RequestMapping
    (
            value = "/Exchange",
            method = RequestMethod.POST,
            consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8",
            headers="Accept=*/*"
    )
    public @ResponseBody Messages contetntView(@RequestBody Messages msgParam,HttpServletResponse response) throws Exception
    {
        Messages msg = new Messages();
        
        String pathToFolder = PATH_MSG + File.separator + msgParam.getMsgName();
        System.out.println("Путь к каталогу с данными из УИК: " + pathToFolder);
        
        ArrayList<String[]> list = new ArrayList<>();
        msg.setMsgName(msgParam.getMsgName());
        msgParam.clear();
        //
        PgSqlJdbc.PgConnect();
        PgSqlJdbc.PgCallStoredProc("infobj.clearshadowtbl()");
//        MsgFilesWork.listFilesForFolder(new File(pathToFolder), ".csv");
        PgSqlJdbc.PgCopyCsvToDb(new File(pathToFolder));
        PgSqlJdbc.PgExecuteUpdate(
                "INSERT INTO infobj.uik_document_verify (document_id, signer_id, verify_result, verify_description, cert_info)\n" +
                "SELECT document_id, vrn, 65536, '" + i18n.getString("tikSignatureIsValid") + "', 'FIO: TEST TIK, SNILS: 03773253765'\n" +
                "FROM infobj.uik_document_signers s\n" +
                "WHERE NOT EXISTS (SELECT 1 FROM infobj.uik_document_verify WHERE document_id=s.document_id AND signer_id=s.vrn)");
        PgSqlJdbc.PgCommit();
        
        PgSqlJdbc.PgExecuteN(
                "with RECURSIVE q (doc_name, id, type_id, document) as (\n" +
                "\n" +
                "SELECT	case when SUBSTR(ud.type_id,1,1) = 'X' then up.doc_name || ' ' || SUBSTR(ud.type_id,2)\n" +
                "	else up.doc_name\n" +
                "	end doc_name\n" +
                ", ud.id, ud.type_id, ud.document\n" +
                "\n" +
                "FROM	infobj.uik_documents ud, voshod.uik_param up\n" +
                "\n" +
                "WHERE \n" +
                "	(ud.type_id = up.doc_type OR SUBSTR(ud.type_id,1,1) = up.doc_type) \n" +
                "\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT	case when SUBSTR(ud.type_id,1,1) = 'X' then up.doc_name || ' ' || SUBSTR(ud.type_id,2)\n" +
                "	else up.doc_name\n" +
                "	end doc_name, ud.id, ud.type_id, ud.document\n" +
                "\n" +
                "\n" +
                "FROM	infobj.uik_documents ud join q on q.id = ud.parent_id , voshod.uik_param up\n" +
                "where UPPER (ud.type_id) = 'IP' OR UPPER (ud.type_id) LIKE 'X%'\n" +
                "\n" +
                "\n" +
                ")\n" +
                "SELECT * FROM q");
        int i = PgSqlJdbc.getCurrentResultSet();
        while (PgSqlJdbc.getResultSet().get(i).next()) 
        {
            String[] str = new String[2];
            str[0] = PgSqlJdbc.getResultSet().get(i).getString("DOC_NAME");
            str[1] = MsgFilesWork.tplDocVerify(
                    PgSqlJdbc.getResultSet().get(i).getString("DOCUMENT"),
                    PgSqlJdbc.getResultSet().get(i).getString("ID")
            );
            list.add(str);
        }
        
        PgSqlJdbc.PgDisconnect();
        //msg.setMsgHtml("645645");
        
        msg.setMsgList(list);
        
        return msg;
    }
    @RequestMapping
    (
            value = "/Exchange",
            method = RequestMethod.POST,
            //consumes = "text/plain;charset=UTF-8",
            produces = "text/plain;charset=UTF-8",
            params = {"exec", "numuik"},
            headers="Accept=text/plain;charset=UTF-8"
    )
    public @ResponseBody String executeMsgView(@RequestParam("exec") String execParam, @RequestParam("numuik") String numuikParam) throws Exception
    {
        //
        String ret = "";
        try
        {
            PgSqlJdbc.PgConnect();
            
            switch (execParam) {
                case "load":
                    PgSqlJdbc.PgCallStoredProc("infobj.uikload(0," + numuikParam.substring(0, 4) + ")");
                    PgSqlJdbc.PgCallStoredProc("infobj.uikhod(0)");
                    ret = i18n.getString("tikLoded");
                    MsgFilesWork.moveCsvToArchive(PATH_MSG + numuikParam, PATH_MSG_ARCHIVE + numuikParam);
                    break;
                case "del":
                    MsgFilesWork.moveCsvToArchive(PATH_MSG + numuikParam, PATH_MSG_ARCHIVE + numuikParam);
                    ret = i18n.getString("tikDeleted");
                    break;
            }
            PgSqlJdbc.PgCommit();
            PgSqlJdbc.PgDisconnect();
        }
        catch(ClassNotFoundException | SQLException | IOException ex)
        {
            try 
            {
                PgSqlJdbc.PgDisconnect();
            } 
            catch (SQLException ex1) 
            {
                System.err.println( i18n.getString("tikMessageParameters") + ": " + execParam + " " + numuikParam.substring(0, 4) + "\n" + i18n.getString("tikErrorMessage") + " executeMsg(): " + ex1.getSQLState() + " " + ex1.getMessage());
            }
            return i18n.getString("tikErrorLoadingMessage") + ex.getMessage();
        }
        System.out.println( i18n.getString("tikMessage") + " " + numuikParam + " " + ret + "\n" + i18n.getString("tikMessageParameters") + ": " + execParam + " " + numuikParam.substring(0, 4));
        return i18n.getString("tikMessage") + " " + ret;
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception ex)
    {
        return "<strong>" + i18n.getString("tikErrorClass") + ": </strong>" + ex.getClass()+"<br /><strong>" + i18n.getString("tikErrorMessage") + ": </strong>" + ex.getMessage() + "<br /><strong>" + i18n.getString("tikErrorFullText") + ": </strong>" + Arrays.toString(ex.getStackTrace());
 
    }
}
