package inventory;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class UserLogin extends JFrame {
	// declare variables
	private static final long serialVersionUID = 1L;
	boolean userMatch = false;
	static int id;
	static String userName;
	static String password;
	final static String columnsIdentifierLogins = "ID , Username , Password\n";
	final static String columnsIdentifierProducts = "ID , Quantity , Name , Category , Price ($)\n";
	final static String columnsIdentifierCategories = "ID , Name , Description\n";
	final static String columnsIdentifierCustomers = "ID , First Name , Last Name , Phone\n";
	final static String columnsIdentifierOrders = "Order ID , Customer ID , First Name , Last Name , Phone , Total ($)\n";
	final static String columnsIdentifierOrder = "Product ID , Product Name , Quantity , Price ($) , Total ($)\n";
	final static String databasePath = String.format("%s", System.getProperty("user.dir"));
	final static String loginsPath = String.format("%s\\Database\\Logins.txt", databasePath);
	final static File loginsFile = new File(loginsPath);
	static String productsPath;
	static File productsFile;
	static String customersPath;
	static File customersFile;
	static String categoriesPath;
	static File categoriesFile;
	static String ordersPath;
	static File ordersFile;
	static String userPath;
	static File userFile;
	static String ordersCustomerPath;;
	static File ordersCustomerFile;
	
	// declare GUI elements
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton buttonLogin;
    private JButton buttonRegister;
    private JPanel contentPane;

    public UserLogin() {
    	DefaultTableModel modelLogins = new DefaultTableModel(0, 0);
    	
    	// create and setup frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 200, 1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Login");
        setVisible(true);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setVisible(true);

        // create and set GUI elements
        JLabel labelTitle = new JLabel("StockAssist");
        labelTitle.setForeground(Color.RED);
        labelTitle.setBackground(Color.BLACK);
        labelTitle.setOpaque(true);
        labelTitle.setFont(new Font("Times New Roman", Font.BOLD, 75));
        labelTitle.setHorizontalAlignment(JLabel.CENTER);
        labelTitle.setBounds(300, 15, 400, 100);
        contentPane.add(labelTitle);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Tahoma", Font.PLAIN, 55));
        usernameField.setBounds(500, 200, 300, 75);
        contentPane.add(usernameField);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 55));
        passwordField.setBounds(500, 350, 300, 75);
        contentPane.add(passwordField);

        JLabel labelUsername = new JLabel("Username");
        labelUsername.setForeground(Color.RED);
        labelUsername.setFont(new Font("Tahoma", Font.PLAIN, 55));
        labelUsername.setBounds(240, 200, 250, 75);
        contentPane.add(labelUsername);

        JLabel labelPassword = new JLabel("Password");
        labelPassword.setForeground(Color.RED);
        labelPassword.setFont(new Font("Tahoma", Font.PLAIN, 55));
        labelPassword.setBounds(250, 350, 250, 75);
        contentPane.add(labelPassword);

        buttonRegister = new JButton("Create Account");
        buttonRegister.setForeground(Color.RED);
        buttonRegister.setBackground(Color.BLACK);
        buttonRegister.setFont(new Font("Tahoma", Font.PLAIN, 30));
        buttonRegister.setBounds(200, 450, 250, 75);
        buttonRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // get user input and store it in assigned variables
            	userName = usernameField.getText();
                password = passwordField.getText();
                userName = userName.trim();
                password = password.trim();
                
                // if statements to check if user name and password meets certain conditions to avoid errors or complications
                if(userName.isEmpty() || password.isEmpty() || Objects.equals(userName, userName.replaceAll("\\s+","")) == false || Objects.equals(password, password.replaceAll("\\s+","")) == false) {
                	JOptionPane.showMessageDialog(null, "Please enter a valid username and password.");
                } else if(userName.contains("/") || password.contains("/")) {
                	JOptionPane.showMessageDialog(null, "Username and password cannot contain '/'.");
                } else {
                	// for loop and if statement to check if user name already exists or not
                	// if so, sets a boolean variable 
                	for(int i = 0; i < modelLogins.getRowCount(); i++) {
                    	Object data = modelLogins.getValueAt(i, 1);
                    	if(Objects.equals(userName, data)) {
                    		userMatch = true;
                    		i = modelLogins.getRowCount();
                    	}
                    }
                    
                	// if statement to decide whether the account will be created or not based on if the user name was found to be taken or not
                    if(userMatch) {
                    	// resets the boolean variable and tells the user that the user name is taken
                    	userMatch = false;
                    	JOptionPane.showMessageDialog(null, "Username already in use. Please try another.");
                    } else {
                    	// sets the new user's ID
                    	id = Integer.valueOf(String.valueOf(modelLogins.getValueAt(modelLogins.getRowCount() - 1, 0))) + 1;
                    	// sets and makes the user file if it does not exist
                    	userPath = String.format("%s\\Database\\%s", databasePath, id);
                    	userFile = new File(userPath);
                    	if(!userFile.exists()) {
                    		userFile.mkdir();
                    	}
                    	// sets the file paths to the database based on the user's ID
                    	productsPath = String.format("%s\\Database\\%s\\Products.txt", databasePath, UserLogin.id);
                    	productsFile = new File(productsPath);
                    	customersPath = String.format("%s\\Database\\%s\\Customers.txt", databasePath, UserLogin.id);
                    	customersFile = new File(customersPath);
                    	categoriesPath = String.format("%s\\Database\\%s\\Categories.txt", databasePath, UserLogin.id);
                    	categoriesFile = new File(categoriesPath);
                    	ordersPath = String.format("%s\\Database\\%s\\Orders.txt", databasePath, UserLogin.id);
                    	ordersFile = new File(ordersPath);
                    	// sets and makes a file directory to the customer orders if it does not exist
                    	ordersCustomerPath = String.format("%s\\Database\\%s\\Orders", databasePath, UserLogin.id);
                    	ordersCustomerFile = new File(ordersCustomerPath);
                    	if(!ordersCustomerFile.exists()) {
                    		ordersCustomerFile.mkdir();
                    	}
                    	
                    	// goes to the main menu and notifies the user
                    	dispose();
                        new UserHome();
                        JOptionPane.showMessageDialog(null, "You have successfully created an account!");
                        
                        // adds the new user to the table and rewrites the table to the logins file
                    	Object[] row = {id, userName, password};
                    	modelLogins.addRow(row);
                        try {
                        	FileWriter fw = new FileWriter(loginsFile.getAbsoluteFile());
                        	BufferedWriter bw = new BufferedWriter(fw);
                        	
                        	bw.write(columnsIdentifierLogins);
                        	for(int i = 0; i < modelLogins.getRowCount(); i++) {
                        		for(int j = 0; j < modelLogins.getColumnCount(); j++) {
                        			bw.write(String.valueOf(modelLogins.getValueAt(i, j)));
                        			if(j < modelLogins.getColumnCount() - 1) {
                        				bw.write(" / ");
                        			}
                        		}
                        		bw.write("\n");
                        	}
                        	bw.close();
                        	fw.close();
                        } catch(Exception ex) {
                        	ex.printStackTrace();
                        }
                    }
                }
            }
        });
        contentPane.add(buttonRegister);
        
        buttonLogin = new JButton("Login");
        buttonLogin.setForeground(Color.RED);
        buttonLogin.setBackground(Color.BLACK);
        buttonLogin.setFont(new Font("Tahoma", Font.PLAIN, 50));
        buttonLogin.setBounds(550, 450, 250, 75);
        buttonLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// gets user input from text boxes and stores them in the assigned variables
                userName = usernameField.getText();
                password = passwordField.getText();
                userName = userName.trim();
                password = password.trim();
                
                // if statements to check if user name and password fields are not empty
                if(userName.isEmpty() || password.isEmpty()) {
                	JOptionPane.showMessageDialog(null, "Please enter a valid username and password.");
                } else {
                	// for loop and if statement to check if both the user name and password match any user
                	// if so, the user id is set to the id associated with the user and the counter is stopped
                	for(int i = 0; i < modelLogins.getRowCount(); i++) {
                		Object dataID = modelLogins.getValueAt(i, 0);
                		Object dataUserName = modelLogins.getValueAt(i, 1);
                    	Object dataPassword = modelLogins.getValueAt(i, 2);
                    	if(Objects.equals(userName, dataUserName) && Objects.equals(password, dataPassword)) {
                    		id = Integer.valueOf((String) dataID);
                    		userMatch = true;
                    		i = modelLogins.getRowCount();
                    	}
                    }
                	
                	// if statement to decide whether the user's login information matched a user
                    if(userMatch) {
                    	userMatch = false;
                    	
                    	// checks if the user's directory exists and creates it if does not
                    	userPath = String.format("%s\\Database\\%s", databasePath, id);
                    	userFile = new File(userPath);
                    	if(!userFile.exists()) {
                    		userFile.mkdir();
                    	}
                    	
                    	// sets the paths to the user's files in the database
                    	productsPath = String.format("%s\\Database\\%s\\Products.txt", databasePath, UserLogin.id);
                    	productsFile = new File(productsPath);
                    	customersPath = String.format("%s\\Database\\%s\\Customers.txt", databasePath, UserLogin.id);
                    	customersFile = new File(customersPath);
                    	categoriesPath = String.format("%s\\Database\\%s\\Categories.txt", databasePath, UserLogin.id);
                    	categoriesFile = new File(categoriesPath);
                    	ordersPath = String.format("%s\\Database\\%s\\Orders.txt", databasePath, UserLogin.id);
                    	ordersFile = new File(ordersPath);
                    	
                    	// Sets and creates the user's customers' orders directory if it does not exists
                    	ordersCustomerPath = String.format("%s\\Database\\%s\\Orders", databasePath, UserLogin.id);
                    	ordersCustomerFile = new File(ordersCustomerPath);
                    	if(!ordersCustomerFile.exists()) {
                    		ordersCustomerFile.mkdir();
                    	}
                    	
                    	// closes the current window and goes to the main menu
                        dispose();
                        new UserHome();
                        JOptionPane.showMessageDialog(null, "You have successfully logged in!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong username or password.");
                    }
                }
            }
        });
        contentPane.add(buttonLogin);
        
        // if statement to check if logins file exists, and if so, creates a new file
        try {
        	if(!loginsFile.exists()) {
        		loginsFile.createNewFile();
        		try {
    				loginsFile.createNewFile();
    				FileWriter fw = new FileWriter(loginsFile.getAbsoluteFile());
                 	BufferedWriter bw = new BufferedWriter(fw);
                 	bw.write(columnsIdentifierLogins);
                 	bw.close();
                 	fw.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}
        	// reads the logins file and puts it into a table model in order to only read the file once
    		BufferedReader br = new BufferedReader(new FileReader(loginsFile));
    		String firstLine = br.readLine().trim();
        	String[] columnsName = firstLine.split(" , ");
        	modelLogins.setColumnIdentifiers(columnsName);
        	Object[] tableLines = br.lines().toArray();
        	
        	for(int i = 0; i < tableLines.length; i++) {
        		String line = tableLines[i].toString().trim();
        		String[] dataRow = line.split(" / ");
        		modelLogins.addRow(dataRow);
        	}
        	br.close();
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
}