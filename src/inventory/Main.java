/*
 * Author: Jeremy Sampl
 * 
 * Program Name: StockAssist
 * 
 * Program Description: StockAssist is an inventory management system that solves the problem of keeping track of lots of inventory on paper and having to do calculations by hand.
 * 		Automatically keeps track of all the quantities of the user's products in their inventory.
 * 		Automatically adjusts the product, category and customer information everywhere in the system with one click.
 * 		Allows products to be categorized by user-created categories.
 * 		Allows the management of customers and store their information for easy contacting.
 * 		Allows the user to create, edit, delete, and store orders, while also being able to sort through the orders based on the customer.
 * 
 * There are 4 main parts of this program:
 *	Categories:
 *		Creating categories allows you to organize your products a little better.
 *		Editing categories will automatically update on any product they are associated with.
 * 		Deleting categories will automatically remove the category from any product they are associated with.
 *	Products:
 *		Your products table will store all your product information, allowing for you to keep track of everything in your inventory, including quantities and prices.
 *		Editing products will automatically update all their information, including price and name, in every order they are in.
 * 		Deleting products will also automatically remove any products from your orders, while also automaticlly updating the total of the order.
 * 	Customers:
 * 		The customers table will store all of your customers' information, which is shown for every order you create.
 * 		Editing customers will automatically update all their information for all orders they have.
 * 		Deleting customers will also automatically delete any orders associated with the customer(s).
 * 	Orders:
 * 		Your orders are your central hub that connects everything together. You can create, edit and delete orders at will.
 * 		Editing any customer or product information will automatically update in every single one of the user's orders.
 * 		Deleting or editing or adding products to an order will automatically take it from your stock and get put back if the user chooses for it to do so.
 * 
 * Administrator Account:
 * The administrator account is the account where all other users login information can be accessed.
 * It provides the ability to manage users, like editing their username and password, or even creating or deleting users.
 * The administrator account cannot be deleted, however, it can be edited.
 * The administrator can also be used as an example of how to use the program.
 * Username: admin
 * Password: admin
 */

package inventory;

import java.awt.EventQueue;

public class Main {
	// launch the application
    public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new UserLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
