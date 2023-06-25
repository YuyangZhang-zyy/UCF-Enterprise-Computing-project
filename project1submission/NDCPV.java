/* Name: Yuyang Zhang
 * Course: CNT 4714 – Spring 2023
 * Assignment title: Project 1 – Event-driven Enterprise Simulation
 * Date: Sunday January 29, 2023
 */

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.security.auth.callback.ConfirmationCallback;
import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.SwingConstants;


import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

//******************************************************************************
public class NDCPV extends JFrame
{

	//set static variables to hold frame dimensions
	private static final int  WIDTH = 700;
	private static final int HEIGHT = 350;
	
	//set Label, Textfield, Button
	private JLabel blankLabel, numLabel, idLabel, qtyLabel, itemLabel, totalLabel;
	private JTextField blankTextField, numTextField, idTextField, qtyTextField, itemTextField, totalTextField;
	private JButton blankButton, findB, purchaseB, viewB, completeB, newb, exitB;
	
	//Creates an Array List of the current item in the inventory
	private ArrayList<String> inventoryLine = new ArrayList<String> ();
	//Creates an Array List of the shopping cart
	private ArrayList<myOrder> shoppingCart = new ArrayList<myOrder> ();
	
	private double shoppingCartSubTotal; //Keeps track of the total after user confirms item
	private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(); //Formats currency
	private DecimalFormat decimalFormat = new DecimalFormat("0.00"); //Formats decimals
	
	//declare reference variables for event handlers
	private FindButtonHandler findbHandler;
	private PurchaseButtonHandler purcbHandler;
	private ViewButtonHandler viewbHandler;
	private CompleteButtonHandler combHandler;
	private NewButtonHandler newbHandler;
	private ExitButtonHandler exitbHandler;
	
	//more static variable
	static NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
	static NumberFormat percentFormatter = NumberFormat.getPercentInstance();
	static DecimalFormat decimalFormatter = (DecimalFormat) percentFormatter;
	
	//set each item attributes array
	static String [] itemIDArray;
	static String [] itemTitleArray;
	static String [] itemInStockArray;
	static double [] itemPriceArray;
	static int [] itemQuantityArray;
	static double [] itemDiscountArray;
	static double [] itemSubtotalArray;
	
	//initiation
	static String itemIDString = "", itemTitle = "", output = "", maxArraySizeStr = "",
		itemPriceStr = "", itemInStocker = "", itemQuantityStr = "", itemSubtotalStr = "", itemDiscountStr = "",
		taxRateStr = "", discountRateStr, orderSubtotalStr;
	static double itemPrice = 0, itemSubtotal = 0, orderSubtotal = 0, orderTotal = 0,
		itemDiscount = 0, orderTaxAccount;
	static int itemQuantity = 0, itemCount = 1, maxArraysize = 0;
	
