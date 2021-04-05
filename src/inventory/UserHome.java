package inventory;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class UserHome extends JFrame {
	// declare variables and GUI elements
	private static final long serialVersionUID = 1L;
    private JButton buttonManageUsers;
    private JLabel labelTitle;
    private JLabel iconUser;
    private JLabel labelUserName;
    private JLabel iconCategories;
    private JLabel labelCategories;
    private JLabel iconProducts;
    private JLabel labelProducts;
    private JLabel iconCustomers;
    private JLabel labelCustomers;
    private JLabel iconOrders;
    private JLabel labelOrders;
    private JComboBox<String> boxAccount;
    private JPanel contentPane;
    
    public UserHome() {
    	// set the settings of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 200, 1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Main Menu");
        setVisible(true);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // create and customize GUI elements
        labelTitle = new JLabel("StockAssist");
        labelTitle.setFont(new Font("Times New Roman", Font.BOLD, 75));
        labelTitle.setForeground(Color.RED);
        labelTitle.setBackground(Color.BLACK);
        labelTitle.setOpaque(true);
        labelTitle.setHorizontalAlignment(JLabel.CENTER);
        labelTitle.setBounds(0, 0, 1000, 100);
        contentPane.add(labelTitle);
        
        // if statement to check if the user is logged in as the administrator
        if(UserLogin.id == 1) {
	        buttonManageUsers = new JButton("Manage Users");
	        buttonManageUsers.setForeground(Color.RED);
	        buttonManageUsers.setBackground(Color.BLACK);
	        buttonManageUsers.setBounds(750, 10, 225, 80);
	        buttonManageUsers.setFont(new Font("Tahoma", Font.PLAIN, 30));
	        buttonManageUsers.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	dispose();
	                new ManageUsers();
	            }
	        });
	        labelTitle.add(buttonManageUsers);
        }
        
        iconUser = new JLabel(new ImageIcon(String.format("%s\\Images\\User.png", UserLogin.databasePath)));
        iconUser.setBounds(10, 10, 80, 80);
        labelTitle.add(iconUser);
        
        labelUserName = new JLabel(UserLogin.userName);
        labelUserName.setFont(new Font("Tahoma", Font.PLAIN, 25));
        labelUserName.setForeground(Color.RED);
        labelUserName.setBounds(100, 20, 150, 25);
        labelTitle.add(labelUserName);
        
        boxAccount = new JComboBox<String>();
        boxAccount.setFont(new Font("Tahoma", Font.PLAIN, 18));
        boxAccount.setForeground(Color.RED);
        boxAccount.setBackground(Color.BLACK);
        boxAccount.setBounds(100, 50, 150, 25);
        boxAccount.addItem("Account");
        boxAccount.addItem("Logout");
        if(UserLogin.id > 1) {
        	boxAccount.addItem("Change Password");
        	boxAccount.addItem("Delete Account");
        }
        boxAccount.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// if statements to check which option the user clicked on
        	   if(boxAccount.getSelectedIndex() == 1) {
        		   int exit = JOptionPane.showConfirmDialog(null, "Are you sure you would like to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        		   // if statement to check if the user confirmed their choice
        		   if(exit == JOptionPane.YES_OPTION) {
        			   dispose();
        			   new UserLogin();
        		   }
        	   } else if(boxAccount.getSelectedIndex() == 2) {
        		   int exit = JOptionPane.showConfirmDialog(null, "Are you sure you would like to change your password?", "Change Password", JOptionPane.YES_NO_OPTION);
        		   // if statement to check if the user confirmed their choice
        		   if(exit == JOptionPane.YES_OPTION) {
        			   dispose();
        			   new ChangePassword();
        		   }
        	   } else if(boxAccount.getSelectedIndex() == 3) {
        		   int exit = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete your account? This cannot be undone.", "Delete Account", JOptionPane.YES_NO_OPTION);
        		   // if statement to check if the user confirmed their choice
        		   if(exit == JOptionPane.YES_OPTION) {
        			   dispose();
        			   new DeleteAccount();
        		   }
        	   }
        	   boxAccount.setSelectedIndex(0);
           }
        });
        labelTitle.add(boxAccount);
        
        iconCategories = new JLabel(new ImageIcon(String.format("%s\\Images\\Categories.png", UserLogin.databasePath)));
        iconCategories.setBounds(200, 125, 150, 150);
        iconCategories.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	// goes to the categories class when the user clicks on the image
            	dispose();
            	new Categories();
            }
        });
        contentPane.add(iconCategories);
        
        labelCategories = new JLabel("Categories");
        labelCategories.setHorizontalAlignment(JLabel.CENTER);
        labelCategories.setFont(new Font("Tahoma", Font.BOLD, 40));
        labelCategories.setForeground(Color.RED);
        labelCategories.setBounds(150, 275, 250, 50);
        contentPane.add(labelCategories);
        
        iconProducts = new JLabel(new ImageIcon(String.format("%s\\Images\\Products.png", UserLogin.databasePath)));
        iconProducts.setBounds(650, 125, 150, 150);
        iconProducts.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	// goes to the products class when the user clicks on the image
            	dispose();
            	new Products();
            }
        });
        contentPane.add(iconProducts);
        
        labelProducts = new JLabel("Products");
        labelProducts.setHorizontalAlignment(JLabel.CENTER);
        labelProducts.setFont(new Font("Tahoma", Font.BOLD, 40));
        labelProducts.setForeground(Color.RED);
        labelProducts.setBounds(600, 275, 250, 50);
        contentPane.add(labelProducts);
        
        iconCustomers = new JLabel(new ImageIcon(String.format("%s\\Images\\Customers.png", UserLogin.databasePath)));
        iconCustomers.setBounds(200, 350, 150, 127);
        iconCustomers.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	// goes to the customers class when the user clicks on the image
            	dispose();
            	new Customers();
            }
        });
        contentPane.add(iconCustomers);
        
        labelCustomers = new JLabel("Customers");
        labelCustomers.setHorizontalAlignment(JLabel.CENTER);
        labelCustomers.setFont(new Font("Tahoma", Font.BOLD, 40));
        labelCustomers.setForeground(Color.RED);
        labelCustomers.setBounds(150, 500, 250, 50);
        contentPane.add(labelCustomers);
        
        iconOrders = new JLabel(new ImageIcon(String.format("%s\\Images\\Orders.png", UserLogin.databasePath)));
        iconOrders.setFont(new Font("Tahoma", Font.PLAIN, 30));
        iconOrders.setBounds(665, 350, 150, 150);
        iconOrders.setBackground(UIManager.getColor("Button.disabledForeground"));
        iconOrders.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	// goes to the orders class when the user clicks on the image
            	dispose();
            	new Orders();
            }
        });
        contentPane.add(iconOrders);
        
        labelOrders = new JLabel("Orders");
        labelOrders.setHorizontalAlignment(JLabel.CENTER);
        labelOrders.setFont(new Font("Tahoma", Font.BOLD, 40));
        labelOrders.setForeground(Color.RED);
        labelOrders.setBounds(600, 500, 250, 50);
        contentPane.add(labelOrders);
    }
}