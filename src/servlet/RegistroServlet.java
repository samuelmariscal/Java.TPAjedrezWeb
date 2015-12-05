package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entidades.Jugador;
import negocio.ControladorLogin;

/**
 * Servlet implementation class RegistroServlet
 */
@WebServlet("/register")
public class RegistroServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistroServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getParameter("btnVolver")!=null){
			response.sendRedirect("login.jsp");
		} else if (request.getParameter("txtDni")=="" || request.getParameter("txtNombre")=="" ||
				request.getParameter("txtApellido")==""){
			request.setAttribute("errorMessage", "Todos los campos son obligatorios");
			request.getRequestDispatcher("/registro.jsp").forward(request, response);
		} else {
			registrar(request,response);
		}
	}

	private void registrar(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int dni = Integer.parseInt(request.getParameter("txtDni"));
		String nombre = request.getParameter("txtNombre");
		String apellido = request.getParameter("txtApellido");
		
		ControladorLogin cl = new ControladorLogin();
		try {
			Jugador j = cl.buscarJugador(dni);
			if (j==null){
				// si el j es nulo es porq no existe
				j = new Jugador();
				j.setDni(dni);
				j.setNombre(nombre);
				j.setApellido(apellido);
				cl.guardarJugador(j);
				request.setAttribute("message", "El jugador ha sido registrado correctamente");
				request.getRequestDispatcher("/registro.jsp").forward(request, response);
			} else {
				request.setAttribute("errorMessage", "El jugador que desea registrar ya existe");
				request.getRequestDispatcher("/registro.jsp").forward(request, response);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			request.setAttribute("errorMessage", "Error de conexion de base de datos");
			request.getRequestDispatcher("/registro.jsp").forward(request, response);
		}
	}

}
