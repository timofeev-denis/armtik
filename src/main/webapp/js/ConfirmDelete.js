$(function() {
	$('.deleteButton').button().click(function(){
		$( "#dialog-confirm-delete" ).dialog( "open" );
	});

        $('#dialog-message').dialog({
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

	$('#dialog-confirm-delete').dialog({
		autoOpen: false,
		modal: true,
		height: 260,
		width: 400,
		buttons: [
                    {
                        text: YES,
                        click: function() {
                                $('#deleteDataForm').submit();
                                console.trace( "YES" );
                                $(this).dialog("close");
                        }
                    },
                    {
                        text: NO,
			click: function() {
				console.trace( "NO" );
				$(this).dialog("close");
			}
                    }
                ]
	});
});
