//java -cp .;"D:\PostgreSQL\pgJDBC\postgresql-42.2.24.jar" LookInnaBook
//For some reason only works with cmd line not powershell

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.math.BigDecimal;
import java.util.HashMap;

class Book{
	public String name;
	public BigDecimal isbn;
	public ArrayList<String> authors;
	public String publisher;
	public double price;
	public double royalty;
	
	public Book(String n, BigDecimal i, String p){
		name = n;
		isbn = i;
		publisher = p;
	}
	public Book(String n, BigDecimal i, String a, String p){
		name = n;
		isbn = i;
		authors = new ArrayList<String>();
		authors.add(a);
		publisher = p;
		price = 0;
		royalty = 0;
	}
	public Book(String n, BigDecimal i, String a, String p, double pr, double roy){
		name = n;
		isbn = i;
		authors = new ArrayList<String>();
		authors.add(a);
		publisher = p;
		price = pr;
		royalty = roy;
	}
	public void print(){
		String strPrice = String.format("%.2f", price);
		String strRoyalty = String.format("%.2f", royalty*100);
		System.out.println(this.name + " - " + this.isbn + "\t Price: $" + strPrice + " Royalty: " + strRoyalty +"%");
		System.out.printf("Authors: ");
		for(int i = 0; i <authors.size(); i++){
			System.out.printf(authors.get(i));
			if(i != authors.size()-1) { System.out.printf(", "); }
			else{System.out.printf("\n");}
		}
		System.out.println("Publisher: " + publisher + "\n");
	}
	public void addAuthor(String a){
		authors.add(a);
	}
}

class Collection{
	public String owner_username;
	public Book collection_book;
	public int stock;
	public double collection_price;
	public Collection(String ou, String bn, BigDecimal i, double pr, int s, String p){
		collection_book = new Book(bn, i, p);
		owner_username = ou;
		stock = s;
		collection_price = pr;
	}
	public void print(){
		String strPrice = String.format("%.2f", collection_price);
		System.out.println(collection_book.name + " - " + collection_book.isbn + " \tOwner: " + owner_username);
		System.out.println("Stock: " + stock + ", Price: $" + strPrice +"\n");
	}
	public void short_print(){
		String strPrice = String.format("%.2f", collection_price);
		System.out.println(collection_book.name + " \tOwner: " + owner_username + ", Price: $" + strPrice);
	}
		
		
}

class Cart{
	public int quantity;
	public Collection cart_item;
	public Cart(int q, Collection c){
		quantity = q;
		cart_item = c;
	}
	public void checkOut(String cust, String bank_num, int bill, int ship){
		System.out.println(cart_item.collection_book.name + "purchased. ");
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", "postgres", "Exhibition2012");
            PreparedStatement statement = connection.prepareStatement("call orderbook(?, ?, ?, ?, ?, ?, ?)");) {
			statement.setString(1, cust);
			statement.setString(2, cart_item.owner_username);
			statement.setLong(3, (cart_item.collection_book.isbn.longValue()));
			statement.setInt(4, quantity);
			statement.setInt(5, bill);
			statement.setInt(6, ship);
			statement.setString(7, bank_num);
			statement.execute();
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
	}
	public void print(){
		cart_item.short_print();
		System.out.println("Quantity: " + quantity+ "\n");
	}
}

