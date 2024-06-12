import java.util.Scanner;
public class LibraryManagement{
	public static void main(String[] args) {
		        Library lib = new Library();
			Admin admin = new Admin();
			User user = new User();
			Scanner sc = new Scanner(System.in);
		while (true) {
		    System.out.println("Enter your choice \n 1. User Login \n 2. Admin Login \n 3. New User \n 4. Exit");
		    int choice = sc.nextInt();
		    sc.nextLine(); 

		    switch (choice) {
		        case 1:
		            System.out.println("Enter the User_id");
		            String userId = sc.nextLine();
		            lib.userLogin(userId);
		            break;
		        case 2:
		            System.out.println("Enter the Admin_id");
		            admin.setAdminId(sc.nextLine());
		            System.out.println("Enter the password:");
		            admin.setPassword(sc.nextLine());
		            lib.login(admin.getAdminId(), admin.getPassword());
		            break;
		        case 3:
		        	System.out.println("Enter the UserId");
				userId = sc.nextLine();
				System.out.println("Enter the UserName");
				String userName = sc.nextLine();
				System.out.println("Enter the Contact");
				String contact = sc.nextLine();
				lib.userRegister(userId,userName,contact);
				break;
		        case 4:
		            System.exit(0);
		            break;
		        default:
		            System.out.println("Invalid choice. Please try again.");
		            break;
		    }
		}
	    }
}
