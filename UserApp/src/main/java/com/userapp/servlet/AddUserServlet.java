package com.userapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/addServlet")
public class AddUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	public void init(ServletConfig config) {

		try {
			ServletContext context = config.getServletContext();
			Enumeration<String> names = context.getInitParameterNames();
			
			while(names.hasMoreElements()) {
			String element = names.nextElement();
				
				System.out.println("Context Params name:"+element);
				System.out.println("Context Params Values:"+context.getInitParameter(element));
			}
			
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(context.getInitParameter("DBurl"),context.getInitParameter("DBusername"),
					context.getInitParameter("DBpassword"));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		try {
			Statement statement = connection.createStatement();
			int result = statement.executeUpdate("insert into user values('" + firstName + "','" + lastName + "','"
					+ email + "','" + password + "')");

			PrintWriter out = response.getWriter();
			if (result > 0) {
				out.print("<H1>USER CREATED</H1>");
			} else {
				out.print("<H1>USER NOT CREATED</H1>");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
