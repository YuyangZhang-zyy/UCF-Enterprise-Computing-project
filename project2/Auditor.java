/*
 * Name: Yuyang Zhang
 * Course: CNT 4714 Spring 2023
 * Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
 * Due Date: February 12, 2023
 */

import java.util.Random;

public class Auditor implements Runnable
{
	private static Random generator = new Random();
	private static Random sleeptime = new Random();
	private BankAccount sharedLocation;
	
	public Auditor(BankAccount shared)
	{
		sharedLocation = shared;
	}
	
	public void run()
	{
		while(true)
		{
			try {
				sharedLocation.audit();
				Thread.sleep(generator.nextInt(10000));
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
