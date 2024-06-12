
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
public class Library{

static  Connection con = null;
    
static{
        try {
             final String URL = "jdbc:mysql://localhost:3306/Library";
    	     final String USER = "root";
    	     final String PASSWORD = ""; 
            Class.forName("com.mysql.cj.jdbc.Driver");
            con  = DriverManager.getConnection(URL, USER, PASSWORD);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
                    
		   Scanner sc =new Scanner(System.in);
		     String bookId="";
		     String bookName="";
		     String authorName="";
		     String memId="";
		     String memName="";String borrowDate="";
		     String actualReturnDate="";
		     String returnDate="";
		     String status="";
		     String returnStatus="";
		     Long contact=0l;
		      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		      Date date = new Date();

    public void addAdmin(String adminId, String password, String name) {
        String sql = "INSERT INTO Admin(Admin_id, Password, Name) VALUES (?, ?, ?)";
        try {
	    PreparedStatement pstmnt = con.prepareStatement(sql);
            pstmnt.setString(1, adminId);
            pstmnt.setString(2, password);
            pstmnt.setString(3, name);
            pstmnt.executeUpdate();
            System.out.println("New admin added successfully.");
        } catch (SQLException e) {
           System.out.println("Admin Id or password already exists");
        }
        
    }
     public void userLogin(String userId) {
        String query = "SELECT * FROM User WHERE User_id = ?";
        try {
             PreparedStatement pstmnt = con.prepareStatement(query);
            pstmnt.setString(1, userId);
            ResultSet rs = pstmnt.executeQuery();
            if (rs.next()) {
                System.out.println("User logged in successfully");
                showUserMenu();
            } else {
                System.out.println("Invalid username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void login(String adminId, String password) {
        String query = "SELECT * FROM Admin WHERE Admin_id = ? AND Password = ?";
        try {
        PreparedStatement pstmnt = con.prepareStatement(query);
            pstmnt.setString(1, adminId);
            pstmnt.setString(2, password);
            ResultSet rs = pstmnt.executeQuery();
            if (rs.next()) {
                System.out.println("Logged in successfully");
                showAdminMenu();
            } else {
                System.out.println("Invalid username or password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void userRegister(String userId, String userName, String contact) {
        String query = "INSERT INTO User(User_id,User_name, Contact) VALUES(?, ?, ?)";

        try {
             PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, contact);
            preparedStatement.executeUpdate();
            System.out.println("User registered successfully");
        } catch (SQLException e) {
           	System.out.println("User already exists");
        }
    }
    public void addBook() throws Exception{
        System.out.println("enter the book_id :");
        String book_id=sc.nextLine();
        System.out.println("enter the book title :");
        String book_title=sc.nextLine();
        System.out.println("enter the Author name :");
        String book_auth=sc.nextLine();
        status = "available";
	createBook(new Book(book_id,book_title,book_auth,status));
        
}

public void createBook(Book b) {
  	
  	String query="insert into Book values(?,?,?,?)";
         try {
		PreparedStatement preparedStatement = con.prepareStatement(query);
		preparedStatement.setString(1,b.getBookId());
		preparedStatement.setString(2,b.getTitle());
		preparedStatement.setString(3,b.getAuthor());
		preparedStatement.setString(4,b.getStatus());
		preparedStatement.executeUpdate();
		System.out.println("Book added!!");
        }
        catch(SQLException e){
	
		 System.out.println("Book is already exist");
		
		
        }

}

    public List<Book> availableAllBook(){
    	List<Book> books = new ArrayList<>();
    	String query = "SELECT * FROM Book";
    	try{
    		Statement st = con.createStatement();
         	ResultSet rs = st.executeQuery(query);
         	while (rs.next()) {
                         String book_id = rs.getString(1);
                         String Title =  rs.getString(2);
                         String Author = rs.getString(3); 
                         String Status = rs.getString(4);
                         Book book = new Book(book_id,Title,Author,Status);
                         books.add(book);
            	}
            	
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	return books;
    }

    public void availableParticularBook(String title)  {
  		  int isPresent = 0;
		String query="Select * from Book where Title=?";
		try{
		PreparedStatement st = con.prepareStatement(query);
		st.setString(1,title);
		ResultSet rs=st.executeQuery();
		
		   while (rs.next()) {
				int columnCount = rs.getMetaData().getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					isPresent=1;
					
					System.out.print(rs.getString(i) + "\t");
				}
				System.out.println();
			    }
				if(isPresent == 0){
						System.out.print("The book is not Available");
			}

		}
		catch(Exception e){
		e.getMessage();
		}

    }
	public void getBookByAuthor(String Author){
		String query = "SELECT Title FROM Book WHERE Author = ?";
		try{
			PreparedStatement stmnt = con.prepareStatement(query);
			stmnt.setString(1,Author);
			ResultSet rs = stmnt.executeQuery();
			System.out.println("The books written by "+Author+" is:\n");
			while(rs.next()){
				System.out.println(rs.getString(1)+"\n");
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    public void removeBook(String id){
    String query="delete from Book where Book_id=?";
    try {
	    PreparedStatement st = con.prepareStatement(query);
	    st.setString(1,id);
	    int rows = st.executeUpdate();
		            if (rows > 0) { 
		                System.out.println("book deleted successfully.");
		            } else {
		                System.out.println("No book found with the specified id.");
		            }
    } catch(Exception e){
   	 e.getMessage();
    }
    }
    public void penalty(String bookId,String memId){

	      int daysBetween=0;
	      String dueDate = "";
	      String retDate = "";  
	      String query1="select Return_Date from Borrow where Book_id=? AND User_id=? AND Return_Status=?";
	     try{
	     PreparedStatement p=con.prepareStatement(query1);
	     p.setString(1,bookId);
	     p.setString(2,memId);
	     p.setString(3,"pending");
	     ResultSet rs=p.executeQuery();
	     if(rs.next()){
	       dueDate=rs.getString(1);
	       //retDate=rs.getString(2);
	     }
	try {
	      Date dateBefore = sdf.parse(dueDate);
	      Date dateAfter = date;
	      long difference = dateAfter.getTime() - dateBefore.getTime();
	      daysBetween = (int)(difference / (1000*60*60*24));
		       
	      System.out.println("Number of Days between dates: "+daysBetween);
	     
	     
	} catch (Exception e) {
	      e.printStackTrace();
	}

	if(daysBetween>0){

		String query2="update Borrow set Day_exceed=? ,           =? where Book_id=? AND User_id=? AND Return_Status=?";
		PreparedStatement p1=con.prepareStatement(query2);
		p1.setInt(1,daysBetween);
		p1.setInt(2,daysBetween*2);
		p1.setString(3,bookId);
		p1.setString(4,memId);
		p1.setString(5,"pending");
		p1.executeUpdate();
	       
	}
		String query3="select *from Borrow where Book_id=? AND User_id=? AND Return_Status=?";
		PreparedStatement p2=con.prepareStatement(query3);
		p2.setString(1,bookId);
		p2.setString(2,memId);
		p2.setString(3,"pending");
		ResultSet rs1=p2.executeQuery();
	       
		if(rs1.next()){
			System.out.println( "Bookid : "+ rs1.getString(1));
			System.out.println("Bookname : "+rs1.getString(2));
			System.out.println("Author name : "+rs1.getString(3));
			System.out.println("Member id : "+rs1.getString(4));
			System.out.println("Member name : "+rs1.getString(5));
			System.out.println("Borrow date : "+rs1.getString(6));
			System.out.println("Due date : "+rs1.getString(7));
			System.out.println("Date of returned "+rs1.getString(8));
			System.out.println("Return status : "+rs1.getString(9));  
			System.out.println("Contact : "+rs1.getLong(10));
			System.out.println("Date exceed : "+rs1.getInt(11));
			System.out.println("Total fine : "+rs1.getInt(12));
		}
		else{
			System.out.println("Invalid bookId or userId");
		}
		}
		catch(Exception e){
			e.getMessage();
		}
	     
           
}

	public void userHistory(){

		String query="select * from Borrow";
		try{
		PreparedStatement p=con.prepareStatement(query);
		ResultSet rs=p.executeQuery();

		System.out.printf("%-10s %-15s %-15s %-10s %-15s %-12s %-12s %-12s %-15s %-10s %-12s %-10s%n",
    "BOOK_ID", "book_name", "Author_name", "MEMBER_ID", "Member_Name", "borrow_date", "due_date", "return_date", "book_status", "Contact", "Date_exceed", "Total_fine");

		while (rs.next()) {
		    System.out.printf("%-10s %-15s %-15s %-10d %-15s %-12s %-12s %-12s %-15s %-10s %-12d %-10d%n",
			rs.getString(1),  
			rs.getString(2), 
			rs.getString(3),  
			rs.getLong(4),    
			rs.getString(5),  
			rs.getString(6), 
			rs.getString(7),  
			rs.getString(8),  
			rs.getString(9),  
			rs.getString(10), 
			rs.getInt(11),   
			rs.getInt(12));   
		}

		}catch(Exception e){
		e.printStackTrace();
		}

	}

	public void borrowBook() {
		Calendar cal=Calendar.getInstance();


		System.out.println("enter the book Id ");
		bookId=sc.next();
		System.out.println("enter the Member Id ");
		memId=sc.next();
		String query ="select Title,Author,Status from Book where Book_id=?";

		try{
		PreparedStatement p=con.prepareStatement(query);


			 p.setString(1,bookId);
		       
			 ResultSet rs=p.executeQuery();
		       
			 
			 if(rs.next()){
			       bookName=rs.getString(1);
			       authorName=rs.getString(2);
			       status=rs.getString(3);
			     
			       //System.out.println(rs.getString(1)+rs.getString(2)+rs.getString(3));
			       
			 }
			 
			 else{
			 System.out.println("no book id exist");
			 }
			 String query1="select User_name,Contact from User where User_id=?";
			   PreparedStatement p1=con.prepareStatement(query1);
			   p1.setString(1,memId);
			   ResultSet rs1=p1.executeQuery();
			   
			if(rs1.next()){
		       
			 memName=rs1.getString(1);
			 contact=rs1.getLong(2);
			 //System.out.println(rs1.getString(1)+" "+rs1.getLong(2));
			       
			 }
			 else{
			 System.out.println("no member id exist");
			 }
		}catch(Exception e){
			e.getMessage();
		}
		
		//System.out.println(status);
		 
		if(status.equalsIgnoreCase("Available")){
			borrowDate=sdf.format(date);
			cal.setTime(date);
			cal.add(Calendar.DATE,20);
			returnDate=sdf.format(cal.getTime());
			actualReturnDate="-";
			returnStatus="pending";
			       
			addBorrowBook(new 		Borrow(bookId,bookName,authorName,memId,memName,contact,borrowDate,returnDate,actualReturnDate,returnStatus));
		}
		else{
			System.out.println("Book is out of stock");
		}

	}
	public void addBorrowBook(Borrow b) {
		String query="insert into Borrow values(?,?,?,?,?,?,?,?,?,?,?,?)";
		try{
			PreparedStatement p=con.prepareStatement(query);

			p.setString(1,b.getBookId());
			p.setString(2,b.getBookName());
			p.setString(3,b.getAuthorName());
			p.setString(4,b.getMemberId());
			p.setString(5,b.getMemberName());
			p.setString(6,b.getBorrowDate());
			p.setString(7,b.getActualReturnDate());
			p.setString(8,b.getReturnDate());
			p.setString(9,b.getStatus());
			p.setLong(10,b.getContact());
			p.setInt(11,0);
			p.setInt(12,0);
			p.executeUpdate();
			 
			System.out.println("book borrowed successfully");
			String query1="update Book set Status=? where Book_id=?";
			PreparedStatement p1=con.prepareStatement(query1);
			p1.setString(1,"Not Available");
			p1.setString(2,b.getBookId());
			p1.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void returnBook(){
		//showReturnMenu();
		System.out.println("enter the book Id ");
		bookId=sc.next();
		System.out.println("enter the Member Id ");
		memId=sc.next();

		String query1="Update Borrow set Due_Date=? , Return_Status=? where Book_id=? AND User_id =? AND Return_Status=?";
		String  query2="update Book set Status=? where Book_id=?";
		try{
			PreparedStatement p=con.prepareStatement(query1);
			p.setString(1,sdf.format(date));
			p.setString(2,"returned");
			p.setString(3,bookId);
			p.setString(4,memId);
			p.setString(5,"pending");
			p.executeUpdate();
			System.out.println("Book Returned successfully");
			PreparedStatement p1=con.prepareStatement(query2);
			p1.setString(1,"available");
			p1.setString(2,bookId);
			p1.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void currentHoldBook(String id){
	 
		String query = "select * from Borrow where User_id=? AND Return_Status=?";

		try{
		PreparedStatement p=con.prepareStatement(query);
		p.setString(1,id);
		p.setString(2,"pending");
		ResultSet rs=p.executeQuery();

		System.out.printf("%-10s %-15s %-15s %-10s %-15s %-12s %-12s %-12s %-15s %-10s %-12s %-10s%n",
    "BOOK_ID", "book_name", "Author_name", "MEMBER_ID", "Member_Name", "borrow_date", "due_date", "return_date", "book_status", "Contact", "Date_exceed", "Total_fine");

		while (rs.next()) {
		    System.out.printf("%-10s %-15s %-15s %-10d %-15s %-12s %-12s %-12s %-15s %-10s %-12d %-10d%n",
			rs.getString(1),  
			rs.getString(2), 
			rs.getString(3),  
			rs.getLong(4),    
			rs.getString(5),  
			rs.getString(6), 
			rs.getString(7),  
			rs.getString(8),  
			rs.getString(9),  
			rs.getString(10), 
			rs.getInt(11),   
			rs.getInt(12));   
		}
		     
		   
		   
		     System.out.println();
		}
		
		catch(SQLException e){
		e.getMessage();
		}

	}
    private void showAdminMenu() {
                while(true){  
				System.out.println("\n-------------------------------------Menu---------------------------------------");
				System.out.println("1.New Admin");
				System.out.println("2.Add Books");
				System.out.println("3.Add Member");
				System.out.println("4.List of All Available books");
				System.out.println("5.Search Book by Name");
				System.out.println("6.Remove Book");
				System.out.println("7.Borrow Book");
				System.out.println("8.Return Book");
				System.out.println("9.Enter the Author name get a book written by him/her");
				System.out.println("10.User History");
				System.out.println("11.Current Holding Book");
				System.out.println("12.Exit");
				System.out.println("--------------------------------------------------------------------------------");
				System.out.println("Enter your choice");
				int choice = sc.nextInt();
				sc.nextLine();
				switch(choice){
					case 1:
						System.out.println("Enter the id:");
                                                 String admin_id = sc.nextLine();
						System.out.println("Enter the password:");
						String password = sc.nextLine();
						System.out.println("Enter the Name:");
						String name = sc.nextLine();
						addAdmin(admin_id,password,name);
						break;
					case 2:
        					 try {
							addBook();
						    } catch (Exception e) {
							e.printStackTrace();
						    }
						 break;
					case 3:
						System.out.println("Enter the UserId");
						String userId = sc.nextLine();
						System.out.println("Enter the UserName");
						String userName = sc.nextLine();
						System.out.println("Enter the Contact");
						String contact = sc.nextLine();
						userRegister(userId,userName,contact);
						break;
					case 4:
						try {
							for(Book book : availableAllBook()) {
							
							System.out.println(book);
							}
						    } catch (Exception e) {
							e.printStackTrace();
						    }
						 break;
					case 5:
						try {
						    System.out.println("Enter the book name:");
						    String title = sc.nextLine();
                                                    availableParticularBook(title);
						} catch (Exception e) {
						    e.printStackTrace();
						}
						break;
					case 6:
						try{
							System.out.println("Enter the book id to be deleted");
							String book_id = sc.nextLine();
							removeBook(book_id);
						}catch(Exception e){
							e.printStackTrace();
						}
						break;
					case 7:
						try{
							borrowBook();
						}catch(Exception e){
							e.printStackTrace();
						}
						break;
					case 8:
						try{
							showReturnMenu();
						}catch(Exception e){
							e.printStackTrace();
						}
						break;
					case 9:
						System.out.println("Enter the Author name");
						String author = sc.nextLine();
						getBookByAuthor(author);
						break;
					case 10:
						userHistory();
						break;
					case 11:
						System.out.println("Enter the UserId");
						userId = sc.nextLine();
						currentHoldBook(userId);
						break;
					case 12:
						System.exit(0);
							try{
								con.close();
							}
							catch(SQLException e){
								e.printStackTrace();
							}
						break;
                                        default:
                                                System.out.println("Invalid choice");
                                                break;
				}
			}    
    }
    public void showUserMenu(){
        while(true){
			System.out.println("\n-------------------------------------Menu---------------------------------------");
				System.out.println("1.List of All Available books");
				System.out.println("2.Search Book by Name");
				System.out.println("3.Enter the Author name get a book written by him/her");
				System.out.println("4.Current Holding Book");
				System.out.println("5.Exit");
				System.out.println("--------------------------------------------------------------------------------");
				System.out.println("Enter your choice");
				int choice = sc.nextInt();
				sc.nextLine();
				switch(choice){
					case 1:
						try {
							for(Book book : availableAllBook()) {
							
							System.out.println(book);
							}
						    } catch (Exception e) {
							e.printStackTrace();
						    }
						 break;
					case 2:
						try {
						    System.out.println("Enter the book name:");
						    String title = sc.nextLine();
						    availableParticularBook(title);
						} catch (Exception e) {
						    e.printStackTrace();
						}
						break;
					case 3:
						System.out.println("Enter the Author name");
						String author = sc.nextLine();
						getBookByAuthor(author);
						break;
					case 4:
						System.out.println("Enter the UserId");
						String userId = sc.nextLine();
						currentHoldBook(userId);
						break;
					case 5:
						System.exit(0);
						try{
							con.close();
							}
							catch(SQLException e){
								e.printStackTrace();
							}
						break;	
				}
		}	
    }
    private void showReturnMenu(){
    	while(true){
    			System.out.println("\n-------------------------------------Menu---------------------------------------");
			System.out.println("1.Penalty");
			System.out.println("2.Return");
			System.out.println("3.Exit");
			System.out.println("Enter your choice");
			int choice = sc.nextInt();
			sc.nextLine();
			switch(choice){
				case 1:
					System.out.println("enter the book Id ");
					bookId=sc.next();
					System.out.println("enter the Member Id ");
					memId=sc.next();
					penalty(bookId,memId);
					break;
				case 2:
					returnBook();
					break;
				case 3:
					System.exit(0);
					try{
						con.close();
					}
						catch(SQLException e){
						e.printStackTrace();
					}
					break;
			}
    	}
    }
}
