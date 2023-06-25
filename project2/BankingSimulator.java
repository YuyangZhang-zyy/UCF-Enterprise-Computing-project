/*
 * Name: Yuyang Zhang
 * Course: CNT 4714 Spring 2023
 * Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
 * Due Date: February 12, 2023
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BankingSimulator 
{
	//Spring 2023 - 10 withdraw, 5 deposit
	public static final int MAX_AGENTS = 16;
	
	
	public static void main(String[] args)
	{
		//thread pool - 16
		ExecutorService applicaton = Executors.newFixedThreadPool(MAX_AGENTS);
		
		BankAccount sharedLocation = new BankAccount();
		try {
			//start thread - random order
			System.out.println("Deposit Agents\t\tWithdrawal Agents\t\tBalance\t\t\t\t\t\t\tTransaction Number");
			System.out.println("--------------\t\t----------------------\t\t-------------------\t\t\t\t\t--------------");
			
			applicaton.execute(new Withdrawal(sharedLocation, "WT4"));
			applicaton.execute(new Withdrawal(sharedLocation, "WT3"));
			applicaton.execute(new Withdrawal(sharedLocation, "WT5"));
			applicaton.execute(new Withdrawal(sharedLocation, "WT6"));
			applicaton.execute(new Depositor(sharedLocation, "DT3"));
			applicaton.execute(new Withdrawal(sharedLocation, "WT2"));
			applicaton.execute(new Depositor(sharedLocation, "DT4"));
			applicaton.execute(new Withdrawal(sharedLocation, "WT1"));
			applicaton.execute(new Withdrawal(sharedLocation, "WT7"));
			applicaton.execute(new Withdrawal(sharedLocation, "WT8"));
			applicaton.execute(new Depositor(sharedLocation, "DT1"));
			applicaton.execute(new Withdrawal(sharedLocation, "WT9"));
			applicaton.execute(new Depositor(sharedLocation, "DT0"));
			applicaton.execute(new Depositor(sharedLocation, "DT2"));
			applicaton.execute(new Withdrawal(sharedLocation, "WT0"));
			applicaton.execute(new Auditor(sharedLocation));
			//applicaton.execute(new Withdrawal(sharedLocation, "DT5"));
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
		
		applicaton.shutdown();
	}
	
}
