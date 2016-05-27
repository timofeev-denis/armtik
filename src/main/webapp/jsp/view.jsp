<%@include file="/header.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include.jsp" %> 
<%@page isErrorPage="true"%>

<style type="text/css" media="print">
    
    @media print {
    #print1 #otchet {
        background-color: white;
        border: 2px dotted #c0c0c0;
        height: 500px;
        width: 600px;
        position: fixed;
        font-size: 14px;
        line-height: 18px;
        -webkit-box-shadow: none;
        -moz-box-shadow:  none;
    }
}
</style>  
<script type="text/javascript" >
    var globObj;
    var globCurrDt = getCurrentDate();
     $(document).ready(function(){
        $("#dialog-message").dialog({
            autoOpen: false,
            modal: true,
            width: 360,
            height: 200,
            buttons: {
                "OK": function() {
                    $(this).dialog("close");
                }
            }
        });
         
        $('form input[type=button]').click(function() {
            var form = $(this).parents("form");
            var inputs = form.find("input[name*='nameFile']");
            var dataaa;
            var js_contenType;
            var js_dataType;
            var button = $(this).attr("name");
            if ( button === 'nameDel')
            {
                dataaa = 'exec=del&numuik='+ inputs.val().toString();
                js_contenType = "application/x-www-form-urlencoded; charset=UTF-8";
                js_dataType = "";
            }
            else if (button === 'nameExec')
            {
                dataaa = {msgName:inputs.val().toString(),msgHtml:""};
                dataaa = JSON.stringify(dataaa);
                js_contenType = "application/json; charset=utf-8";
                js_dataType = "json";
            }
            
            //alert(window.location.pathname);
            $.ajax({ 
            url: window.location.pathname, 
            type: 'POST', 
            contentType: js_contenType,
            dataType: js_dataType,
            data: dataaa,
            success: function(response){
                if (button === 'nameDel')
                {
                    $( "#dialog-message" ).html(response);
                    $( "#dialog-message" ).dialog("open");
                    form.parent("li").remove();
                    return;
                }
                //$('#view_result').html("");
                var obj = JSON.stringify(response);
                obj = JSON.parse(obj);
                globObj = obj.msgList;
                //$('#text-documents').html("nameFile:- " + obj.msgName +  "</br>List text: " + obj.msgList +"</br>HTML text:- " + obj.msgHtml );
                //return;
                getList(obj.msgList);
                //$('#splitter').jqxSplitter({ width: '100%', height: '100%', panels: [{ size: '20%', min: 250 }, { size: '70%', collapsible: false}] });
                //$('#splitter').jqxSplitter({ splitBarSize: '3px' });
                
                $( "#dialog-confirm" ).dialog({
                    resizable: true,
                    height: 600,
                    width: '90%',
                    modal: true,
                    title: "<%=i18n.getString("tikViewMessage")%>: " + obj.msgName,
                    buttons: {
                      "<%=i18n.getString("tikLoad")%>": function() {
                        //$( this ).dialog( "close" );
                        $.ajax({
                            url: window.location.pathname,
                            type: 'POST',
                            //contentType: "text/plain; charset=utf-8",
                            //dataType: "text",
                            data: 'exec=load&numuik='+ obj.msgName,
                            success: function (data) {
                                //alert(data);
                                $( "#dialog-confirm" ).dialog( "close" );
                                $( "#dialog-message" ).html(data);
                                $( "#dialog-message" ).dialog("open");
                                form.parent("li").remove();
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                $('#error').empty().append(textStatus + "<br />" + errorThrown + "<br />" + jqXHR.responseText);
                            }
                        });
                      },
                      "<%=i18n.getString("tikPrint1")%>": function() {
                        $("#print1").printElement({leaveOpen:true, printMode:'popup', overrideElementCSS:true});
                      },
                      "<%=i18n.getString("tikPrint2")%>": function() {
                        $("#print2").printElement({leaveOpen:true, printMode:'popup',overrideElementCSS:true});
                      },
                      "<%=i18n.getString("tikSkip")%>": function() {
                        $( this ).dialog( "close" );
                      }
                    }
                  });
                  $('#dialog-confirm').layout({ 
			name:                   'splitter', // for debugging & auto-adding buttons (see below)
                        autoResize:             true,	// try to maintain pane-percentages
                        autoReopen:             true,	// auto-open panes that were previously auto-closed due to 'no room'
			autoBindCustomButtons:  true,
			west__paneSelector:     '#list-documents',
			center__paneSelector:   '#text-documents',
                        //east__paneSelector:     'text-documents', 
			west__size:             .35,		// percentage size expresses as a decimal
			east__size:             .20,
			minSize:                100,
		//,	center__minWidth:		239 // 3 x 75 (panes) + 2 x 6 (spacing) + 2 x 1 (center-border)
			noRoomToOpenAction:     "hide" // 'close' or 'hide' when no room to open a pane at minSize
                    });

                //alert("nameFile:- " + obj.msgName +"</br>HTML text:- " + obj.msgHtml);
            },
            error: function(jqXHR, textStatus, errorThrown){      
                $('#error').empty().append(textStatus + "<br />" + errorThrown + "<br />" + jqXHR.responseText);
            }
        });
      });
      
    });
    function getList(data) 
    {
        $("#list-documents").empty();
        $("#text-documents").empty();
        //$("#text-documents").html();
        var obj = data;
        ul_list = $("<ul>");
        //ul_text = $("<ul>");
        for (var i = 0, l = obj.length; i < l; ++i) 
        {
            ul_list.append("<li><a id=\"dlist" + i + "\" onclick='selectedList(\"dlist" + i + "\")' href='#'>" + obj[i][0] + "</a></li>");
            //ul_text.append("<li id='dtext" + i + "' class='text-hide'>" + obj[i][1] + "</li>");
        }
        $("#list-documents").append(ul_list);
        //$("#text-documents").append(ul_text);
    }
    function selectedList(elementId)
    {
        $('[id^="dlist"]').each(function() {
            $(this).removeClass("list-selected");
        });
        var i = elementId.replace("dlist","");
        ul_text = $("<ul>");
        ul_text.append("<li id='dtext" + i + "'>" + globObj[i][1] + "</li>");
        $("#text-documents").empty();
        $("#text-documents").append(ul_text);
        $("#currdt").append("<%=i18n.getString("tikFormed")%>: " + globCurrDt);
        //alert(elementId);
        $("#"+elementId).addClass("list-selected");
    }
    function getCurrentDate()
    {
        var ndata=new Date();
        var month=ndata.getMonth();
        var date=ndata.getDate();
        var year=ndata.getYear();

        if (month < 10) {month = "0" + month; }
        if (date < 10) {date = "0" + date; }
        return date +"."+ month +"."+ year;
    }
    function printBlock(print_id)
    {
        productDesc = document.getElementById(print_id).outerHTML;
        body = document.getElementById("text_doc1");
        body.className += 'printSelected';
        body.innerHTML +='<div id="print_view" class="printSelection">' + productDesc + '</div>';
        //window.print();
        //window.setTimeout(pageCleaner, 0);

        return false;
    }

    function pageCleaner()
    {
            body = document.getElementById("text_doc1");
            body.className = '';
            element = document.getElementById('print_view');
            element.parentNode.removeChild(element);
    }
</script>

<center>
<h1><%=i18n.getString("tikIncomingMessages")%></h1>
<c:set var="test" value="1" />
<ul class="files">
    <c:forEach items="${msg_name}" var="current">
    <li>
        <form:form id="${current}" method="POST">
            <span class="file_name"><c:out value="${current}"/>
                <input type="button" name="nameExec" value = "<%=i18n.getString("tikProcess")%>" /> 
                <input type="button" name="nameDel" value = "<%=i18n.getString("tikDelete")%>" /> 
                <input type="hidden" name="nameFile" value = "${current}" />
            </span>
        </form:form>
    </li>
    </c:forEach>
</ul>
</center>
<div id="dialog-confirm">
    <div style="border: none;" id='splitter'>
        <div id="list-documents"></div>
        <div id="text-documents"></div>
    </div>
</div>
<div id="dialog-message"></div>
<div id="error"></div>
<%@include file="/footer.jsp" %>