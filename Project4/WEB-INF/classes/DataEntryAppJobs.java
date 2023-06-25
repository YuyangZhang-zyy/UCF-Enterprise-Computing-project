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

public class DataEntryAppJobs extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	private MysqlDataSource dataSource;
	private Connection jdbcConnection;
	private Statement statement;
	public String message;

	public DataEntryAppJobs() throws SQLException, IOException 
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
		String field31 = request.getParameter("field31");
		String field32 = request.getParameter("field32");
		String field33 = request.getParameter("field33");
		String field34 = request.getParameter("field34");
		message = "";

		try {
			statement.executeUpdate("INSERT INTO jobs VALUES ('" + field31 + "', '" + field32 + "', '" 
					+ field33 + "', '" + field34 + "')");
				
			message = "<tr><td style=\"background: green;\"><font color=#FFFFFF><b>"
					+ "New jobs record:(" + field31 + "', '" + field32 + "', '" + field33 + "', '" 
					+ field34 + ") – successfully entered into database. <br></font></td></tr>";
		} catch (Exception e) {
			message = "<tr><td style=\"background: red;\"><font color=#FFFFFF><b>"
					+ "Error executing the SQL statement:</b><br>" 
					+ e.getMessage() + "</tr></td></font>";
		}

		request.setAttribute("message", message);
		request.getRequestDispatcher("/dataentryHome.jsp").forward(request, response);
	}
}