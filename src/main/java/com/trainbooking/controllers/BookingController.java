package com.trainbooking.controllers;

import com.trainbooking.bookingcomponent.Booking;
import com.trainbooking.bookingcomponent.BookingComponent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class BookingController {

    private final BookingComponent bookingComponent;

    public BookingController(BookingComponent bookingComponent) {
        this.bookingComponent = bookingComponent;
    }

    @GetMapping("/booking")
    public String bookingPage(@RequestParam(required = false) String userID, Model model) {
        if (userID != null) {
            model.addAttribute("userID", userID);
        }
        return "booking";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/payment")
    public String paymentPage(@RequestParam String bookingID,
                              @RequestParam String userID,
                              Model model) {
        Booking booking = bookingComponent.getLatestBooking();
        if (booking != null) {
            model.addAttribute("bookingID", bookingID);
            model.addAttribute("paymentDue", booking.getTotalCost());
        }
        model.addAttribute("userID", userID);
        return "payment";
    }

    @PostMapping("/book")
    public String book(@RequestParam String userID,
                       @RequestParam String origin,
                       @RequestParam String destination,
                       @RequestParam int passengers,
                       @RequestParam String date,
                       Model model) {

        // Validate date is in the future
        LocalDate selectedDate = LocalDate.parse(date);
        LocalDate today = LocalDate.now();

        if (selectedDate.isBefore(today) || selectedDate.isEqual(today)) {
            model.addAttribute("message", "Error: Departure date must be in the future!");
            model.addAttribute("userID", userID);
            return "booking";
        }

        LocalDateTime departure = selectedDate.atStartOfDay();
        boolean success = bookingComponent.createBooking(userID, origin, destination, departure, passengers);
        if (success) {
            Booking booking = bookingComponent.getLatestBooking();
            // Redirect to payment page
            return "redirect:/payment?bookingID=" + booking.getBookingID() + "&userID=" + userID;
        } else {
            model.addAttribute("message", "Booking failed. Check your inputs.");
            model.addAttribute("userID", userID);
            return "booking";
        }
    }

    @PostMapping("/pay")
    public String pay(@RequestParam String bookingID,
                      @RequestParam String userID,
                      Model model) {
        boolean success = bookingComponent.confirmBooking(bookingID);
        if (success) {
            model.addAttribute("message", "Payment successful! Your booking is confirmed.");
        } else {
            model.addAttribute("message", "Payment failed. Please try again.");
        }
        model.addAttribute("userID", userID);
        // Redirect back to booking page with message
        return "redirect:/booking?userID=" + userID;
    }
}
