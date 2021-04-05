package inventory;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class Customers extends JFrame {
	// declare variables
	private static final long serialVersionUID = 1L;
	static boolean matchID = false;
	static ArrayList<String> customersEdited = new ArrayList<String>();
	static ArrayList<String> customersDeleted = new ArrayList<String>();
	static String customerID;
	static String customerFirstName;
	static String customerLastName;
	static String customerPhone;
	static String pathToDelete;
	static File fileToDelete;
	
	// declare GUI elements
	private JLabel labelCustomers;
	private JLabel labelCustomersID;
	private JLabel labelCustomersFirstName;
	private JLabel labelCustomersLastName;
	private JLabel labelCustomerPhone;
	private JTextField textCustomerID;
	private JTextField textCustomerFirstName;
	private JTextField textCustomerLastName;
	private JTextField textCustomerPhone;
	private JButton buttonMenu;
	private JButton buttonAdd;
	private JButton buttonEdit;
	private JButton buttonDelete;
	private JButton buttonClear;
	private JButton buttonUp;
	private JButton buttonDown;
	private static javax.swing.JTable tableCustomers;
	private JScrollPane jScrollPane;
	private JPanel contentPane;
	
	public Customers() {
		// creates table models needed to be able to access the database
		DefaultTableModel model = new DefaultTableModel(0, 0);
		DefaultTableModel modelOld = new DefaultTableModel(0, 0);
		DefaultTableModel modelOrders = new DefaultTableModel(0, 0);
		
		// creates and sets parameters for the panel
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(450, 200, 1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Customers");
        setVisible(true);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setVisible(true);
        
        // warns the user that their recent changes to the table will not be saved if they exit without returning to the menu first 
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		int save = JOptionPane.showConfirmDialog(null, "Are you sure you would like to exit? Any changes made will not be saved.", "Exit", JOptionPane.YES_NO_OPTION);
        		if(save == JOptionPane.YES_OPTION) {
        			dispose();
        		}
        	}
        });
        
        // create and set parameters for GUI elements
        labelCustomers = new JLabel("Customers");
        labelCustomers.setForeground(Color.RED);
        labelCustomers.setFont(new Font("Times New Roman", Font.BOLD, 60));
        labelCustomers.setBounds(350, 0, 600, 75);
        contentPane.add(labelCustomers);
        
        labelCustomersID = new JLabel("ID");
        labelCustomersID.setForeground(Color.RED);
        labelCustomersID.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelCustomersID.setBounds(180, 100, 200, 50);
        contentPane.add(labelCustomersID);
        
        labelCustomersFirstName = new JLabel("First Name");
        labelCustomersFirstName.setForeground(Color.RED);
        labelCustomersFirstName.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelCustomersFirstName.setBounds(25, 175, 300, 50);
        contentPane.add(labelCustomersFirstName);
        
        labelCustomersLastName = new JLabel("Last Name");
        labelCustomersLastName.setForeground(Color.RED);
        labelCustomersLastName.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelCustomersLastName.setBounds(25, 250, 300, 50);
        contentPane.add(labelCustomersLastName);
        
        labelCustomerPhone = new JLabel("Phone #");
        labelCustomerPhone.setForeground(Color.RED);
        labelCustomerPhone.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelCustomerPhone.setBounds(75, 325, 300, 50);
        contentPane.add(labelCustomerPhone);
        
        textCustomerID = new JTextField();
        textCustomerID.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        textCustomerID.setBounds(250, 100, 200, 50);
        contentPane.add(textCustomerID);
        
        textCustomerFirstName = new JTextField();
        textCustomerFirstName.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        textCustomerFirstName.setBounds(250, 175, 200, 50);
        contentPane.add(textCustomerFirstName);
        
        textCustomerLastName = new JTextField();
        textCustomerLastName.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        textCustomerLastName.setBounds(250, 250, 200, 50);
        contentPane.add(textCustomerLastName);
        
        textCustomerPhone = new JTextField();
        textCustomerPhone.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        textCustomerPhone.setBounds(250, 325, 200, 50);
        contentPane.add(textCustomerPhone);
        
        buttonMenu = new JButton("Menu");
        buttonMenu.setFont(new Font("Tahoma", Font.PLAIN, 45));
        buttonMenu.setBounds(10, 10, 150, 75);
        buttonMenu.setForeground(new java.awt.Color(255, 0, 51));
        buttonMenu.setBackground(Color.BLACK);
        buttonMenu.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// goes to the main menu
        		dispose();
        		new UserHome();
                
        		// if statement to check if any customers were edited or deleted
        		if(customersEdited.size() > 0 || customersDeleted.size() > 0) {
        			// if statement to check if the orders file exists and creates it if not
        			if(!UserLogin.ordersFile.exists()) {
        	        	try {
        					UserLogin.ordersFile.createNewFile();
        					FileWriter fw = new FileWriter(UserLogin.ordersFile.getAbsoluteFile());
        	             	BufferedWriter bw = new BufferedWriter(fw);
        	             	bw.write(UserLogin.columnsIdentifierOrders);
        	             	bw.close();
        	             	fw.close();
        				} catch (IOException e1) {
        					e1.printStackTrace();
        				}
        	        }
        			// reads the orders file in a try/catch and sets it inside of the assigned table model
        			try {
                		BufferedReader br = new BufferedReader(new FileReader(UserLogin.ordersFile));
                		String firstLine = br.readLine().trim();
                    	String[] columnsName = firstLine.split(" , ");
                    	modelOrders.setColumnIdentifiers(columnsName);
                    	Object[] tableLines = br.lines().toArray();
                    	
                    	for(int i = 0; i < tableLines.length; i++) {
                    		String line = tableLines[i].toString().trim();
                    		String[] dataRow = line.split(" / ");
                    		modelOrders.addRow(dataRow);
                    	}
                    	br.close();
                	} catch (Exception ex) {
                		ex.printStackTrace();
                	}
            		
        			// if statement to check if any customers were edited
            		if(customersEdited.size() > 0) {
            			// for loop to go through every customer edited
            			for(int i = 0; i < customersEdited.size(); i++) {
            				// for loop and if statement to check if the edited customer existed prior to entering the class
            				for(int j = 0; j < modelOld.getRowCount(); j++) {
            					if(customersEdited.get(i).equals(modelOld.getValueAt(j, 0))) {
            						// if statement to check if the customer was not deleted after getting edited
            						if(!customersDeleted.contains(customersEdited.get(i))) {
            							// for loop and if statement to find the row of the edited customer in the table model
            		                    for(int k = 0; k < model.getRowCount(); k++) {
            		                    	if(model.getValueAt(k, 0).equals(customersEdited.get(i))) {
            		                    		// for loop to go through every order and if statement to check if the customer matches the customer that was edited
            		                    		// then edits the customer's information to the new information
            		                    		for(int h = 0; h < modelOrders.getRowCount(); h++) {
                    		                    	if(modelOrders.getValueAt(h, 1).equals(customersEdited.get(i))) {
                    		                    		modelOrders.setValueAt(String.valueOf(model.getValueAt(k, 1)), h, 2);
                    		                    		modelOrders.setValueAt(String.valueOf(model.getValueAt(k, 2)), h, 3);
                    		                    		modelOrders.setValueAt(String.valueOf(model.getValueAt(k, 3)), h, 4);
                    		                    	}
                    		                    }
            		                    		// closes the for loop (no need to check the rest of the table because it can only exist once)
            		                    		k = model.getRowCount();
            		                    	}
            		                    }
            						}
            						// closes the for loop (no need to check the rest of the table because it can only exist once)
            						j = modelOld.getRowCount();
            					}
            				}
            			}
            		}
            		// if statement to check if any customers were deleted
            		if(customersDeleted.size() > 0) {
            			// for loop to go through every customer deleted
            			for(int i = 0; i < customersDeleted.size(); i++) {
            				// for loop and if statement to find the row of the customer deleted in the old table
            				for(int j = 0; j < modelOld.getRowCount(); j++) {
            					if(customersDeleted.get(i).equals(modelOld.getValueAt(j, 0))) {
            						// sets the directory to delete based on the customer ID that was obtained from finding it in the model
            						pathToDelete = String.format("%s\\Database\\%s\\Orders\\%s", UserLogin.databasePath, UserLogin.id, customersDeleted.get(i));
                        			fileToDelete = new File(pathToDelete);
                        			if(fileToDelete.exists()) {
                        				String[] entries = fileToDelete.list();
                        				for(String s: entries){
                        				    File currentFile = new File(fileToDelete.getPath(),s);
                        				    currentFile.delete();
                        				}
                        				fileToDelete.delete();
                        			}
                        			// for loop and if statement to delete any orders that pertain to that specific customer
        		                    for(int h = 0; h < modelOrders.getRowCount(); h++) {
        		                    	if(modelOrders.getValueAt(h, 1).equals(customersDeleted.get(i))) {
                		                    modelOrders.removeRow(h);
        		                    	}
        		                    }
        		                    // closes the for loop (no need to check the rest of the table because it can only exist once)
            						j = modelOld.getRowCount();
            					}
            				}
            			}
            		}
            		
            		// rewrites the orders file from the updated table
            		try {
                     	FileWriter fw = new FileWriter(UserLogin.ordersFile.getAbsoluteFile());
                     	BufferedWriter bw = new BufferedWriter(fw);
                     	
                     	bw.write(UserLogin.columnsIdentifierOrders);
                     	for(int i = 0; i < modelOrders.getRowCount(); i++) {
                     		for(int j = 0; j < modelOrders.getColumnCount(); j++) {
                     			bw.write((String)modelOrders.getValueAt(i, j));
                     			if(j < modelOrders.getColumnCount() - 1) {
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
        		
        		// rewrites the customers file from the updated table
        		try {
                 	FileWriter fw = new FileWriter(UserLogin.customersFile.getAbsoluteFile());
                 	BufferedWriter bw = new BufferedWriter(fw);
                 	
                 	bw.write(UserLogin.columnsIdentifierCustomers);
                 	for(int i = 0; i < tableCustomers.getRowCount(); i++) {
                 		for(int j = 0; j < tableCustomers.getColumnCount(); j++) {
                 			bw.write((String)tableCustomers.getModel().getValueAt(i, j));
                 			if(j < tableCustomers.getColumnCount() - 1) {
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
        		
        		// while loops to remove every item in the array lists for later use (otherwise, the values would stay even after going to another class)
        		while(customersEdited.size() > 0) {
            		customersEdited.remove(0);
            	}
                while(customersDeleted.size() > 0) {
            		customersDeleted.remove(0);
            	}
        	}
        });
        contentPane.add(buttonMenu);

        buttonAdd = new JButton("Add");
        buttonAdd.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonAdd.setBounds(25, 400, 125, 50);
        buttonAdd.setForeground(new java.awt.Color(255, 0, 51));
        buttonAdd.setBackground(Color.BLACK);
        buttonAdd.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// stores all the values in the text fields in their assigned variables
        		customerID = textCustomerID.getText();
        		customerFirstName = textCustomerFirstName.getText();
        		customerLastName = textCustomerLastName.getText();
        		customerPhone = textCustomerPhone.getText();
        		customerID = customerID.trim();
        		customerFirstName = customerFirstName.trim();
        		customerLastName = customerLastName.trim();
        		customerPhone = customerPhone.trim();
        		
        		// if statements to check if the text fields meet certain requirements to avoid errors
        		if(customerID.isEmpty() || customerFirstName.isEmpty()) {
        			JOptionPane.showMessageDialog(null, "Please enter at least an ID and a name.");
        		} else if(Objects.equals(customerID, customerID.replaceAll("\\s+","")) == false || customerID.chars().allMatch(Character::isDigit) == false) {
        			JOptionPane.showMessageDialog(null, "Please enter a valid ID.");
        		} else if(customerFirstName.contains("/") || customerLastName.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Name cannot contain '/'.");
        		} else if(customerPhone.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Phone number cannot contain '/'.");
        		} else {
        			// for loop and if statement to check if the ID is already being used
        			for(int i = 0; i < model.getRowCount(); i++) {
                    	Object data = model.getValueAt(i, 0);
                    	if(Objects.equals(customerID, data)) {
                    		matchID = true;
                    	}
                    }
                    
        			// if statements to decide whether the customer will be made based on if it the ID is taken or not
                    if(matchID) {
                    	matchID = false;
                    	JOptionPane.showMessageDialog(null, "ID already in use.");
                    } else {
                    	// adds the new customer and notifies the user
                    	String[] dataRow = {customerID, customerFirstName, customerLastName, customerPhone};
                		model.addRow(dataRow);
                		tableCustomers.getSelectionModel().clearSelection();
                		tableCustomers.setRowSelectionInterval(tableCustomers.getRowCount() - 1, tableCustomers.getRowCount() - 1);
                		JOptionPane.showMessageDialog(null, "Successfully added the customer.");
                    }
                }
        	}
        });
        contentPane.add(buttonAdd);
        
        buttonEdit = new JButton("Edit");
        buttonEdit.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonEdit.setBounds(175, 400, 125, 50);
        buttonEdit.setForeground(new java.awt.Color(255, 0, 51));
        buttonEdit.setBackground(Color.BLACK);
        buttonEdit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// stores the values from the text boxes in the assigned variables
        		customerID = textCustomerID.getText();
        		customerFirstName = textCustomerFirstName.getText();
        		customerLastName = textCustomerLastName.getText();
        		customerPhone = textCustomerPhone.getText();
        		customerID = customerID.trim();
        		customerFirstName = customerFirstName.trim();
        		customerLastName = customerLastName.trim();
        		customerPhone = customerPhone.trim();
        		int[] rowsSelected = tableCustomers.getSelectedRows();
        		
        		// if statements to check if the values meet certain conditions to avoid errors
        		if(tableCustomers.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a customer to edit.");
        		} else if(customerID.isEmpty() || customerFirstName.isEmpty()) {
        			JOptionPane.showMessageDialog(null, "Please enter at least an ID and a name.");
        		} else if(Objects.equals(customerID, customerID.replaceAll("\\s+","")) == false || customerID.chars().allMatch(Character::isDigit) == false) {
        			JOptionPane.showMessageDialog(null, "Please enter a valid ID.");
        		} else if(customerFirstName.contains("/") || customerLastName.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Name cannot contain '/'.");
        		} else if(customerPhone.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Phone number cannot contain '/'.");
        		} else if(rowsSelected.length > 1) {
        			JOptionPane.showMessageDialog(null, "Cannot edit multiple customers at once.");
        		} else if(!customerID.equals(model.getValueAt(tableCustomers.getSelectedRow(), 0))) {
        			JOptionPane.showMessageDialog(null, "Cannot edit the customer ID once the customer is added to the system.");
        		} else {
        			// for loop and if statement to check if the ID is already being used
    				for(int i = 0; i < model.getRowCount(); i++) {
                    	Object data = model.getValueAt(i, 0);
                    	if(Objects.equals(customerID, data) && i != tableCustomers.getSelectedRow()) {
                    		matchID = true;
                    	}
                    }
                    
    				// if statements to decide whether the customer will be edited based on if the ID is unique
                    if(matchID) {
                    	matchID = false;
                    	JOptionPane.showMessageDialog(null, "ID already in use.");
                    } else {
                    	// if statement to add the edited customer to the array list if it is not already in it for future use
                    	if(!customersEdited.contains(customerID)) {
                    		customersEdited.add(customerID);
                    	}
                    	// edits the table
                		model.setValueAt(customerID, tableCustomers.getSelectedRow(), 0);
                		model.setValueAt(customerFirstName, tableCustomers.getSelectedRow(), 1);
                		model.setValueAt(customerLastName, tableCustomers.getSelectedRow(), 2);
                		model.setValueAt(customerPhone, tableCustomers.getSelectedRow(), 3);
                		JOptionPane.showMessageDialog(null, "Successfully edited the customer.");
                    }
                }
        	}
        });
        contentPane.add(buttonEdit);
        
        buttonDelete = new JButton("Delete");
        buttonDelete.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonDelete.setBounds(25, 500, 125, 50);
        buttonDelete.setForeground(new java.awt.Color(255, 0, 51));
        buttonDelete.setBackground(Color.BLACK);
        buttonDelete.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// if statement to check if the user has selected a row to delete or not
        		if(tableCustomers.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a customer to delete.");
        		} else {
        			// confirm dialog to make sure the user wants to delete the customer(s)
        			int a = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected customer(s) and all of their orders?");
        			if (a == JOptionPane.YES_OPTION) {
            			int[] selectedRows = tableCustomers.getSelectedRows();
            			// for loop to go through every customer selected
                		for(int i = 0; i < selectedRows.length; i++) {
                			// if statement to add the customer to the list of customers deleted if it is not already in there
                			if(!customersDeleted.contains(model.getValueAt(selectedRows[0], 0))) {
                        		customersDeleted.add(String.valueOf(model.getValueAt(selectedRows[0], 0)));
                        	}
                			model.removeRow(selectedRows[0]);
                		}
                		// clears the text boxes and notifies the user
            			textCustomerID.setText("");
            			textCustomerFirstName.setText("");
            			textCustomerLastName.setText("");
            			textCustomerPhone.setText("");
            			JOptionPane.showMessageDialog(null, "Successfully deleted the selected customer(s).");
                    }
        		}
        	}
        });
        contentPane.add(buttonDelete);
        
        buttonClear = new JButton("Clear");
        buttonClear.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonClear.setBounds(175, 500, 125, 50);
        buttonClear.setForeground(new java.awt.Color(255, 0, 51));
        buttonClear.setBackground(Color.BLACK);
        buttonClear.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// clears the text boxes
        		textCustomerID.setText("");
        		textCustomerFirstName.setText("");
        		textCustomerLastName.setText("");
        		textCustomerPhone.setText("");
        	}
        });
        contentPane.add(buttonClear);
        
        buttonUp = new JButton("Up");
        buttonUp.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonUp.setBounds(325, 400, 125, 50);
        buttonUp.setForeground(new java.awt.Color(255, 0, 51));
        buttonUp.setBackground(Color.BLACK);
        buttonUp.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int[] selectedRows = tableCustomers.getSelectedRows();
        		// if statement to check if the user has selected a row or not
        		if(tableCustomers.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a row to move.");
        		} else {
        			// if statement to check if the row can go up
        			if(selectedRows[0] > 0) {
        				// for loop to go through every row selected
        				for(int i = 0; i < selectedRows.length; i++) {
        					// moves the selected row up
            				model.moveRow(selectedRows[i], selectedRows[i], selectedRows[i] -1);
        				}
        				// moves the selection interval with the row(s) that moved up
            			tableCustomers.setRowSelectionInterval(selectedRows[0] - 1, selectedRows[selectedRows.length - 1] - 1);
        			}
        		}
        	}
        });
        contentPane.add(buttonUp);
        
        buttonDown = new JButton("Down");
        buttonDown.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonDown.setBounds(325, 500, 125, 50);
        buttonDown.setForeground(new java.awt.Color(255, 0, 51));
        buttonDown.setBackground(Color.BLACK);
        buttonDown.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int[] selectedRows = tableCustomers.getSelectedRows();
        		// if statement to check if a row is selected
        		if(tableCustomers.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a row to move.");
        		} else {
        			// if statement to check if the row can go down
    				if(selectedRows[selectedRows.length - 1] < model.getRowCount() - 1) {
    					// for loop to go through every row selected
    					for(int i = selectedRows.length - 1; i >= 0; i--) {
    						// moves the selected row down
    						model.moveRow(selectedRows[i], selectedRows[i], selectedRows[i] + 1);
        				}
    					// moves the selection interval with the row(s) that moved down
    					tableCustomers.setRowSelectionInterval(selectedRows[0] + 1, selectedRows[selectedRows.length - 1] + 1);
    				}
        		}
        	}
        });
        contentPane.add(buttonDown);
        
        tableCustomers = new JTable(model) {
        	private static final long serialVersionUID = 1L;
        	public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
             }
        };
        tableCustomers.setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 20));
        tableCustomers.getTableHeader().setForeground(Color.BLACK);
        tableCustomers.getTableHeader().setBackground(Color.RED);
        tableCustomers.getTableHeader().setReorderingAllowed(false);
        tableCustomers.setForeground(new java.awt.Color(255, 0, 51));
        tableCustomers.setBackground(Color.BLACK);
        tableCustomers.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableCustomers.setRowHeight(30);
        tableCustomers.setSelectionBackground(new java.awt.Color(255, 0, 51));
        tableCustomers.setBounds(475, 100, 500, 450);
        tableCustomers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent evt) {
                if(tableCustomers.getRowCount() > 0 && tableCustomers.getSelectedRow() >= 0) {
                	int row = tableCustomers.getSelectedRow();
                    textCustomerID.setText(model.getValueAt(row, 0).toString());
                    textCustomerFirstName.setText(model.getValueAt(row, 1).toString());
                    textCustomerLastName.setText(model.getValueAt(row, 2).toString());
                    textCustomerPhone.setText(model.getValueAt(row, 3).toString());
                }
            }
        });
        jScrollPane = new JScrollPane(tableCustomers);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        jScrollPane.setBounds(475, 100, 500, 450);
        jScrollPane.getViewport().setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 20));
        jScrollPane.getViewport().setForeground(new java.awt.Color(255, 0, 51));
        jScrollPane.setForeground(new java.awt.Color(255, 0, 51));
        jScrollPane.getViewport().setBackground(Color.BLACK);
        jScrollPane.setBackground(Color.BLACK);
        jScrollPane.getVerticalScrollBar().setBackground(Color.BLACK);
        jScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.RED;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(Color.BLACK);
                return button;
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createIncreaseButton(orientation);
                button.setBackground(Color.BLACK);
                return button;
            }
            
        });
        contentPane.add(jScrollPane, BorderLayout.CENTER);
        
        // creates the customer file if it does not exist
        if(!UserLogin.customersFile.exists()) {
        	try {
        		UserLogin.customersFile.createNewFile();
				FileWriter fw = new FileWriter(UserLogin.customersFile.getAbsoluteFile());
             	BufferedWriter bw = new BufferedWriter(fw);
             	bw.write(UserLogin.columnsIdentifierCustomers);
             	bw.close();
             	fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        // reads the customers file and sets it in the assigned table
        try {
    		BufferedReader br = new BufferedReader(new FileReader(UserLogin.customersFile));
    		String firstLine = br.readLine().trim();
        	String[] columnsName = firstLine.split(" , ");
        	model.setColumnIdentifiers(columnsName);
        	modelOld.setColumnIdentifiers(columnsName);
        	Object[] tableLines = br.lines().toArray();
        	
        	for(int i = 0; i < tableLines.length; i++) {
        		String line = tableLines[i].toString().trim();
        		String[] dataRow = line.split(" / ");
        		model.addRow(dataRow);
        		modelOld.addRow(dataRow);
        	}
        	br.close();
        	tableCustomers.setModel(model);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}
}
