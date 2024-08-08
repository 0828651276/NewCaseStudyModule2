import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AloneAirlineSystem system = new AloneAirlineSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Wellcome to Alone Airline System - Where Love Begins ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    system.registerCustomer();
                    break;
                case 2:
                    Customer loggedInCustomer = system.loginCustomer();
                    if (loggedInCustomer != null) {
                        customerMenu(system, loggedInCustomer);
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void customerMenu(AloneAirlineSystem system, Customer customer) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Book a Ticket");
            System.out.println("2. Modify a Ticket");
            System.out.println("3. Delete a Ticket");
            System.out.println("4. View All Tickets");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    system.bookTicket(customer);
                    break;
                case 2:
                    system.modifyTicket(customer);
                    break;
                case 3:
                    system.deleteTicket(customer);
                    break;
                case 4:
                    system.viewAllTickets();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
