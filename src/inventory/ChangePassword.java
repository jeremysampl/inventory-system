package inventory;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class ChangePassword extends JFrame {
	// declare variables
	private static final long serialVersionUID = 1L;
	String passwordOld;
	String passwordNew;
	String passwordConfirm;
	
	// declare GUI elements
    private JPanel contentPane;
    private JLabel labelChangePassword;
    private JLabel labelPasswordOld;
    private JLabel labelPasswordNew;
    private JLabel labelPasswordConfirm;
    private JPasswordField textPasswordOld;
    private JPasswordField textPasswordNew;
    private JPasswordField textPasswordConfirm;
    private JButton buttonCancel;
    private JButton buttonConfirm;
    private JTable loginsTable;
	
	public ChangePassword() {
		// create the table model needed to store the login information
		DefaultTableModel model = new DefaultTableModel(0, 0);
		
		// create and set the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 200, 1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Change Password");
        setVisible(true);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // create and set GUI elements
        labelChangePassword = new JLabel("Change Password");
        labelChangePassword.setForeground(Color.RED);
        labelChangePassword.setFont(new Font("Times New Roman", Font.BOLD, 60));
        labelChangePassword.setBounds(275, 0, 600, 75);
        contentPane.add(labelChangePassword);
        
        labelPasswordOld = new JLabel("Previous Password:");
        labelPasswordOld.setForeground(Color.RED);
        labelPasswordOld.setFont(new Font("Tahoma", Font.PLAIN, 35));
        labelPasswordOld.setBounds(75, 100, 300, 75);
        contentPane.add(labelPasswordOld);
        
        labelPasswordNew = new JLabel("New Password:");
        labelPasswordNew.setForeground(Color.RED);
        labelPasswordNew.setFont(new Font("Tahoma", Font.PLAIN, 35));
        labelPasswordNew.setBounds(125, 200, 250, 75);
        contentPane.add(labelPasswordNew);
        
        labelPasswordConfirm = new JLabel("Comfirm Password:");
        labelPasswordConfirm.setForeground(Color.RED);
        labelPasswordConfirm.setFont(new Font("Tahoma", Font.PLAIN, 35));
        labelPasswordConfirm.setBounds(25, 300, 350, 75);
        contentPane.add(labelPasswordConfirm);
        
        textPasswordOld = new JPasswordField();
        textPasswordOld.setFont(new Font("Tahoma", Font.PLAIN, 60));
        textPasswordOld.setBounds(400, 100, 500, 75);
        contentPane.add(textPasswordOld);
        
        textPasswordNew = new JPasswordField();
        textPasswordNew.setFont(new Font("Tahoma", Font.PLAIN, 60));
        textPasswordNew.setBounds(400, 200, 500, 75);
        contentPane.add(textPasswordNew);
        
        textPasswordConfirm = new JPasswordField();
        textPasswordConfirm.setFont(new Font("Tahoma", Font.PLAIN, 60));
        textPasswordConfirm.setBounds(400, 300, 500, 75);
        contentPane.add(textPasswordConfirm);
        
        buttonCancel = new JButton("Cancel");
        buttonCancel.setForeground(Color.RED);
        buttonCancel.setBackground(Color.BLACK);
        buttonCancel.setFont(new Font("Tahoma", Font.PLAIN, 60));
        buttonCancel.setBounds(75, 450, 250, 100);
        buttonCancel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// goes back to the main menu
        		dispose();
        		new UserHome();
        	}
        });
        contentPane.add(buttonCancel);
        
        buttonConfirm = new JButton("Confirm");
        buttonConfirm.setForeground(Color.RED);
        buttonConfirm.setBackground(Color.BLACK);
        buttonConfirm.setFont(new Font("Tahoma", Font.PLAIN, 60));
        buttonConfirm.setBounds(675, 450, 250, 100);
        buttonConfirm.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// stores the user input from the text boxes to their assigned variables
        		passwordOld = textPasswordOld.getText().trim();
        		passwordNew = textPasswordNew.getText().trim();
        		passwordConfirm = textPasswordConfirm.getText().trim();
        		
        		// if statements to check if the user entered the right information and to avoid errors
        		if(!Objects.equals(passwordOld, UserLogin.password)) {
        			JOptionPane.showMessageDialog(null, "Password is incorrect. Please try again.");
        		} else if(!Objects.equals(passwordNew, passwordConfirm)) {
        			JOptionPane.showMessageDialog(null, "New password does not match. Please try again.");
        		} else if(passwordNew.contains("/")) {
        			JOptionPane.showMessageDialog(null, "The new password cannot contain '/'.");
        		} else if(!Objects.equals(passwordNew, passwordNew.replaceAll("\\s+",""))) {
        			JOptionPane.showMessageDialog(null, "The new password cannot contain spaces.");
        		} else {
        			try {
        				// reads the logins file and sets it inside of the table model
                        BufferedReader br = new BufferedReader(new FileReader(UserLogin.loginsFile));
                        String firstLine = br.readLine().trim();
                    	String[] columnsName = firstLine.split(" , ");
                        Object[] tableLines = br.lines().toArray();
                        
                        for(int i = 0; i < tableLines.length; i++) {
                    		String line = tableLines[i].toString().trim();
                    		String[] dataRow = line.split(" / ");
                    		if(Integer.parseInt(dataRow[0]) == UserLogin.id) {
                    			model.setValueAt(passwordNew, i, 2);
                    			i = tableLines.length;
                    		}
                    	}
                        br.close();
                        
                        // try/catch to rewrite the file with the updated password
                        try {
                         	FileWriter fw = new FileWriter(UserLogin.loginsFile.getAbsoluteFile());
                         	BufferedWriter bw = new BufferedWriter(fw);
                         	
                         	bw.write(UserLogin.columnsIdentifierLogins);
                         	for(int i = 0; i < loginsTable.getRowCount(); i++) {
                         		for(int j = 0; j < loginsTable.getColumnCount(); j++) {
                         			bw.write((String)loginsTable.getModel().getValueAt(i, j));
                         			if(j < loginsTable.getColumnCount() - 1) {
                         				bw.write(" / ");
                         			}
                         		}
                         		bw.write("\n");
                         	}
                         	bw.close();
                         	fw.close();
                         	
                         	// goes to the main menu and notifies the user
                         	dispose();
                         	new UserHome();
                         	JOptionPane.showMessageDialog(null, "You have successfully changed your password.");
                         } catch(Exception ex) {
                         	ex.printStackTrace();
                         }
        			} catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException e1) {
    					e1.printStackTrace();
    				}  
        		}
        	}
        });
        contentPane.add(buttonConfirm);
        
        // creates the invisible table used to rewrite the logins file after changing the password
        loginsTable = new JTable(model);
        loginsTable.setVisible(false);
        
        try {
        	// creates the login file if it does not exist
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
        	// reads the login file and sets it in the table model
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
