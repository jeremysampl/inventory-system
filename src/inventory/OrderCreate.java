package inventory;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;
import java.text.*;

public class OrderCreate extends JFrame {
	// declare variables
	private static final long serialVersionUID = 1L;
	static boolean matchID = false;
	static ArrayList<String> arrayListProductsID = new ArrayList<String>();
	static String customerID;
	static String orderID;
	static String productID;
	static String productName;
	static String productQuantity;
	static String productPrice;
	static String productTotal;
	static String orderTotal = "0";
	static String newProductQuantity;
	static String newProductQuantity2;
	static int currentCustomerRow;
	String ordersCustomerPath;
	File ordersCustomerFile;
	String orderPath;
	File orderFile;
	String pathToDelete;
	File fileToDelete;
	DecimalFormat decimal = new DecimalFormat("0.00");
	
	// declare GUI elements
	private JLabel labelOrders;
	private JLabel labelOrderQuantity;
	private JLabel labelTableCustomers;
	private JLabel labelTableProducts;
	private JLabel labelOrderTotal;
	private JTextField textProductQuantity;
	private JTextField textOrderTotal;
	private JButton buttonMenu;
	private JButton buttonAddToOrder;
	private JButton buttonAddOrder;
	private JButton buttonEdit;
	private JButton buttonDelete;
	private JButton buttonClear;
	private javax.swing.JTable tableProducts;
	private javax.swing.JTable tableCustomers;
	private javax.swing.JTable tableOrderAdd;
	private JScrollPane scrollProducts;
	private JScrollPane scrollCustomers;
	private JScrollPane scrollOrderAdd;
	private JPanel contentPane;
	
