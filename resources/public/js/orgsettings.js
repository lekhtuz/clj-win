function removeFileToUpload()
{
	$('#logo-upload').val('');
}

$(function() {
	var logoOnchange = function() {
		if (this.checked) {
			$('#logo-groupbox').show();
		} else {
			$('#logo-groupbox').hide();
		}
	}

	var bolMappingOnchange = function() {
		if (this.checked) {
			$('#order-bol-mapping').removeAttr('disabled');
		} else {
			$('#order-bol-mapping').attr('disabled', 'disabled');
		}
	}

	console.log("initpage started")
	
	$('#use-logo-in-bol').change(logoOnchange);
	$('#order-bol-mapping-cb').change(bolMappingOnchange);
	
	logoOnchange();
	bolMappingOnchange();
});

