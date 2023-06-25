<%--Name: Yuyang Zhang
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
	color: red;
	font-size: 28pt;
}

h2 {
	color: cyan;
	font-size: 24pt;
}

h3 {
	color: white;
	font-size: 16pt;
}

input[type="text"]  {
	color: yellow;
	background: #976d00;
	font-weight: bold;
    width: 95%;
}

input[type="submit"] {
	background: #976d00;
	color: lime;
}

input[type="button"] {
	color: red;
	background: #976d00;
}

span {color: red;}

table {
	font-family: Verdana;
	border: 2px solid yellow;
	text-align: center;
	table-layout: fixed;
    width:700px;
}

th, td {
	padding: 5px;
	border: 1px solid yellow;
	color: white;
	background: black;
    table-layout: fixed;
}

.center {
	margin-left: auto;
	margin-right: auto;
}

div{
	padding: 25px  50px;
	height: 150px;
	border: 1px solid white;
	text-align: center;
}

span.title{
	position: relative;
	top: -35px;
	left: -300px;
	background: black;
	color: white;
    text-align: left;
}

-->
</style>
</head>
<body>
	<script>
		function hideTable() {
			document.getElementById("resultTable").style.display = 'none';
		}
	</script>

	<h1>
		<b> Welcome to the Spring 2023 Project 4 Enterprise Database System</b><br>
	</h1>
	<h2>Data Entry Application</h2>
	<hr class="solid">
	<h3>
		You are connected to the Project 4 Enterprise System database as a
		<span>data-entry-level</span> user. <br> Enter the data values in a
		form below to add a new record to the corresponding database table.
	</h3>
	<hr class="solid">
	<br>
	<div>
		<span class="title">Suppliers Record Insert</span>
		<form action="dataentrySuppliers" method="post">
			<table class="center">
				<tr>
					<td>snum</td>
					<td>sname</td>
					<td>status</td>
					<td>city</td>
				</tr>
				<tr>
					<td><input type='text' name='field1' /></td>
					<td><input type='text' name='field2' /></td>
					<td><input type='text' name='field3' /></td>
					<td><input type='text' name='field4' /></td>
				</tr>
			</table>
			<br>
		<input type="submit" value="Enter Supplier Record Into Database" /> <input
			type="button" value="Clear Data and Results" onclick="hideTable();" />
	</form>
</div>
<br>

<div>
	<span class="title">Parts Record Insert</span>
	<form action="dataentryParts" method="post">
		<table class="center">
			<tr>
				<td>pnum</td>
				<td>pname</td>
				<td>color</td>
				<td>weight</td>
				<td>city</td>
			</tr>
			<tr>
				<td><input type='text' name='field21' /></td>
				<td><input type='text' name='field22' /></td>
				<td><input type='text' name='field23' /></td>
				<td><input type='text' name='field24' /></td>
				<td><input type='text' name='field25' /></td>
			</tr>
		</table>
		<br>
		<input type="submit" value="Enter Part Record Into Database" /> <input
			type="button" value="Clear Data and Results" onclick="hideTable();" />
	</form>
</div>
<br>

<div>
	<span class="title">Jobs Record Insert</span>
	<form action="dataentryJobs" method="post">
		<table class="center">
			<tr>
				<td>jnum</td>
				<td>jname</td>
				<td>numworkers</td>
				<td>city</td>
			</tr>
			<tr>
				<td><input type='text' name='field31' /></td>
				<td><input type='text' name='field32' /></td>
				<td><input type='text' name='field33' /></td>
				<td><input type='text' name='field34' /></td>
			</tr>
		</table>
		<br>
		<input type="submit" value="Enter Job Record Into Database" /> <input
			type="button" value="Clear Data and Results" onclick="hideTable();" />
	</form>
</div>
<br>

<div>
	<span class="title">Shipments Record Insert</span>
	<form action="dataentryShipments" method="post">
		<table class="center">
			<tr>
				<td>snum</td>
				<td>pnum</td>
				<td>jnum</td>
				<td>quantity</td>
			</tr>
			<tr>
				<td><input type='text' name='field41' /></td>
				<td><input type='text' name='field42' /></td>
				<td><input type='text' name='field43' /></td>
				<td><input type='text' name='field44' /></td>
			</tr>
		</table>
		<br>
		<input type="submit" value="Enter Shipment Record Into Database" /> <input
			type="button" value="Clear Data and Results" onclick="hideTable();" />
	</form>
</div>
<br>

<h3>Database Results:</h3>
    
	<% 
	Object message = request.getAttribute("message");
	if (message == null)
		message = "";
	%>
	<table id="resultTable" class='center'>
		<%=message%>
	</table>
</body>
</html>