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

public class DataEntryAppParts extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	private MysqlDataSource dataSource;
	private Connection jdbcConnection;
	private Statement statement;
	public String message;

	public DataEntryAppParts() throws SQLException, IOException 
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
		String field21 = request.getParameter("field21");
		String field22 = request.getParameter("field22");
		String field23 = request.getParameter("field23");
		String field24 = request.getParameter("field24");
		String field25 = request.getParameter("field25");
		message = "";

		try {
			statement.executeUpdate("INSERT INTO parts VALUES ('" + field21 + "', '" + field22 
					+ "', '" + field23 + "', '" + field24 + "', '" + field25 + "')");
				
			message = "<tr><td style=\"background: green;\"><font color=#FFFFFF><b>" + "New parts record:(" 
					+ field21 + "', '" + field22 + "', '" + field23 + "', '" + field24 + "', '" + field25 
					+ ") – successfully entered into database. <br></font></td></tr>";
		} catch (Exception e) {
			message = "<tr><td style=\"background: red;\"><font color=#FFFFFF><b>"
					+ "Error executing the SQL statement:</b><br>" 
					+ e.getMessage() + "</tr></td></font>";
		}

		request.setAttribute("message", message);
		request.getRequestDispatcher("/dataentryHome.jsp").forward(request, response);
	}
}