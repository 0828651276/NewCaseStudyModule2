import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AloneAirlineSystem {
    private static final int MAX_SEATS_PER_FLIGHT = 5;
    private List<Customer> customers;
    private List<Flight> flights;
    private List<Ticket> tickets;

    public AloneAirlineSystem() {
        customers = new ArrayList<>();
        flights = new ArrayList<>();
        tickets = new ArrayList<>();
        loadFlights();
        loadTickets();
        loadCustomers();
    }

    private void loadFlights() {
        flights.add(new Flight("AA101", "Hanoi", "Da Nang", "12:00", 3600000, "2024/08/10"));
        flights.add(new Flight("AA102", "Hanoi", "Ho Chi Minh", "15:00", 4000000, "2024/08/10"));
        flights.add(new Flight("AA103", "Da Nang", "Ho Chi Minh", "19:00", 4500000, "2024/08/10"));
    }

    private void loadTickets() {
        try (BufferedReader reader = new BufferedReader(new FileReader("listtickets.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 10) {
                    Ticket ticket = new Ticket(
                            parts[2],
                            parts[3],
                            parts[4],
                            parts[5],
                            Double.parseDouble(parts[6]),
                            parts[0],
                            parts[1],
                            parts[8],
                            parts[7],
                            parts[9]
                    );
                    tickets.add(ticket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomers() {
        try (BufferedReader personReader = new BufferedReader(new FileReader("person.txt"));
             BufferedReader passReader = new BufferedReader(new FileReader("passCustomer.txt"))) {
            String personLine, passLine;
            while ((personLine = personReader.readLine()) != null && (passLine = passReader.readLine()) != null) {
                String[] personParts = personLine.split(",");
                String[] passParts = passLine.split(",");
                if (personParts.length == 4 && passParts.length == 2) {
                    Customer customer = new Customer(personParts[0], personParts[1], personParts[2], personParts[3]);
                    customers.add(customer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        String cccd;
        do {
            System.out.print("Enter your CCCD: ");
            cccd = scanner.nextLine();
            if (isCCCDRegistered(cccd)) {
                System.out.println("CCCD already registered. Please enter a different CCCD.");
            }
        } while (isCCCDRegistered(cccd));

        String phoneNumber;
        do {
            System.out.print("Enter your phone number: ");
            phoneNumber = scanner.nextLine();
            if (isPhoneNumberRegistered(phoneNumber)) {
                System.out.println("Phone number already registered. Please enter a different phone number.");
            }
        } while (isPhoneNumberRegistered(phoneNumber) || !Customer.isValidPhoneNumber(phoneNumber));

        String email;
        do {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            if (!Customer.isValidEmail(email)) {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        } while (!Customer.isValidEmail(email));

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        Customer customer = new Customer(name, cccd, phoneNumber, email);
        customers.add(customer);
        saveCustomerData(customer, password);
        System.out.println("Registration successful!");
    }

    private boolean isPhoneNumberRegistered(String phoneNumber) {
        for (Customer customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCCCDRegistered(String cccd) {
        for (Customer customer : customers) {
            if (customer.getCccd().equals(cccd)) {
                return true;
            }
        }
        return false;
    }

    private void saveCustomerData(Customer customer, String password) {
        try (BufferedWriter personWriter = new BufferedWriter(new FileWriter("person.txt", true));
             BufferedWriter passWriter = new BufferedWriter(new FileWriter("passCustomer.txt", true))) {
            personWriter.write(customer.getName() + "," + customer.getCccd() + "," + customer.getPhoneNumber() + "," + customer.getEmail());
            personWriter.newLine();
            passWriter.write(customer.getPhoneNumber() + "," + password);
            passWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Customer loginCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (isAdmin(phoneNumber, password)) {
            System.out.println("Login successful! Welcome admin.");
            showAdminMenu();
            return null; // Admin không phải là một đối tượng khách hàng
        }

        Customer customer = findCustomerByPhoneNumber(phoneNumber);
        if (customer != null && isValidPassword(phoneNumber, password)) {
            System.out.println("Login successful! Welcome " + customer.getName());
            return customer;
        } else {
            System.out.println("Invalid phone number or password.");
            return null;
        }
    }

    private boolean isAdmin(String phoneNumber, String password) {
        return phoneNumber.equals("0988888888") && password.equals("admin69@");
    }

    private boolean isValidSeatPosition(String seatPosition) {
        if (seatPosition.length() < 2 || seatPosition.length() > 3) {
            return false;
        }

        char row = seatPosition.charAt(0);
        if (row < 'A' || row > 'E') {
            return false;
        }

        String numberPart = seatPosition.substring(1);
        try {
            int number = Integer.parseInt(numberPart);
            if (number < 1 || number > 10) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    private Customer findCustomerByPhoneNumber(String phoneNumber) {
        for (Customer customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                return customer;
            }
        }
        return null;
    }

    private boolean isValidPassword(String phoneNumber, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("passCustomer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(phoneNumber) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void bookTicket(Customer customer) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Available flights:");
        for (Flight flight : flights) {
            System.out.println(flight);
        }

        System.out.print("Enter flight number to book: ");
        String flightNumber = scanner.nextLine();

        Flight selectedFlight = findFlightByNumber(flightNumber);
        if (selectedFlight == null) {
            System.out.println("Flight not found.");
            return;
        }

        if (isFlightFullyBooked(flightNumber, selectedFlight.getDepartureDate())) {
            System.out.println("Flight is fully booked. Please select a different flight.");
            return;
        }

        System.out.print("Select ticket type (1. Economy, 2. Business, 3. First Class, 4. Cancel): ");
        int ticketTypeChoice = scanner.nextInt();
        scanner.nextLine();

        double price;
        switch (ticketTypeChoice) {
            case 1:
                price = selectedFlight.getBasePrice();
                break;
            case 2:
                price = selectedFlight.getBasePrice() + 2000000;
                break;
            case 3:
                price = selectedFlight.getBasePrice() + 4000000;
                break;
            case 4:
                System.out.println("Ticket booking canceled.");
                return;
            default:
                System.out.println("Invalid choice. Booking canceled.");
                return;
        }

        System.out.print("Enter seat position (e.g., A1, B2): ");
        String seatPosition = scanner.nextLine();

        if (!isValidSeatPosition(seatPosition)) {
            System.out.println("Invalid seat position. Please use format like A1, B2, etc.");
            return;
        }

        if (isSeatAlreadyBooked(flightNumber, seatPosition, selectedFlight.getDepartureDate())) {
            System.out.println("Seat already booked. Please choose a different seat.");
            return;
        }

        Ticket ticket = new Ticket(
                selectedFlight.getFlightNumber(),
                selectedFlight.getDeparture(),
                selectedFlight.getDestination(),
                selectedFlight.getDepartureTime(),
                price,
                customer.getName(),
                customer.getEmail(),
                seatPosition,
                customer.getPhoneNumber(),
                selectedFlight.getDepartureDate()
        );
        tickets.add(ticket);
        saveTickets();
        System.out.println("Ticket booked successfully!");
        System.out.println(ticket);
    }

    private Flight findFlightByNumber(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    private boolean isFlightFullyBooked(String flightNumber, String departureDate) {
        int count = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getFlightNumber().equals(flightNumber) && ticket.getDepartureDate().equals(departureDate)) {
                count++;
            }
        }
        return count >= MAX_SEATS_PER_FLIGHT;
    }


    private boolean isSeatAlreadyBooked(String flightNumber, String seatPosition, String departureDate) {
        for (Ticket ticket : tickets) {
            if (ticket.getFlightNumber().equals(flightNumber) && ticket.getSeatPosition().equals(seatPosition) && ticket.getDepartureDate().equals(departureDate)) {
                return true;
            }
        }
        return false;
    }

    public void modifyTicket(Customer customer) {
        Scanner scanner = new Scanner(System.in);

        List<Ticket> customerTickets = getCustomerTickets(customer);
        if (customerTickets.isEmpty()) {
            System.out.println("You have no tickets to modify.");
            return;
        }

        System.out.println("Your tickets:");
        for (int i = 0; i < customerTickets.size(); i++) {
            System.out.println((i + 1) + ". " + customerTickets.get(i));
        }

        System.out.print("Enter the ticket number to modify: ");
        int ticketIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (ticketIndex < 0 || ticketIndex >= customerTickets.size()) {
            System.out.println("Invalid ticket number.");
            return;
        }

        Ticket ticketToModify = customerTickets.get(ticketIndex);

        System.out.println("Enter new flight number (or press Enter to keep current): ");
        String newFlightNumber = scanner.nextLine();
        if (!newFlightNumber.isEmpty()) {
            Flight newFlight = findFlightByNumber(newFlightNumber);
            if (newFlight != null && !isFlightFullyBooked(newFlightNumber, newFlight.getDepartureDate())) {
                ticketToModify.setFlightNumber(newFlightNumber);
                ticketToModify.setDepartureTime(newFlight.getDepartureTime());
                ticketToModify.setArrival(newFlight.getDestination());
            } else {
                System.out.println("Invalid flight number or flight fully booked. Keeping current flight.");
            }
        }

        System.out.println("Enter new seat position (or press Enter to keep current): ");
        String newSeatPosition = scanner.nextLine();
        if (!newSeatPosition.isEmpty()) {
            if (!isSeatAlreadyBooked(ticketToModify.getFlightNumber(), newSeatPosition, ticketToModify.getDepartureDate())) {
                ticketToModify.setSeatPosition(newSeatPosition);
            } else {
                System.out.println("Seat already booked. Keeping current seat.");
            }
        }

        saveTickets();
        System.out.println("Ticket modified successfully!");
    }

    private List<Ticket> getCustomerTickets(Customer customer) {
        List<Ticket> customerTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getCustomerEmail().equals(customer.getEmail())) {
                customerTickets.add(ticket);
            }
        }
        return customerTickets;
    }

    public void deleteTicket(Customer customer) {
        Scanner scanner = new Scanner(System.in);

        List<Ticket> customerTickets = getCustomerTickets(customer);
        if (customerTickets.isEmpty()) {
            System.out.println("You have no tickets to delete.");
            return;
        }

        System.out.println("Your tickets:");
        for (int i = 0; i < customerTickets.size(); i++) {
            System.out.println((i + 1) + ". " + customerTickets.get(i));
        }

        System.out.print("Enter the ticket number to delete: ");
        int ticketIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (ticketIndex < 0 || ticketIndex >= customerTickets.size()) {
            System.out.println("Invalid ticket number.");
            return;
        }

        Ticket ticketToDelete = customerTickets.get(ticketIndex);
        tickets.remove(ticketToDelete);
        saveTickets();
        System.out.println("Ticket deleted successfully!");
    }

    private void saveTickets() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("listtickets.csv"))) {
            for (Ticket ticket : tickets) {
                String ticketInfo = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        ticket.getCustomerName(),
                        ticket.getCustomerPhoneNumber(),
                        ticket.getFlightNumber(),
                        ticket.getDeparture(),
                        ticket.getArrival(),
                        ticket.getDepartureDate(),
                        ticket.getDepartureTime(),
                        ticket.getSeatPosition(),
                        ticket.getPrice(),
                        ticket.getCustomerEmail());

                writer.write(ticketInfo);
            }
        } catch (IOException e) {
            System.out.println("Error writing to tickets file: " + e.getMessage());
        }
    }

    public void viewAllTickets() {
        if (tickets.isEmpty()) {
            System.out.println("No tickets found.");
            return;
        }

        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }
    }

    public void showAdminMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Admin Menu:");
            System.out.println("1. View all tickets");
            System.out.println("2. View all flights");
            System.out.println("3. Add a new flight");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllTickets();
                    break;
                case 2:
                    viewAllFlights();
                    break;
                case 3:
                    addNewFlight();
                    break;
                case 4:
                    System.out.println("Exiting admin menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    private void viewAllFlights() {
        if (flights.isEmpty()) {
            System.out.println("No flights found.");
            return;
        }

        for (Flight flight : flights) {
            System.out.println(flight);
        }
    }

    private void addNewFlight() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine();

        System.out.print("Enter departure location: ");
        String departure = scanner.nextLine();

        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        System.out.print("Enter departure time: ");
        String departureTime = scanner.nextLine();

        System.out.print("Enter base price: ");
        double basePrice = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter departure date (YYYY/MM/DD): ");
        String departureDate = scanner.nextLine();

        Flight newFlight = new Flight(flightNumber, departure, destination, departureTime, basePrice, departureDate);
        flights.add(newFlight);
        System.out.println("Flight added successfully!");
    }
}
