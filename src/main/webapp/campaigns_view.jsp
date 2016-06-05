<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
                <div class="campaigns">
                    <h1 class="page_title"><%=i18n.getString("tikCampaignsList")%>:</h1>
                    <ul>
                        <li><div class="campaigns_icon"></div><a href="${pageContext.request.pathInfo}?vrnvibref=462401515484839">Eleição para o cargo de Presidente do Partido</a></li>
                        <li><div class="campaigns_icon"></div><a href="${pageContext.request.pathInfo}?vrnvibref=562401515484839">Eleição dos Candidatos ao Comitê Central</a></li>
                        <li><div class="campaigns_icon"></div><a href="${pageContext.request.pathInfo}?vrnvibref=662401515484839">Ratificação dos Candidatos Eleitos nas Conferências Provinciais</a></li>
                    </ul>
                </div>
                <!--
<%=request.getAttribute("javax.servlet.forward.request_uri").toString()%>
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