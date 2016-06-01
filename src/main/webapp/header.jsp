<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="ru.voskhod.tik.Settings"%>
<%@page import="ru.voskhod.loc.i18n"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!-- HEADER -->
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="css/style.css" />
        <link type="text/css" rel="stylesheet" href="css/demo.css" />
        <link type="text/css" rel="stylesheet" href="css/jquery.mmenu.all.css" />
        <link type="text/css" rel="stylesheet" href="css/jquery-ui.css" />
        <link type="text/css" rel="stylesheet" href="css/style.css" />
        <link type="text/css" rel="stylesheet" href="css/layout-default-latest.css" type="text/css" />
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/jquery.mmenu.min.all.js"></script>

        <script type="text/javascript" src="js/ChartNew.js"></script>
        <script type="text/javascript" src="js/jquery-ui.js"></script>
        <script type="text/javascript" src="js/jquery.layout-latest.js"></script>
        <script type="text/javascript" src="js/jquery.printElement.min.js"></script>
        <script type="text/javascript">
            <%
                Settings.init(getServletContext());
            %>
            $(function() {
                var $menu = $('nav#menu');

                $menu.mmenu({
                    dragOpen: true,
                    footer: {
                        add: true,
                        //content: "db host: <%= Settings.getProperty("DB_HOST").replace("\\", "\\\\") %><br>PATH_MSG: <%= Settings.getProperty("PATH_MSG").replace("\\", "\\\\") %><br>PATH_MSG_ARCHIVE: <%= Settings.getProperty("PATH_MSG_ARCHIVE").replace("\\", "\\\\") %> "
                        content: "<%=i18n.getString("tikAppTitle")%> 1.1"
                    }
                });

                $menu.find( 'li > a' ).click(function() {
                    console.log( 'click ' + $(this).data('url') );
                    setTimeout( "window.location = '" + $(this).data('url') + "';", 300 );
                });

                switch(window.location.hash) {
                    case "#results":
                    default:
                        active_block = 0;
                        break;
                    case "#turnout":
                        active_block = 1;
                        break;
                    case "#protocol":
                        active_block = 2;
                        break;
                }

                $( "#accordion" ).accordion({
                    heightStyle: "content",
                    active : active_block
                });

            });
        </script>
        <title><%=i18n.getString("tikAppTitle")%></title>
    </head>
    <body>
        <div id="page">
            <div class="header FixedTop">
                    <a href="#menu"></a>
<%

    Class.forName("org.postgresql.Driver");
    String url = "jdbc:postgresql://" + Settings.getProperty("DB_HOST") + ":5432/RA71T026";
    //String url = "jdbc:postgresql://localhost:5432/RA71T026";
    Connection conn = DriverManager.getConnection(url, "admin", "admin");
    Statement st = null;
    try {
        st = conn.createStatement();
    } catch(SQLException ex) {
        %>
            console.error( "Создание подключения: " + "<%= ex.getMessage() %>");
        <%
    }
    // Результаты голосования
    ResultSet rs = null;
    //String vrnvibref = "4714026159790";   // старые выборы
    String vrnvibref = (String) request.getSession().getAttribute("vrnvibref");
    if (vrnvibref == null) {
        vrnvibref = "462401515484839";   // Эбола
        request.getSession().setAttribute("vrnvibref", vrnvibref);
    }

    //String campaignName = "";
    String electionName = "";
    String vrntvd = "";
    vrntvd = request.getParameter("vrntvd");
    boolean isTIK = false;
    if( vrntvd == null ) {
//        vrntvd = "462401515484844";
//        vrntvd = "562401515484844"; -- другие выборы для Эбола
        try {
            rs = st.executeQuery("select vrn FROM voshod.tvd WHERE vrnvibref=" + vrnvibref + " and vidtvd='0'");
            rs.next();
            vrntvd = rs.getString(1);
        }catch(SQLException ex) {
            %>
                <script>
                console.log( "ERROR: <%=ex.getMessage()%>" );
                </script>
            <%
        }
        isTIK = true;
    } else {
    }



    //out.print(request.getSession().getAttribute("vrnvibref"));
    try {
        rs = st.executeQuery("select namvibor FROM voshod.viboryrefer WHERE vrn=" + vrnvibref);
        rs.next();
        electionName = rs.getString(1);
        out.print( electionName );
        rs.close();
    } catch(SQLException ex) {
        %>
            console.error( "<%= ex.getMessage() %>");
        <%
    }
%>

            </div>
            <div class="content">
<!-- /HEADER -->

<!-- CONTENT -->