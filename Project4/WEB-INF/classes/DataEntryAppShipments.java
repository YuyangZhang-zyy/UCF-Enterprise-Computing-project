/*Name: Yuyang Zhang
 Course: CNT 4714 - Project Four - Spring 2023
 Assignment title: Developing A Three-Tier Distributed Web-Based Application
 Date: April 23, 2023
*/

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataEntryAppShipments extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	private MysqlDataSource dataSource;
	private Connection jdbcConnection;
	private Statement statement;
	public String message;

	public DataEntryAppShipments() throws SQLException, IOException 
	{
		Properties properties = new Properties();
		FileInputStream filein = new FileInputStream("C:/Program Files/Apache Software Foundation/Tomcat 10.1_Tomcat1017/webapps/Project4/WEB-INF/lib/dataentry.properties");//set properties file address
		properties.load(filein);

		dataSource = new MysqlDataSource();
		dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
		dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
		dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
		
		filein.close();

		jdbcConnection = dataSource.getConnection();
		statement = jdbcConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String field41 = request.getParameter("field41");
		String field42 = request.getParameter("field42");
		String field43 = request.getParameter("field43");
		String field44 = request.getParameter("field44");
		message = "";

		try {
			statement.executeUpdate("INSERT INTO shipments VALUES ('" + field41 + "', '" + field42 + "', '" + field43 + "', '" + field44 + "')");
			// Business Logic
			int quantity = Integer.parseInt(field44);
			if (quantity >= 100) 
			{
				statement.executeUpdate("UPDATE suppliers SET status = status + 5 WHERE snum = '" + field41 + "'");
				
				message = "<tr><td style=\"background: green;\"><font color=#FFFFFF><b>" 
						+ "New shipments record:(" + field41 + "', '" + field42 + "', '" + field43 + "', '" + field44 
						+ ") – successfully entered into database.  Business logic triggered.<br></font></td></tr>";
			}
			else
			{
				message = "<tr><td style=\"background: green;\"><font color=#FFFFFF><b>"
						+ "New shipments record:(" + field41 + "', '" + field42 + "', '" + field43 + "', '" + field44 
						+ ") – successfully entered into database. Business logic not triggered.<br></font></td></tr>";
			}
		} catch (Exception e) {
			message = "<tr><td style=\"background: red;\"><font color=#FFFFFF><b>"
					+ "Error executing the SQL statement:</b><br>" 
					+ e.getMessage() + "</tr></td></font>";
		}

		request.setAttribute("message", message);
		request.getRequestDispatcher("/dataentryHome.jsp").forward(request, response);
	}
}