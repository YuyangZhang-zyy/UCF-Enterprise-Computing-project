/*
 * Name: Yuyang Zhang
 * Course: CNT 4714 Spring 2023
 * Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
 * Due Date: February 12, 2023
 */

public interface TheBank 
{
	public abstract void deposit(int amount, String threadName);
	public abstract void withdraw(int amount, String threadName);
	public abstract void audit();
	//public abstract void flagged_transaction();
}
