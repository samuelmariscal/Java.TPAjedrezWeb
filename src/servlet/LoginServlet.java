package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entidades.Jugador;
import entidades.Partida;
import negocio.ControladorLogin;
import negocio.ControladorPartida;

/**
 * Servlet implementation class Login
 */
@WebServlet("/signin")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			if (request.getParameter("btnNuevoJugador")!=null){
				response.sendRedirect("registro.jsp");
			} else if (request.getParameter("txtJugadorUno")=="" || request.getParameter("txtJugadorDos")==""){
				request.setAttribute("errorMessage", "Todos los campos son obligatorios");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			} else if (request.getParameter("btnContinuar")!=null){
				// continua partida anterior
				response.sendRedirect("partida.jsp");
				// la partida anteror ya estaba guardada en session al momento de preguntar
			} else if (request.getParameter("btnSobreescribir")!=null){
				try {
					ControladorPartida cp = new ControladorPartida();
					Partida p = (Partida) request.getSession().getAttribute("partida");
					Jugador j1 = p.getJ1();
					Jugador j2 = p.getJ2();
					cp.borrarFichasYPartida(p);
					p = cp.iniciarPartida(j1, j2);
					p.setTablero(cp.inicializarTablero(p));
					HttpSession session = request.getSession();
					session.setAttribute("partida", p);
					response.sendRedirect("partida.jsp");
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					request.setAttribute("errorMessaage", "Error de conexion de base de datos.");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				}
			} else {
				logOn(request, response);
			}
		}catch (NumberFormatException nfe){
			request.setAttribute("errorMessage", "Datos invalidos");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	private void logOn(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int dni1 = Integer.parseInt(request.getParameter("txtJugadorUno"));
		int dni2 = Integer.parseInt(request.getParameter("txtJugadorDos"));
		
		ControladorLogin cl = new ControladorLogin();
		try {	
				Jugador j1 = cl.buscarJugador(dni1);
				Jugador j2 = cl.buscarJugador(dni2);
				
				if (j1==null){
					// alguno de los dos jugadores no esta registrado o es incorrecto
					// redirijo a la pagina de login e imprimo un mensaje de error
					request.setAttribute("errorMessage", "Jugador 1 no esta registrado");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				} else if (j2==null){
					request.setAttribute("errorMessage", "Jugador 2 no esta registrado");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				} else {
					ControladorPartida cp = new ControladorPartida();
					Partida p = cp.partidaAnterior(j1, j2);
					if (p==null){
						p = cp.iniciarPartida(j1, j2);
						p.setTablero(cp.inicializarTablero(p));
						HttpSession session = request.getSession();
						session.setAttribute("partida", p);
						response.sendRedirect("partida.jsp");
					}
					else {
						// si hay partidas anteriores hay q preguntarle al usuario si la quiere continuar o no
						p.setTablero(cp.cargarTablero(p));
						HttpSession session = request.getSession();
						session.setAttribute("partida", p);
						request.setAttribute("message", "Existe una partida anterior, desea continuarla?");
						request.getRequestDispatcher("login.jsp").forward(request, response);
					}
				}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			request.setAttribute("errorMessage", "Problemas de conexion con la base de datos");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

}