class User{
	public String username;
	public String name;
	public String usertype;
	public ArrayList<Cart> shopping_cart;
	public String bank_num;
	public int ship_add;
	public int bill_add;
	public String email;
	public String company_name;
	public User(){
		username="";
		usertype="user";
		shopping_cart = new ArrayList<Cart>();
	}
	public void checkOut(){
		Scanner scanInput = new Scanner(System.in);
		System.out.printf("Bank Number: ");
		String temp_bank = scanInput.nextLine();
		System.out.println("Billing Address");
		System.out.println("---------------");
		int temp_billing = getAddress();
		System.out.println("Shipping Address");
		System.out.println("----------------");
		int temp_shipping = getAddress();
		if(usertype == "customer"){
			shopping_cart.forEach((c) -> { c.checkOut(username, temp_bank, temp_billing, temp_shipping); });
		}
		shopping_cart.clear();
	}
	public void logIn(){
		Scanner getInput = new Scanner(System.in);
		String newUserType = "";
		String checkUsername;
		boolean valid = false;
		if(this.usertype.equals("user")){
			while(!newUserType.equals("owner") && !newUserType.equals("customer")){
				System.out.printf("Log In as an owner or a customer?: ");
				newUserType = getInput.nextLine().toLowerCase();
			}
			System.out.printf("Input Username: ");
			checkUsername = getInput.nextLine();
			if(newUserType.equals("owner")){
				try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", "postgres", "Exhibition2012");
					PreparedStatement statement = connection.prepareStatement("select * from owner where username = ?");) {
					statement.setString(1, checkUsername);
					ResultSet resultSet = statement.executeQuery();
					while(resultSet.next()){
						 if(checkUsername.equals(resultSet.getString("username"))){
							 System.out.println("Log In Success");
							 this.username = resultSet.getString("username");
							 this.bank_num = resultSet.getString("bank_num");
							 this.email = resultSet.getString("email");
							 this.company_name = resultSet.getString("company_name");
							 this.usertype = "owner";
						 }
						 else{ System.out.println("Log In Attempt Failed"); }
					}
				} catch (Exception sqle) {
					System.out.println("Exception: " + sqle);
				}
			}
			else if(newUserType.equals("customer")){
				try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", "postgres", "Exhibition2012");
					PreparedStatement statement = connection.prepareStatement("select * from customer where username = ?");) {
					statement.setString(1, checkUsername);
					ResultSet resultSet = statement.executeQuery();
					while(resultSet.next()){
						if(checkUsername.equals(resultSet.getString("username"))){
							 System.out.println("Log In Success");
							 this.username = resultSet.getString("username");
							 this.name = resultSet.getString("name");
							 this.bank_num = resultSet.getString("bank_num");
							 this.usertype = "customer";
						}
						 else{ System.out.println("Log In Attempt Failed"); }
					}
				} catch (Exception sqle) {
					System.out.println("Exception: " + sqle);
				}
			}
		}
			
	}
	
	public void logOut(){
		username = "";
		name = "";
		usertype = "user";
		bank_num  = "";
		ship_add = 0;
		bill_add = 0;
		email = "";
		company_name = "";
	}
	
	public void addToCart(int q, Collection c){
		for(int i=0; i<shopping_cart.size(); i++){
			if(shopping_cart.get(i).cart_item.equals(c)){
				shopping_cart.get(i).quantity +=q;
				return;
			}
		}
		shopping_cart.add(new Cart(q, c));
	}
	
	public void viewCart(){
		for(int i=0; i<shopping_cart.size(); i++){
			System.out.printf("(" + i + ") ");
			shopping_cart.get(i).print();
		}
	}
	
	public void cartMenu(){
		Scanner scanInput = new Scanner(System.in);
		String inputCMD = "";
		int inputNum = 0;
		int quantityNum = 0;
		while(true){
			this.viewCart();
			if(this.shopping_cart.size() > 0)System.out.printf("Remove Book");
			if(this.usertype.equals("customer")) { System.out.printf(" | Check Out"); }
			System.out.printf(" | Exit/Return\n");
			System.out.printf("Choose: ");
			inputCMD = scanInput.nextLine().toLowerCase();
			if(inputCMD.equals("exit") || inputCMD.equals("return")){ return; }
			else if(this.usertype.equals("customer") && inputCMD.equals("check out")){
				this.checkOut();
			}
			else if(this.shopping_cart.size() > 0 || inputCMD.equals("remove book")){
				do{
					System.out.printf("Remove which book from the cart (by number): ");
					inputNum = scanInput.nextInt();
					scanInput.nextLine();
				}while(this.shopping_cart.size()<inputNum || inputNum<0);
				do{
					System.out.printf("Remove how many? (0 to cancel): ");
					quantityNum = scanInput.nextInt();
					scanInput.nextLine();
				}while(this.shopping_cart.get(inputNum).quantity < quantityNum || quantityNum<=0);
				if(quantityNum == 0){ System.out.printf("Remove Cancelled"); continue;}
				if(this.shopping_cart.get(inputNum).quantity - quantityNum == 0){
					this.shopping_cart.remove(inputNum);
				} else {this.shopping_cart.get(inputNum).quantity = this.shopping_cart.get(inputNum).quantity - quantityNum; }
			}
			else {System.out.println("Invalid Input");}
		}
	}
	public static int getAddress(){
		int address_id = 0;
		Scanner scanInput = new Scanner(System.in);
		String street_address = "";
		System.out.printf("street_address: ");
		street_address = scanInput.nextLine();
		String city = "";
		System.out.printf("city: ");
		city = scanInput.nextLine();
		String region = "";
		System.out.printf("region: ");
		region = scanInput.nextLine();
		String country = "";
		System.out.printf("country: ");
		country = scanInput.nextLine();
		String postalcode = "";
		System.out.printf("postalcode: ");
		postalcode = scanInput.nextLine();
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", "postgres", "Exhibition2012");
            PreparedStatement statement = connection.prepareStatement("insert into address(street_address, city, admin_area, country, postal_code) values(?, ?, ?, ?, ?) returning address_id");) {
			statement.setString(1, street_address);
			statement.setString(2, city);
			statement.setString(3, region);
			statement.setString(4, country);
			statement.setString(5, postalcode);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				address_id = resultSet.getInt("address_id");
			}
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
			
        }
		return address_id;
	}
}
	

