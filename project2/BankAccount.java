/*
 * Name: Yuyang Zhang
 * Course: CNT 4714 Spring 2023
 * Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
 * Due Date: February 12, 2023
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount implements TheBank
{
	private int balance;
	private int number;
    private Lock accessLock = new ReentrantLock();
    private Condition canDeposit = accessLock.newCondition();
    private Condition canWithdraw = accessLock.newCondition();

    public BankAccount()
    {
        this.balance = 0;
        this.number = 0;
    }
    
    public int getBalance()
    {
    	return balance;
    }
    
    public int getNumber()
    {
    	return number;
    }

    public void withdraw(int amount, String threadName)
    {
        accessLock.lock();

        try
        {
            // Check to see if there is enough money in the account to make the withdrawal.
            if ((balance - amount) >= 0)
            {
                balance -= amount;
                number++;
                System.out.print("\t\t\tAgent " + threadName + " withdraws $" + amount);
                System.out.print("\t\t(-) Balance is $" + balance);
                System.out.println("\t\t\t\t\t" + number);
                if (amount > 75)
                {
                	System.out.println("\n***Flagged Transaction - Withdrawal Agent " + threadName + " Made A Withdrawal In Exccess Of $75.00 USD - See Flagged Transaction Log.\n");
                	flagged_transaction ft = new flagged_transaction(amount, threadName, number);
                	ft.fTransaction();
                }
            }

            // If there isn't enough cash in the account, block the withdrawal.
            else
            {
                System.out.print("\t\t\tAgent " + threadName + " withdraws $" + amount);
                System.out.print("\t\t(******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!!");
                System.out.println();
                canDeposit.signalAll();
                canWithdraw.await();
            }
        }

        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        finally
        {
            accessLock.unlock();
        }
    }

    
	public void deposit(int amount, String threadName)
    {
        accessLock.lock();

        try
        {
            // Add cash into account regardless of the current balance.
            balance += amount;
            number++;
            System.out.print("Agent " + threadName + " deposits $" + amount);
            System.out.print("\t\t\t\t\t(+) Balance is $" + balance);
            System.out.println("\t\t\t\t\t" + number);
            if (amount > 350)
            {
            	System.out.println("\n***Flagged Transaction - Depositor Agent " + threadName + " Made A Deposit In Exccess Of $350.00 USD - See Flagged Transaction Log.\n");
            	flagged_transaction ft = new flagged_transaction(amount, threadName, number);
            	ft.fTransaction();
            }
            canWithdraw.signalAll();
            canDeposit.await();
        }

        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        finally
        {
            accessLock.unlock();
        }
    }
	
	
	
	
	public void audit()
	{
		accessLock.lock();

        try
        {
        	System.out.println("\n********************************************************************************************************************************\n");
            System.out.print("\tAUDITOR FINDS CURRENT ACCOUNT BALANCE TO BE: $" + getBalance());
            System.out.println("\tNumber of transactions sicne last audit is: " + getNumber());
            System.out.println("\n********************************************************************************************************************************\n");
            canWithdraw.signalAll();
            canDeposit.await();
        }

        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        finally
        {
            accessLock.unlock();
        }
	}

}