	//tax and discount rate
	final static double TAX_RATE = 0.060,
						DISCOUNT_FOR_05 = .10,
						DISCOUNT_FOR_10 = .15,
						DISCOUNT_FOR_15 = .20;
	String fileName;
	
	
//******************************************************************************
	public NDCPV()
	{
		setTitle("Nile Dot Com - Spring 2023");//set the title of the frame
		setSize(WIDTH, HEIGHT);//set the frame size
		
		//instantiate JLabel
		blankButton = new JButton(" ");
		blankLabel = new JLabel(" ", SwingConstants.RIGHT);
		idLabel = new JLabel("Enter item ID for Item #" + itemCount + ":", SwingConstants.RIGHT);
		qtyLabel = new JLabel("Enter quantity for Item #" + itemCount + ":", SwingConstants.RIGHT);
		itemLabel = new JLabel("Details for Item #" + itemCount + ":", SwingConstants.RIGHT);
		totalLabel = new JLabel("Order subtotal for " + (itemCount - 1) + " item(s):", SwingConstants.RIGHT);
		
		//instantiate JTextField objects
		blankTextField = new JTextField();
		idTextField = new JTextField();
		qtyTextField = new JTextField();
		itemTextField = new JTextField();
		totalTextField = new JTextField();
		
		//instantiate buttons and register handlers
		findB = new JButton("Find Item #" + itemCount );
		findbHandler = new FindButtonHandler();
		findB.addActionListener(findbHandler);
		
		purchaseB = new JButton("Purchase Item #" + itemCount);
		purcbHandler = new PurchaseButtonHandler();
		purchaseB.addActionListener(purcbHandler);
		
		viewB = new JButton("View Current Order");
		viewbHandler = new ViewButtonHandler();
		viewB.addActionListener(viewbHandler);
		
		completeB = new JButton("Complete Order - Check Out");
		combHandler = new CompleteButtonHandler();
		completeB.addActionListener(combHandler);
		
		newb = new JButton("Start New Order");
		newbHandler = new NewButtonHandler();
		newb.addActionListener(newbHandler);
		
		exitB = new JButton("Exit (Close App)");
		exitbHandler = new ExitButtonHandler();
		exitB.addActionListener(exitbHandler);
		
		//initiate for buttons, fields
		findB.setEnabled(true);
		purchaseB.setEnabled(false);
		viewB.setEnabled(false);
		completeB.setEnabled(false);
		newb.setEnabled(true);
		exitB.setEnabled(true);
		itemTextField.setEditable(false);
		totalTextField.setEditable(false);
		blankTextField.setEditable(false);
		blankTextField.setBackground(Color.BLACK);
		blankTextField.setVisible(false);
		blankButton.setBackground(Color.BLUE);
		blankButton.setVisible(false);
		
		Container pane = getContentPane();//get a content pane for the frame
		//create a 6 rows, 2 cols grid layout
		GridLayout grid6by2 = new GridLayout(6,2,8,4);
		GridLayout grid4by2 = new GridLayout(4,2,8,3);//grid layout for buttons
		GridLayout grid1by1 = new GridLayout(1,1,2,2);//spacer
		
		//create panels
		JPanel northPanel = new JPanel();
		JPanel southPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		
		//set layouts for panel
		northPanel.setLayout(grid6by2);
		centerPanel.setLayout(grid1by1);
		southPanel.setLayout(grid4by2);
		
		//add labels to panel
		northPanel.add(blankLabel);
		northPanel.add(blankTextField);
		idLabel.setForeground(Color.YELLOW);
		northPanel.add(idLabel);
		northPanel.add(idTextField);
		qtyLabel.setForeground(Color.YELLOW);
		northPanel.add(qtyLabel);
		northPanel.add(qtyTextField);
		itemLabel.setForeground(Color.YELLOW);
		northPanel.add(itemLabel);
		northPanel.add(itemTextField);
		totalLabel.setForeground(Color.YELLOW);
		northPanel.add(totalLabel);
		northPanel.add(totalTextField);
		
		//add spacer to center
		centerPanel.setBackground(Color.CYAN);
		
		//add buttons to southPanel
		southPanel.add(findB);
		southPanel.add(purchaseB);
		southPanel.add(viewB);
		southPanel.add(completeB);
		southPanel.add(newb);
		southPanel.add(exitB);
		southPanel.add(blankButton); southPanel.add(blankButton);
		
		//add panels to content pane
		pane.add(northPanel, BorderLayout.NORTH);
		pane.add(centerPanel, BorderLayout.CENTER);
		pane.add(southPanel, BorderLayout.SOUTH);
		
		centerFrame(WIDTH, HEIGHT);//call method to center frame on screen
		pane.setBackground(Color.DARK_GRAY);
		northPanel.setBackground(Color.DARK_GRAY);
		southPanel.setBackground(Color.CYAN);
	}
//******************************************************************************
	public void centerFrame(int frameWidth, int frameHeight)
	{
		//create a Toolkit
		Toolkit aToolkit = Toolkit.getDefaultToolkit();
		
		//create a dimension with user screen information
		Dimension screen = aToolkit.getScreenSize();
		
		//assign x, y position of upper-left corner of frame
		int xPositionFrame = (screen.width - frameWidth) / 2;
		int yPositionFrame = (screen.height - frameHeight) / 2;
		
		//method to center frame on user's screen
		setBounds(xPositionFrame, yPositionFrame, frameWidth, frameHeight);
	}
//******************************************************************************
	private class FindButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String outputMessage, priceData, itemIDFromFile, itemInStockFromFile, itemDescriptionFromFile, itemIDFromText, itemQuantityFromText, item = null;//create buffer
			String[] itemInfo = null;
			boolean idFound = false, isQtyOK = false, itemInStock = true;//set boolean variable
			int quantity;
			File inventoryFile = new File("inventory.txt");//scan file
		    Scanner aScanner;
		    