public class LookInnaBook {
    public static void main(String[] args) {
		Scanner scanInput = new Scanner(System.in);
		User client = new User();
		String inputCMD = "";
		boolean exit = false;
		while(!exit){
			System.out.println("\nLookInnaBook " + client.usertype + " Menu");
			if(client.usertype.equals("user")){
				System.out.println("Browse Collections");
				System.out.println("View Cart");
				System.out.println("Log In");
				System.out.println("Create Account");
				
			}
			else if(client.usertype.equals("customer")){
				System.out.println("Browse Collections");
				System.out.println("View Cart");
				System.out.println("View Profile");
				System.out.println("Log Out");
			}
			else if(client.usertype.equals("owner")){
				System.out.println("Browse Collections");
				System.out.println("Browse Books");
				System.out.println("Own Collection");
				//System.out.println("View Profile");
				System.out.println("Log Out");
			}
			else{
				client.usertype = "user";
			}
			System.out.println("Exit");
			System.out.printf("Choose: ");
			inputCMD = scanInput.nextLine().toLowerCase();
			System.out.println("-------------");
			if(inputCMD.equals("x") || inputCMD.equals("exit")){
				exit = true;
			}
			else if(inputCMD.equals("log in") && client.usertype.equals("user")){
				client.logIn();
			}
			else if(inputCMD.equals("log out") && (client.usertype.equals("customer") || client.usertype.equals("owner"))){
				client.logOut();
			}
			else if(inputCMD.equals("browse collections")){
				collectionsMenu(client);
			}
			else if(inputCMD.equals("view cart") && (client.usertype.equals("customer") || client.usertype.equals("user"))){
				client.cartMenu();
			}
			else if(inputCMD.equals("view profile") && client.usertype.equals("customer")){
				customerProfile(client);
			}
			else if(inputCMD.equals("create account") && client.usertype.equals("user")){
				createAccount(client);
			}
			else if(inputCMD.equals("browse books") && client.usertype.equals("owner")){
				bookMenu(client);
			}
			else if(inputCMD.equals("own collection") && client.usertype.equals("owner")){
				ArrayList<Collection> collections = getCollections("%%", client.username);
				for(int i=0; i<collections.size(); i++){
					System.out.printf("(" + i + ") ");
					collections.get(i).print();
				}
			}
			else{
				System.out.println("Invalid Input");
			}
		}
    }
	
