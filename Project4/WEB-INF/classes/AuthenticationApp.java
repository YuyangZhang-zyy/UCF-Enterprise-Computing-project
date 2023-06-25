/*Name: Yuyang Zhang
 Course: CNT 4714 - Project Four - Spring 2023
 Assignment title: Developing A Three-Tier Distributed Web-Based Application
 Date: April 23, 2023
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JOptionPane;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.Scanner;

@SuppressWarnings("serial")
public class AuthenticationApp extends HttpServlet 
{
    // process "get" request from client
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        String inBoundUserName = request.getParameter("username");
        String inBoundPassword = request.getParameter("psswd");

        boolean userCredentialsOK = false;

        // credentials file location will vary depending on Tomcat host
        File credentialsFile = new File("C:/Program Files/Apache Software Foundation/Tomcat 10.1_Tomcat1017/webapps/Project4/WEB-INF/lib/credentials.txt");//set credentials file address
        try (Scanner scanner = new Scanner(credentialsFile)) 
        {
            // while loop to read credentials from file
            while (scanner.hasNextLine() && credentialsFile != null && !userCredentialsOK) 
            {
                String line = scanner.nextLine();
                String[] credentials = line.split(",");

                // if user credentials match then set userCredentialsOK to true and break loop
                if (inBoundUserName.equals(credentials[0]) && inBoundPassword.equals(credentials[1]))
                    userCredentialsOK = true;
            }

        } catch (IOException e) {
            // problem reading from file error
            e.printStackTrace();
            response.sendRedirect("errorpage.jsp");
            return;
        }

        if (userCredentialsOK) 
        {
            // redirect to correct front-end page based on username
            switch (inBoundUserName) 
            {
            	case "root":
            		response.sendRedirect("rootHome.jsp");
            		break;
            	case "client":
            		response.sendRedirect("clientHome.jsp");
            		break;
            	case "dataentry":
            		response.sendRedirect("dataentryHome.jsp");
            		break;
            	default:
            		response.sendRedirect("errorpage.jsp");
            		break;
            }
        } 
        else
            // user credentials match failed - access denied - redirect to error page
            response.sendRedirect("errorpage.jsp");
    }
}