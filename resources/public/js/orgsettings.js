function removeFileToUpload()
{
	document.getElementById('logo-upload').value = '';
}

function turnLogoOnOff()
{
	if (document.getElementById('use-logo-in-bol').checked) {
		document.getElementById('logo-groupbox').style.display = 'block';
	} else {
		document.getElementById('logo-groupbox').style.display = 'none';
	}
}

function turnBolMappingOnOff()
{
	if (document.getElementById('order-bol-mapping-cb').checked) {
		document.getElementById('order-bol-mapping').disabled = false;
	} else {
		document.getElementById('order-bol-mapping').disabled = true;
	}
}

function initpage()
{
	console.log("initpage started")
	turnBolMappingOnOff();
}
