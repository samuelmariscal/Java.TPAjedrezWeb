package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entidades.Ficha;
import entidades.Partida;
import negocio.ControladorPartida;

/**
 * Servlet implementation class PartidaServlet
 */
@WebServlet("/play")
public class PartidaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PartidaServlet() {
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
		realizarMovimiento(request,response);
	}

	private void realizarMovimiento(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try{
		if (request.getParameter("txtFila")=="" || request.getParameter("txtColumna")==""){
			request.setAttribute("errorMessage", "Todos los campos son obligatorios");
			request.getRequestDispatcher("/partida.jsp").forward(request, response);
		} else {
			if (Integer.parseInt(request.getParameter("txtFila"))>8 || 
					Integer.parseInt(request.getParameter("txtColumna"))>8 || 
					Integer.parseInt(request.getParameter("txtColumna"))<1 || 
					Integer.parseInt(request.getParameter("txtFila"))<1){
				request.setAttribute("errorMessage", "Movimiento invalido");
				request.getRequestDispatcher("/partida.jsp").forward(request, response);
			} else {
				ControladorPartida cp = new ControladorPartida();
				Ficha f = null;
				HttpSession session = request.getSession();
				Partida p = (Partida) session.getAttribute("partida");
				
				// obtengo la ficha seleccionada en el option de la pagina
				String fichaElegida = request.getParameter("cmbFichas");
				if (p.getTurno()==1){
					for (Ficha fi : p.getTablero()){
						if (fichaElegida.equals(fi.getNombre()) && fi.getDni()==p.getJ1().getDni()){
							f=fi;
							break;
						}
					}
				} else {
					for (Ficha fi : p.getTablero()){
						if (fichaElegida.equals(fi.getNombre()) && fi.getDni()==p.getJ2().getDni()){
							f=fi;
						}
					}
					}
				
				// valido que el movimiento sea posible y si lo es efectuo el movimiento
				if (f.validarMovimiento(Integer.parseInt(request.getParameter("txtColumna")),
						Integer.parseInt(request.getParameter("txtFila"))) && 
						cp.validarMovimiento(Integer.parseInt(request.getParameter("txtColumna")), 
								Integer.parseInt(request.getParameter("txtFila")), f.getDni(), p)){
					
					f.setPosX(Integer.parseInt(request.getParameter("txtColumna")));
					f.setPosY(Integer.parseInt(request.getParameter("txtFila")));
					
					// recorre el tablero y se fija si hay una ficha enemiga ahi
					for (Ficha objetivo : p.getTablero()){
						if (f.getPosX() == objetivo.getPosX() && f.getPosY() == objetivo.getPosY() && f.getDni()!=objetivo.getDni()){
							objetivo.setPosX(0);
							objetivo.setPosY(0);
							objetivo.setEstado(false);
							if (objetivo.getNombre().contains("r") && !objetivo.isEstado()){
								try {
									cp.borrarFichasYPartida(p);
									// borro la session y lo reenvio al login
									request.setAttribute("winMessage", "El jugador "+f.getDni()+" ha ganado la partida!");
									request.getRequestDispatcher("login.jsp").forward(request, response);;
								} catch (ClassNotFoundException | SQLException e) {
									// TODO Auto-generated catch block
									request.setAttribute("errorMessage", "Error de base de datos");
									request.getRequestDispatcher("partida.jsp").forward(request, response);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									request.setAttribute("errorMessage", "Error de imput/output");
									request.getRequestDispatcher("partida.jsp").forward(request, response);
								}
								
							}	
						} 
						}
					request.setAttribute("message", "El movimiento ha sido efectuado correctamente");
					if (p.getTurno()==1){
						p.setTurno(2);
					} else {
						p.setTurno(1);
					}
					try {
						// actualizo en base de datos
						cp.guardarPartidaYFichas(p.getTablero(),p);
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						request.setAttribute("errorMessage", "Error de base de datos");
						request.getRequestDispatcher("partida.jsp").forward(request, response);
					}
					// en este punto guardo la session actualizada y vuelvo a la partida
				
					session.setAttribute("partida", p);
					request.getRequestDispatcher("partida.jsp").forward(request, response);
					}else {
						request.setAttribute("errorMessage", "La ficha no puede ser movida a ese sitio");
						request.getRequestDispatcher("partida.jsp").forward(request, response);
				}
			}
		}		
	}catch (NumberFormatException nfe){
		request.setAttribute("errorMessage", "Datos invalidos");
		request.getRequestDispatcher("partida.jsp").forward(request, response);
	}		
	}
}
