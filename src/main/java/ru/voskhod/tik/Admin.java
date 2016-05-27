/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.voskhod.tik;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.voskhod.loc.i18n;

/**
 *
 * @author Timofeev
 */
public class Admin extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            request.getRequestDispatcher("/header.jsp").include(request, response);        
            out.println( "<script>\n"
                    + "var YES = '" + i18n.getString("tikYes") + "';\n"
                    + "var NO = '" + i18n.getString("tikNo") + "';\n"
                    + "</script>\n" );
            out.println( "<script src='" + request.getContextPath() + "/js/ConfirmDelete.js'></script>\n" );

            if (request.getParameter("deleteData") != null) {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://" + Settings.getProperty("DB_HOST") + ":5432/RA71T026";
                //String url = "jdbc:postgresql://localhost:5432/RA71T026";
                Connection conn = null;
                Statement st = null;
                String msg = "";
                try {
                    conn = DriverManager.getConnection(url, "admin", "admin");
                    st = conn.createStatement();
                    // Удаление данных
                    st.executeUpdate("delete from voshod.xodgol" +
                        " where vrntvd  in (select vrn from voshod.tvd" +
                        " where vrnvibref=" + Settings.getProperty("VRNVIBREF") + ")");
                    st.executeUpdate("delete from voshod.xod_end " +
                        " where vrn  in (select vrn from voshod.tvd" +
                        " where vrnvibref=" + Settings.getProperty("VRNVIBREF") + ")");
                    st.executeUpdate( "delete from voshod.pg where vrntvd in (select vrn from voshod.tvd" +
                        " where vrnvibref=" + Settings.getProperty("VRNVIBREF") + ")" );
                    st.executeUpdate("update voshod.tvd " +
                        " set prprot1=null, lastvrem=null" +
                        " where vrnvibref=" + Settings.getProperty("VRNVIBREF") + "");
                    st.executeUpdate("delete from voshod.document_signers");
                    st.executeUpdate("delete from voshod.documents");
                    st.executeUpdate("COMMIT");
                    msg = i18n.getString("tikAllDataDeleted");
                } catch (SQLException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                    msg = String.format( i18n.getString( "tikErrorDeletingData" ), ex.getMessage() );
                }
    
                if(!msg.equals("")) {
                    
                    out.println( "<script>$(function() {" );
                    out.println( "  $('#dialog-message').html(\"" + msg + "\");" );
                    out.println( "  $(\"#dialog-message\").dialog(\"open\");" );
                    out.println( "});</script>\n" );
                }
            }
            out.println( "<div id=\"dialog-confirm-delete\" title=\"" + i18n.getString("tikDataDeletion") + "\">\n" +
                "  <p>" + i18n.getString("tikAllDataWillBeDeleted") + "<br><br>" + i18n.getString("tikAreYouShure") + "</p>\n" +
                "</div>" );
            out.println( "<div id=\"dialog-message\" title=\"" + i18n.getString("tikDataDeletion") + "\"></div>" );
            out.println( "<form id='deleteDataForm' action='" + request.getRequestURI() + "'>" );
            out.println( "<h3>" + i18n.getString("tikClickDeleteButtonToDelete") + ".</h3>" );
            out.println( "  <input type='hidden' name='deleteData' value='1'>" );
            out.println( "  <input type='button' class='deleteButton' value='" + i18n.getString("tikDeleteData") + "'>" );
            out.println( "</form>" );
            request.getRequestDispatcher("/footer.jsp").include(request, response);        
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
