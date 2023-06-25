/*
	Name: Yuyang Zhang
	Course: CNT 4714 Spring 2023
	Assignment title: Project 3 – A Two-tier Client-Server Application
	Date:  March 9, 2023
*/
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;

// ResultSet rows and columns are counted from 1 and JTable 
// rows and columns are counted from 0. When processing 
// ResultSet rows or columns for use in a JTable, it is 
// necessary to add 1 to the row or column number to manipulate
// the appropriate ResultSet column (i.e., JTable column 0 is 
// ResultSet column 1 and JTable row 0 is ResultSet row 1).
public class RSTM extends AbstractTableModel 
{
   private Connection connection;
   private Statement statement;
   private ResultSet resultSet;
   private ResultSetMetaData metaData;
   private int numberOfRows;
   // keep track of database connection status
   private boolean connectedToDatabase = false;
   
   // constructor initializes resultSet and obtains its meta data object;
   // determines number of rows       
   public RSTM (String propfile, String user, String pass) throws SQLException, ClassNotFoundException 
   {
	   Properties properties = new Properties();
	   FileInputStream filein = null;
	   MysqlDataSource dataSource = null;
	   
	   //receive the database info from DQR
	   try {
		    //read properties file
		    filein = new FileInputStream(propfile);
		    properties.load(filein);
	    	dataSource = new MysqlDataSource();
	    	
	    	if((user.equals(properties.getProperty("MYSQL_DB_USERNAME"))) && (pass.equals(properties.getProperty("MYSQL_DB_PASSWORD")))) 
	    	{
	    		dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
	    		dataSource.setUser(user);
	    		dataSource.setPassword(pass);
	    		
	    		// connect to database project3 and query database
	  	        // establish connection to database
	    		Connection connection = dataSource.getConnection();
	    		
	    		// create Statement to query database
	            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	            
	            // update database connection status
	            connectedToDatabase = true;
	            DQR.setConnectedStatus(properties.getProperty("MYSQL_DB_URL"));
	    	}
	    	else 
	    	{
	    		connectedToDatabase = false;
	    		DQR.nosetConnectedStatus();
	    	}
	  } //end try
      catch (SQLException sqlException) {
    	  	sqlException.printStackTrace();
    	  	System.exit(1);
      } // end catch
	  catch (IOException e) {
			e.printStackTrace();
	  }

   } // end constructor RSTM

   // get class that represents column type
   public Class getColumnClass( int column ) throws IllegalStateException 
   {
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");

      // determine Java class of column
      try {
         String className = metaData.getColumnClassName(column + 1);
         
         // return Class object that represents className
         return Class.forName(className);
      } // end try
      catch (Exception exception) {
         exception.printStackTrace();
      } // end catch
      
      return Object.class; // if problems occur above, assume type Object
   } // end method getColumnClass

   // get number of columns in ResultSet
   public int getColumnCount() throws IllegalStateException 
   {   
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");

      // determine number of columns
      try {
         return metaData.getColumnCount(); 
      } // end try
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
      } // end catch
      
      return 0; // if problems occur above, return 0 for number of columns
   } // end method getColumnCount

   // get name of a particular column in ResultSet
   public String getColumnName(int column) throws IllegalStateException {    
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");

      // determine column name
      try {
         return metaData.getColumnName(column + 1);  
      } // end try
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
      } // end catch
      
      return ""; // if problems, return empty string for column name
   } // end method getColumnName

   // return number of rows in ResultSet
   public int getRowCount() throws IllegalStateException {      
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");
 
      return numberOfRows;
   } // end method getRowCount

   // obtain value in particular row and column
   public Object getValueAt(int row, int column) throws IllegalStateException 
   {
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");

      // obtain a value at specified ResultSet row and column
      try {
		 resultSet.next();  /* fixes a bug in MySQL/Java with date format */
         resultSet.absolute(row + 1);
         return resultSet.getObject(column + 1);
      } // end try
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
      } // end catch
      
      return ""; // if problems, return empty string object
   } // end method getValueAt
   
   // set new database query string
   public void setQuery(String query) throws SQLException, IllegalStateException 
   {
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");

      // specify query and execute it
      resultSet = statement.executeQuery(query);

      // obtain meta data for ResultSet
      metaData = resultSet.getMetaData();

      // determine number of rows in ResultSet
      numberOfRows = 0;
      while (resultSet.next()) 
          numberOfRows++;
      
      // operationslog for queries
      Properties properties = new Properties();
      FileInputStream in = null;
      try {
    	  in = new FileInputStream("project3app.properties");
      } catch (FileNotFoundException e2) {
    	  e2.printStackTrace();
      }
      try {
    	  properties.load(in);
      } catch (IOException e1) {
    	  e1.printStackTrace();
      }
      try (Connection conn = DriverManager.getConnection(properties.getProperty("MYSQL_DB_URL"), properties.getProperty("MYSQL_DB_USERNAME"), properties.getProperty("MYSQL_DB_PASSWORD"))){
          String updateQuery = "UPDATE operationscount set num_queries = num_queries + 1";
          try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
              ps.executeUpdate();
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      
      // notify JTable that model has changed
      fireTableStructureChanged();
   } // end method setQuery
   

   // set new database update-query string
   public void setUpdate(String query) throws SQLException, IllegalStateException 
   {
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");

      // specify query and execute it
      int res = statement.executeUpdate(query);
      
      //operationslog for update
      Properties properties = new Properties();
      FileInputStream in = null;
      try {
    	  in = new FileInputStream("project3app.properties");
      } catch (FileNotFoundException e2) {
    	  e2.printStackTrace();
      }
      try {
    	  properties.load(in);
      } catch (IOException e1) {
    	  e1.printStackTrace();
      }
      try (Connection conn = DriverManager.getConnection(properties.getProperty("MYSQL_DB_URL"), properties.getProperty("MYSQL_DB_USERNAME"), properties.getProperty("MYSQL_DB_PASSWORD"))){
          String updateQuery = "UPDATE operationscount set num_updates = num_updates + 1";
          try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
              ps.executeUpdate();
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      
      // notify JTable that model has changed
      fireTableStructureChanged();
   } // end method setUpdate

   // close Statement and Connection               
   public void disconnectFromDatabase() 
   {              
      if (!connectedToDatabase)                  
         return;
      // close Statement and Connection            
      try {
    	  if (statement != null)
    	  {
    		  statement.close();
    	  }
    	  if(connection != null)
    	  {
    		  connection.close();
    	  }                      
      } // end try                                 
      catch (SQLException sqlException) {                                            
         sqlException.printStackTrace();           
      } // end catch
      // update database connection status
      finally  {                                            
         connectedToDatabase = false;
         DQR.desetConnectedStatus();
      } // end finally                             
   } // end method disconnectFromDatabase          
}  // end class ResultSetTableModel
