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
import javax.swing.plaf.basic.*;

public class Categories extends JFrame {
	// declare variables
	private static final long serialVersionUID = 1L;
	static boolean matchID = false;
	static boolean matchName = false;
	static ArrayList<String> categoriesEdited = new ArrayList<String>();
	static ArrayList<String> categoriesDeleted = new ArrayList<String>();
	static String categoryID;
	static String categoryName;
	static String categoryDescription;
	
	// declare GUI elements
	private JLabel labelCategories;
	private JLabel labelCategoryID;
	private JLabel labelCategoryName;
	private JLabel labelCategoryDescription;
	private JTextField textCategoryID;
	private JTextField textCategoryName;
	private JTextField textCategoryDescription;
	private JButton buttonMenu;
	private JButton buttonAdd;
	private JButton buttonEdit;
	private JButton buttonDelete;
	private JButton buttonClear;
	private JButton buttonUp;
	private JButton buttonDown;
	private static javax.swing.JTable tableCategories;
	private JScrollPane jScrollPane;
	private JPanel contentPane;
	
	public Categories() {
		// creates table models needed to be able to access the database
		DefaultTableModel model = new DefaultTableModel(0, 0);
		DefaultTableModel modelOld = new DefaultTableModel(0, 0);
		DefaultTableModel modelProducts = new DefaultTableModel(0, 0);
		
		// creates and sets parameters for the panel
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(450, 200, 1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Categories");
        setVisible(true);
        contentPane = new JPanel(new BorderLayout());
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
        labelCategories = new JLabel("Categories");
        labelCategories.setForeground(Color.RED);
        labelCategories.setFont(new Font("Times New Roman", Font.BOLD, 60));
        labelCategories.setBounds(350, 0, 600, 75);
        contentPane.add(labelCategories);
        
        labelCategoryID = new JLabel("ID");
        labelCategoryID.setForeground(Color.RED);
        labelCategoryID.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelCategoryID.setBounds(180, 100, 200, 50);
        contentPane.add(labelCategoryID);
        
        labelCategoryName = new JLabel("Name");
        labelCategoryName.setForeground(Color.RED);
        labelCategoryName.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelCategoryName.setBounds(125, 200, 200, 50);
        contentPane.add(labelCategoryName);
        
        labelCategoryDescription = new JLabel("Description");
        labelCategoryDescription.setForeground(Color.RED);
        labelCategoryDescription.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelCategoryDescription.setBounds(25, 300, 300, 50);
        contentPane.add(labelCategoryDescription);
        
        textCategoryID = new JTextField();
        textCategoryID.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        textCategoryID.setBounds(250, 100, 200, 50);
        contentPane.add(textCategoryID);
        
        textCategoryName = new JTextField();
        textCategoryName.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        textCategoryName.setBounds(250, 200, 200, 50);
        contentPane.add(textCategoryName);
        
        textCategoryDescription = new JTextField();
        textCategoryDescription.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        textCategoryDescription.setBounds(250, 300, 200, 50);
        contentPane.add(textCategoryDescription);

        buttonMenu = new JButton("Menu");
        buttonMenu.setFont(new Font("Tahoma", Font.PLAIN, 45));
        buttonMenu.setBounds(10, 10, 150, 75);
        buttonMenu.setForeground(new java.awt.Color(255, 0, 51));
        buttonMenu.setBackground(Color.BLACK);
        buttonMenu.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// if statement to check if any categories were edited or deleted
        		if(categoriesEdited.size() > 0 || categoriesDeleted.size() > 0) {
        			// if statement to check if the products file exists and creates it if not
        			if(!UserLogin.productsFile.exists()) {
        	        	try {
        					UserLogin.productsFile.createNewFile();
        					FileWriter fw = new FileWriter(UserLogin.productsFile.getAbsoluteFile());
        	             	BufferedWriter bw = new BufferedWriter(fw);
        	             	bw.write(UserLogin.columnsIdentifierProducts);
        	             	bw.close();
        	             	fw.close();
        				} catch (IOException e1) {
        					e1.printStackTrace();
        				}
        	        }
        			// reads the products file in a try/catch and sets it inside of the assigned table model
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
        	        	}
        	        	br.close();
        	    	} catch (Exception ex) {
        	    		ex.printStackTrace();
        	    	}
            		
        	        // if statement to check if any categories were edited
            		if(categoriesEdited.size() > 0) {
            			// for loop to go through every category edited
            			for(int i = 0; i < categoriesEdited.size(); i++) {
            				// for loop and if statement to check if the edited category existed prior to entering the class
            				for(int j = 0; j < modelOld.getRowCount(); j++) {
            					if(categoriesEdited.get(i).equals(modelOld.getValueAt(j, 0))) {
            						// if statement to check if the category was not deleted after getting edited
            						if(!categoriesDeleted.contains(categoriesEdited.get(i))) {
            							// for loop and if statement to find the row of the edited category in the table model
            		                    for(int k = 0; k < model.getRowCount(); k++) {
            		                    	if(model.getValueAt(k, 0).equals(categoriesEdited.get(i))) {
            		                    		// for loop to go through every product and if statement to check if the category matches the category that was edited
            		                    		// then edits the category name to the new name
            		                    		for(int h = 0; h < modelProducts.getRowCount(); h++) {
            		                    			if(modelProducts.getValueAt(h, 3).equals(modelOld.getValueAt(j, 1))) {
                    		                    		modelProducts.setValueAt(String.valueOf(model.getValueAt(k, 1)), h, 3);
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
            		// if statement to check if any categories were deleted
            		if(categoriesDeleted.size() > 0) {  
            			// for loop to go through every category deleted
            			for(int i = 0; i < categoriesDeleted.size(); i++) {
            				// for loop and if statement to find the row of the category deleted in the old table
            				for(int j = 0; j < modelOld.getRowCount(); j++) {
            					if(categoriesDeleted.get(i).equals(modelOld.getValueAt(j, 0))) {
            						// for loop and if statement to remove every instance of that category in the products
        		                    for(int h = 0; h < modelProducts.getRowCount(); h++) {
        		                    	if(modelProducts.getValueAt(h, 3).equals(modelOld.getValueAt(j, 1))) {
        		                    		modelProducts.setValueAt("None", h, 3);
                		                }
        		                    }
        		                    // closes the for loop (no need to check the rest of the table because it can only exist once)
            						j = modelOld.getRowCount();
            					}
            				}
            			}
            		}
            		// rewrites the new product file with the edited and deleted categories
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
        		
        		// rewrites the category file
                try {
                 	FileWriter fw = new FileWriter(UserLogin.categoriesFile.getAbsoluteFile());
                 	BufferedWriter bw = new BufferedWriter(fw);
                 	
                 	bw.write(UserLogin.columnsIdentifierCategories);
                 	for(int i = 0; i < tableCategories.getRowCount(); i++) {
                 		for(int j = 0; j < tableCategories.getColumnCount(); j++) {
                 			bw.write((String)tableCategories.getModel().getValueAt(i, j));
                 			if(j < tableCategories.getColumnCount() - 1) {
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
                while(categoriesEdited.size() > 0) {
                	categoriesEdited.remove(0);
            	}
                while(categoriesDeleted.size() > 0) {
            		categoriesDeleted.remove(0);
            	}
                
                // goes to the home screen
                dispose();
        		new UserHome();
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
        		categoryID = textCategoryID.getText();
        		categoryName = textCategoryName.getText();
        		categoryDescription = textCategoryDescription.getText();
        		categoryID = categoryID.trim();
        		categoryName = categoryName.trim();
        		categoryDescription = categoryDescription.trim();
        		
        		// if statements to check if the text fields meet certain requirements to avoid errors
        		if(categoryID.isEmpty() || categoryName.isEmpty() || categoryDescription.isEmpty()) {
        			JOptionPane.showMessageDialog(null, "Please do not leave any values empty.");
        		} else if(Objects.equals(categoryID, categoryID.replaceAll("\\s+","")) == false || categoryID.chars().allMatch(Character::isDigit) == false) {
        			JOptionPane.showMessageDialog(null, "Please enter a valid ID.");
        		} else if(categoryName.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Name cannot contain '/'.");
        		} else if(categoryDescription.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Description cannot contain '/'.");
        		} else {
        			// for loop and if statement to check if the ID is already being used
        			for(int i = 0; i < model.getRowCount(); i++) {
                    	if(Objects.equals(categoryID, model.getValueAt(i, 0))) {
                    		matchID = true;
                    		i = model.getRowCount();
                    	} else if(Objects.equals(categoryName, model.getValueAt(i, 1))) {
                    		matchName = true;
                    		i = model.getRowCount();
                    	}
                    }
                    
        			// if statements to decide whether the category will be made based on if it meets the conditions
                    if(matchID) {
                    	matchID = false;
                    	JOptionPane.showMessageDialog(null, "ID already in use.");
                    } else if(matchName) { 
                    	matchName = false;
                    	JOptionPane.showMessageDialog(null, "Category already exists.");
                    } else {
                    	// adds the new category and notifies the user
                    	String[] dataRow = {categoryID, categoryName, categoryDescription};
                		model.addRow(dataRow);
                		tableCategories.getSelectionModel().clearSelection();
                		tableCategories.setRowSelectionInterval(tableCategories.getRowCount() - 1, tableCategories.getRowCount() - 1);
                		JOptionPane.showMessageDialog(null, "Successfully added the category.");
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
        		categoryID = textCategoryID.getText();
        		categoryName = textCategoryName.getText();
        		categoryDescription = textCategoryDescription.getText();
        		categoryID = categoryID.trim();
        		categoryName = categoryName.trim();
        		categoryDescription = categoryDescription.trim();
        		int[] rowsSelected = tableCategories.getSelectedRows();
        		
        		// if statements to check if the values meet certain conditions to avoid errors
        		if(tableCategories.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a category to edit.");
        		} else if(categoryID.isEmpty() || categoryName.isEmpty() || categoryDescription.isEmpty()) {
        			JOptionPane.showMessageDialog(null, "Please do not leave any values empty.");
        		} else if(Objects.equals(categoryID, categoryID.replaceAll("\\s+","")) == false || categoryID.chars().allMatch(Character::isDigit) == false) {
        			JOptionPane.showMessageDialog(null, "Please enter a valid ID.");
        		} else if(categoryName.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Name cannot contain '/'.");
        		} else if(categoryDescription.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Description cannot contain '/'.");
        		} else if(rowsSelected.length > 1) {
        			JOptionPane.showMessageDialog(null, "Cannot edit multiple categories at once.");
        		} else if(!categoryID.equals(model.getValueAt(tableCategories.getSelectedRow(), 0))) {
        			JOptionPane.showMessageDialog(null, "Cannot edit the category ID once the category is added to the system.");
        		} else {
        			// for loop and if statements to check if the category name or ID is already being used
        			for(int i = 0; i < model.getRowCount(); i++) {
                    	if(Objects.equals(categoryID, model.getValueAt(i, 0)) && i != tableCategories.getSelectedRow()) {
                    		matchID = true;
                    		i = model.getRowCount();
                    	}
                    	if(Objects.equals(categoryName, model.getValueAt(i, 1)) && i != tableCategories.getSelectedRow()) {
                    		matchName = true;
                    		i = model.getRowCount();
                    	}
                    }
                    
        			// if statements to decide whether the category will be edited based on if the ID and name are unique
                    if(matchID) {
                    	matchID = false;
                    	JOptionPane.showMessageDialog(null, "ID already in use.");
                    } else if(matchName) { 
                    	matchName = false;
                    	JOptionPane.showMessageDialog(null, "Category already exists.");
                    } else {
                    	// if statement to add the edited category to the array list if it is not already in it for future use
                    	if(!categoriesEdited.contains(categoryID)) {
                    		categoriesEdited.add(categoryID);
                    	}
                    	// edits the table
                		model.setValueAt(categoryID, tableCategories.getSelectedRow(), 0);
                		model.setValueAt(categoryName, tableCategories.getSelectedRow(), 1);
                		model.setValueAt(categoryDescription, tableCategories.getSelectedRow(), 2);
                		JOptionPane.showMessageDialog(null, "Successfully edited the category.");
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
        		if(tableCategories.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a category to delete.");
        		} else {
        			// confirm dialog to make sure the user wants to delete the category
        			int a = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected category?", "Delete Category", JOptionPane.YES_NO_OPTION);
        			if (a == JOptionPane.YES_OPTION) {
            			int[] selectedRows = tableCategories.getSelectedRows();
            			// for loop to go through every category selected
                		for(int i = 0; i < selectedRows.length; i++) {
                			// if statement to add the category to the list of categories deleted if it is not already in there
                			if(!categoriesDeleted.contains(model.getValueAt(selectedRows[0], 0))) {
                        		categoriesDeleted.add(String.valueOf(model.getValueAt(selectedRows[0], 0)));
                        	}
                			model.removeRow(selectedRows[0]);
                		}
                		// clears the text boxes and notifies the user
            			textCategoryID.setText("");
            			textCategoryName.setText("");
            			textCategoryDescription.setText("");
            			JOptionPane.showMessageDialog(null, "Successfully deleted the selected category.");
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
        		textCategoryID.setText("");
        		textCategoryName.setText("");
        		textCategoryDescription.setText("");
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
        		int[] selectedRows = tableCategories.getSelectedRows();
        		// if statement to check if the user has selected a row or not
        		if(tableCategories.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a row to move.");
        		} else {
        			// if statement to check if the row can be moved
    				if(selectedRows[0] > 0) {
    					// for loop to go through every row selected
    					for(int i = 0; i < selectedRows.length; i++) {
    						// moves the selected row up
    						model.moveRow(selectedRows[i], selectedRows[i], selectedRows[i] -1);
        				}
    					// moves the selection interval with the row(s) that moved up
    					tableCategories.setRowSelectionInterval(selectedRows[0] - 1, selectedRows[selectedRows.length - 1] - 1);
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
        		int[] selectedRows = tableCategories.getSelectedRows();
        		// if statement to check if a row is selected
        		if(tableCategories.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a row to move.");
        		} else {
        			// if statement to check if the row can get moved down
    				if(selectedRows[selectedRows.length - 1] < model.getRowCount() - 1) {
    					// for loop to go through every row selected and then move the row down
    					for(int i = selectedRows.length - 1; i >= 0; i--) {
    						model.moveRow(selectedRows[i], selectedRows[i], selectedRows[i] + 1);
        				}
    					// moves the selection interval with the row(s) that moved down
    					tableCategories.setRowSelectionInterval(selectedRows[0] + 1, selectedRows[selectedRows.length - 1] + 1);
    				}
        		}
        	}
        });
        contentPane.add(buttonDown);
        
        tableCategories = new JTable(model) {
        	private static final long serialVersionUID = 1L;
        	// sets table not editable
        	public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
             }
        };
        tableCategories.setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 24));
        tableCategories.getTableHeader().setForeground(Color.BLACK);
        tableCategories.getTableHeader().setBackground(Color.RED);
        tableCategories.getTableHeader().setReorderingAllowed(false);
        tableCategories.setForeground(new java.awt.Color(255, 0, 51));
        tableCategories.setBackground(Color.BLACK);
        tableCategories.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableCategories.setRowHeight(30);
        tableCategories.setSelectionBackground(new java.awt.Color(255, 0, 51));
        tableCategories.setBounds(475, 100, 500, 450);
        tableCategories.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent evt) {
                if(tableCategories.getRowCount() > 0 && tableCategories.getSelectedRow() >= 0) {
                	int row = tableCategories.getSelectedRow();
                    textCategoryID.setText(model.getValueAt(row, 0).toString());
                    textCategoryName.setText(model.getValueAt(row, 1).toString());
                    textCategoryDescription.setText(model.getValueAt(row, 2).toString());
                }
            }
        });
        jScrollPane = new JScrollPane(tableCategories);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        jScrollPane.setBounds(475, 100, 500, 450);
        jScrollPane.getViewport().setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 24));
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

        // if statement to check if category file needs to be created or not
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
        // reads the category file and sets it into the table model
        try {
    		BufferedReader br = new BufferedReader(new FileReader(UserLogin.categoriesFile));
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
        	tableCategories.setModel(model);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}
}