		    try 
		    {
		        aScanner = new Scanner(inventoryFile);//scan file

		        itemIDFromText = idTextField.getText();
		        itemQuantityFromText = qtyTextField.getText();
		        quantity = Integer.parseInt(itemQuantityFromText);

		        //Create space for the values to be added
		        inventoryLine.add("0");
		        inventoryLine.add("0");
		        inventoryLine.add("0");
		        inventoryLine.add("0");
		        inventoryLine.add("0");
		        inventoryLine.add("0");
		        
		        while(aScanner.hasNext()) 
		        {
		            item = aScanner.nextLine();
		            itemInfo = item.split(","); //Splits string

		            //setting the values of the current item	
		            itemIDFromFile = itemInfo[0].trim();
		            itemDescriptionFromFile = itemInfo[1].trim();
		            itemInStockFromFile = itemInfo[2].trim();
		            priceData = itemInfo[3].trim();

		            //setting the Boolean value
		            itemInStock = Boolean.parseBoolean(itemInStockFromFile);

		            //itemIDs match
		            if (itemIDFromFile.equals(itemIDFromText)) 
		            {
		            	idFound = true;
		            	inventoryLine.set(0, itemIDFromFile);
		            	inventoryLine.set(1, itemDescriptionFromFile);
		            	inventoryLine.set(2, itemInStockFromFile);
		            	inventoryLine.set(3, priceData);
		            	//item quantity is in the limit
		            	if (quantity >= 1)
		            		isQtyOK = true;
		            	//item found, leave the loop
		            	break;
		            }
		        }
		        //item was not found, print the error
		        if (!idFound) 
		        {
		        	outputMessage = "item ID " + itemIDFromText + " not in file";
		        	JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
		        	clear();
		        }
		        else 
		        {
		        	//item quantity not okay, or nothing entered
		        	if (!isQtyOK) 
		        	{
		        		outputMessage = "Invalid input for number of line items or quantity of items";
		        		JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
		        		clear();
		        	}
		        	//item not in stock, print the error
		        	if (!itemInStock) 
		        	{
		        		outputMessage = "Sorry... that item is out of stock, please try another item";
		        		JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
		        		clear();
		        	}
		        	else 
		        	{
		        		//price
		        		double price = Double.parseDouble(inventoryLine.get(3));
		        		double discountCalc = getDiscount(quantity);
		        		double totalPriceForItem = (double)(price * quantity) - ((price * quantity) * (discountCalc / 100));
		        		int discount = (int) discountCalc;
		        		String displayItem = inventoryLine.get(0) + "  " + inventoryLine.get(1) + "  " + currencyFormatter.format(price) + "  " + quantity + "  " + discount + "%  " + currencyFormatter.format(totalPriceForItem);
		        		itemTextField.setText(displayItem);
		        		purchaseB.setEnabled(true);
		        		findB.setEnabled(false);
		        	}
		        }
		        
		        //Close scanner
		        aScanner.close();

		    } catch (FileNotFoundException e1) {
		    	 e1.printStackTrace();
		    } catch (NumberFormatException e1) {
		    	//print error
		    	outputMessage = "You Clicked the Find Item button, but did not enter quantity!";
		    	JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - INFORMATION MESSAGE", JOptionPane.INFORMATION_MESSAGE);
		    	clear();
		    }
		}
	}
//******************************************************************************
	private class PurchaseButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//price
			String itemQuantityFromText = qtyTextField.getText();
		    int quantity = Integer.parseInt(itemQuantityFromText);
		    double price = Double.parseDouble(inventoryLine.get(3));
		    double discountCalc = getDiscount(quantity);
		    int discount = (int) discountCalc;
		    double totalPriceForItem = (double)(price * quantity) - ((price * quantity) * (discountCalc / 100));
			
		    myOrder item = new myOrder();

		    //set item
		    item.setItemID(inventoryLine.get(0));
		    item.setDescription(inventoryLine.get(1));
		    item.setStock(inventoryLine.get(2));
		    item.setPrice(inventoryLine.get(3));
		    item.setQuantity(itemQuantityFromText);
		    
		    String temp = String.valueOf(discount);
		    item.setDiscount(temp);

		    temp = String.valueOf(totalPriceForItem);
		    item.setTotalPrice(temp);

		    //Add the confirmed item to shopping cart
		    shoppingCart.add(item);
		    
		    //Calculate total
		    shoppingCartSubTotal += totalPriceForItem;

		    //Update text fields and buttons
		    totalTextField.setText("" + currencyFormatter.format(shoppingCartSubTotal) + "");
		    viewB.setEnabled(true);
		    completeB.setEnabled(true);

		    String outputmessage = "Item #" + itemCount + " accepted. Added to your cart.";
		    JOptionPane.showMessageDialog(null, outputmessage, "Nile Dot Com - Item Confirmed", JOptionPane.PLAIN_MESSAGE);
			
		    //Increments itemNumber
		    itemCount++;

		    //Create Java Labels
		    idLabel.setText("Enter Item ID for Item #" + itemCount + ":  ");
		    qtyLabel.setText("Enter quantity for Item #" + itemCount + ":  ");
		    itemLabel.setText("Details for Item #" + itemCount + ":  ");
		    totalLabel.setText("Order subtotal for " + (itemCount - 1)+ " item(s):  ");

		    //Create Java Buttons
		    findB.setEnabled(true);
		    purchaseB.setEnabled(false);
		    findB.setText("Find Item #" + itemCount);
		    purchaseB.setText("Purchase Item #" + itemCount);

		    //Clears the text fields
		    clear();
		}
	}	
