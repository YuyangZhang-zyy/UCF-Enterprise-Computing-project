/*
 * Name: Yuyang Zhang
 * Course: CNT 4714 Spring 2023
 * Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
 * Due Date: February 12, 2023
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class flagged_transaction
{
	private int amount;
	private String tname;
	private int number;
	
	public flagged_transaction(int a, String n, int nu)
	{
		amount = a;
		tname = n;
		number = nu;
	}
	
	public void fTransaction()
	{
		StringBuilder ctr = new StringBuilder();
		Format dateFormat = new SimpleDateFormat("MM/dd/YY, hh:mm:ss a z");//date and time
		String curDate = dateFormat.format(new Date());
		String lines = "";
		
		if(tname.charAt(0) == 'W')
			lines += ("\tWithdrawal Agent " + tname + " issued withdrawal of $" + amount + " at: " + curDate + " Transaction Number: " + number + "\n\n");
		else if(tname.charAt(0) == 'D')
			lines += ("Depositor Agent " + tname + " issued deposit of $" + amount + " at: " + curDate + " Transaction Number: " + number + "\n\n");
		
		ctr.append(lines);
		
		File file = new File("transactions.txt");//create transaction
	    FileWriter writer = null;
	    try {
	    	try {
	          //Allow the file to store multiple transactions
	          writer = new FileWriter(file, true);
	        } catch (IOException e1) {
	          e1.printStackTrace();
	        }
	        //Append StringBuilder to the file
	        writer.append(ctr);
	    } catch (IOException e1) {
	    	e1.printStackTrace();
	    } finally {
	        if (writer != null)
	          try {
	            writer.close();
	          } catch (IOException e1) {
	            e1.printStackTrace();
	          }
	      }
	}
}
