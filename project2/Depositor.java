/*
 * Name: Yuyang Zhang
 * Course: CNT 4714 Spring 2023
 * Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
 * Due Date: February 12, 2023
 */

import java.util.Random;


public class Depositor implements Runnable 
{
	private static int MAX_DEPOSIT = 500;
	private static Random generator = new Random();
	private static Random sleeptime = new Random();
	private BankAccount sharedLocation;
	String tname;
	
	
	//constructor
	public Depositor(BankAccount shared, String name)
	{
		sharedLocation = shared;
		tname = name;
	}
	
	public void run()
	{
		//System.out.println("Hello! My name is: " + tname);
		while(true)
		{
			try {
				sharedLocation.deposit(generator.nextInt(MAX_DEPOSIT) + 1, tname);
				Thread.sleep(generator.nextInt(3000));
				Thread.sleep(sleeptime.nextInt(1500) + 1);
			}
			catch(Exception exception) {
				exception.printStackTrace();
			}
			/*catch(InterruptedException exception) {
				exception.printStackTrace();
			}*/
		}
	}
}
