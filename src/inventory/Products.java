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
import java.math.*;
import java.text.DecimalFormat;

public class Products extends JFrame {
	// declare variables
	private static final long serialVersionUID = 1L;
	static boolean matchID = false;
	static boolean priceIsDouble = true;
	static boolean orderEdited = false;
	static String[] categories;
	static ArrayList<String> arrayListCategories = new ArrayList<String>();
	static ArrayList<String> productsEdited = new ArrayList<String>();
	static ArrayList<String> productsDeleted = new ArrayList<String>();
	static ArrayList<String> ordersIDs = new ArrayList<String>();
	static ArrayList<String> customersIDs = new ArrayList<String>();
	static String newOrderTotal;
	static String newProductTotal;
	static String productID;
	static String productQuantity;
	static String productName;
	static String productPrice;
	static String customerOrdersPath;
	static File customerOrdersFile;
	DecimalFormat decimal = new DecimalFormat("0.00");
	
	// declare GUI elements
	private JLabel labelProducts;
	private JLabel labelProductID;
	private JLabel labelProductQuantity;
	private JLabel labelProductName;
	private JLabel labelProductCategory;
	private JLabel labelProductPrice;
	private JTextField textProductID;
	private JTextField textProductQuantity;
	private JTextField textProductName;
	private JComboBox<String> boxProductCategory;
	private JTextField textProductPrice;
	private JButton buttonMenu;
	private JButton buttonAdd;
	private JButton buttonEdit;
	private JButton buttonDelete;
	private JButton buttonClear;
	private JButton buttonUp;
	private JButton buttonDown;
	private static javax.swing.JTable tableProducts;
	private JScrollPane jScrollPane;
	private JPanel contentPane;
	
