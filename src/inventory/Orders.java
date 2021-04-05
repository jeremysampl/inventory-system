package inventory;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;

public class Orders extends JFrame {
	// declare variables
	private static final long serialVersionUID = 1L;
	static boolean matchID = false;
	static boolean edit = false;
	static ArrayList<String> arrayListProductsID = new ArrayList<String>();
	static String orderTotal;
	static String orderID;
	static String customerID;
	static int[] rowsSelected;
	static String[] customers;
	static ArrayList<String> arrayListCustomers = new ArrayList<String>();
	static ArrayList<String> arrayListOrdersIDs = new ArrayList<String>();
	String ordersCustomerPath;
	File ordersCustomerFile;
	String orderPath;
	File orderFile;
	
	// declare GUI elements
	private JLabel labelOrders;
	private JLabel labelCustomers;
	private JButton buttonMenu;
	private JButton buttonAdd;
	private JButton buttonEdit;
	private JButton buttonDelete;
	private JComboBox<String> boxCustomers;
	private javax.swing.JTable tableOrders;
	private javax.swing.JTable tableOrder;
	private JScrollPane scrollOrders;
	private JScrollPane scrollOrder;
	private JPanel contentPane;
	
	public Orders() {
		// creates the table models needed to access and edit information from the database
		DefaultTableModel modelOrders = new DefaultTableModel(0, 0);
		DefaultTableModel modelOrder = new DefaultTableModel(0, 0);
		modelOrder.setColumnIdentifiers(UserLogin.columnsIdentifierOrder.split(" , "));
		DefaultTableModel modelCustomers = new DefaultTableModel(0, 0);
		DefaultTableModel modelProducts = new DefaultTableModel(0, 0);
		
		// creates and sets frame and options
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 200, 1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Orders");
        setVisible(true);
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setVisible(true);
        
        // create and set GUI elements
        labelOrders = new JLabel("Orders");
        labelOrders.setForeground(Color.RED);
        labelOrders.setFont(new Font("Times New Roman", Font.BOLD, 50));
        labelOrders.setBounds(425, 0, 400, 40);
        contentPane.add(labelOrders);
        
        labelCustomers = new JLabel("Customers");
        labelCustomers.setForeground(Color.RED);
        labelCustomers.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        labelCustomers.setBounds(825, 0, 125, 25);
        contentPane.add(labelCustomers);
        
        boxCustomers = new JComboBox<String>();
        boxCustomers.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        boxCustomers.setBounds(775, 25, 200, 25);
        boxCustomers.setForeground(new java.awt.Color(255, 0, 51));;
        boxCustomers.setBackground(Color.BLACK);
        boxCustomers.setUI(new BasicComboBoxUI() {
        	// custom combo box colors and remove the buttons
        	@Override
            protected ComboPopup createPopup() {
                return new BasicComboPopup(comboBox) {
                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane scroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                        scroller.setForeground(new java.awt.Color(255, 0, 51));
                        scroller.setBackground(Color.BLACK);
                        scroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                        	@Override
                            protected void configureScrollBarColors() {
                                this.thumbColor = Color.RED;
                            }
                        	@Override
                            protected JButton createDecreaseButton(int orientation) {
                                return createZeroButton();
                            }
                            @Override
                            protected JButton createIncreaseButton(int orientation) {
                                return createZeroButton();
                            }
                            @Override
                            public Dimension getPreferredSize(JComponent c) {
                                return new Dimension(10, super.getPreferredSize(c).height);
                            }
                            private JButton createZeroButton() {
                                return new JButton() {
                                    @Override
                                    public Dimension getMinimumSize() {
                                        return new Dimension(new Dimension(0, 0));
                                    }
                                    @Override
                                    public Dimension getPreferredSize() {
                                        return new Dimension(new Dimension(0, 0));
                                    }
                                    @Override
                                    public Dimension getMaximumSize() {
                                        return new Dimension(new Dimension(0, 0));
                                    }
                                };
                            }
                        });
                        return scroller;
                    }
                };
            }
        });
        boxCustomers.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// removes everything in the orders table
        		while(modelOrders.getRowCount() > 0) {
        			modelOrders.removeRow(0);
        		}
        		
        		// read the orders file in a try/catch (in order to make sure everything is in the orders table)
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
        		
        		// checks if the customer selected is a singular customer
        		if(boxCustomers.getSelectedIndex() > 0) {
        			// gets the customerID of the customer selected
        			customerID = String.valueOf(modelCustomers.getValueAt(boxCustomers.getSelectedIndex() - 1, 0));
        			// for loop and if statement to go through all rows of the table and remove any orders that are not for the specified customers
        			for(int i = 0; i < modelOrders.getRowCount();) {
        				if(!customerID.equals(modelOrders.getValueAt(i, 1))) {
        					modelOrders.removeRow(i);
        				} else {
        					++i;
        				}
        			}
        		}
        		tableOrders.getSelectionModel().clearSelection();
        	}
        });
        contentPane.add(boxCustomers);

        buttonMenu = new JButton("Menu");
        buttonMenu.setFont(new Font("Tahoma", Font.PLAIN, 25));
        buttonMenu.setBounds(0, 0, 100, 50);
        buttonMenu.setForeground(new java.awt.Color(255, 0, 51));
        buttonMenu.setBackground(Color.BLACK);
        buttonMenu.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// goes back to the main menu
        		dispose();
        		new UserHome();
        	}
        });
        contentPane.add(buttonMenu);
        
        buttonAdd = new JButton("Add Order");
        buttonAdd.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonAdd.setBounds(25, 500, 300, 50);
        buttonAdd.setForeground(new java.awt.Color(255, 0, 51));
        buttonAdd.setBackground(Color.BLACK);
        buttonAdd.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// goes to the create order screen
                dispose();
                new OrderCreate();
        	}
        });
        contentPane.add(buttonAdd);
        
        buttonEdit = new JButton("Edit Order");
        buttonEdit.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonEdit.setBounds(350, 500, 300, 50);
        buttonEdit.setForeground(new java.awt.Color(255, 0, 51));
        buttonEdit.setBackground(Color.BLACK);
        buttonEdit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// if statement to check if there is only one row selected
        		if(tableOrders.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select an order to edit.");
        		} else if(tableOrders.getSelectedRows().length != 1) {
        			JOptionPane.showMessageDialog(null, "Can only edit one order at a time.");
        		} else {
        			// goes to create order screen, but to edit the order selected
        			edit = true;
            		dispose();
                    new OrderCreate();
        		}
        	}
        });
        contentPane.add(buttonEdit);
        
        buttonDelete = new JButton("Delete Order");
        buttonDelete.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonDelete.setBounds(675, 500, 300, 50);
        buttonDelete.setForeground(new java.awt.Color(255, 0, 51));
        buttonDelete.setBackground(Color.BLACK);
        buttonDelete.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// checks if the user has selected at least one order to delete
        		if(tableOrders.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select an order to delete.");
        		} else {
        			// confirm dialog to make sure the user really wants to deleted the selected order(s)
        			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected order(s)?", "Delete Order", JOptionPane.YES_NO_OPTION);
        			if (confirm == JOptionPane.YES_OPTION) {
        				// confirm dialog to allow the user to choose whether the items in the order will get put back into the inventory
        				int a = JOptionPane.showConfirmDialog(null, "Is it due to a cancellation? (if so, items will return back to inventory)", "Remove Order(s) Reason", JOptionPane.YES_NO_OPTION);
        				int[] selectedRows = tableOrders.getSelectedRows();
        				// if the user wants items to go back to inventory
        				if (a == JOptionPane.YES_OPTION) {
        					// for loop to remove anything in the order viewer table
            				for(int i = 0; i < selectedRows.length; i++) {
            					while(modelOrder.getRowCount() > 0) {
                        			modelOrder.removeRow(0);
                        		}
                        		
            					// creates the customer orders' directory if it does not exist 
                        		customerID = String.valueOf(tableOrders.getValueAt(selectedRows[i], 1));
                        		ordersCustomerPath = String.format("%s\\Database\\%s\\Orders\\%s", UserLogin.databasePath, UserLogin.id, customerID);
                    			ordersCustomerFile = new File(ordersCustomerPath);
                    			if(!ordersCustomerFile.exists()) {
                    				ordersCustomerFile.mkdir();
                    			}
                    			
                    			orderID = String.valueOf(tableOrders.getValueAt(selectedRows[i], 0));
                    			orderPath = String.format("%s\\Database\\%s\\Orders\\%s\\%s.txt", UserLogin.databasePath, UserLogin.id, customerID, orderID);
                    			orderFile = new File(orderPath);
                        		// rewrites the order table in order to use it to get the quantity values to add it back to the inventory
                    			if(orderFile.exists()) {
                    				try {
                                		BufferedReader br = new BufferedReader(new FileReader(orderFile));
                                		String firstLine = br.readLine().trim();
                                    	String[] columnsName = firstLine.split(" , ");
                                    	modelOrder.setColumnIdentifiers(columnsName);
                                    	Object[] tableLines = br.lines().toArray();
                                    	
                                    	for(int j = 0; j < tableLines.length; j++) {
                                    		String line = tableLines[j].toString().trim();
                                    		String[] dataRow = line.split(" / ");
                                    		modelOrder.addRow(dataRow);
                                    	}
                                    	br.close();
                                    	tableOrder.setModel(modelOrder);
                                	} catch (Exception ex) {
                                		ex.printStackTrace();
                                	}
                    			}
                    			// for loop to add all the products in the order back to the inventory
            					for(int j = 0; j < modelOrder.getRowCount(); j++) {
                        			String newProductQuantity = String.valueOf(Integer.valueOf(String.valueOf(modelProducts.getValueAt(arrayListProductsID.indexOf(tableOrder.getValueAt(j, 0)), 1))) + Integer.valueOf(String.valueOf(modelOrder.getValueAt(j, 2))));
                        		    modelProducts.setValueAt(newProductQuantity, arrayListProductsID.indexOf(tableOrder.getValueAt(j, 0)), 1);
            					}
            					// deletes the file if it exists
                    			if(orderFile.exists()) {
                    				orderFile.delete();
                    			}
            				}
            				// rewrites the products file to update the quantities
            				try {
                             	FileWriter fw = new FileWriter(UserLogin.productsFile.getAbsoluteFile());
                             	BufferedWriter bw = new BufferedWriter(fw);
                             	
                             	bw.write(UserLogin.columnsIdentifierProducts);
                             	for(int i = 0; i < modelProducts.getRowCount(); i++) {
                             		for(int j = 0; j < modelProducts.getColumnCount(); j++) {
                             			bw.write((String)modelProducts.getValueAt(i, j));
                             			if(j < modelProducts.getColumnCount() - 1) {
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
        				// if user does not want items to go back to inventory
        				if (a == JOptionPane.NO_OPTION) {
        					// for loop to go through every order the user is trying to delete
        					for(int i = 0; i < selectedRows.length; i++) {
        						// makes the customers' order directory if it does not exist
                        		customerID = String.valueOf(tableOrders.getValueAt(selectedRows[i], 1));
                        		ordersCustomerPath = String.format("%s\\Database\\%s\\Orders\\%s", UserLogin.databasePath, UserLogin.id, customerID);
                    			ordersCustomerFile = new File(ordersCustomerPath);
                    			if(!ordersCustomerFile.exists()) {
                    				ordersCustomerFile.mkdir();
                    			}
                    			
                    			// deletes the order file if it exists
                    			orderID = String.valueOf(tableOrders.getValueAt(selectedRows[i], 0));
                    			orderPath = String.format("%s\\Database\\%s\\Orders\\%s\\%s.txt", UserLogin.databasePath, UserLogin.id, customerID, orderID);
                    			orderFile = new File(orderPath);
                    			if(orderFile.exists()) {
                    				orderFile.delete();
                    			}
        					}
        				}
        				
        				// if statement to check if the user has filtered the orders table to a specific customer
        				if(boxCustomers.getSelectedIndex() > 0) {
        					// for loop to change the selected rows to the rows of the entire table without filtering
        					for(int i = 0; i < selectedRows.length; i++) {
        						selectedRows[i] = arrayListOrdersIDs.indexOf(modelOrders.getValueAt(selectedRows[i], 0));
        					}
        					
        					// removes every row in the orders table
        					while(modelOrders.getRowCount() > 0) {
        						modelOrders.removeRow(0);
        					}
        					
        					// rewrites the orders table in order to have every order present
        					try {
        			    		BufferedReader br = new BufferedReader(new FileReader(UserLogin.ordersFile));
        			    		String firstLine = br.readLine().trim();
        			        	firstLine.split(" , ");
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
        				}
        				
        				// for loop to remove the orders the user is trying to delete from the orders table
                		for(int i = selectedRows.length - 1; i >= 0; i--) {
                			modelOrders.removeRow(selectedRows[i]);
                			arrayListOrdersIDs.remove(selectedRows[i]);
                		}
                		
                		// clears the order table
                		while(modelOrder.getRowCount() > 0) {
                			modelOrder.removeRow(0);
                		}
                		
                		// rewrites the order table without the orders that the user deleted
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
                		
                		// checks again if the user had the table filtered for specific customer
                		if(boxCustomers.getSelectedIndex() > 0) {
                			customerID = String.valueOf(modelCustomers.getValueAt(boxCustomers.getSelectedIndex() - 1, 0));
                			// for loop to remove any orders from the table that are not for that specific customer
                			for(int i = 0; i < modelOrders.getRowCount();) {
                				if(!customerID.equals(modelOrders.getValueAt(i, 1))) {
                					modelOrders.removeRow(i);
                				} else {
                					++i;
                				}
                			}
                		}
                		// deselects any order that was automatically selected
                		tableOrders.getSelectionModel().clearSelection();
                		tableOrder.getSelectionModel().clearSelection();
            			JOptionPane.showMessageDialog(null, "Successfully deleted the selected order.");
                    }
        		}
        	}
        });
        contentPane.add(buttonDelete);
        
        tableOrders = new JTable(modelOrders) {
        	private static final long serialVersionUID = 1L;
        	public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
             }
        };
        tableOrders.setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 24));
        tableOrders.getTableHeader().setForeground(Color.BLACK);
        tableOrders.getTableHeader().setBackground(Color.RED);
        tableOrders.getTableHeader().setReorderingAllowed(false);
        tableOrders.setForeground(new java.awt.Color(255, 0, 51));
        tableOrders.setBackground(Color.BLACK);
        tableOrders.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableOrders.setRowHeight(30);
        tableOrders.setSelectionBackground(new java.awt.Color(255, 0, 51));
        tableOrders.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent evt) {
        		// checks if the table contains at least one row to avoid errors
                if(tableOrders.getRowCount() > 0 && tableOrders.getSelectedRow() >= 0) {
                	rowsSelected = tableOrders.getSelectedRows();
                	
                	//  clears the order table if only one row is selected to get it ready to display the order
                	if(rowsSelected.length == 1) {
                		while(modelOrder.getRowCount() > 0) {
                			modelOrder.removeRow(0);
                		}
                		
                		// creates the customers' directory if it does not exists to avoid errors
                		orderTotal = String.valueOf(tableOrders.getValueAt(rowsSelected[0], 5));
                		customerID = String.valueOf(tableOrders.getValueAt(rowsSelected[0], 1));
                		ordersCustomerPath = String.format("%s\\Database\\%s\\Orders\\%s", UserLogin.databasePath, UserLogin.id, customerID);
            			ordersCustomerFile = new File(ordersCustomerPath);
            			if(!ordersCustomerFile.exists()) {
            				ordersCustomerFile.mkdir();
            			}
            			
            			orderID = String.valueOf(tableOrders.getValueAt(rowsSelected[0], 0));
            			orderPath = String.format("%s\\Database\\%s\\Orders\\%s\\%s.txt", UserLogin.databasePath, UserLogin.id, customerID, orderID);
            			orderFile = new File(orderPath);
                		
            			// reads the order file and puts it into the assigned table in order for the user to view
            			if(orderFile.exists()) {
            				try {
                        		BufferedReader br = new BufferedReader(new FileReader(orderFile));
                        		String firstLine = br.readLine().trim();
                            	String[] columnsName = firstLine.split(" , ");
                            	modelOrder.setColumnIdentifiers(columnsName);
                            	Object[] tableLines = br.lines().toArray();
                            	
                            	for(int i = 0; i < tableLines.length; i++) {
                            		String line = tableLines[i].toString().trim();
                            		String[] dataRow = line.split(" / ");
                            		modelOrder.addRow(dataRow);
                            	}
                            	br.close();
                            	tableOrder.setModel(modelOrder);
                        	} catch (Exception ex) {
                        		ex.printStackTrace();
                        	}
            			}
                	}
                }
            }
        });
        scrollOrders = new JScrollPane(tableOrders);
        scrollOrders.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        scrollOrders.setBounds(25, 50, 950, 250);
        scrollOrders.getViewport().setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 24));
        scrollOrders.getViewport().setForeground(new java.awt.Color(255, 0, 51));
        scrollOrders.setForeground(new java.awt.Color(255, 0, 51));
        scrollOrders.getViewport().setBackground(Color.BLACK);
        scrollOrders.setBackground(Color.BLACK);
        scrollOrders.getVerticalScrollBar().setBackground(Color.BLACK);
        scrollOrders.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
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
        contentPane.add(scrollOrders, BorderLayout.CENTER);
        
        tableOrder = new JTable(modelOrder) {
        	private static final long serialVersionUID = 1L;
        	public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
             }
        };
        tableOrder.setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 24));
        tableOrder.getTableHeader().setForeground(Color.BLACK);
        tableOrder.getTableHeader().setBackground(Color.RED);
        tableOrder.getTableHeader().setReorderingAllowed(false);
        tableOrder.setForeground(new java.awt.Color(255, 0, 51));
        tableOrder.setBackground(Color.BLACK);
        tableOrder.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableOrder.setRowHeight(30);
        tableOrder.setSelectionBackground(new java.awt.Color(255, 0, 51));
        
        scrollOrder = new JScrollPane(tableOrder);
        scrollOrder.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        scrollOrder.setBounds(25, 300, 950, 175);
        scrollOrder.getViewport().setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 24));
        scrollOrder.getViewport().setForeground(new java.awt.Color(255, 0, 51));
        scrollOrder.setForeground(new java.awt.Color(255, 0, 51));
        scrollOrder.getViewport().setBackground(Color.BLACK);
        scrollOrder.setBackground(Color.BLACK);
        scrollOrder.getVerticalScrollBar().setBackground(Color.BLACK);
        scrollOrder.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
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
        contentPane.add(scrollOrder, BorderLayout.CENTER);
        
        // creates and writes the column identifiers if the orders file does not already exist
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
        
        // reads the orders file and puts it into the assigned table
        try {
    		BufferedReader br = new BufferedReader(new FileReader(UserLogin.ordersFile));
    		String firstLine = br.readLine().trim();
        	String[] columnsName = firstLine.split(" , ");
        	modelOrders.setColumnIdentifiers(columnsName);
        	Object[] tableLines = br.lines().toArray();
        	
        	// removes anything in the array list in order to be able to refill it with updated values
        	while(arrayListOrdersIDs.size() > 0) {
        		arrayListOrdersIDs.remove(0);
        	}
        	
        	for(int i = 0; i < tableLines.length; i++) {
        		String line = tableLines[i].toString().trim();
        		String[] dataRow = line.split(" / ");
        		modelOrders.addRow(dataRow);
        		// adds every order ID to an array list for more efficiency when rewriting files that require the order ID in the path
        		arrayListOrdersIDs.add(dataRow[0]);
        	}
        	br.close();
        	tableOrders.setModel(modelOrders);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
        
        // creates and writes the column identifiers if the customers file does not already exist
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
        
        // reads the customers file and puts it into the assigned table
        try {
    		BufferedReader br = new BufferedReader(new FileReader(UserLogin.customersFile));
    		String firstLine = br.readLine().trim();
        	String[] columnsName = firstLine.split(" , ");
        	modelCustomers.setColumnIdentifiers(columnsName);
        	Object[] tableLines = br.lines().toArray();
        	
        	// removes anything in the array list in order to be able to refill it with updated values
        	while(arrayListCustomers.size() > 0) {
        		arrayListCustomers.remove(0);
        	}
        	arrayListCustomers.add("All");
        	
        	for(int i = 0; i < tableLines.length; i++) {
        		String line = tableLines[i].toString().trim();
        		String[] dataRow = line.split(" / ");
        		modelCustomers.addRow(dataRow);
        		// adds every customer ID to an array list for more efficiency when rewriting files that require the customer's ID in the path
        		arrayListCustomers.add(dataRow[1] + " " + dataRow[2]);
        	}
        	br.close();
        	// sets the values in the combo box for the user to access
        	customers = arrayListCustomers.toArray(new String[0]);
        	boxCustomers.setModel(new DefaultComboBoxModel<String>(customers));
        	boxCustomers.setSelectedIndex(0);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
        
        // creates and writes the column identifiers if the products file does not already exist
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
        
        // reads the products file and puts it into the assigned table
        try {
    		BufferedReader br = new BufferedReader(new FileReader(UserLogin.productsFile));
    		String firstLine = br.readLine().trim();
        	String[] columnsName = firstLine.split(" , ");
        	modelProducts.setColumnIdentifiers(columnsName);
        	Object[] tableLines = br.lines().toArray();
        	
        	// removes anything in the array list in order to be able to refill it with updated values
        	while(arrayListProductsID.size() > 0) {
        		arrayListProductsID.remove(0);
        	}
        	
        	for(int i = 0; i < tableLines.length; i++) {
        		String line = tableLines[i].toString().trim();
        		String[] dataRow = line.split(" / ");
        		modelProducts.addRow(dataRow);
        		// adds every product ID to an array list for more efficiency when needing the product ID to add the quantity of the products in the order back to the inventory
        		arrayListProductsID.add(dataRow[0]);
        	}
        	br.close();
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}
}
