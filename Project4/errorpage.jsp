<%-- Name: Yuyang Zhang
 Course: CNT 4714 - Project Four - Spring 2023
 Assignment title: Developing A Three-Tier Distributed Web-Based Application
 Date: April 23, 2023
--%>

<!DOCTYPE html>
<html lang="en">

<head>
<title>CNT4714 Project 4</title>
<meta charset="utf-8">
<style type="text/css">
<!--
body {
	background: black;
	text-align: center;
	font-family: Arial;
}

h1 {
	color: yellow;
	font-size: 28pt;
background: blue;
}

h2 {
	color: red;
	font-size: 36pt;
}

h3 {
	color: #708090;
	font-size: 24pt;
}

-->
</style>
</head>
<body>
	<h1> CNT 4714 Enterprise Database System</h1>
<br><br><br>
	<h2>Authentication Error</h2>
<br><br><br>

<h3> Access To System Is Denied <br>Please Try Again</h3>
	
	<% 
	Object message = request.getAttribute("message");
	if (message == null)
		message = "";
	%>
	<%=message%>
</body>
</html>