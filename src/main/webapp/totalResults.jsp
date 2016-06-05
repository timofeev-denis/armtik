<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="ru.voskhod.tik.TurnOut"%>
<%@page import="ru.voskhod.tik.Settings"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Scanner"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="java.nio.file.Paths"%>
<%@page import="java.nio.file.Files"%>
<%@page import="java.io.File"%>
<%@page import="ru.voskhod.tik.PersonsCollection"%>
<%@page import="ru.voskhod.tik.Person"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="ru.voskhod.loc.i18n"%>
<%@ page import="java.nio.file.Path" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>

<div id="accordion">
    <h3><%=i18n.getString("tikVotingResults")%></h3>
    <div style=" text-align: center;">
        <div style="width: 90%;display: inline-block;text-align: center;">
            <script>
                <%

                String vrnvibref1 = "462401515484839";
                String vrnvibref2 = "562401515484839";
                String vrnvibref3 = "662401515484839";

                String vrntvd1 = "462401515484844";
                String vrntvd2 = "562401515484844";
                String vrntvd3 = "662401515484844";

                String uikvrn1 = "462401515484945";
                String uikvrn2 = "462401515484946";
                String uikNumber = request.getParameter("uikNumber");
                if( uikNumber == null ) {
                    pageContext.setAttribute("commissionName", i18n.getString("tikTitle"));
                } else {
                    pageContext.setAttribute("commissionName", i18n.getString("tikUIK") + " №" + uikNumber);
                }
                PersonsCollection persons = new PersonsCollection();
                PersonsCollection persons2 = new PersonsCollection();
                PersonsCollection persons3 = new PersonsCollection();

                // Шаблон сводной таблицы
                String summaryTableTemplate = "";
                if(isTIK) {
                    Path path =Paths.get(getServletContext().getRealPath("/html/SummaryTable_" + Settings.getProperty("LANGUAGE") + ".html"));
                    List<String> summaryTableLines = Files.readAllLines(path, StandardCharsets.UTF_8);

                    for( String s: summaryTableLines) {
                        summaryTableTemplate += s;
                    }
                }

                ArrayList<String> photos = new ArrayList<String>();
                photos.add( "LuisMamonaJoaoLama.jpg" );
                photos.add( "JoseLuisFrancisco.jpg" );
                photos.add( "MassungunaAlexAfonso.jpg" );
                photos.add( "SebastiaoArsenioCabungula.jpg" );
                photos.add( "HermenegildodaCosta.jpg" );
                photos.add( "AderitoWaldemarAlvesdeCarvalho.jpg" );
                photos.add( "FelisbertoSebastiaodaGracaAmaral.jpg" );
                photos.add( "CarlosManuelGoncalvesAlonso.jpg" );
                photos.add( "RicardoJobEstevao.jpg" );
                photos.add( "FernandoAgostinhodaCosta.jpg" );

                Class.forName("org.postgresql.Driver");
                try {
                    st = conn.createStatement();
                } catch(SQLException ex) {
                    %>
                console.error( "Создание подключения: " + "<%= ex.getMessage() %>");
            <%
        }
        // Результаты голосования
        rs = null;
        int resultsCount;
        boolean hasResults = false;
        try {
            resultsCount = 0;
            rs = st.executeQuery("select numsved, p.famil, p.imia, p.otch, kolza, p.photo "
                    + "from voshod.pg_golosa g, voshod.pgb_sved s, voshod.uchvib u, voshod.persona p "
                    + "where g.vrnsved=s.vrnsved "
                    + "and katsv='1' "
                    + "and u.vrn=s.numsvreestr "
                    + "and u.vrnpersona=p.vrn "
                    + "and g.vrn in (select vrn from voshod.pg where "
                    + "vrntvd in (select vrn from voshod.tvd where vrnvibref=" + vrnvibref1 + " and vrn=" + vrntvd1 + ")"
                    + "and numver=0) ORDER BY kolza DESC, numsved");

            while (rs.next()) {
                persons.addPerson(new Person(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
                resultsCount++;
            }
            hasResults &= resultsCount > 0;

            rs = st.executeQuery("select numsved, p.famil, p.imia, p.otch, kolza, p.photo "
                    + "from voshod.pg_golosa g, voshod.pgb_sved s, voshod.uchvib u, voshod.persona p "
                    + "where g.vrnsved=s.vrnsved "
                    + "and katsv='1' "
                    + "and u.vrn=s.numsvreestr "
                    + "and u.vrnpersona=p.vrn "
                    + "and g.vrn in (select vrn from voshod.pg where "
                    + "vrntvd in (select vrn from voshod.tvd where vrnvibref=" + vrnvibref2 + " and vrn=" + vrntvd2 + ")"
                    + "and numver=0) ORDER BY kolza DESC, numsved");

            while (rs.next()) {
                persons2.addPerson(new Person(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
                resultsCount++;
            }
            hasResults &= resultsCount > 0;

            rs = st.executeQuery("select numsved, p.famil, p.imia, p.otch, kolza, p.photo "
                    + "from voshod.pg_golosa g, voshod.pgb_sved s, voshod.uchvib u, voshod.persona p "
                    + "where g.vrnsved=s.vrnsved "
                    + "and katsv='1' "
                    + "and u.vrn=s.numsvreestr "
                    + "and u.vrnpersona=p.vrn "
                    + "and g.vrn in (select vrn from voshod.pg where "
                    + "vrntvd in (select vrn from voshod.tvd where vrnvibref=" + vrnvibref3 + " and vrn=" + vrntvd3 + ")"
                    + "and numver=0) ORDER BY kolza DESC, numsved");

            while (rs.next()) {
                persons3.addPerson(new Person(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
                resultsCount++;
            }
            hasResults &= resultsCount > 0;

            rs.close();
        } catch(SQLException ex) {
            %>
                console.error( "Ошибка при определении результов голосования: " + "<%= ex.getMessage() %>");
                <%
            }
            // Результаты голосования не найдены
            if (!hasResults) {
                %>
                console.info( "Результаты голосования не найдены!" );
                <%

                try {
                    rs = st.executeQuery("select numsved, p.famil, p.imia, p.otch, p.photo "
                        + "from voshod.pgb_sved s, voshod.uchvib u, voshod.persona p "
                        + "where u.vrnvibref = " + vrnvibref1 + " "
                        + "and katsv='1' "
                        + "and u.vrn=s.numsvreestr "
                        + "and u.vrnpersona=p.vrn "
                        + "ORDER BY  numsved");
                    while (rs.next()) {
                        persons.addPerson(new Person(rs.getString(2), rs.getString(3), rs.getString(4), 0));
                    }

                    rs = st.executeQuery("select numsved, p.famil, p.imia, p.otch, p.photo "
                        + "from voshod.pgb_sved s, voshod.uchvib u, voshod.persona p "
                        + "where u.vrnvibref = " + vrnvibref2 + " "
                        + "and katsv='1' "
                        + "and u.vrn=s.numsvreestr "
                        + "and u.vrnpersona=p.vrn "
                        + "ORDER BY  numsved");
                    while (rs.next()) {
                        persons2.addPerson(new Person(rs.getString(2), rs.getString(3), rs.getString(4), 0));
                    }

                    rs = st.executeQuery("select numsved, p.famil, p.imia, p.otch, p.photo "
                        + "from voshod.pgb_sved s, voshod.uchvib u, voshod.persona p "
                        + "where u.vrnvibref = " + vrnvibref3 + " "
                        + "and katsv='1' "
                        + "and u.vrn=s.numsvreestr "
                        + "and u.vrnpersona=p.vrn "
                        + "ORDER BY  numsved");
                    while (rs.next()) {
                        persons3.addPerson(new Person(rs.getString(2), rs.getString(3), rs.getString(4), 0));
                    }

            rs.close();
        } catch(SQLException ex) {
            %>
                console.error( "Список кандидатов: " + "<%= ex.getMessage() %>");
                <%
            }
        }
        // Количество обработанных протоколов
        try {
            rs = st.executeQuery("select count(*) total, count(prprot1) done "
                    + " from voshod.tvd where"
                    + " vrnvibref=" + vrnvibref
                    + " and tvdrod=(select vncode from voshod.tvd where vrn=" + vrntvd + ")"
                    + " and vidtvd='5'");
            rs.next();
            pageContext.setAttribute("protocolTotal", rs.getInt(1));
            pageContext.setAttribute("protocolDone", rs.getInt(2));
            rs.close();
        } catch(SQLException ex) {
            %>
                console.error( "Количество обработанных протоколов: " + "<%= ex.getMessage() %>");
                <%
            }

            // Наименование выборов и дата голосования
            String electionDateVote = null;
            try {
                rs = st.executeQuery("SELECT namvibor, to_char(datgol, 'DD') "
                        + "|| ' ' "
                        + " || case to_char(datgol, 'MM') "
                        + " when '01' then '" + i18n.getString("tikMonth01") + "' "
                        + " when '02' then '" + i18n.getString("tikMonth02") + "'"
                        + " when '03' then '" + i18n.getString("tikMonth03") + "'"
                        + " when '04' then '" + i18n.getString("tikMonth04") + "'"
                        + " when '05' then '" + i18n.getString("tikMonth05") + "'"
                        + " when '06' then '" + i18n.getString("tikMonth06") + "'"
                        + " when '07' then '" + i18n.getString("tikMonth07") + "'"
                        + " when '08' then '" + i18n.getString("tikMonth08") + "'"
                        + " when '09' then '" + i18n.getString("tikMonth09") + "'"
                        + " when '10' then '" + i18n.getString("tikMonth10") + "'"
                        + " when '11' then '" + i18n.getString("tikMonth11") + "'"
                        + " when '12' then '" + i18n.getString("tikMonth12") + "'"
                        + " end"
                        + " || ' ' "
                        + " || to_char(datgol, 'YYYY') "
                        + " FROM voshod.viboryrefer"
                        + " where vrn=" + vrnvibref);
                rs.next();
                electionDateVote = rs.getString(2);
                rs.close();
            } catch(SQLException ex) {
                %>
                console.error( "Наименование выборов и дата голосования: " + "<%= ex.getMessage() %>");
                <%
            }
            // Явка
            String turnOutLabels = "";
            String turnOutData = "";
            try {
                String q = "select "
                        + " (select kolza from voshod.pg_golosa where "
                        + "vrn in (select vrn from voshod.pg where vrntvd=" + vrntvd + " and numver=0) "
                        + "and vrnsved in (select vrnsved from voshod.pgb_sved where vrn=" + vrnsved + " and katsv='0' and numsved=1)) votersCurrent, "
                        + "(select kolza from voshod.pg_golosa where "
                        + "vrn in (select vrn from voshod.pg where vrntvd=" + vrntvd + " and numver=0) "
                        + "and vrnsved in (select vrnsved from voshod.pgb_sved where vrn=" + vrnsved + " and katsv='0' and numsved=2)) votersTotal "
                        + "from voshod.dual";
                %>
                console.log( "***** <%=q%>" );
                <%
                rs = st.executeQuery( q );
                if( rs.next() ) {
                    pageContext.setAttribute("votersTotal", rs.getInt(1));
                    pageContext.setAttribute("votersCurrent", rs.getInt(2));
                }
                // Данные не найдены - устанавливаем явке значение "0"
                if(pageContext.getAttribute("votersCurrent") == null || pageContext.getAttribute("votersCurrent").toString().equals("0")) {
                    pageContext.setAttribute("votersTotal", 1);
                    pageContext.setAttribute("votersCurrent", 0);
                }
                rs.close();

                // Ход за все ОВ
                ArrayList<TurnOut> turnOut = new ArrayList<TurnOut>();
                if( isTIK ) {
                    rs = st.executeQuery("select sum(kolreg) as total, sum(kolgolos) as current, kontrvr "
                            + " from voshod.XODGOL x, (select vrn from voshod.tvd where vrnvibref=" + vrnvibref + " and vidtvd='5') t where x.vrntvd=t.vrn"
                            + " group by kontrvr order by 3");
                } else {
                    rs = st.executeQuery("select kolreg, kolgolos, kontrvr from voshod.XODGOL x, "
                            + "(select vrn from voshod.tvd where vrnvibref=" + vrnvibref + " and vidtvd='5' AND vrn=" + vrntvd + ") t "
                            + "where x.vrntvd=t.vrn order by kontrvr");
                }
                while(rs.next()) {
                    turnOut.add(new TurnOut(rs.getInt(1), rs.getInt(2), rs.getString(3)));
                }

                if( turnOut.size() > 0 ) {
                    if( isTIK ) {
                        // Заменяем данные за последнее ОВ
                        TurnOut tmp;
                        tmp = turnOut.get(turnOut.size() - 1);
                        tmp.setTotal(Integer.parseInt(pageContext.getAttribute("votersTotal").toString()));
                        tmp.setCurrent(Integer.parseInt(pageContext.getAttribute("votersCurrent").toString()));
                        turnOut.set(turnOut.size() - 1, tmp);
                    }
                    DecimalFormat df = new DecimalFormat("##.#");
                    double percents;
                    // Формируем данные для диаграммы
                    for(TurnOut t : turnOut) {
                        turnOutLabels += "\"" + t.getTime() + "\", ";
                        percents = (double) t.getCurrent() / t.getTotal() * 100;
                        turnOutData += "\"" + df.format(percents).replace(",", ".") + "\", ";
                    }
                    turnOutLabels = turnOutLabels.substring(0, turnOutLabels.length() - 2);
                    turnOutData = turnOutData.substring(0, turnOutData.length() - 2);
                } else {
                    // Данные не найдены - поканываем "0"
                    turnOutLabels = "\"00:00\"";
                    turnOutData = "\"0\"";
                }

            } catch(SQLException ex) {
                %>
                console.error( "Явка: " + "<%= ex.getMessage().replace("\"", "'").replace("\n", "") %>");
                <%
                pageContext.setAttribute("votersTotal", 1);
                pageContext.setAttribute("votersCurrent", 0);
            }
            String protocolTemplate = "";
            boolean protocolIsEmpty = true;
            if (isTIK) {
                // Шаблон протокола ТИК
                %>
                console.info( "<%=Paths.get(getServletContext().getRealPath("/html/TikProtocol_" + Settings.getProperty("LANGUAGE") + "_" + vrnvibref + ".html")).toString().replace("\\", "\\\\")%>" );
                <%

                List<String> protocolLines = Files.readAllLines(Paths.get(getServletContext().getRealPath("/html/TikProtocol_" + Settings.getProperty("LANGUAGE") + "_" + vrnvibref + ".html")), StandardCharsets.UTF_8);
                for( String s: protocolLines) {
                    protocolTemplate += s;
                }

                // Протокол ТИК: наименование выборов и дата голосования
                protocolTemplate = protocolTemplate.replace("{ElectionName}", electionName);
                protocolTemplate = protocolTemplate.replace("{ElectionDateVote}", electionDateVote);

                // Подготовка итогового протокола (чтение данных и вставка в шаблон)
                try {
                    rs = st.executeQuery("select s.vrnsved, numsved, case katsv when '0' then txtsvedlong else txtsved end, kolza"
                            + " from voshod.pg p, voshod.pg_golosa g, voshod.pgb_sved s"
                            + " where g.vrnsved=s.vrnsved"
                            + " and katsv in ('0', '1' ) "
                            + " and g.vrn=p.vrn "
                            + " and p.vrntvd=" + vrntvd
                            + " and numver=0"
                            + " ORDER BY katsv, numsved");
                    while (rs.next()) {
                        protocolTemplate = protocolTemplate.replace("{" + rs.getString(1) + "}", rs.getString(4));
                        protocolIsEmpty = false;
                    }
                    rs.close();
                } catch (SQLException ex) {
                    %>
                console.error( "Подготовка итогового протокола " + "<%= ex.getMessage()%>");
                <%
            }
            // Количество обработанных протоколов в протоколе ТИК
            protocolTemplate = protocolTemplate.replace("{tu}", pageContext.getAttribute("protocolTotal").toString());
            protocolTemplate = protocolTemplate.replace("{pu}", pageContext.getAttribute("protocolDone").toString());

            // Подготовка сводной таблицы (чтение данных и вставка в шаблон)
            try {
                rs = st.executeQuery("select"
                        + " cast(t.vrn as text) || '_' || cast(s.vrnsved as text) as data_id,"
                        + " coalesce((select kolza from voshod.pg p, voshod.pg_golosa g where"
                        + " vrnblank=s.vrn"
                        + " and p.vrntvd=t.vrn"
                        + " and p.vrn=g.vrn"
                        + " and p.numver=0"
                        + " and vrnsved=s.vrnsved),0) as data_value"
                        + " from voshod.tvd t, voshod.pgb_sved s"
                        + " where t.vrnvibref=" + vrnvibref
                        + " and (t.vrn=" + vrntvd + " or t.tvdrod=(select vncode from voshod.tvd where vrn=" + vrntvd + "))"
                        + " and s.vrn=" + vrnsved
                        + " and katsv in ('0', '1')"
                        + " order by vidtvd, numtvd, katsv, numsved");

                while (rs.next()) {
                    summaryTableTemplate = summaryTableTemplate.replace("{" + rs.getString(1) + "}", rs.getString(2));
                }
                rs.close();
            } catch(SQLException ex) {
                %>
                console.error( "Подготовка сводной таблицы (чтение данных и вставка в шаблон): " + "<%= ex.getMessage() %>");
                <%
            }
            summaryTableTemplate = summaryTableTemplate.replace("{ElectionName}", electionName);
            summaryTableTemplate = summaryTableTemplate.replace("{ElectionDateVote}", electionDateVote);

            // Количество обработанных протоколов в сводной таблице
            summaryTableTemplate = summaryTableTemplate.replace("{tu}", pageContext.getAttribute("protocolTotal").toString());
            summaryTableTemplate = summaryTableTemplate.replace("{pu}", pageContext.getAttribute("protocolDone").toString());

        } else {
            // Протокол УИК
            try {
                rs = st.executeQuery("select document, to_char( date_add, 'DD.MM.YYYY в HH24:MI' ) FROM voshod.documents d WHERE"
                        + " type_id ='IP'"
                        + " and cast(uiknum as double precision)=(select numtvd from voshod.tvd where vrn=" + vrntvd + ")"
                        + " and date_add=(select max(date_add) from voshod.documents where type_id =d.type_id and vrndata=d.vrndata and uiknum=d.uiknum)");
                if( rs.next() ) {
                    protocolTemplate = rs.getString(1);
                    protocolTemplate = protocolTemplate.replace("#SignDateTime#", rs.getString(2));
                    protocolIsEmpty = false;
                }
                rs.close();
            } catch(SQLException ex) {
                %>
                console.error( "Протокол УИК " + "<%= ex.getMessage() %>");
                <%
            }
        }
        st.close();

    %>

                var barChartRealData = {
                    labels : [<%= persons.getLabels(0, 2) %>],
                    datasets : [{
                        fillColor : "rgba(151,187,205,0.5)",
                        strokeColor : "rgba(151,187,205,0.8)",
                        highlightFill : "rgba(151,187,205,0.75)",
                        highlightStroke : "rgba(151,187,205,1)",
                        data : [<%= persons.getVotes(0, 2) %>]
                    }]
                };

                var barChartRealData2 = {
                    labels : [<%= persons2.getLabels(0, 2) %>],
                    datasets : [{
                        fillColor : "rgba(151,187,205,0.5)",
                        strokeColor : "rgba(151,187,205,0.8)",
                        highlightFill : "rgba(151,187,205,0.75)",
                        highlightStroke : "rgba(151,187,205,1)",
                        data : [<%= persons2.getVotes(0, 2) %>]
                    }]
                };

                var barChartRealData3 = {
                    labels : [<%= persons3.getLabels(0, 2) %>],
                    datasets : [{
                        fillColor : "rgba(151,187,205,0.5)",
                        strokeColor : "rgba(151,187,205,0.8)",
                        highlightFill : "rgba(151,187,205,0.75)",
                        highlightStroke : "rgba(151,187,205,1)",
                        data : [<%= persons3.getVotes(0, 2) %>]
                    }]
                };

                var options = {
                    graphMax: <%= persons.getBarMax() %>,
                    animationSteps : 80,
                    canvasBorders : false,
                    canvasBordersWidth : 1,
                    canvasBordersColor : "#e0e0e0",
                    legend : false,
                    inGraphDataShow : true,
                    annotateDisplay : true,
                    graphTitleFontSize: 18,
                    scaleShowLabels : false,
                    inGraphDataFontFamily: "Helvetica, Tahoma, Arial",
                    inGraphDataFontSize: 28,
                    inGraphDataFontStyle: "bold",
                    inGraphDataYPosition: 1,
                    inGraphDataFontColor: "#4775a9",
                    inGraphDataTmpl: "<" + "%=v3%>%",
                    scaleFontSize: 14,
                    scaleFontStyle: "normal",
                    scaleFontFamily: "Lucida Console, Monaco, monospace",
                    annotateLabel: "<" + "%=v3%>%" // угловая скобка отделена, т.к. вместе с % - это тэг jsp
                };

                var options2 = {
                    graphMax: <%= persons2.getBarMax() %>,
                    animationSteps : 80,
                    canvasBorders : false,
                    canvasBordersWidth : 1,
                    canvasBordersColor : "#e0e0e0",
                    legend : false,
                    inGraphDataShow : true,
                    annotateDisplay : true,
                    graphTitleFontSize: 18,
                    scaleShowLabels : false,
                    inGraphDataFontFamily: "Helvetica, Tahoma, Arial",
                    inGraphDataFontSize: 28,
                    inGraphDataFontStyle: "bold",
                    inGraphDataYPosition: 1,
                    inGraphDataFontColor: "#4775a9",
                    inGraphDataTmpl: "<" + "%=v3%>%",
                    scaleFontSize: 14,
                    scaleFontStyle: "normal",
                    scaleFontFamily: "Lucida Console, Monaco, monospace",
                    annotateLabel: "<" + "%=v3%>%" // угловая скобка отделена, т.к. вместе с % - это тэг jsp
                };

                var options3 = {
                    graphMax: <%= persons3.getBarMax() %>,
                    animationSteps : 80,
                    canvasBorders : false,
                    canvasBordersWidth : 1,
                    canvasBordersColor : "#e0e0e0",
                    legend : false,
                    inGraphDataShow : true,
                    annotateDisplay : true,
                    graphTitleFontSize: 18,
                    scaleShowLabels : false,
                    inGraphDataFontFamily: "Helvetica, Tahoma, Arial",
                    inGraphDataFontSize: 28,
                    inGraphDataFontStyle: "bold",
                    inGraphDataYPosition: 1,
                    inGraphDataFontColor: "#4775a9",
                    inGraphDataTmpl: "<" + "%=v3%>%",
                    scaleFontSize: 14,
                    scaleFontStyle: "normal",
                    scaleFontFamily: "Lucida Console, Monaco, monospace",
                    annotateLabel: "<" + "%=v3%>%" // угловая скобка отделена, т.к. вместе с % - это тэг jsp
                };
            </script>

            <div class="campaign_info"><img src="img/president.png" width="200" height="200"><p>Eleição para o cargo de Presidente do Partido</p></div>
            <div class="campaign_graphics"><canvas id="canvas" height="400" width="700"></canvas></div>
            <div class="clearfix"></div>

            <div class="campaign_info"><img src="img/mpla.png" width="200" height="200"><p>Eleição dos Candidatos ao Comitê Central</p></div>
            <div class="campaign_graphics"><canvas id="canvas2" height="400" width="700"></canvas></div>
            <div class="clearfix"></div>

            <div class="campaign_info"><img src="img/mpla_flag.png" width="200" height="200"><p>Ratificação dos Candidatos Eleitos nas Conferências Provinciais</p></div>
            <div class="campaign_graphics"><canvas id="canvas3" height="400" width="700"></canvas></div>
            <div class="clearfix"></div>

            <script>
                window.onload = function(){
                    var myBar1 = new Chart(document.getElementById("canvas").getContext("2d")).Bar(barChartRealData, options);
                    var myBar2 = new Chart(document.getElementById("canvas2").getContext("2d")).Bar(barChartRealData2, options2);
                    var myBar3 = new Chart(document.getElementById("canvas3").getContext("2d")).Bar(barChartRealData3, options3);
                }
            </script>

        </div>
    </div>

</div>
<%
    request.setAttribute("showTurnout", "1");
    if( uikNumber == null ) {
        request.setAttribute("showProtocols", "1");
    } else {
        request.setAttribute("showProtocols", "0");
    }
%>
<%@include file="footer.jsp" %>
