<%@page import="java.text.DecimalFormat"%>
<%@page import="ru.voskhod.loc.i18n"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- <h2>Данные на 16:00</h2> -->
<!-- request.getAttribute("showTurnout") -->
<center>
    <%
        if (pageContext.getAttribute("commissionName") != null ) {
            %>
            <h2 class="comission_name"><%= pageContext.getAttribute("commissionName") %></h2>
            <%
        }
    %>
    
<%
DecimalFormat df = new DecimalFormat("##.#");
if( request.getAttribute("showTurnout") != null && request.getAttribute("showTurnout").equals("1")) {
%>
    <div id="canvas-holder" class="doughnut_chart">
        <canvas id="chart-area" width="200" height="200"/>
    </div>
    <p class="doughnut_label"><%=pageContext.getAttribute("votersCurrent")%>/<%=pageContext.getAttribute("votersTotal")%><br>
        <%=i18n.getString("tikTurnout")%></p>
    <br>
    <br>
    <script>
    var DoughnutData = [
        {
            <%
            String votersPercents = "";
            int votersTotal = 0;
            int votersCurrent = 0;
            try {
                votersTotal = Integer.parseInt(pageContext.getAttribute("votersTotal").toString());
                votersCurrent = Integer.parseInt(pageContext.getAttribute("votersCurrent").toString());
                if (votersTotal == votersCurrent) {
                    votersPercents = "100%";
                } else {
                    votersPercents = df.format((double) votersCurrent / (votersTotal) * 100).replace(",", ".") + "%";
                }
            } catch( Exception ex ) {
    //                ex.printStackTrace();
            }
            %>
            value: <%= votersCurrent %>,
            color: "#4775a9",
            highlight: "#638fc5",
            label: "Неявка"
        },
        {
            value: <%= votersTotal - votersCurrent %>,
            //color: "#96acd5",
            color: "rgba(151,187,205,0.5)",
            highlight: "#b1c8f3",
            label: "Явка"
        }
    ];
    console.log( "votersTotal: <%=votersTotal%>, votersCurrent: <%=votersCurrent%>");
    var opts = {legend : true,
        crossText : ["<%= votersPercents %>"],
        crossTextIter: ["all"],
        crossTextOverlay : [true],
        crossTextFontSize : [28],
        crossTextFontStyle : ["bold"],
        //crossTextFontFamily : ["Verdana"],
        crossTextFontFamily : ["Helvetica, Tahoma, Arial"],
        crossTextFontColor : ["#4775a9"],
        crossTextRelativePosX : [2],
        crossTextRelativePosY : [2],
        crossTextPosX : [0],
        crossTextPosY : [0],
        crossTextAlign : ["center"],
        crossTextBaseline : ["middle"],
        canvasBorders : false,
        animationEasing: 'easeOutQuint',
        animationSteps: 80
    };

    var myPie = new Chart(document.getElementById("chart-area").getContext("2d")).Doughnut(DoughnutData, opts);

    </script>
<%
}
%>

</center>
