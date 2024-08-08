public class Ticket {
    private String flightNumber;
    private String departure;
    private String arrival;
    private String departureTime;
    private double price;
    private String customerName;
    private String customerEmail;
    private String seatPosition;
    private String customerPhoneNumber;
    private String departureDate;

    public Ticket(String flightNumber, String departure, String arrival, String departureTime, double price, String customerName, String customerEmail, String seatPosition, String customerPhoneNumber, String departureDate) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.price = price;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.seatPosition = seatPosition;
        this.customerPhoneNumber = customerPhoneNumber;
        this.departureDate = departureDate;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {

        this.flightNumber = flightNumber;
    }

    public String getDeparture() {

        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public double getPrice() {
        return price;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getSeatPosition() {
        return seatPosition;
    }

    public void setSeatPosition(String seatPosition) {
        this.seatPosition = seatPosition;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public static boolean validateSeatPosition(String seatPosition) {
        String regex = "^[A-Z]{1}\\d{2}$";
        return seatPosition.matches(regex);
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "flightNumber='" + flightNumber + '\'' +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", price=" + price +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", seatPosition='" + seatPosition + '\'' +
                ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                ", departureDate='" + departureDate + '\'' +
                '}';
    }
}