//******************************************************************************
	private class ViewButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String output = "";

			//print data
		    for (int i = 0; i < itemCount - 1; i++) 
		    {
		    	Double totalPriceFormat = Double.parseDouble(shoppingCart.get(i).getTotalPrice());
		    	output += ((i + 1) + ". " + shoppingCart.get(i).getItemID() + " " + shoppingCart.get(i).getDescription() + " $" + shoppingCart.get(i).getPrice() + " " + shoppingCart.get(i).getQuantity() + " " + shoppingCart.get(i).getDiscount() + "% " + currencyFormat.format(totalPriceFormat) + "\n");
		    }

		    JOptionPane.showMessageDialog(null, output, "Nile Dot Com - Current Shopping Cart Status", JOptionPane.INFORMATION_MESSAGE);
		}
	}
//******************************************************************************
	private class CompleteButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//set performed data
			Format dateFormat = new SimpleDateFormat("MM/dd/YY, hh:mm:ss a z");//date and time
		    Format permutationDate = new SimpleDateFormat("ddMMYYYYhhmm");//transaction id
		    StringBuilder output = new StringBuilder();
		    StringBuilder invoice = new StringBuilder();
		    String permDate = permutationDate.format(new Date());
		    String curDate = dateFormat.format(new Date());
		    String items = "", taxStr, taxRate, orderSubTotal, orderTotalStr, thank;
		    String lines = "";
		    double tax = 0.06 * shoppingCartSubTotal;
		    double orderTotal = tax + shoppingCartSubTotal;
		    int display = itemCount - 1;

		    String totalMessage = "Date: " + curDate.replaceFirst("0", "") + "\n\nNumber of line items: " + display + "\n\nItem# / ID / Title / Price / Qty / Disc % / Subtotal:\n";

		    //Create output with every item in the shoppingCart
		    for (int i = 0; i < itemCount - 1; i++) 
		    {
		    	Double totalPriceFormat = Double.parseDouble(shoppingCart.get(i).getTotalPrice());
		        items += ((i + 1) + ". " + shoppingCart.get(i).getItemID() + " " + shoppingCart.get(i).getDescription() + " $" + shoppingCart.get(i).getPrice() + " " + shoppingCart.get(i).getQuantity() + " " + shoppingCart.get(i).getDiscount() + "% " + currencyFormat.format(totalPriceFormat) + "\n");
		    }

		    orderSubTotal = "\n\nOrder Subtotal: " + currencyFormat.format(shoppingCartSubTotal) + "\n\n";

		    taxRate = "Tax rate: 6%\n\n";

		    taxStr = Double.toString(tax);
		    taxStr = "Tax amount: " + currencyFormat.format(tax) + "\n\n";

		    orderTotalStr = Double.toString(orderTotal);
		    orderTotalStr = "ORDER TOTAL: " + currencyFormat.format(orderTotal) + "\n\n";

		    thank = "Thanks for shopping at Nile Dot Com!\n";

		    //Appending strings to the StringBuilder
		    output.append(totalMessage);
		    output.append(items);
		    output.append(orderSubTotal);
		    output.append(taxRate);
		    output.append(taxStr);
		    output.append(orderTotalStr);
		    output.append(thank);

		    JOptionPane.showMessageDialog(null, output, "Nile Dot Com - FINAL INVOICE", JOptionPane.INFORMATION_MESSAGE);

		    for (int i = 0; i < itemCount - 1; i++) 
		    {
		    	Double totalPriceFormat = Double.parseDouble(shoppingCart.get(i).getTotalPrice());
		        Double discountDecimal = Double.parseDouble(shoppingCart.get(i).getDiscount()) / 100;
		        lines += (permDate + ", " + shoppingCart.get(i).getItemID() + ", " + shoppingCart.get(i).getDescription() + ", " + shoppingCart.get(i).getPrice() + ", " + shoppingCart.get(i).getQuantity() + ", " + discountDecimal + ", $" + decimalFormat.format(totalPriceFormat) + ", " + curDate.replaceFirst("0", "") + " \n");
		    }

		    invoice.append(lines);

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
		        writer.append(invoice);
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
		    idTextField.setEnabled(false);
		    qtyTextField.setEnabled(false);
		}
	}
