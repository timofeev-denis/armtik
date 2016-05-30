<%@page import="ru.voskhod.loc.i18n"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- /CONTENT -->

<!-- FOOTER -->
            <div id="sidebar">
                <%@include file="sidebar.jsp" %>
            </div>
        </div>
        <nav id="menu">
            <ul>
                <li><a href="#" data-url="${pageContext.request.contextPath}/"><%=i18n.getString("tikHoldingCampaign")%></a></li>
                <li><a href="#" data-url="${pageContext.request.contextPath}/Campaigns"><%=i18n.getString("tikChangingCampaign")%></a></li>
                <li><a href="#" data-url="${pageContext.request.contextPath}/Exchange"><%=i18n.getString("tikLoadingUIKData")%></a></li>
                <li><a href="#" data-url="${pageContext.request.contextPath}/Admin"><%=i18n.getString("tikAdministration")%></a></li>
            </ul>
        </nav>

    </body>
</html>
<!-- /FOOTER -->