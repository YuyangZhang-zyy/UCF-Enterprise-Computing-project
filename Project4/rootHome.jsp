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
	color: white;
	text-align: center;
	font-family: Arial;
}

h1 {
	color: yellow;
	font-size: 28pt;
}

h2 {
	color: lime;
	font-size: 24pt;
}

h3 {
	color: white;
	font-size: 16pt;
}


input {
	color: yellow;
	background: #696969;
	font-weight: bold;
	font-size: 16pt;
}

input[type="text"] {
	background: #2F4F4F;
	font-size: 16pt;
}

input[type="submit"] {
	background: #696969;
	color: lime;
}

input[type="reset"] {
	background: #696969;
	color: red;
}

p {
	color: black;
	font-size: 13pt;
}

span {color: red;}

table {
	font-family: Verdana;
	border: 3px solid black;
	text-align: center;
}

textarea {
	background: blue;
	color: white;
	font-family: Verdana;
	font-size: 15pt;
	width: 900px;
	height: 275px;
}

th, td {
	padding: 5px;
	border: 1px solid white;
	color: lime;
	background: black;
}

.main {
	color: white;
}

.center {
	margin-left: auto;
	margin-right: auto;
}

#bl {
	color: #708090
}
-->
</style>
</head>

<body>
	<script>
		function hideTable() 
		{
			document.getElementById("resultTable").style.display = 'none';
			document.getElementById("altTable").style.display = 'none';
		}
	</script>
	<h1>
		<b> Welcome to the Spring 2023 Project 4 Enterprise Database System</b><br>
	</h1>
	<h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using
		A Tomcat Container</h2>
	<hr class="solid">
	<h3>
		You are connected to the Project 4 Enterprise System database as a
		<span>root-level</span> user. <br> Please enter any valid SQL query or update
		command in the box below.
	</h3>
	<br><br>
	<form action="rootUser" method="post">
		<table class="center">
			<tr>
				<td><textarea rows="15" cols="50" name="query"></textarea></td>
			</tr>
		</table>
		<br>
		<input type="submit" value="Execute Command" /> <input type="reset"
			value="Reset Form" /> <input type="button" value="Clear Results"
			onclick="hideTable();" />
	</form>
	<br><br>
	<h3>All execution results will appear below this line.</h3><br>
	<hr class="solid"><br>
	<h3>Database Results:</h3>
	
	<% 
	Object message = request.getAttribute("message");
	if (message == null)
		message = "";
	%>
	<%=message%>
</body>
</html>