	public Products() {
		// creates table models needed to easily interact with the database
		DefaultTableModel model = new DefaultTableModel(0, 0);
		DefaultTableModel modelOld = new DefaultTableModel(0, 0);
		DefaultTableModel modelOrders = new DefaultTableModel(0, 0);
		DefaultTableModel modelOrder = new DefaultTableModel(0, 0);
		
		// creates and sets parameters for the frame
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(450, 200, 1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Products");
        setVisible(true);
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setVisible(true);
        
        // warns the user that their recent changes will not be saved when they exit the class by closing the program
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		int save = JOptionPane.showConfirmDialog(null, "Are you sure you would like to exit? Any changes will not be saved.", "Exit", JOptionPane.YES_NO_OPTION);
        		if(save == JOptionPane.YES_OPTION) {
        			dispose();
        		}
        	}
        });
        
        // create and set GUI elements
        labelProducts = new JLabel("Products");
        labelProducts.setForeground(Color.RED);
        labelProducts.setFont(new Font("Times New Roman", Font.BOLD, 60));
        labelProducts.setBounds(375, 0, 600, 75);
        contentPane.add(labelProducts);
        
        labelProductID = new JLabel("ID");
        labelProductID.setForeground(Color.RED);
        labelProductID.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        labelProductID.setBounds(200, 100, 200, 40);
        contentPane.add(labelProductID);
        
        labelProductQuantity = new JLabel("Quantity");
        labelProductQuantity.setForeground(Color.RED);
        labelProductQuantity.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        labelProductQuantity.setBounds(115, 150, 200, 40);
        contentPane.add(labelProductQuantity);
        
        labelProductName = new JLabel("Name");
        labelProductName.setForeground(Color.RED);
        labelProductName.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        labelProductName.setBounds(150, 200, 200, 40);
        contentPane.add(labelProductName);
        
        labelProductCategory = new JLabel("Category");
        labelProductCategory.setForeground(Color.RED);
        labelProductCategory.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        labelProductCategory.setBounds(110, 250, 200, 40);
        contentPane.add(labelProductCategory);
        
        labelProductPrice = new JLabel("Price ($)");
        labelProductPrice.setForeground(Color.RED);
        labelProductPrice.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        labelProductPrice.setBounds(125, 300, 300, 40);
        contentPane.add(labelProductPrice);
        
        textProductID = new JTextField();
        textProductID.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        textProductID.setBounds(250, 100, 200, 40);
        contentPane.add(textProductID);
        
        textProductQuantity = new JTextField();
        textProductQuantity.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        textProductQuantity.setBounds(250, 150, 200, 40);
        contentPane.add(textProductQuantity);
        
        textProductName = new JTextField();
        textProductName.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        textProductName.setBounds(250, 200, 200, 40);
        contentPane.add(textProductName);
        
        boxProductCategory = new JComboBox<String>();
        boxProductCategory.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        boxProductCategory.setBounds(250, 250, 200, 40);
        boxProductCategory.setForeground(new java.awt.Color(255, 0, 51));;
        boxProductCategory.setBackground(Color.BLACK);
        boxProductCategory.setUI(new BasicComboBoxUI() {
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
        contentPane.add(boxProductCategory);
        
        textProductPrice = new JTextField();
        textProductPrice.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        textProductPrice.setBounds(250, 300, 200, 40);
        contentPane.add(textProductPrice);

        buttonMenu = new JButton("Menu");
        buttonMenu.setFont(new Font("Tahoma", Font.PLAIN, 45));
        buttonMenu.setBounds(10, 10, 150, 75);
        buttonMenu.setForeground(new java.awt.Color(255, 0, 51));
        buttonMenu.setBackground(Color.BLACK);
        buttonMenu.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// goes back to the main menu
        		dispose();
        		new UserHome();
        		
        		// if statement to check if any products were edited or deleted
        		if(productsEdited.size() > 0 || productsDeleted.size() > 0) {
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
        			
        			// removes anything inside the array lists from previous use
        			while(ordersIDs.size() > 0) {
        				ordersIDs.remove(0);
        				customersIDs.remove(0);
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
                    		ordersIDs.add(dataRow[0]);
                    		customersIDs.add(dataRow[1]);
                    	}
                    	br.close();
                	} catch (Exception ex) {
                		ex.printStackTrace();
                	}
            		
        			// if statement to check if any products were edited
            		if(productsEdited.size() > 0) {
            			// for loop to go through every one of the user's orders in the database
            			for(int i = 0; i < ordersIDs.size(); i++) {
            				// checks if the specific order file exists
            				customerOrdersPath = String.format("%s\\Database\\%s\\Orders\\%s\\%s.txt", UserLogin.databasePath, UserLogin.id, customersIDs.get(i), ordersIDs.get(i));
            				customerOrdersFile = new File(customerOrdersPath);
        					if(customerOrdersFile.exists()) {
        						// removes anything inside the order table
        						while(modelOrder.getRowCount() > 0) {
        							modelOrder.removeRow(0);
        						}
        						
        						// reads the specific order file to the assigned table in order to edit the products inside
        						try {
        	                		BufferedReader br = new BufferedReader(new FileReader(customerOrdersFile));
        	                		String firstLine = br.readLine().trim();
        	                    	String[] columnsName = firstLine.split(" , ");
        	                    	modelOrder.setColumnIdentifiers(columnsName);
        	                    	Object[] tableLines = br.lines().toArray();
        	                    	
        	                    	for(int h = 0; h < tableLines.length; h++) {
        	                    		String line = tableLines[h].toString().trim();
        	                    		String[] dataRow = line.split(" / ");
        	                    		modelOrder.addRow(dataRow);
        	                    	}
        	                    	br.close();
        	                	} catch (Exception ex) {
        	                		ex.printStackTrace();
        	                	}
        						newOrderTotal = String.valueOf(modelOrders.getValueAt(i, 5));
        						
        						// for loop to go through every products edited
        						for(int k = 0; k < productsEdited.size(); k++) {
        							// for loop and if statement to find if the product exists in the order and get the row
        							for(int l = 0; l < modelOrder.getRowCount(); l++) {
        								if(modelOrder.getValueAt(l ,0).equals(productsEdited.get(k))) {
        									// for loop and if statement to find the row of the product in the products table
        									for(int h = 0; h < model.getRowCount(); h++) {
        										if(model.getValueAt(h, 0).equals(productsEdited.get(k))) {
        											orderEdited = true;
        											// formats and sets the new product total and order total based on the new product price
        											newProductTotal = decimal.format(Double.valueOf(String.valueOf(modelOrder.getValueAt(l, 2))) * Double.valueOf(String.valueOf(model.getValueAt(h, 4))));
                									newOrderTotal = decimal.format(Double.valueOf(newOrderTotal) - Double.valueOf(String.valueOf(modelOrder.getValueAt(l, 4))) + Double.valueOf(newProductTotal));
                									// sets the new values in the table
                									modelOrder.setValueAt(model.getValueAt(h, 2), l, 1);
                									modelOrder.setValueAt(model.getValueAt(h, 4), l, 3);
                									modelOrder.setValueAt(newProductTotal, l, 4);
                									// closes the for loop
                									h = model.getRowCount();
        										}
        									}
        									// closes the for loop
        									l = modelOrder.getRowCount();
        								}
        							}
        						}
        						// if statement to check if an order was edited to avoid errors
        						if(orderEdited) {
        							// formats and sets the new order total
        							decimal.format(Double.valueOf(newOrderTotal));
                        		    modelOrders.setValueAt(newOrderTotal, i, 5);
                        		    
                        		    // rewrites the updated order file from from the table that was edited with the new values
                        		    try {
                                     	FileWriter fw = new FileWriter(customerOrdersFile.getAbsoluteFile());
                                     	BufferedWriter bw = new BufferedWriter(fw);
                                     	
                                     	bw.write(UserLogin.columnsIdentifierOrder);
                                     	for(int k = 0; k < modelOrder.getRowCount(); k++) {
                                     		for(int j = 0; j < modelOrder.getColumnCount(); j++) {
                                     			bw.write((String)modelOrder.getValueAt(k, j));
                                     			if(j < modelOrder.getColumnCount() - 1) {
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
            		}
            		// if statement to check if any products were deleted
            		if(productsDeleted.size() > 0) {
            			// for loop to go through every one of the user's orders in the database
            			for(int i = 0; i < ordersIDs.size(); i++) {
            				// checks if the specific order file exists
            				customerOrdersPath = String.format("%s\\Database\\%s\\Orders\\%s\\%s.txt", UserLogin.databasePath, UserLogin.id, customersIDs.get(i), ordersIDs.get(i));
            				customerOrdersFile = new File(customerOrdersPath);
        					if(customerOrdersFile.exists()) {
        						// removes anything inside the order table
        						while(modelOrder.getRowCount() > 0) {
        							modelOrder.removeRow(0);
        						}
        						
        						// reads the specific order file to the assigned table in order to delete the products inside
        						try {
        	                		BufferedReader br = new BufferedReader(new FileReader(customerOrdersFile));
        	                		String firstLine = br.readLine().trim();
        	                    	String[] columnsName = firstLine.split(" , ");
        	                    	modelOrder.setColumnIdentifiers(columnsName);
        	                    	Object[] tableLines = br.lines().toArray();
        	                    	
        	                    	for(int h = 0; h < tableLines.length; h++) {
        	                    		String line = tableLines[h].toString().trim();
        	                    		String[] dataRow = line.split(" / ");
        	                    		modelOrder.addRow(dataRow);
        	                    	}
        	                    	br.close();
        	                	} catch (Exception ex) {
        	                		ex.printStackTrace();
        	                	}
        						newOrderTotal = String.valueOf(modelOrders.getValueAt(i, 5));
        						
        						// for loop to go through every products deleted
        						for(int k = 0; k < productsDeleted.size(); k++) {
        							// for loop and if statement to find if the product exists in the order and get the row
        							for(int l = 0; l < modelOrder.getRowCount(); l++) {
        								if(modelOrder.getValueAt(l ,0).equals(productsDeleted.get(k))) {
        									orderEdited = true;
        									// formats and sets the new order total based on the new product price
        									newOrderTotal = String.valueOf(Double.valueOf(newOrderTotal) - Double.valueOf(String.valueOf(modelOrder.getValueAt(l, 4))));
        									// remove the product from the table
        									modelOrder.removeRow(l);
        									
        									// if statement to delete the order if it was the last product in the order
        									if(modelOrder.getRowCount() < 1) {
        										customerOrdersFile.delete();
        										modelOrders.removeRow(i);
        										ordersIDs.remove(i);
        										customersIDs.remove(i);
        										k = productsDeleted.size();
        									}
        									l = modelOrder.getRowCount();
        								}
        							}
        						}
        						if(orderEdited) {
        							// formats the new order total and edits it in the table
        							decimal.format(Double.valueOf(newOrderTotal));
                        		    if(modelOrders.getRowCount() > 0) {
                        		    	modelOrders.setValueAt(newOrderTotal, i, 5);
                        		    }
                        		    
                        		    // writes the new order file from the table if it exists
                        		    if(customerOrdersFile.exists()) {
                        		    	try {
                                         	FileWriter fw = new FileWriter(customerOrdersFile.getAbsoluteFile());
                                         	BufferedWriter bw = new BufferedWriter(fw);
                                         	
                                         	bw.write(UserLogin.columnsIdentifierOrder);
                                         	for(int k = 0; k < modelOrder.getRowCount(); k++) {
                                         		for(int j = 0; j < modelOrder.getColumnCount(); j++) {
                                         			bw.write((String)modelOrder.getValueAt(k, j));
                                         			if(j < modelOrder.getColumnCount() - 1) {
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
                        		    
                        		    // has the stay on the same index in the array list because if the table is empty, the index was deleted so the next index to check replaced the previous one
                        		    if(modelOrder.getRowCount() < 1) {
                        		    	--i;
                        		    }
        						}
            				}
            			}
            		}
            		
            		// rewrites the orders file with the updated values from the table
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
        		
        		// rewrites the product file with the updated values and removed products
                try {
                 	FileWriter fw = new FileWriter(UserLogin.productsFile.getAbsoluteFile());
                 	BufferedWriter bw = new BufferedWriter(fw);
                 	
                 	bw.write(UserLogin.columnsIdentifierProducts);
                 	for(int i = 0; i < tableProducts.getRowCount(); i++) {
                 		for(int j = 0; j < tableProducts.getColumnCount(); j++) {
                 			bw.write((String)tableProducts.getModel().getValueAt(i, j));
                 			if(j < tableProducts.getColumnCount() - 1) {
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
                while(productsEdited.size() > 0) {
            		productsEdited.remove(0);
            	}
                while(productsDeleted.size() > 0) {
            		productsDeleted.remove(0);
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
        		productID = textProductID.getText();
        		productQuantity = textProductQuantity.getText();
        		productName = textProductName.getText();
        		productPrice = textProductPrice.getText();
        		productID = productID.trim();
        		productQuantity = productQuantity.trim();
        		productName = productName.trim();
        		productPrice = productPrice.trim();
        		
        		// checks if the product price is numeric
        		try {
        			Double.parseDouble(productPrice);
        		} catch(NumberFormatException e1) {
        			priceIsDouble = false;
        		}
        		// formats the price if it is numeric
        		if(priceIsDouble) {
        			decimal.format(Double.valueOf(productPrice));
        		}
        		
        		// if statements to check if the values meet certain conditions to avoid errors
        		if(productID.isEmpty() || productQuantity.isEmpty() || productName.isEmpty() || productPrice.isEmpty()) {
        			JOptionPane.showMessageDialog(null, "Please do not leave any values empty.");
        		} else if(Objects.equals(productID, productID.replaceAll("\\s+","")) == false || productID.chars().allMatch(Character::isDigit) == false) {
        			JOptionPane.showMessageDialog(null, "Please enter a valid ID.");
        		} else if(Objects.equals(productQuantity, productQuantity.replaceAll("\\s+","")) == false || productQuantity.chars().allMatch(Character::isDigit) == false) {
        			JOptionPane.showMessageDialog(null, "Please enter a valid quantity.");
        		} else if(productName.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Name cannot contain '/'.");
        		} else if(Objects.equals(productPrice, productPrice.replaceAll("\\s+","")) == false || priceIsDouble == false || productPrice.contains("-")) {
        			JOptionPane.showMessageDialog(null, "Please enter a valid price.");
        		} else if(boxProductCategory.getSelectedIndex() < 0) { 
        			JOptionPane.showMessageDialog(null, "Please select a category.");
        		} else {
        			// for loop and if statement to check if the ID is already being used
        			for(int i = 0; i < model.getRowCount(); i++) {
                    	Object data = model.getValueAt(i, 0);
                    	if(Objects.equals(productID, data)) {
                    		matchID = true;
                    	}
                    }
                    
        			// if statement to decide whether the product will be made based on if it the ID is taken or not
                    if(matchID) {
                    	matchID = false;
                    	JOptionPane.showMessageDialog(null, "ID already in use.");
                    } else {
                    	// adds the new product and notifies the user
                    	String[] dataRow = {productID, productQuantity, productName, categories[boxProductCategory.getSelectedIndex()], productPrice};
                		model.addRow(dataRow);
                		tableProducts.getSelectionModel().clearSelection();
                		tableProducts.setRowSelectionInterval(tableProducts.getRowCount() - 1, tableProducts.getRowCount() - 1);
                		JOptionPane.showMessageDialog(null, "Successfully added the product.");
                    }
                }
        		// resets the boolean that determines whether the price is a double
        		priceIsDouble = true;
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
        		// stores all the values in the text fields in their assigned variables
        		productID = textProductID.getText();
        		productQuantity = textProductQuantity.getText();
        		productName = textProductName.getText();
        		productPrice = textProductPrice.getText();
        		productID = productID.trim();
        		productQuantity = productQuantity.trim();
        		productName = productName.trim();
        		productPrice = productPrice.trim();
        		int[] rowsSelected = tableProducts.getSelectedRows();
        		
        		// checks if the product price is numeric
        		try {
        			Double.parseDouble(productPrice);
        		} catch(NumberFormatException e1) {
        			priceIsDouble = false;
        		}
        		// formats the price if it is numeric
        		if(priceIsDouble) {
        			decimal.format(Double.valueOf(productPrice));
        		}
        		
        		if(tableProducts.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a product to edit.");
        		} else if(productID.isEmpty() || productQuantity.isEmpty() || productName.isEmpty() || productPrice.isEmpty()) {
        			JOptionPane.showMessageDialog(null, "Please do not leave any values empty.");
        		} else if(Objects.equals(productID, productID.replaceAll("\\s+","")) == false || productID.chars().allMatch(Character::isDigit) == false) {
        			JOptionPane.showMessageDialog(null, "Please enter a valid ID.");
        		} else if(Objects.equals(productQuantity, productQuantity.replaceAll("\\s+","")) == false || productQuantity.chars().allMatch(Character::isDigit) == false) {
        			JOptionPane.showMessageDialog(null, "Please enter a valid quantity.");
        		} else if(productName.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Name cannot contain '/'.");
        		} else if(Objects.equals(productPrice, productPrice.replaceAll("\\s+","")) == false || priceIsDouble == false) {
        			JOptionPane.showMessageDialog(null, "Please enter a valid price.");
        		} else if(rowsSelected.length > 1) {
        			JOptionPane.showMessageDialog(null, "Cannot edit multiple products at once.");
        		} else if(boxProductCategory.getSelectedIndex() < 0) { 
        			JOptionPane.showMessageDialog(null, "Please select a category.");
        		} else if(!productID.equals(model.getValueAt(tableProducts.getSelectedRow(), 0))) {
        			JOptionPane.showMessageDialog(null, "Cannot edit the product ID once the product is added to the inventory.");
        		} else {
        			// for loop and if statement to check if the ID is already being used
                    for(int i = 0; i < model.getRowCount(); i++) {
                    	Object data = model.getValueAt(i, 0);
                    	if(Objects.equals(productID, data) && i != tableProducts.getSelectedRow()) {
                    		matchID = true;
                    	}
                    }
                    
                 // if statement to decide whether the product will be edited based on if it the ID is taken or not
                    if(matchID) {
                    	matchID = false;
                    	JOptionPane.showMessageDialog(null, "ID already in use.");
                    } else {
                    	// if statement to add the edited customer to the array list if it is not already in it for future use
                    	if(!productsEdited.contains(productID)) {
                    		productsEdited.add(productID);
                    	}
                    	// edits the table
                		model.setValueAt(productID, tableProducts.getSelectedRow(), 0);
                		model.setValueAt(productQuantity, tableProducts.getSelectedRow(), 1);
                		model.setValueAt(productName, tableProducts.getSelectedRow(), 2);
                		model.setValueAt(categories[boxProductCategory.getSelectedIndex()], tableProducts.getSelectedRow(), 3);
                		model.setValueAt(productPrice, tableProducts.getSelectedRow(), 4);
                		textProductPrice.setText(productPrice);
                		JOptionPane.showMessageDialog(null, "Successfully edited the product.");
                    }
                }
        		// resets the boolean that determines whether the price is a double
        		priceIsDouble = true;
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
        		if(tableProducts.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a product to delete.");
        		} else {
        			// confirm dialog to make sure the user wants to delete the product(s)
        			int a = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected product?", "Delete Product", JOptionPane.YES_NO_OPTION);
        			if (a == JOptionPane.YES_OPTION) {
            			int[] selectedRows = tableProducts.getSelectedRows();
            			// for loop to go through every product selected
                		for(int i = 0; i < selectedRows.length; i++) {
                			// if statement to add the customer to the list of products deleted if it is not already in there
                			if(!productsDeleted.contains(model.getValueAt(selectedRows[0], 0))) {
                        		productsDeleted.add(String.valueOf(model.getValueAt(selectedRows[0], 0)));
                        	}
                			model.removeRow(selectedRows[0]);
                		}
                		// clears the text boxes and notifies the user
            			textProductID.setText("");
            			textProductQuantity.setText("");
            			textProductName.setText("");
            			boxProductCategory.setSelectedIndex(0);
            			textProductPrice.setText("");
            			JOptionPane.showMessageDialog(null, "Successfully deleted the selected product.");
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
        		textProductID.setText("");
        		textProductQuantity.setText("");
        		textProductName.setText("");
        		boxProductCategory.setSelectedIndex(0);
        		textProductPrice.setText("");
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
        		int[] selectedRows = tableProducts.getSelectedRows();
        		if(tableProducts.getSelectedRow() < 0) {
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
            			tableProducts.setRowSelectionInterval(selectedRows[0] - 1, selectedRows[selectedRows.length - 1] - 1);
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
        		int[] selectedRows = tableProducts.getSelectedRows();
        		if(tableProducts.getSelectedRow() < 0) {
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
    					tableProducts.setRowSelectionInterval(selectedRows[0] + 1, selectedRows[selectedRows.length - 1] + 1);
    				}
        		}
        	}
        });
        contentPane.add(buttonDown);
        
        tableProducts = new JTable(model) {
        	private static final long serialVersionUID = 1L;
        	public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
             }
        };
        tableProducts.setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 20));
        tableProducts.getTableHeader().setForeground(Color.BLACK);
        tableProducts.getTableHeader().setBackground(Color.RED);
        tableProducts.getTableHeader().setReorderingAllowed(false);
        tableProducts.setForeground(new java.awt.Color(255, 0, 51));
        tableProducts.setBackground(Color.BLACK);
        tableProducts.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableProducts.setRowHeight(30);
        tableProducts.setSelectionBackground(new java.awt.Color(255, 0, 51));
        tableProducts.setBounds(475, 100, 500, 450);
        tableProducts.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent evt) {
                if(tableProducts.getRowCount() > 0 && tableProducts.getSelectedRow() >= 0) {
                	int row = tableProducts.getSelectedRow();
                    textProductID.setText(model.getValueAt(row, 0).toString());
                    textProductQuantity.setText(model.getValueAt(row, 1).toString());
                    textProductName.setText(model.getValueAt(row, 2).toString());
                    boxProductCategory.setSelectedIndex(arrayListCategories.indexOf(model.getValueAt(row, 3).toString()));
                    textProductPrice.setText(model.getValueAt(row, 4).toString());
                }
            }
            });
        jScrollPane = new JScrollPane(tableProducts);
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
        
        // creates the product file if it does not exist
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
        
        // reads the products file and sets it in the assigned table
        try {
    		BufferedReader br = new BufferedReader(new FileReader(UserLogin.productsFile));
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
        	tableProducts.setModel(model);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		
    	}
        
        // creates the categories file if it does not exist
        if(!UserLogin.categoriesFile.exists()) {
        	try {
				UserLogin.categoriesFile.createNewFile();
				FileWriter fw = new FileWriter(UserLogin.categoriesFile.getAbsoluteFile());
             	BufferedWriter bw = new BufferedWriter(fw);
             	bw.write(UserLogin.columnsIdentifierCategories);
             	bw.close();
             	fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        // reads the categories file and sets it in the assigned table
        try {
    		BufferedReader br = new BufferedReader(new FileReader(UserLogin.categoriesFile));
    		String firstLine = br.readLine().trim();
        	Object[] tableLines = br.lines().toArray();
        	
        	// removes anything in the array list in order to be able to refill it with updated values
        	while(arrayListCategories.size() > 0) {
        		arrayListCategories.remove(0);
        	}
        	
        	arrayListCategories.add("None");
        	for(int i = 0; i < tableLines.length; i++) {
        		String line = tableLines[i].toString().trim();
        		String[] dataRow = line.split(" / ");
        		// adds every category to an array list for the combo box to read
        		arrayListCategories.add(dataRow[1]);
        	}
        	br.close();
        	categories = arrayListCategories.toArray(new String[0]);
        	boxProductCategory.setModel(new DefaultComboBoxModel<String>(categories));
        	boxProductCategory.setSelectedIndex(0);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}
}