//******************************************************************************
	private class NewButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//Reset Labels
		    idLabel.setText("Enter Item ID for Item #1:  ");
		    qtyLabel.setText("Enter quantity for Item #1:  ");
		    itemLabel.setText("Details for Item #1:  ");
		    totalLabel.setText("Order subtotal for 0 item(s):  ");

		    //Reset TextFields
		    idTextField.setText("");
		    qtyTextField.setText("");
		    itemTextField.setText("");
		    totalTextField.setText("");

		    //Reset Buttons
		    findB.setText("Find Item #1");
		    purchaseB.setText("Purchase Item #1");
		    completeB.setText("Complete Order");
		    exitB.setText("Exit");

		    //Disable Buttons and TextFields
		    //Enable the user input TextFields
		    idTextField.setEnabled(true);
		    qtyTextField.setEnabled(true);
		    totalTextField.setEnabled(false);
		    itemTextField.setEnabled(false);
		    
		    findB.setEnabled(true);
		    purchaseB.setEnabled(false);
		    viewB.setEnabled(false);
		    completeB.setEnabled(false);

		    //Make the disabled TextFields easier to read
		    totalTextField.setDisabledTextColor(Color.BLACK);
		    itemTextField.setDisabledTextColor(Color.BLACK);

		    //Reset itemCount, arrays, and shoppingCartSubTotal
		    itemCount = 1;
		    inventoryLine.clear();
		    shoppingCart.clear();
		    shoppingCartSubTotal = 0;
		}
	}
//******************************************************************************
	private class ExitButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String outputMessage;
			outputMessage = "You Clicked the Exit (Close App) button - IT WORKS!";
			JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - INFORMATION MESSAGE", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
	}
//******************************************************************************
	public int getDiscount(int quantity)//set discount rate for different quantity
	{
	   if (quantity >= 1 && quantity <= 4) 
		   return 0;
	   if (quantity >= 5 && quantity <= 9)
		   return 10;
	   if (quantity >= 10 && quantity <= 14)
		   return 15;
	   if (quantity >= 15)
		   return 20;
	   return 0;
	}
//******************************************************************************
	public void clear() //Clears the text fields
	{
	    idTextField.setText("");
	    qtyTextField.setText("");
	}
//******************************************************************************
	public class myOrder//setter and getter for order
	{
		private String itemID;
		private String price;
		private String description;
		private String inStock;
		private String discount;
		private String totalPrice;
		private String quantity;
		
		//item ID
		public String getItemID()
		{
			return itemID;
		}
		public void setItemID(String itemID) 
		{
			this.itemID = itemID;
		}
				
		//quantity
		public String getQuantity() 
		{
			return quantity;
		}
		public void setQuantity(String quantity) 
		{
			this.quantity = quantity;
		}
				
		//is the item in stock
		public String getStock() 
		{
			return inStock;
		}
		public void setStock(String inStock) 
		{
			this.inStock = inStock;
		}
				
		//description of item
		public String getDescription() 
		{
			return description;
		}
		public void setDescription(String description) 
		{
			this.description = description;
		}
				
		//price value
		public String getPrice() 
		{
			return price;
		}
		public void setPrice(String price) 
		{
			this.price = price;
		}
				
		//discount value
		public String getDiscount() 
		{
			return discount;
		}	
		public void setDiscount(String discount) 
		{
			this.discount = discount;
		}
				
		//total price of the item(s)
		public String getTotalPrice() 
		{
			return totalPrice;
		}	
		public void setTotalPrice(String totalPrice) 
		{
			this.totalPrice = totalPrice;
		}
	}
//******************************************************************************
	public static void main(String[] args) 
	{
		JFrame aNewStore = new NDCPV();
		aNewStore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aNewStore.setLocationRelativeTo(null);
		aNewStore.setVisible(true);
	}
}