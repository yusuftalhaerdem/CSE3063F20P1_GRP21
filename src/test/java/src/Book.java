package src;

public class Book {

    private String firstName;
    private String lastName;

    private String[] address;

    public Book() {
    }

    public Book(String firstName, String lastName, String[] address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    // getters and setters, equals(), toString() .... (omitted for brevity)
}
