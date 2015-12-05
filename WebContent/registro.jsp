<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Registro</title>
<link rel="stylesheet" href="css/style.css" type="text/css"></link>
</head>
<body>
	<form action="register" method="post">
		<br>Ingrese sus datos para registrarse:<br>
		<br>
		<table>
		<tr>
			<td>Dni:</td>
			<td><input type="text" name="txtDni" id="txtDni"></td>
		</tr>
		<tr>
			<td>Nombre:</td>
			<td><input type="text" name="txtNombre" id="txtNombre"></td>
		</tr>
		<tr>
			<td>Apellido:</td>
			<td><input type="text" name="txtApellido" id="txtApellido"></td>
		</tr>
		<tr>
		<td><button type="submit" name="btnRegistrar" id="btnRegistrar">Registrar jugador</button></td>
		<td><button type="submit" name="btnVolver" id="btnVolver">Volver</button></td>
		</tr>
		</table>
	</form>
	<% if (request.getAttribute("errorMessage")!= null) { %>
			<label style="color: red;" id=error><%=request.getAttribute("errorMessage")%></label>
	<% } else if (request.getAttribute("message")!=null){ %>
			<label style="color: green;" id=message><%=request.getAttribute("message")%></label>
	<% } %>
</body>
</html>