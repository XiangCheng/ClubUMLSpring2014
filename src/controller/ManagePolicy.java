package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManagePolicy extends HttpServlet{

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	//Renders manageProject.jsp
		System.out.println("enters manage ManagePolicy servlet");
	RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/JSP/managePolicy.jsp");
	//dispatcher.forward(request, response);
	dispatcher.forward(request, response);
	}
	
	
}
