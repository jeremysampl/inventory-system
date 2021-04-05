package inventory;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;

public class ManageUsers extends JFrame {
	// declare variables
	private static final long serialVersionUID = 1L;
	static boolean matchUser = false;
	static String userID;
	static String userName;
	static String userPassword;
	public static String pathToDelete;
	public static File fileToDelete;
	
	// declare GUI elements
	private JLabel labelManageUsers;
	private JLabel labelUserID;
	private JLabel labelUsername;
	private JLabel labelPassword;
	private JTextField textUserID;
	private JTextField textUsername;
	private JTextField textPassword;
	private JButton buttonMenu;
	private JButton buttonAdd;
	private JButton buttonEdit;
	private JButton buttonDelete;
	private JButton buttonClear;
	private static javax.swing.JTable tableLogins;
	private JScrollPane jScrollPane;
	private JPanel contentPane;
	
	public ManageUsers() {
		// creates the table model needed to store the logins information
		DefaultTableModel model = new DefaultTableModel(0, 0);
		
		// creates and sets the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 200, 1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Manage Users");
        setVisible(true);
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setVisible(true);
        
        // asks the user if they would like to save the changes to the table before they exit
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		int save = JOptionPane.showConfirmDialog(null,
        			"Would you like to save before exiting?", "Save", JOptionPane.YES_NO_OPTION);
        		if(save == JOptionPane.YES_OPTION) {
        			try {
                     	FileWriter fw = new FileWriter(UserLogin.loginsFile.getAbsoluteFile());
                     	BufferedWriter bw = new BufferedWriter(fw);
                     	
                     	bw.write(UserLogin.columnsIdentifierLogins);
                     	for(int i = 0; i < tableLogins.getRowCount(); i++) {
                     		for(int j = 0; j < tableLogins.getColumnCount(); j++) {
                     			bw.write((String)tableLogins.getModel().getValueAt(i, j));
                     			if(j < tableLogins.getColumnCount() - 1) {
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
        		dispose();
        	}
        });
        
        // create and set GUI elements
        labelManageUsers = new JLabel("Manage Users");
        labelManageUsers.setForeground(Color.RED);
        labelManageUsers.setFont(new Font("Times New Roman", Font.PLAIN, 60));
        labelManageUsers.setBounds(325, 0, 600, 75);
        contentPane.add(labelManageUsers);
        
        labelUserID = new JLabel("ID");
        labelUserID.setForeground(Color.RED);
        labelUserID.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelUserID.setBounds(180, 100, 200, 50);
        contentPane.add(labelUserID);
        
        labelUsername = new JLabel("Username");
        labelUsername.setForeground(Color.RED);
        labelUsername.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelUsername.setBounds(50, 200, 200, 50);
        contentPane.add(labelUsername);
        
        labelPassword = new JLabel("Password");
        labelPassword.setForeground(Color.RED);
        labelPassword.setFont(new Font("Times New Roman", Font.PLAIN, 45));
        labelPassword.setBounds(50, 300, 300, 50);
        contentPane.add(labelPassword);
        
        textUserID = new JTextField();
        textUserID.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        textUserID.setBounds(250, 100, 200, 50);
        textUserID.setEditable(false);
        contentPane.add(textUserID);
        
        textUsername = new JTextField();
        textUsername.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        textUsername.setBounds(250, 200, 200, 50);
        contentPane.add(textUsername);
        
        textPassword = new JTextField();
        textPassword.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        textPassword.setBounds(250, 300, 200, 50);
        contentPane.add(textPassword);

        buttonMenu = new JButton("Menu");
        buttonMenu.setFont(new Font("Tahoma", Font.PLAIN, 45));
        buttonMenu.setBounds(10, 10, 150, 75);
        buttonMenu.setForeground(new java.awt.Color(255, 0, 51));
        buttonMenu.setBackground(Color.BLACK);
        buttonMenu.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// goes to main menu
        		dispose();
        		new UserHome();
        		
        		// rewrites the logins file to save the changes
                try {
                 	FileWriter fw = new FileWriter(UserLogin.loginsFile.getAbsoluteFile());
                 	BufferedWriter bw = new BufferedWriter(fw);
                 	
                 	bw.write(UserLogin.columnsIdentifierLogins);
                 	for(int i = 0; i < tableLogins.getRowCount(); i++) {
                 		for(int j = 0; j < tableLogins.getColumnCount(); j++) {
                 			bw.write((String)tableLogins.getModel().getValueAt(i, j));
                 			if(j < tableLogins.getColumnCount() - 1) {
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
        });
        contentPane.add(buttonMenu);
        
        buttonAdd = new JButton("Add");
        buttonAdd.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonAdd.setBounds(25, 400, 125, 50);
        buttonAdd.setForeground(new java.awt.Color(255, 0, 51));
        buttonAdd.setBackground(Color.BLACK);
        buttonAdd.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// stores the user's input into the assigned variables
        		userName = textUsername.getText();
        		userPassword = textPassword.getText();
        		userName = userName.trim();
        		userPassword = userPassword.trim();
        		
        		// if statements to check if the values meet certain conditions to avoid errors
        		if(userName.isEmpty() || userPassword.isEmpty()) {
        			JOptionPane.showMessageDialog(null, "Please enter a username and password.");
        		} else if(userName.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Username cannot contain '/'.");
        		} else if(userPassword.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Password cannot contain '/'.");
        		} else {
        			// for loop and if statement to check if the username is already taken by checking every username in the table
        			for(int i = 0; i < model.getRowCount(); i++) {
                    	if(Objects.equals(userName, model.getValueAt(i, 1))) {
                    		matchUser = true;
                    	}
                    }
                    
        			// if statement to decide whether the user will be added based on if the username is taken or not
                    if(matchUser) {
                    	matchUser = false;
                    	JOptionPane.showMessageDialog(null, "Username already in use.");
                    } else {
                    	// adds the user into the table and notifies the user
                    	String[] dataRow = {String.valueOf(Integer.valueOf(String.valueOf(model.getValueAt(model.getRowCount() - 1, 0))) + 1), userName, userPassword};
                		model.addRow(dataRow);
                		tableLogins.getSelectionModel().clearSelection();
                		tableLogins.setRowSelectionInterval(tableLogins.getRowCount() - 1, tableLogins.getRowCount() - 1);
                		textUserID.setText(model.getValueAt(tableLogins.getRowCount() - 1, 0).toString());
                		JOptionPane.showMessageDialog(null, "Successfully added the user.");
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
        		// stores the user's input into the assigned variables
        		userName = textUsername.getText();
        		userPassword = textPassword.getText();
        		userName = userName.trim();
        		userPassword = userPassword.trim();
        		int[] rowsSelected = tableLogins.getSelectedRows();
        		
        		// if statements to check if certain conditions are met to avoid errors
        		if(tableLogins.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a user to edit.");
        		} else if(userName.isEmpty() || userPassword.isEmpty()) {
        			JOptionPane.showMessageDialog(null, "Please enter a username and password.");
        		} else if(userName.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Name cannot contain '/'.");
        		} else if(userPassword.contains("/")) {
        			JOptionPane.showMessageDialog(null, "Password cannot contain '/'.");
        		} else if(rowsSelected.length > 1) {
        			JOptionPane.showMessageDialog(null, "Cannot edit multiple categories at once.");
        		} else if(tableLogins.getSelectedRow() == 0 && !Objects.equals(userName, "admin")) {
        			JOptionPane.showMessageDialog(null, "Cannot edit the administrator username.");
        		} else {
        			// for loop and if statement to check if the username exists in the table already
        			for(int i = 0; i < model.getRowCount(); i++) {
                    	if(Objects.equals(userName, model.getValueAt(i, 1)) && i != tableLogins.getSelectedRow()) {
                    		matchUser = true;
                    	}
                    }
                    
        			// if statement to decide whether user will be edited or not based on if the username was taken or not
                    if(matchUser) {
                    	matchUser = false;
                    	JOptionPane.showMessageDialog(null, "Username already in use.");
                    } else {
                    	// edits the values of the user in the table and notifies the user
                		model.setValueAt(userName, tableLogins.getSelectedRow(), 1);
                		model.setValueAt(userPassword, tableLogins.getSelectedRow(), 2);
                		JOptionPane.showMessageDialog(null, "Successfully edited the user.");
                    }
                }
        	}
        });
        contentPane.add(buttonEdit);
        
        buttonDelete = new JButton("Delete");
        buttonDelete.setFont(new Font("Times New Roman", Font.PLAIN, 35));
        buttonDelete.setBounds(325, 400, 125, 50);
        buttonDelete.setForeground(new java.awt.Color(255, 0, 51));
        buttonDelete.setBackground(Color.BLACK);
        buttonDelete.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// if statements to avoid errors
        		if(tableLogins.getSelectedRow() < 0) {
        			JOptionPane.showMessageDialog(null, "Please select a user to delete.");
        		} else if(Integer.valueOf(String.valueOf(model.getValueAt(tableLogins.getSelectedRow(), 0))) == 1) {
        			JOptionPane.showMessageDialog(null, "Cannot delete the administrator.");
        		} else {
        			// confirm dialog to make sure the user would like to deleted the selected user(s)
        			int a = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected user(s)?", "Delete User", JOptionPane.YES_NO_OPTION);
        			if (a == JOptionPane.YES_OPTION) {
            			int[] selectedRows = tableLogins.getSelectedRows();
            			// for loop to go through every user selected
                		for(int i = 0; i < selectedRows.length; i++) {
                			// gets the user's ID and removes the row from the table
                			userID = String.valueOf(tableLogins.getValueAt(selectedRows[0], 0));
                			model.removeRow(selectedRows[0]);
                			
                			// sets the directory to delete based on the user's ID and then deletes everything inside of it
                			pathToDelete = String.format("%s\\Database\\%s", UserLogin.databasePath, userID);
                			fileToDelete = new File(pathToDelete);
                			if(fileToDelete.exists()) {
                				String[] entries = fileToDelete.list();
                				for(String s: entries){
                				    File currentFile = new File(fileToDelete.getPath(),s);
                				    currentFile.delete();
                				}
                				fileToDelete.delete();
                			}
                		}
                		
                		// clears the text fields and notifies the user
            			textUserID.setText("");
            			textUsername.setText("");
            			textPassword.setText("");
            			JOptionPane.showMessageDialog(null, "Successfully deleted the selected user(s).");
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
        		// clears the text fields
        		textUserID.setText("");
        		textUsername.setText("");
        		textPassword.setText("");
        	}
        });
        contentPane.add(buttonClear);
        
        tableLogins = new JTable(model) {
        	private static final long serialVersionUID = 1L;
        	public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
             }
        };
        tableLogins.setFont(new java.awt.Font("Century Gothic", Font.PLAIN, 24));
        tableLogins.getTableHeader().setForeground(Color.BLACK);
        tableLogins.getTableHeader().setBackground(Color.RED);
        tableLogins.getTableHeader().setReorderingAllowed(false);
        tableLogins.setForeground(new java.awt.Color(255, 0, 51));
        tableLogins.setBackground(Color.BLACK);
        tableLogins.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableLogins.setRowHeight(30);
        tableLogins.setSelectionBackground(new java.awt.Color(255, 0, 51));
        tableLogins.setBounds(475, 100, 500, 450);
        tableLogins.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent evt) {
        		// if statement to check if there is a value in the table to avoid errors
                if(tableLogins.getRowCount() > 0 && tableLogins.getSelectedRow() >= 0) {
                	// sets the text in the text field to the values in the row selected
                	int row = tableLogins.getSelectedRow();
                    textUserID.setText(model.getValueAt(row, 0).toString());
                    textUsername.setText(model.getValueAt(row, 1).toString());
                    textPassword.setText(model.getValueAt(row, 2).toString());
                }
            }
        });
        jScrollPane = new JScrollPane(tableLogins);
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
        
        // creates the logins file if it does not exist
        if(!UserLogin.loginsFile.exists()) {
        	try {
				UserLogin.loginsFile.createNewFile();
				FileWriter fw = new FileWriter(UserLogin.loginsFile.getAbsoluteFile());
             	BufferedWriter bw = new BufferedWriter(fw);
             	bw.write(UserLogin.columnsIdentifierLogins);
             	bw.close();
             	fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        // reads the logins file and sets it inside the table
        try {
    		BufferedReader br = new BufferedReader(new FileReader(UserLogin.loginsFile));
    		String firstLine = br.readLine().trim();
        	String[] columnsName = firstLine.split(" , ");
        	model.setColumnIdentifiers(columnsName);
        	Object[] tableLines = br.lines().toArray();
        	
        	for(int i = 0; i < tableLines.length; i++) {
        		String line = tableLines[i].toString().trim();
        		String[] dataRow = line.split(" / ");
        		model.addRow(dataRow);
        	}
        	br.close();
        	tableLogins.setModel(model);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		
    	}
	}
}
