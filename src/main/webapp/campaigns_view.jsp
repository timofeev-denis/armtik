<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
                <div class="campaigns">
                    <h1 class="page_title"><%=i18n.getString("tikCampaignsList")%>:</h1>
                    <ul>
                        <li><div class="campaigns_icon"></div><a href="${pageContext.request.contextPath}"><%=electionName%></a></li>
                    </ul>
                </div>
                <!--
                ${campaignID}
                <div id="intro">					
                        <p><strong>This is a demo.</strong><br />
                                Click the menu icon to open the menu.</p>

                        <p>The links in the menu link to a section on the same page, some small javascript makes the page scroll smoothly.</p>
                </div>
                <div id="first">
                        <p><strong>This is the first section.</strong><br />
                                Notice how the fixed header and footer slide out along with the page.</p>

                        <p><a href="#menu">Open the menu.</a></p>
                </div>
                <div id="second">
                        <p><strong>This is the second section.</strong><br />
                                <a href="#menu">Open the menu.</a></p>
                </div>
                <div id="third">
                        <p><strong>This is the third section.</strong><br />
                                <a href="#menu">Open the menu.</a></p>
                </div>
                -->
<!-- footer -->
<%@include file="footer.jsp" %>