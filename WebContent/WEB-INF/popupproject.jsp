<html>
<head>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js">\
</script><link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css" />
<script>
$(document).ready(function(){
	
	$( "#dialog" ).hide();
	
  $("#btn1").click(function(){
    $( "#dialog" ).dialog();
  });
  
  	$( "#dialogAdd" ).hide();
	
  $("#btn2").click(function(){
    $( "#dialogAdd" ).dialog();
  });
  
});
</script>



</head>
<body>
<!-----------------For Update Project----------->
<button id="btn1">UpdateProject</button><br>

<div id="dialog" title="UpdateProject">
  
<form action="" method="post">
	<input type="hidden" name="rowID" value="">
<p>
Product ID:<br />
<input type="text" name="productid" size="8" maxlength="8"
value="" />
</p>
<p>
ProductName:<br />
<input type="text" name="productname" size="25" maxlength="25"
value="" />
</p>
<p>
StartDate:<br />
<input type="text" name="StartDate" size="6" maxlength="6"
value="" />
</p>
<p>
Description:<br />
<textarea name="description" rows="5" cols="25">
</textarea>
</p>
<p>
<input type="submit" name="submit" value="Submit" />
</p>
</form>
  </div>
  <!---------------------Add project---------------------->
  
  <button id="btn2">AddProject</button><br>
  
  
  
  <div id="dialogAdd" title="Add Project">
  
<form action="" method="post">
	<input type="hidden" name="rowID" value="">
<p>
Product ID:<br />
<input type="text" name="productid" size="8" maxlength="8"
value="" />
</p>
<p>
ProductName:<br />
<input type="text" name="productname" size="25" maxlength="25"
value="" />
</p>
<p>
StartDate:<br />
<input type="text" name="StartDate" size="6" maxlength="6"
value="" />
</p>
<p>
Description:<br />
<textarea name="description" rows="5" cols="25">
</textarea>
</p>
<p>
<input type="submit" name="submit" value="Submit" />
</p>
</form>
  </div>










</body>
</html>