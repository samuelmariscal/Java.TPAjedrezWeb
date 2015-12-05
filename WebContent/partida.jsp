<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import= "entidades.Partida, java.util.ArrayList, entidades.Ficha"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Partida</title>
<link rel="stylesheet" href="css/style.css" type="text/css"></link>
</head>
<body>
	<!-- declaro una variable partida y desp en el scriptlet le inserto la partida de la session
	pretendo sacar todos los datos que necesito de la partida  -->
	<%! Partida p; %>
	<% p=(Partida)session.getAttribute("partida"); %>
	<form id="formPartida" name="formPartida" action="play" method="post">
	<div class="CSSTable">
		<table>
			<tr>
				<td><label id="lblJugadorUno"><%=p.getJ1().getDni() %></label></td>
				<td>VS.</td>
				<td><label id="lblJugadosDos"><%=p.getJ2().getDni() %></label></td>
			</tr>
			<tr>
				<td>
				TABLERO:<br>
				<%for (Ficha f : p.getTablero()){
					if (f.getDni()==p.getJ1().getDni()){%>
						<%=f.getNombre()%>--<%=f.getPosX()%>--<%=f.getPosY()%>--<%=f.isEstado()%><br>
					<%}} %>
				</td>
				<td></td>
				<td>TABLERO:<br>
				<%for (Ficha f : p.getTablero()){
					if (f.getDni()==p.getJ2().getDni()){%>
						<%=f.getNombre()%>--<%=f.getPosX()%>--<%=f.getPosY()%>--<%=f.isEstado()%><br>
					<%}} %>
				</td>
			</tr>
		</table>
		</div>
		<br>
		TURNO DE:<%if (p.getTurno()==1) { %>
			<%=p.getJ1().getDni()%>
			<%} else { %>
			<%=p.getJ2().getDni()%>
			<% } %><br>
		<!-- el select tag q viene aca abajo vendria a reemplazar al combobox del form original -->
		Ficha:
		<select name="cmbFichas">
			<%if (p.getTurno()==1){
				for (Ficha f : p.getTablero()){
					if(f.isEstado() && f.getDni()==p.getJ1().getDni()){ %>
						<option value="<%=f.getNombre()%>"><%=f.getNombre()%></option>
			<% } } } else { 
				for (Ficha f : p.getTablero()){ 
					if (f.isEstado() && f.getDni()==p.getJ2().getDni()){%>
						<option value="<%=f.getNombre()%>"><%=f.getNombre()%></option>
			<% }}} %>		
		</select>
		Fila:<input type="text" name="txtFila" id="txtFila">
		Columna:<input type="text" name="txtColumna" id="txtColumna"><br><br>
		<button type="submit" name="btnRealizarMovimiento">Realizar Movimiento</button>
	</form>
	<!-- espacio reservado para mensajes de error -->
	<%if (request.getAttribute("errorMessage")!=null){ %>
		<label style="color: red;" id=error><%=request.getAttribute("errorMessage") %></label>
	<%} else if (request.getAttribute("message")!=null){ %>
		<label style="color: green;" id=error><%=request.getAttribute("message") %></label>
	<%} %>
</body>
</html>