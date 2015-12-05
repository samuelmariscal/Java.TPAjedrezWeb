<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" href="css/style.css" type="text/css"></link>
</head>
<body>
	<body>
	<form action="signin" method="post">
		<table>
		<tr>
			<td>Por favor ingresen sus dni:</td>
		</tr>
		<tr>
			<td>Jugador 1:</td>
			<td><input type="text" name="txtJugadorUno" id="txtJugadorUno"></td>
		</tr>
		<tr>
			<td>Jugador 2:</td>
			<td><input type="text" name="txtJugadorDos" id="txtJugadorDos"></td>
		</tr>
		<tr>
			<td><button type="submit" name="btnNuevaPartida">Iniciar nueva partida</button></td>
			<td><button type="submit" name="btnNuevoJugador">Registrar nuevo jugador</button></td>
		</table>
	</form>
	<%-- este if va a validar el atributo recibido de la request --%>
		<% if (request.getAttribute("errorMessage")!= null) { %>
			<label style="color:#891919; ;" id=error><%=request.getAttribute("errorMessage")%></label>
		<% } else if (request.getAttribute("message")!=null){ %>
			<form action="signin" method="post">
				<%= request.getAttribute("message") %>
				<button type="submit" name="btnContinuar">Continuar partida anterior</button>
				<button type="submit" name="btnSobreescribir">Sobreescribir partida</button>
			</form>
		<% } else if (request.getAttribute("winMessage")!=null){ %>
			<label style="color: blue" id=win><%=request.getAttribute("winMessage") %></label>
			<%session.invalidate(); 
			}%>
			
</body>
</html>