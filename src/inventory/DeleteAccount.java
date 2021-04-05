package inventory;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class DeleteAccount extends JFrame {
	// declare variables
	private static final long serialVersionUID = 1L;
	String password;
	String passwordNew;
	String passwordConfirm;
	public static String pathToDelete;
	public static File fileToDelete;
	
	// declare GUI elements
    private JPanel contentPane;
    private JLabel labelDeleteAccount;
    private JLabel labelPassword;
    private JPasswordField textPassword;
    private JButton buttonCancel;
    private JButton buttonConfirm;
    private JTable tableLogins;
	
	public DeleteAccount() {
		// create the table model needed to store login information
		DefaultTableModel model = new DefaultTableModel(0, 0);
		
		// create and set the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 200, 1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Delete Account");
        setVisible(true);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // create and set GUI elements
        labelDeleteAccount = new JLabel("Delete Account");
        labelDeleteAccount.setForeground(Color.RED);
        labelDeleteAccount.setFont(new Font("Times New Roman", Font.BOLD, 60));
        labelDeleteAccount.setBounds(300, 0, 600, 75);
        contentPane.add(labelDeleteAccount);
        
        labelPassword = new JLabel("Password:");
        labelPassword.setForeground(Color.RED);
        labelPassword.setFont(new Font("Tahoma", Font.PLAIN, 35));
        labelPassword.setBounds(200, 200, 300, 75);
        contentPane.add(labelPassword);
        
        textPassword = new JPasswordField();
        textPassword.setFont(new Font("Tahoma", Font.PLAIN, 60));
        textPassword.setBounds(400, 200, 500, 75);
        contentPane.add(textPassword);
        
        buttonCancel = new JButton("Cancel");
        buttonCancel.setFont(new Font("Tahoma", Font.PLAIN, 60));
        buttonCancel.setBounds(75, 450, 250, 100);
        buttonCancel.setForeground(new java.awt.Color(255, 0, 51));
        buttonCancel.setBackground(Color.BLACK);
        buttonCancel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// goes to the main menu
        		dispose();
        		new UserHome();
        	}
        });
        contentPane.add(buttonCancel);
        
        buttonConfirm = new JButton("Confirm");
        buttonConfirm.setFont(new Font("Tahoma", Font.PLAIN, 60));
        buttonConfirm.setBounds(675, 450, 250, 100);
        buttonConfirm.setForeground(new java.awt.Color(255, 0, 51));
        buttonConfirm.setBackground(Color.BLACK);
        buttonConfirm.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// stores the password the user entered and checks if it matches the user's password
        		password = textPassword.getText().trim();
        		if(!Objects.equals(password, UserLogin.password)) {
        			JOptionPane.showMessageDialog(null, "Password is incorrect. Please try again.");
        		} else {
        			// confirm dialog to make sure the user would like to delete their account
        			int a = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete your account? This cannot be undone.", "Delete Account", JOptionPane.YES_NO_OPTION);
        			if (a == JOptionPane.YES_OPTION) {
        				// for loop and if statement to find and remove the user's login information from the table model
        				for(int i = 0; i < model.getRowCount(); i++) {
        					if(Integer.valueOf(String.valueOf(model.getValueAt(i, 0))) == UserLogin.id) {
        						model.removeRow(i);
        						i = model.getRowCount();
        					}
        				}
        				// rewrites the logins file with the user removed
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
        				
        				// find the user's directory to delete and deletes all content inside
        				pathToDelete = String.format("%s\\Database\\%s", UserLogin.databasePath, UserLogin.id);
            			fileToDelete = new File(pathToDelete);
            			if(fileToDelete.exists()) {
            				String[] entries = fileToDelete.list();
            				for(String s: entries){
            				    File currentFile = new File(fileToDelete.getPath(),s);
            				    currentFile.delete();
            				}
            				fileToDelete.delete();
            			}
            			
            			// goes to the login page and notifies the user
            			dispose();
            			new UserLogin();
            			JOptionPane.showMessageDialog(null, "Successfully deleted the account.");
        			}
        		}
        	}
        });
        contentPane.add(buttonConfirm);
        
        // creates the invisible table needed to store the login information
        tableLogins = new JTable(model);
        tableLogins.setVisible(false);
        
        try {
        	// creates a login file if it does not exist
        	if(!UserLogin.loginsFile.exists()) {
        		UserLogin.loginsFile.createNewFile();
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
        	
        	// reads the logins file and sets it into the assigned table model
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
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}
}