	public OrderCreate() {
		// create table models needed to store data from the database
		DefaultTableModel modelProducts = new DefaultTableModel(0, 0);
		DefaultTableModel modelCustomers = new DefaultTableModel(0, 0);
		DefaultTableModel modelOrderAdd = new DefaultTableModel(0, 0);
        modelOrderAdd.setColumnIdentifiers(UserLogin.columnsIdentifierOrder.split(" , "));
        DefaultTableModel modelOrders = new DefaultTableModel(0, 0);
		
        // create and set the frame parameters
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(450, 200, 1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        if(Orders.edit) {
        	setTitle("Edit Order");
        } else {
        	setTitle("New Order");
        }
        setVisible(true);
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setVisible(true);
        
        // warns the user that there is an unfinished order if they try to close the program while the order table is not empty
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		if(tableOrderAdd.getRowCount() > 0) {
        			int exit = JOptionPane.showConfirmDialog(null, "There is an unfinshed order. Exit anyways?", "Exit", JOptionPane.YES_NO_OPTION);
                	if(exit == JOptionPane.YES_OPTION) {
                		dispose();
                	}
        		}
        	}
        });
        
        // create and set GUI elements
        labelOrders = new JLabel("New Order");
        // if statement to change the label if editing an order
        if(Orders.edit) {
        	labelOrders.setText("Edit Order");
        }
        labelOrders.setForeground(Color.RED);
        labelOrders.setFont(new Font("Times New Roman", Font.BOLD, 60));
        labelOrders.setBounds(350, 0, 600, 75);
        contentPane.add(labelOrders);
        
        labelOrderQuantity = new JLabel("Quantity");
        labelOrderQuantity.setForeground(Color.RED);
        labelOrderQuantity.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        labelOrderQuantity.setBounds(180, 300, 200, 50);
        contentPane.add(labelOrderQuantity);
        
        labelTableCustomers = new JLabel("Customers");
        labelTableCustomers.setForeground(Color.RED);
        labelTableCustomers.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelTableCustomers.setBounds(160, 50, 300, 50);
        contentPane.add(labelTableCustomers);
        
        labelTableProducts = new JLabel("Products");
        labelTableProducts.setForeground(Color.RED);
        labelTableProducts.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelTableProducts.setBounds(655, 50, 300, 50);
        contentPane.add(labelTableProducts);
        
        labelOrderTotal = new JLabel("Total");
        labelOrderTotal.setForeground(Color.RED);
        labelOrderTotal.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelOrderTotal.setBounds(785, 375, 200, 50);
        contentPane.add(labelOrderTotal);
        
        textOrderTotal = new JTextField("$0.00");
        textOrderTotal.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        textOrderTotal.setForeground(Color.RED);
        textOrderTotal.setBackground(Color.BLACK);
        textOrderTotal.setBounds(700, 425, 275, 50);
        textOrderTotal.setEditable(false);
        textOrderTotal.setHorizontalAlignment(JTextField.CENTER);
        contentPane.add(textOrderTotal);
        
        textProductQuantity = new JTextField();
        textProductQuantity.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        textProductQuantity.setBounds(335, 300, 200, 50);
        contentPane.add(textProductQuantity);

        buttonMenu = new JButton("Orders");
        buttonMenu.setFont(new Font("Tahoma", Font.PLAIN, 30));
        buttonMenu.setBounds(10, 10, 150, 75);
        buttonMenu.setForeground(new java.awt.Color(255, 0, 51));
        buttonMenu.setBackground(Color.BLACK);
        buttonMenu.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// if statement to check if the table is empty or not
        		if(tableOrderAdd.getRowCount() > 0) {
        			// if the table is not empty, prompts the user to make sure they want to cancel editing the order
        			int exit = JOptionPane.showConfirmDialog(null, "There is an unfinshed order. Return to orders anyways?", "Exit?", JOptionPane.YES_NO_OPTION);
                	if(exit == JOptionPane.YES_OPTION) {
                		// goes back to orders page
                		dispose();
                		new Orders();
                		Orders.edit = false;
                	}
        		} else {
        			// goes back to orders page
        			dispose();
            		new Orders();
            		Orders.edit = false;
        		}
        	}
        });
        contentPane.add(buttonMenu);
        
        buttonAddToOrder = new JButton("Add");
        buttonAddToOrder.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonAddToOrder.setBounds(25, 325, 125, 50);
        buttonAddToOrder.setForeground(new java.awt.Color(255, 0, 51));
        buttonAddToOrder.setBackground(Color.BLACK);
        buttonAddToOrder.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// if statement to check if the user selected a product to add
        		if(tableProducts.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a product to add to the order.");
        		} else {
        			// gets values needed to add to the table and stores them in their assigned variables
        			productID = String.valueOf(tableProducts.getValueAt(tableProducts.getSelectedRow(), 0));
            		productName = String.valueOf(tableProducts.getValueAt(tableProducts.getSelectedRow(), 2));
            		productQuantity = textProductQuantity.getText().trim();
            		productPrice = String.valueOf(tableProducts.getValueAt(tableProducts.getSelectedRow(), 4));
            		
            		// if statement to check if the quantity is a valid positive number in order to avoid errors
            		if(productQuantity.isEmpty() || Objects.equals(productQuantity, productQuantity.replaceAll("\\s+","")) == false || productQuantity.chars().allMatch(Character::isDigit) == false || Integer.valueOf(productQuantity) < 1) {
            			JOptionPane.showMessageDialog(null, "Please enter a valid quantity.");
            		} else {
            			// for loop and if statement to go through every row and check if the product is already in the order
            			for(int i = 0; i < modelOrderAdd.getRowCount(); i++) {
                        	if(Objects.equals(productID, modelOrderAdd.getValueAt(i, 0))) {
                        		matchID = true;
                        		i = modelOrderAdd.getRowCount();
                        	}
                        }
            			
            			// if statement to decide whether the product will be added to the order based on the boolean variable
            			if(matchID) {
            				matchID = false;
                        	JOptionPane.showMessageDialog(null, "Product is already in the order.");
            			} else {
            				// calculate the product total and new inventory quantity of the product
            				productTotal = decimal.format(Double.valueOf(productPrice) * Double.valueOf(productQuantity));
                		    newProductQuantity = String.valueOf(Integer.valueOf(String.valueOf(tableProducts.getValueAt(tableProducts.getSelectedRow(), 1))) - Integer.valueOf(productQuantity)); 
                		    // if statement to check if the quantity trying to take from inventory is greater than the product quanitity currently in inventory
                		    if(Integer.valueOf(newProductQuantity) < 0) {
                		    	JOptionPane.showMessageDialog(null, "Not enough items in inventory.");
                		    } else {
                		    	// changes the new product quantity in inventory, calculates the order total and shows the new total
                		    	tableProducts.setValueAt(newProductQuantity, tableProducts.getSelectedRow(), 1);
                    		    orderTotal = decimal.format(Double.valueOf(orderTotal) + Double.valueOf(productTotal));
                    		    textOrderTotal.setText("$" + orderTotal);
                    		    
                    		    // adds the data to the order table
                    			String[] dataRow = {productID, productName, productQuantity, productPrice, productTotal};
                        		modelOrderAdd.addRow(dataRow);
                        		tableOrderAdd.setRowSelectionInterval(tableOrderAdd.getRowCount() - 1, tableOrderAdd.getRowCount() - 1);
                        		textProductQuantity.setText("");
                        		JOptionPane.showMessageDialog(null, "Successfully added the product to the order.");
                		    }
            			}
        			}
                }
        	}
        });
        contentPane.add(buttonAddToOrder);
        
        buttonAddOrder = new JButton("Create Order");
        // changes the text of the button if editing an order
        if(Orders.edit == true) {
        	buttonAddOrder.setText("Edit Order");
        }
        buttonAddOrder.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonAddOrder.setBounds(700, 500, 275, 50);
        buttonAddOrder.setForeground(new java.awt.Color(255, 0, 51));
        buttonAddOrder.setBackground(Color.BLACK);
        buttonAddOrder.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// if statements to check if a customer is selected and if there is at least one item in the order
        		if(tableCustomers.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a customer to designate the order for.");
        		} else if(tableOrderAdd.getRowCount() == 0) {
        			JOptionPane.showMessageDialog(null, "Please add at least one product to the order.");
        		} else {
        			// try/catch because of the file writer
        			try {
        				// if statements to decide how the order ID will be chosen
        				if(Orders.edit) {
        					orderID = Orders.orderID;
        				} else if(modelOrders.getRowCount() > 0) {
        					orderID = String.valueOf(Integer.valueOf(String.valueOf(modelOrders.getValueAt(modelOrders.getRowCount() - 1, 0))) + 1);
        				} else {
        					orderID = "1";
        				}
        				
        				// if statement that edits the existing order if editing an order or adds a new one if creating a new order
        				if(Orders.edit) {
        					int row = Orders.rowsSelected[0];
        					modelOrders.setValueAt(orderID, row, 0);
        					modelOrders.setValueAt(tableCustomers.getValueAt(tableCustomers.getSelectedRow(), 0), row, 1);
        					modelOrders.setValueAt(tableCustomers.getValueAt(tableCustomers.getSelectedRow(), 1), row, 2);
        					modelOrders.setValueAt(tableCustomers.getValueAt(tableCustomers.getSelectedRow(), 2), row, 3);
        					modelOrders.setValueAt(tableCustomers.getValueAt(tableCustomers.getSelectedRow(), 3), row, 4);
        					modelOrders.setValueAt(orderTotal, row, 5);
        				} else {
                     	Object[] row = {orderID, tableCustomers.getValueAt(tableCustomers.getSelectedRow(), 0), tableCustomers.getValueAt(tableCustomers.getSelectedRow(), 1), tableCustomers.getValueAt(tableCustomers.getSelectedRow(), 2), tableCustomers.getValueAt(tableCustomers.getSelectedRow(), 3), orderTotal};
        				modelOrders.addRow(row);
        				}
        				
        				// file writer to update the user's orders file
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
                     	
                     	// file writer to update the products' file (update quantities)
                     	FileWriter wf = new FileWriter(UserLogin.productsFile.getAbsoluteFile());
                     	BufferedWriter wb = new BufferedWriter(wf);
                     	
                     	wb.write(UserLogin.columnsIdentifierProducts);
                     	for(int i = 0; i < modelProducts.getRowCount(); i++) {
                     		for(int j = 0; j < modelProducts.getColumnCount(); j++) {
                     			wb.write((String)modelProducts.getValueAt(i, j));
                     			if(j < modelProducts.getColumnCount() - 1) {
                     				wb.write(" / ");
                     			}
                     		}
                     		wb.write("\n");
                     	}
                     	wb.close();
                     	wf.close();
                    } catch(Exception ex) {
                    	ex.printStackTrace();
                    }
        			
        			// checks if the customer's file directory is created
        			customerID = String.valueOf(tableCustomers.getValueAt(tableCustomers.getSelectedRow(), 0));
        			ordersCustomerPath = String.format("%s\\Database\\%s\\Orders\\%s", UserLogin.databasePath, UserLogin.id, customerID);
        			ordersCustomerFile = new File(ordersCustomerPath);
        			if(!ordersCustomerFile.exists()) {
        				ordersCustomerFile.mkdir();
        			}
        			
        			// creates the order file if it does not already exist
        			orderPath = String.format("%s\\Database\\%s\\Orders\\%s\\%s.txt", UserLogin.databasePath, UserLogin.id, customerID, orderID);
        			orderFile = new File(orderPath);
        			if(!orderFile.exists()) {
        				try {
							orderFile.createNewFile();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
        			}
        			
        			// if statement to check if the customer the order is allocated for is the same as the original customer for that order
        			// if so, deletes the old order file
        			if(!customerID.equals(Orders.customerID)) {
        				pathToDelete = String.format("%s\\Database\\%s\\Orders\\%s\\%s.txt", UserLogin.databasePath, UserLogin.id, Orders.customerID, Orders.orderID);
            			fileToDelete = new File(pathToDelete);
            			if(fileToDelete.exists()) {
            				fileToDelete.delete();
            			}
        			}
        			
        			// file writer to add the new order to the database
        			try {
                     	FileWriter fw = new FileWriter(orderFile.getAbsoluteFile());
                     	BufferedWriter bw = new BufferedWriter(fw);
                     	
                     	bw.write(UserLogin.columnsIdentifierOrder);
                     	for(int i = 0; i < tableOrderAdd.getRowCount(); i++) {
                     		for(int j = 0; j < tableOrderAdd.getColumnCount(); j++) {
                     			bw.write((String)tableOrderAdd.getModel().getValueAt(i, j));
                     			if(j < tableOrderAdd.getColumnCount() - 1) {
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
        			
        			orderTotal = "0";
        			// if statement to decide whether to return to the orders based on if the user was editing an existing order or not
        			if(Orders.edit) {
        				// returns to the orders
        				dispose();
        				new Orders();
        				JOptionPane.showMessageDialog(null, "Successfully edited the order.");
        			} else {
        				// sets up the tables to be ready to add another order
        				textProductQuantity.setText("");
        				while(modelOrderAdd.getRowCount() > 0) {
        					modelOrderAdd.removeRow(0);
        				}
        				tableProducts.getSelectionModel().clearSelection();
        				tableCustomers.getSelectionModel().clearSelection();
        				JOptionPane.showMessageDialog(null, "Successfully created the order.");	
        			}
                }
        	}
        });
        contentPane.add(buttonAddOrder);
        
        buttonEdit = new JButton("Edit");
        buttonEdit.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonEdit.setBounds(25, 400, 125, 50);
        buttonEdit.setForeground(new java.awt.Color(255, 0, 51));
        buttonEdit.setBackground(Color.BLACK);
        buttonEdit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// if statements to check if the user has selected a product in the order and in the products table to edit
        		if(tableOrderAdd.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a product in the order to edit.");
        		} else if(tableProducts.getSelectedRow() < 0) { 
        			JOptionPane.showMessageDialog(null, "Please select a product.");
        		} else {
        			// stores needed values in assigned variables
        			productID = String.valueOf(tableProducts.getValueAt(tableProducts.getSelectedRow(), 0));
            		productName = String.valueOf(tableProducts.getValueAt(tableProducts.getSelectedRow(), 2));
            		productQuantity = textProductQuantity.getText().trim();
            		productPrice = String.valueOf(tableProducts.getValueAt(tableProducts.getSelectedRow(), 4));
            		
            		// if statement to check if the quantity entered is a valid quantity
            		if(productQuantity.isEmpty() || Objects.equals(productQuantity, productQuantity.replaceAll("\\s+","")) == false || productQuantity.chars().allMatch(Character::isDigit) == false || Integer.valueOf(productQuantity) < 1) {
            			JOptionPane.showMessageDialog(null, "Please enter a valid quantity.");
            		} else {
            			// for loop and if statement to go through every row of the table in order to check if the ID already exists
            			for(int i = 0; i < modelOrderAdd.getRowCount(); i++) {
                        	if(Objects.equals(productID, modelOrderAdd.getValueAt(i, 0)) && i != tableOrderAdd.getSelectedRow()) {
                        		matchID = true;
                        		i = modelOrderAdd.getRowCount();
                        	}
                        }
                        
            			// if statement that checks if the loop found an ID match or not
                        if(matchID) {
                        	matchID = false;
                        	JOptionPane.showMessageDialog(null, "Product is already in the order.");
                        } else {
                        	
                        	productTotal = decimal.format(Double.valueOf(productPrice) * Double.valueOf(productQuantity));
                        	
                        	// if statement to decide whether the product the user is trying to edit to is the same as the previous product
                        	// if not, different calculations to properly count the stock is necessary
                		    int previousProductRow = Integer.valueOf(String.valueOf(arrayListProductsID.indexOf(tableOrderAdd.getValueAt(tableOrderAdd.getSelectedRow(), 0))));
                		    if(tableProducts.getSelectedRow() == previousProductRow) {
                		    	newProductQuantity = String.valueOf(Integer.valueOf(String.valueOf(tableProducts.getValueAt(tableProducts.getSelectedRow(), 1))) + Integer.valueOf(String.valueOf(modelOrderAdd.getValueAt(tableOrderAdd.getSelectedRow(), 2))) - Integer.valueOf(productQuantity)); 
                		    } else {
                		    	newProductQuantity = String.valueOf(Integer.valueOf(String.valueOf(tableProducts.getValueAt(tableProducts.getSelectedRow(), 1))) - Integer.valueOf(productQuantity)); 
                		    	newProductQuantity2 = String.valueOf(Integer.valueOf(String.valueOf(tableProducts.getValueAt(arrayListProductsID.indexOf(tableOrderAdd.getValueAt(tableOrderAdd.getSelectedRow(), 0)), 1))) + Integer.valueOf(String.valueOf(modelOrderAdd.getValueAt(tableOrderAdd.getSelectedRow(), 2)))); 
                		    }
                		    
                		    // if statement to check if the user has enough of the product chosen
                		    if(Integer.valueOf(newProductQuantity) < 0) {
                		    	JOptionPane.showMessageDialog(null, "Not enough items in inventory.");
                		    } else {
                		    	// sets the new product quantities in the products table
                		    	tableProducts.setValueAt(newProductQuantity, tableProducts.getSelectedRow(), 1);
                		    	if(tableProducts.getSelectedRow() != previousProductRow) {
                		    		tableProducts.setValueAt(newProductQuantity2, previousProductRow, 1);
                		    	}
                		    	
                		    	// sets the new order total and displays it
                		    	orderTotal = decimal.format(Double.valueOf(orderTotal) - Double.valueOf(String.valueOf(tableOrderAdd.getValueAt(tableOrderAdd.getSelectedRow(), 4))) + Double.valueOf(productTotal));
                    		    textOrderTotal.setText("$" + orderTotal);
                		    	
                    		    // updates the values of the product selected in the table
                		    	modelOrderAdd.setValueAt(productID, tableOrderAdd.getSelectedRow(), 0);
                        		modelOrderAdd.setValueAt(productName, tableOrderAdd.getSelectedRow(), 1);
                        		modelOrderAdd.setValueAt(productQuantity, tableOrderAdd.getSelectedRow(), 2);
                        		modelOrderAdd.setValueAt(productPrice, tableOrderAdd.getSelectedRow(), 3);
                        		modelOrderAdd.setValueAt(productTotal, tableOrderAdd.getSelectedRow(), 4);
                        		JOptionPane.showMessageDialog(null, "Successfully edited the product in the order.");
                		    }
                        }
                    }
                }
        	}
        });
        contentPane.add(buttonEdit);
        
        buttonDelete = new JButton("Remove");
        buttonDelete.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        buttonDelete.setBounds(25, 475, 125, 50);
        buttonDelete.setForeground(new java.awt.Color(255, 0, 51));
        buttonDelete.setBackground(Color.BLACK);
        buttonDelete.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// if statement to check if the user selected at least one product in the order
        		if(tableOrderAdd.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a product in the order to remove.");
        		} else {
        			// confirm dialog to make sure that the user wants to deleted the selected product(s) from the order
        			int a = JOptionPane.showConfirmDialog(null, "Are you sure you would like to remove the selected product(s) from the order?", "Remove Product(s)", JOptionPane.YES_NO_OPTION);
        			if (a == JOptionPane.YES_OPTION) {
        				// for loop to go through every product the user has selected for removal
            			int[] selectedRows = tableOrderAdd.getSelectedRows();
                		for(int i = 0; i < selectedRows.length; i++) {
                			// formats and displays the new total
                			orderTotal = decimal.format(Double.valueOf(orderTotal) - Double.valueOf(String.valueOf(tableOrderAdd.getValueAt(selectedRows[0], 4))));
                		    textOrderTotal.setText("$" + orderTotal);
                		    
                		    // adds the stock back into inventory and then removes the row from the order's table
                			newProductQuantity = String.valueOf(Integer.valueOf(String.valueOf(tableProducts.getValueAt(arrayListProductsID.indexOf(tableOrderAdd.getValueAt(selectedRows[0], 0)), 1))) + Integer.valueOf(String.valueOf(modelOrderAdd.getValueAt(selectedRows[0], 2))));
                		    tableProducts.setValueAt(newProductQuantity, arrayListProductsID.indexOf(tableOrderAdd.getValueAt(selectedRows[0], 0)), 1);
                			modelOrderAdd.removeRow(selectedRows[0]);
                		}
                		// empty any selections to give it the feel like something was removed
                		textProductQuantity.setText("");
                		tableProducts.getSelectionModel().clearSelection();
            			JOptionPane.showMessageDialog(null, "Successfully removed the selected product(s) from the order.");
                    }
        		}
        	}
        });
        contentPane.add(buttonDelete);
        
        buttonClear = new JButton("Clear");
        buttonClear.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonClear.setBounds(550, 300, 125, 50);
        buttonClear.setForeground(new java.awt.Color(255, 0, 51));
        buttonClear.setBackground(Color.BLACK);
        buttonClear.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// clears the text box
        		textProductQuantity.setText("");
        	}
        });
        contentPane.add(buttonClear);
        
        tableProducts = new JTable(modelProducts) {
        	private static final long serialVersionUID = 1L;
        	public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
             }
        };
        tableProducts.setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 18));
        tableProducts.getTableHeader().setForeground(Color.BLACK);
        tableProducts.getTableHeader().setBackground(Color.RED);
        tableProducts.getTableHeader().setReorderingAllowed(false);
        tableProducts.setForeground(new java.awt.Color(255, 0, 51));
        tableProducts.setBackground(Color.BLACK);
        tableProducts.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableProducts.setRowHeight(30);
        tableProducts.setSelectionBackground(new java.awt.Color(255, 0, 51));
        tableProducts.setBounds(475, 100, 500, 450);
        tableProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        scrollProducts = new JScrollPane(tableProducts);
        scrollProducts.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        scrollProducts.setBounds(500, 100, 475, 200);
        scrollProducts.getViewport().setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 18));
        scrollProducts.getViewport().setForeground(new java.awt.Color(255, 0, 51));
        scrollProducts.setForeground(new java.awt.Color(255, 0, 51));
        scrollProducts.getViewport().setBackground(Color.BLACK);
        scrollProducts.setBackground(Color.BLACK);
        scrollProducts.getVerticalScrollBar().setBackground(Color.BLACK);
        scrollProducts.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
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
        contentPane.add(scrollProducts, BorderLayout.CENTER);
        
        tableCustomers = new JTable(modelCustomers) {
        	private static final long serialVersionUID = 1L;
        	public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
             }
        };
        tableCustomers.setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 18));
        tableCustomers.getTableHeader().setForeground(Color.BLACK);
        tableCustomers.getTableHeader().setBackground(Color.RED);
        tableCustomers.getTableHeader().setReorderingAllowed(false);
        tableCustomers.setForeground(new java.awt.Color(255, 0, 51));
        tableCustomers.setBackground(Color.BLACK);
        tableCustomers.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableCustomers.setRowHeight(30);
        tableCustomers.setSelectionBackground(new java.awt.Color(255, 0, 51));
        tableCustomers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        scrollCustomers = new JScrollPane(tableCustomers);
        scrollCustomers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        scrollCustomers.setBounds(25, 100, 450, 200);
        scrollCustomers.getViewport().setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 18));
        scrollCustomers.getViewport().setForeground(new java.awt.Color(255, 0, 51));
        scrollCustomers.setForeground(new java.awt.Color(255, 0, 51));
        scrollCustomers.getViewport().setBackground(Color.BLACK);
        scrollCustomers.setBackground(Color.BLACK);
        scrollCustomers.getVerticalScrollBar().setBackground(Color.BLACK);
        scrollCustomers.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
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
        contentPane.add(scrollCustomers, BorderLayout.CENTER);
        
        tableOrderAdd = new JTable(modelOrderAdd) {
        	private static final long serialVersionUID = 1L;
        	public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
             }
        };
        tableOrderAdd.setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 18));
        tableOrderAdd.getTableHeader().setForeground(Color.BLACK);
        tableOrderAdd.getTableHeader().setBackground(Color.RED);
        tableOrderAdd.getTableHeader().setReorderingAllowed(false);
        tableOrderAdd.setForeground(new java.awt.Color(255, 0, 51));
        tableOrderAdd.setBackground(Color.BLACK);
        tableOrderAdd.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableOrderAdd.setRowHeight(30);
        tableOrderAdd.setSelectionBackground(new java.awt.Color(255, 0, 51));
        tableOrderAdd.setBounds(475, 100, 500, 450);
        tableOrderAdd.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent evt) {
        		// if statement to check if there is at least a row in to table to avoid errors
                if(tableOrderAdd.getRowCount() > 0 && tableOrderAdd.getSelectedRow() >= 0) {
                	int[] rows = tableOrderAdd.getSelectedRows();
                	// if statement to only perform the actions inside the brackets if only one row is selected
                	if(rows.length == 1) {
                		// automatically selects the product in inventory associated with the product in the order
                		int tableProductsRow = arrayListProductsID.indexOf(modelOrderAdd.getValueAt(tableOrderAdd.getSelectedRow(), 0));
                		tableProducts.setRowSelectionInterval(tableProductsRow, tableProductsRow);
                	}
                }
            }
        });
        scrollOrderAdd = new JScrollPane(tableOrderAdd);
        scrollOrderAdd.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        scrollOrderAdd.setBounds(175, 350, 500, 200);
        scrollOrderAdd.getViewport().setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 18));
        scrollOrderAdd.getViewport().setForeground(new java.awt.Color(255, 0, 51));
        scrollOrderAdd.setForeground(new java.awt.Color(255, 0, 51));
        scrollOrderAdd.getViewport().setBackground(Color.BLACK);
        scrollOrderAdd.setBackground(Color.BLACK);
        scrollOrderAdd.getVerticalScrollBar().setBackground(Color.BLACK);
        scrollOrderAdd.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
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
        contentPane.add(scrollOrderAdd, BorderLayout.CENTER);
        
        // checks if the products file exists, then creates it if not
        if(!UserLogin.productsFile.exists()) {
        	try {
				UserLogin.productsFile.createNewFile();
				FileWriter fw = new FileWriter(UserLogin.productsFile.getAbsoluteFile());
             	BufferedWriter bw = new BufferedWriter(fw);
             	bw.write(UserLogin.columnsIdentifierProducts);
             	bw.close();
             	fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        // reads the products file and sets it in the associated table
        try {
    		BufferedReader br = new BufferedReader(new FileReader(UserLogin.productsFile));
    		String firstLine = br.readLine().trim();
        	String[] columnsName = firstLine.split(" , ");
        	modelProducts.setColumnIdentifiers(columnsName);
        	Object[] tableLines = br.lines().toArray();
        	
        	for(int i = 0; i < tableLines.length; i++) {
        		String line = tableLines[i].toString().trim();
        		String[] dataRow = line.split(" / ");
        		modelProducts.addRow(dataRow);
        		arrayListProductsID.add(dataRow[0]);
        	}
        	br.close();
        	tableProducts.setModel(modelProducts);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		
    	}
        
        // checks if the customers file exists, then creates it if not
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
        // reads the customers file and sets it in the associated table
        try {
    		BufferedReader br = new BufferedReader(new FileReader(UserLogin.customersFile));
    		String firstLine = br.readLine().trim();
        	String[] columnsName = firstLine.split(" , ");
        	modelCustomers.setColumnIdentifiers(columnsName);
        	Object[] tableLines = br.lines().toArray();
        	
        	for(int i = 0; i < tableLines.length; i++) {
        		String line = tableLines[i].toString().trim();
        		String[] dataRow = line.split(" / ");
        		modelCustomers.addRow(dataRow);
        	}
        	br.close();
        	tableCustomers.setModel(modelCustomers);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		
    	}
        
        // checks if the orders file exists and creates it if not
        if(!UserLogin.ordersFile.exists()) {
        	try {
				UserLogin.ordersFile.createNewFile();
				FileWriter fw = new FileWriter(UserLogin.ordersFile.getAbsoluteFile());
             	BufferedWriter bw = new BufferedWriter(fw);
             	bw.write(UserLogin.columnsIdentifierOrders);
             	bw.close();
             	fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        // reads the orders file and puts it into the associated table model which is not visible to the user
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
        
        // only does the calculations below if editing an order
        if(Orders.edit) {
        	// for loop to go through every table and if statement to check for a customer match
        	// automatically selects the original customer of the order
        	for(int j = 0; j < tableCustomers.getRowCount(); j++) { 
        		if(tableCustomers.getValueAt(j, 0).equals(Orders.customerID)) {
        			tableCustomers.setRowSelectionInterval(j, j);
        			j = tableCustomers.getRowCount();
        		}
        	}
        	
        	// sets the order total to the previous total
        	textOrderTotal.setText("$" + Orders.orderTotal);
        	orderTotal = Orders.orderTotal;
        	
        	// reads the order file and displays it inside the associated table
        	orderPath = String.format("%s\\Database\\%s\\Orders\\%s\\%s.txt", UserLogin.databasePath, UserLogin.id, Orders.customerID, Orders.orderID);
			orderFile = new File(orderPath);
        	if(orderFile.exists()) {
        		try {
            		BufferedReader br = new BufferedReader(new FileReader(orderFile));
            		String firstLine = br.readLine().trim();
                	String[] columnsName = firstLine.split(" , ");
                	modelOrderAdd.setColumnIdentifiers(columnsName);
                	Object[] tableLines = br.lines().toArray();
                	
                	for(int i = 0; i < tableLines.length; i++) {
                		String line = tableLines[i].toString().trim();
                		String[] dataRow = line.split(" / ");
                		modelOrderAdd.addRow(dataRow);
                	}
                	br.close();
                	tableOrderAdd.setModel(modelOrderAdd);
            	} catch (Exception ex) {
            		ex.printStackTrace();
            	}
        	} else {
        		// if file does not exists for some reason, simply sets the column identifiers
        		String[] columnsName = UserLogin.columnsIdentifierOrder.split(" , ");
        		modelOrderAdd.setColumnIdentifiers(columnsName);
        		tableOrderAdd.setModel(modelOrderAdd);
        	}
        }
	}
}