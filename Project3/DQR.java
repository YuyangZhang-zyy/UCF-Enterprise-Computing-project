/*
	Name: Yuyang Zhang
	Course: CNT 4714 Spring 2023
	Assignment title: Project 3 – A Two-tier Client-Server Application
	Date:  March 9, 2023
*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class DQR extends JFrame 
{
   private RSTM tableModel = null;
   private JTextArea queryArea;
   private JTextField textFieldUser;
   private JPasswordField passwordField;
   private static JLabel lblConnectionStatus;
   String[] comboBoxProp = {"root.properties", "client.properties", "project3app.properties"};
   private JTable resultTable;
   private JScrollPane scrollPane1;
   private String propText, userText, passText;
   
   //create RSTM and GUI
   public DQR() 
   {
	   	super("SQL Client APP – (MJL – CNT 4714 – Spring 2023)");

      	//set up JTextArea in which user types queries
        queryArea = new JTextArea("", 3, 100);
        queryArea.setWrapStyleWord(true);
        queryArea.setLineWrap(true);
         
        JScrollPane scrollPane = new JScrollPane(queryArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
         
        //create Box to manage placement of queryArea and submitButton in GUI
        Box box = Box.createHorizontalBox();
        box.setBounds(345, 38, 355, 120);
        box.add(scrollPane);
        
        resultTable = new JTable(tableModel);
        resultTable.setGridColor(Color.BLACK);
        getContentPane().setLayout(null);
        
        getContentPane().add(box);
        scrollPane1 = new JScrollPane(resultTable);
        scrollPane1.setPreferredSize(new Dimension(764, 202));
        getContentPane().add(scrollPane1);
         
        //set up JButton for submitting queries
        JButton submitButton = new JButton("Execute SQL Command");
        submitButton.setBounds(505, 168, 180, 23);
        getContentPane().add(submitButton);
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.BLACK);
        submitButton.setOpaque(true);
        
        //create submitButton event listener
        submitButton.addActionListener(new ActionListener() 
        {
        	//pass query to table model
        	public void actionPerformed(ActionEvent event) 
        	{
        		try {
              		//split up the commands from the text area
        			String[] currQuery = queryArea.getText().split(";");
              			for (int i = 0; i < currQuery.length; i++) 
              			{
              				currQuery[i] = currQuery[i].replaceAll("\n", " ");
              				String[] currLineSplit = currQuery[i].split(" ");

              				if (currLineSplit[0].toLowerCase().equals("select") == true || currLineSplit[1].toLowerCase().equals("select") == true) 
              				{
              					tableModel.setQuery(currQuery[i]);
                			  
              					resultTable = new JTable(tableModel);
              					resultTable.setGridColor(Color.BLACK);
        				        
              					//place GUI components on content pane
              					scrollPane1 = new JScrollPane(resultTable);
              					scrollPane1.setVisible(true);
              					scrollPane1.setBounds(35, 262, 665, 190);
              					getContentPane().add(scrollPane1);
              				}
              				else 
              				{
              					clearResults();
              					tableModel.setUpdate(currQuery[i]);
              				}
              			}
              		} //end try
              		catch (SQLException sqlException) {
              			JOptionPane.showMessageDialog(null, 
              			sqlException.getMessage(), "Database error", 
              			JOptionPane.ERROR_MESSAGE);                 
              		} // end outer catch
              	} // end actionPerformed
           	}  // end ActionListener inner class          
         ); // end call to addActionListener
         
         //set up the button for clear result window
         JButton btnClearResult = new JButton("Clear Result Window");
         btnClearResult.setBounds(20, 462, 165, 23);
         btnClearResult.setBackground(Color.YELLOW);
         btnClearResult.setForeground(Color.BLACK);
         getContentPane().add(btnClearResult);
         btnClearResult.addActionListener(new ActionListener() 
         {
          	public void actionPerformed(ActionEvent e) 
          	{
          		clearResults();
          	}
         });
         
         //label for the enter command
         JLabel lblEnterCommand = new JLabel("Enter An SQL Command");
         lblEnterCommand.setBounds(345, 10, 200, 23);
         lblEnterCommand.setForeground(Color.BLUE);
         getContentPane().add(lblEnterCommand);
         
         //set up the button that clears the command
         JButton btnCommandClear = new JButton("Clear SQL Command");
         btnCommandClear.setBounds(330, 168, 165, 23);
         btnCommandClear.setBackground(Color.WHITE);
         btnCommandClear.setForeground(Color.RED);
         getContentPane().add(btnCommandClear);
         btnCommandClear.addActionListener(new ActionListener() 
         {
        	 public void actionPerformed(ActionEvent e) 
        	 {
           		if (e.getSource() == btnCommandClear)
           			queryArea.setText("");
        	 }
         });
         
         //label for the results window
         JLabel lblResultWindow = new JLabel("SQL Execution Result Window");
         lblResultWindow.setForeground(Color.BLUE);
         lblResultWindow.setBounds(35, 234, 200, 23);
         getContentPane().add(lblResultWindow);
         
         //label for properties file
         JLabel propLabel = new JLabel("Properties File");
         propLabel.setBounds(10, 46, 110, 23);
         getContentPane().add(propLabel);
         
         //combo box that contains the properties files that can be selected
         JComboBox propComboBox = new JComboBox(comboBoxProp);
         propComboBox.setBounds(125, 43, 190, 29);
         getContentPane().add(propComboBox);
         
         //set up the button that connects to the database with the input
         JButton btnConnectDB = new JButton("Connect to Database");
         btnConnectDB.setBounds(20, 168, 165, 23);
         btnCommandClear.setBackground(Color.BLUE);
         btnCommandClear.setForeground(Color.YELLOW);
         getContentPane().add(btnConnectDB);
         btnConnectDB.addActionListener(new ActionListener() 
         {
        	 public void actionPerformed(ActionEvent e) 
        	 {
        		 if (e.getSource() == btnConnectDB) 
        		 {
          			//properties file
          			propText = propComboBox.getSelectedItem().toString();
          			if (propText.length() == 0) 
          			{
          				JOptionPane.showMessageDialog(null, "You need to select a properties file from the dropdown list.");
          				return;
          			}
          			
 					//username input
 					userText = textFieldUser.getText();
 					if (userText.length() == 0) 
 					{
 						JOptionPane.showMessageDialog(null, "You need to enter a username for the DB connection.");
 						return;
 					}
 					
 					//password input
 					passText = String.valueOf(passwordField.getPassword());
 					if (passText.length() == 0) 
 					{
 						JOptionPane.showMessageDialog(null, "You need to enter a password for the DB connection.");
 						return;
 					}
 					
 					try {
 						//pass the info to ResultSetTableModel for connecting
 						tableModel = new RSTM(propText, userText, passText);
 					} catch (ClassNotFoundException e1) {
 						JOptionPane.showMessageDialog(null, "MySQL driver not found", "Driver not found", JOptionPane.ERROR_MESSAGE);
 					         
 					    System.exit(1);// terminate
 					} catch (SQLException e1) {
 						JOptionPane.showMessageDialog( null, e1.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE );
 					    
 					    //ensure database connection is closed
 						if(tableModel != null)
 						{
 							tableModel.disconnectFromDatabase();
 						}
 					    System.exit(1);// terminate
 					}
          		}
          	}
          });
         
         //panel for connection status
         JPanel panelStatus = new JPanel();
         panelStatus.setBackground(Color.BLACK);
         FlowLayout fl_panelStatus = (FlowLayout) panelStatus.getLayout();
         fl_panelStatus.setAlignment(FlowLayout.LEFT);
         panelStatus.setBounds(20, 201, 665, 23);
         getContentPane().add(panelStatus);
         
         //label for connection status
         lblConnectionStatus = new JLabel("No Connection Now");
         lblConnectionStatus.setHorizontalAlignment(SwingConstants.CENTER);
         panelStatus.add(lblConnectionStatus);
         lblConnectionStatus.setForeground(Color.RED);
         
         //label for the input area
         JLabel lblDBInfo = new JLabel("Connection Details");
         lblDBInfo.setForeground(Color.BLUE);
         lblDBInfo.setBounds(10, 10, 200, 23);
         getContentPane().add(lblDBInfo);
         
         //label for the username input
         JLabel lblUser = new JLabel("Username");
         lblUser.setBounds(10, 86, 110, 23);
         getContentPane().add(lblUser);
         
         //text field for the username input
         textFieldUser = new JTextField();
         textFieldUser.setBounds(125, 83, 190, 29);
         getContentPane().add(textFieldUser);
         textFieldUser.setColumns(10);
         
         //label for the password input
         JLabel lblPass = new JLabel("Password");
         lblPass.setBounds(10, 126, 110, 23);
         getContentPane().add(lblPass);
         
         // password field for the password input
         passwordField = new JPasswordField();
         passwordField.setBounds(125, 123, 190, 29);
         getContentPane().add(passwordField);
         
         // panel that contains the results window
         JPanel panel = new JPanel();
         panel.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
         panel.setBackground(UIManager.getColor("Button.light"));
         panel.setBounds(35, 262, 665, 190);
         getContentPane().add(panel);

         setSize(730, 540); // set window size
         setVisible(true); // display window  

         // dispose of window when user quits application (this overrides the default of HIDE_ON_CLOSE)
         setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      
         // ensure database connection is closed when user quits application
         addWindowListener(new WindowAdapter() 
         {
        	 // disconnect from database and exit when window has closed
        	 public void windowClosed(WindowEvent event) 
        	 {
        		 if(tableModel != null)
        			 tableModel.disconnectFromDatabase();
        		 System.exit(1);
        	 } // end method windowClosed
         } // end WindowAdapter inner class
        		 ); // end call to addWindowListener
   	} // end DisplayQueryResults constructor
   
   // updates the connection status label after a successful connection
   public static void setConnectedStatus(String url) 
   {
	   lblConnectionStatus.setForeground(Color.YELLOW);
	   lblConnectionStatus.setText("CONNECTED TO: " + url + ".");
   }
   
   // updates the connection status label after a disconnection
   public static void desetConnectedStatus() 
   {
	   lblConnectionStatus.setForeground(Color.RED);
	   lblConnectionStatus.setText("No Connection Now");
   }
   
   public static void nosetConnectedStatus() 
   {
	   lblConnectionStatus.setForeground(Color.RED);
	   lblConnectionStatus.setText("NO CONNECTED – User Credentials Do No Match Properties File!");
   }
   
   // clears the results window
   public void clearResults() 
   {
	   scrollPane1.setVisible(false);
   }
   
   // execute application
   public static void main(String args[]) 
   {
      new DQR();     
   } // end main
} // end class DisplayQueryResults