	public static void customerProfile(User client){
		System.out.println("Order History");
		System.out.println("-------------");
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", "postgres", "Exhibition2012");
            PreparedStatement statement = connection.prepareStatement("select * from customerprofile where customer_username = ?");
			)
		{
			statement.setString(1, client.username);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				System.out.println(resultSet.getString("book_name") + "\t Cost: $" + resultSet.getDouble("orderprice") + "\t Quantity: " +resultSet.getInt("quantity")+ 
				"\nFrom: " + resultSet.getString("owner_username") + " \tDate: " + resultSet.getObject("transfer_date") + "\n");
			}
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
			
        }
	}
	
	public static void createAccount(User client){
		Scanner getInput = new Scanner(System.in);
		String newUserType = "";
		String newUsername = "";
		while(!newUserType.equals("owner") && !newUserType.equals("customer")){
			System.out.printf("Sign Up as an owner or a customer?: ");
			newUserType = getInput.nextLine().toLowerCase();
		}
		System.out.printf("Input Username: ");
		newUsername = getInput.nextLine();
		if(newUserType.equals("owner")){
				try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", "postgres", "Exhibition2012");
					PreparedStatement statement = connection.prepareStatement("insert into owner(username) values(?) returning username");) {
					statement.setString(1, newUsername);
					System.out.println(statement.toString());
					ResultSet resultSet = statement.executeQuery();
					while(resultSet.next()){
						client.username = resultSet.getString("username");
						client.usertype = "owner";
					}
				} catch (Exception sqle) {
					System.out.println("Exception: " + sqle);
				}
			}
			else if(newUserType.equals("customer")){
				try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", "postgres", "Exhibition2012");
					PreparedStatement statement = connection.prepareStatement("insert into customer(username) values(?) returning username");) {
					statement.setString(1, newUsername);
					ResultSet resultSet = statement.executeQuery();
					while(resultSet.next()){
						client.username = resultSet.getString("username");
						client.usertype = "customer";
					}
				} catch (Exception sqle) {
					System.out.println("Exception: " + sqle);
				}
			}
		
	}
	
	public static void collectionsMenu(User client){
		Scanner scanInput = new Scanner(System.in);
		String inputCMD = "";
		int inputNum = 0;
		int quantityNum = 0;
		ArrayList<Collection> collections = getCollections("%%", "%%");
		for(int i=0; i<collections.size(); i++){
			System.out.printf("(" + i + ") ");
			collections.get(i).print();
		}
		while(true){
			System.out.printf("View Book | Search Books");
			if(client.usertype.equals("customer") || client.usertype.equals("user")) { System.out.printf(" | Add Book to Cart"); }
			System.out.printf(" | Exit/Return\n");
			System.out.printf("Choose: ");
			inputCMD = scanInput.nextLine().toLowerCase();
			if(inputCMD.equals("exit") || inputCMD.equals("return")){ return; }
			else if(inputCMD.equals("search books")){
				System.out.printf("Search for Books with * in the name: ");
				inputCMD = scanInput.nextLine().toLowerCase();
				for(int i=0; i<collections.size(); i++){
					if((collections.get(i).collection_book.name).toLowerCase().contains(inputCMD)){
						System.out.printf("(" + i + ") ");
						collections.get(i).print();
					}
				}
			}
			else if(inputCMD.equals("add book to cart") && (client.usertype.equals("customer") || client.usertype.equals("user"))){
				do{
					System.out.printf("Add which book to the cart (by number): ");
					inputNum = scanInput.nextInt();
					scanInput.nextLine();
				}while(collections.size()<inputNum || inputNum<0);
				do{
					System.out.printf("Buy how many? (0 to cancel): ");
					quantityNum = scanInput.nextInt();
					scanInput.nextLine();
				}while(collections.get(inputNum).stock < quantityNum || quantityNum<=0);
				if(quantityNum == 0){ System.out.printf("Order Cancelled"); continue;}
				client.addToCart(quantityNum, collections.get(inputNum));
			}
			else {System.out.println("Invalid Input");}
			
		}
	}
	
	public static ArrayList<Collection> getCollections(String inbook, String inowner){
		ArrayList<Collection> collections = new ArrayList<Collection>();
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", "postgres", "Exhibition2012");
            PreparedStatement statement = connection.prepareStatement("select * from viewstore where book_name like ? and owner_username like ?");
			)
		{
			statement.setString(1, inbook);
			statement.setString(2, inowner);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				 collections.add(new Collection(resultSet.getString("owner_username"), resultSet.getString("book_name"), resultSet.getBigDecimal("isbn"), 
				 resultSet.getDouble("price"), resultSet.getInt("stock"), resultSet.getString("pub_email")));
			}
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
			
        }
		return collections;
	}
	
	public static void bookMenu(User client){
		Scanner scanInput = new Scanner(System.in);
		String inputCMD = "";
		BigDecimal inISBN;
		int quantityNum = 0;
		BigDecimal setPrice;
		HashMap<BigDecimal, Book> offeredBooks;
		while(true){
			System.out.println("---------------------------------\n");
			offeredBooks = getBooks();
			for (BigDecimal i : offeredBooks.keySet()) {
				System.out.printf("("+i+"): ");
				offeredBooks.get(i).print();
			}
			System.out.println("Stock Book | Exit/Return\n");
			System.out.printf("Choose: ");
			inputCMD = scanInput.nextLine().toLowerCase();
			if(inputCMD.equals("exit") || inputCMD.equals("return")){ return; }
			else if(inputCMD.equals("stock book")) { 
				System.out.printf("Add which book to the cart (isbn): ");
				inISBN = scanInput.nextBigDecimal();
				scanInput.nextLine();
				if(offeredBooks.containsKey(inISBN)){
					do{
					System.out.printf("Buy how many? (0 to cancel): ");
					quantityNum = scanInput.nextInt();
					scanInput.nextLine();
					}while(quantityNum<0);
					if(quantityNum == 0){ System.out.printf("Order Cancelled"); continue;}
					do{
						System.out.printf("Set the price($): ");
						setPrice = scanInput.nextBigDecimal();
						scanInput.nextLine();
					}while(setPrice.longValue()<0);
					addBooktoCollection(client, inISBN, quantityNum, setPrice);
				}
				else { System.out.println("Book with ISBN " + inISBN.longValue() + " does not exist\n"); }
			}
			else {System.out.println("Invalid Input");}
		}
	}
	public static void addBooktoCollection(User client, BigDecimal inISBN, int quantityNum, BigDecimal price){
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", "postgres", "Exhibition2012");
			PreparedStatement statement = connection.prepareStatement("call buyfrompublisher(?, ?, ?, ?)");) {
			statement.setString(1, client.username);
			statement.setBigDecimal(2, inISBN);
			statement.setInt(3, quantityNum);
			statement.setBigDecimal(4, price);
			statement.execute();
		} catch (Exception sqle) {
			System.out.println("Exception: " + sqle);
		}
		return;
	}
	
	public static HashMap<BigDecimal, Book> getBooks(){
		HashMap<BigDecimal, Book> books = new HashMap<BigDecimal, Book>();
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", "postgres", "Exhibition2012");
            PreparedStatement statement = connection.prepareStatement("select * from allbooks");
			)
		{
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				if (!books.containsKey(resultSet.getBigDecimal("isbn"))){
					books.put(resultSet.getBigDecimal("isbn"), new Book(resultSet.getString("name"), resultSet.getBigDecimal("isbn"), 
					resultSet.getString("author"), resultSet.getString("pub_name"), resultSet.getDouble("price"), resultSet.getDouble("royalty")));
				}
				else{
					books.get(resultSet.getBigDecimal("isbn")).addAuthor(resultSet.getString("author"));
				}
			}
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
			
        }
		return books;
	}
		
}


