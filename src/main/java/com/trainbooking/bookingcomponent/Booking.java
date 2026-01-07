package com.trainbooking.bookingcomponent;


import java.time.LocalDateTime;

public class Booking {

    private String bookingID;
    private String userID;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private int numberOfPassengers;
    private double totalCost;

    public Booking(String bookingID,
                   String userID,
                   String origin,
                   String destination,
                   LocalDateTime departureTime,
                   int numberOfPassengers,
                   double totalCost) {

        this.bookingID = bookingID;
        this.userID = userID;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.numberOfPassengers = numberOfPassengers;
        this.totalCost = totalCost;
    }

    public String getBookingID() { return bookingID; }
    public String getUserID() { return userID; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public int getNumberOfPassengers() { return numberOfPassengers; }
    public double getTotalCost() { return totalCost; }
}
