package com.trainbooking.bookingcomponent;

import com.trainbooking.bookingcomponent.interfaces.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class BookingComponent {

    private static final Set<String> LOCATIONS = Set.of(
            "Nairobi", "Nakuru", "Kericho", "Kisumu", "Kakamega"
    );

    private Map<String, Double> routeFares = new HashMap<>();
    private Map<String, Booking> bookings = new HashMap<>();

    private IPaymentService paymentService;
    private IAvailabilityService availabilityService;
    private IAuthService authService;

    public BookingComponent(IPaymentService paymentService,
                            IAvailabilityService availabilityService,
                            IAuthService authService) {

        this.paymentService = paymentService;
        this.availabilityService = availabilityService;
        this.authService = authService;

        initializeFares();
    }

    // ================= CREATE BOOKING =================
    public boolean createBooking(String userID,
                                 String origin,
                                 String destination,
                                 LocalDateTime departureTime,
                                 int numberOfPassengers) {

        if (!authService.isValidUser(userID)) return false;
        if (!LOCATIONS.contains(origin) || !LOCATIONS.contains(destination)) return false;
        if (origin.equals(destination)) return false;
        if (departureTime.isBefore(LocalDateTime.now())) return false;
        if (numberOfPassengers <= 0) return false;
        if (!availabilityService.isSeatAvailable(origin, destination, departureTime.toString())) return false;

        double totalCost = calculateTotalCost(origin, destination, numberOfPassengers);
        String bookingID = generateBookingID();

        Booking booking = new Booking(
                bookingID,
                userID,
                origin,
                destination,
                departureTime,
                numberOfPassengers,
                totalCost
        );

        bookings.put(bookingID, booking);
        availabilityService.reserveSeat();
        return true;
    }

    // ================= CONFIRM BOOKING =================
    public boolean confirmBooking(String bookingID) {
        if (!bookings.containsKey(bookingID)) return false;
        Booking booking = bookings.get(bookingID);
        return paymentService.processPayment(bookingID, booking.getTotalCost());
    }

    // ================= CANCEL BOOKING =================
    public boolean cancelBooking(String bookingID) {
        if (!bookings.containsKey(bookingID)) return false;
        availabilityService.releaseSeat();
        bookings.remove(bookingID);
        return true;
    }

    // ================= FARE LOGIC =================
    private void initializeFares() {
        addFare("Nairobi", "Nakuru", 800);
        addFare("Nairobi", "Kericho", 1200);
        addFare("Nairobi", "Kisumu", 1500);
        addFare("Nairobi", "Kakamega", 1800);
        addFare("Nakuru", "Kericho", 600);
        addFare("Nakuru", "Kisumu", 1000);
        addFare("Nakuru", "Kakamega", 1400);
        addFare("Kericho", "Kisumu", 500);
        addFare("Kericho", "Kakamega", 900);
        addFare("Kisumu", "Kakamega", 400);
    }

    private void addFare(String origin, String destination, double fare) {
        routeFares.put(origin + "-" + destination, fare);
        routeFares.put(destination + "-" + origin, fare);
    }

    private double getFare(String origin, String destination) {
        return routeFares.get(origin + "-" + destination);
    }

    private String generateBookingID() {
        return UUID.randomUUID().toString();
    }

    // ================= PUBLIC METHOD FOR UI =================
    public double calculateTotalCost(String origin, String destination, int passengers) {
        return getFare(origin, destination) * passengers;
    }

    public Booking getLatestBooking() {
        if (bookings.isEmpty()) return null;
        // Return the most recent booking (last added)
        return bookings.values().stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }
}
