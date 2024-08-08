public class Flight {
    private String flightNumber;
    private String departure;
    private String destination;
    private String departureTime;
    private double basePrice;
    private String departureDate;

    public Flight(String flightNumber, String departure, String destination, String departureTime, double basePrice, String departureDate) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.basePrice = basePrice;
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

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public static boolean validateFlightNumber(String flightNumber) {
        return flightNumber.matches("[A-B]{2}\\d{3}");
    }

    public static boolean validateDate(String date) {
        return date.matches("\\d{4}/\\d{2}/\\d{2}");
    }

    public static boolean validateTime(String time) {
        return time.matches("\\d{2}:\\d{2}");
    }

    public static boolean validateBasePrice(double basePrice) {
        return basePrice > 0;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", basePrice=" + basePrice +
                ", departureDate='" + departureDate + '\'' +
                '}';
    }
}
