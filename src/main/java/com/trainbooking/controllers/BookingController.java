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
    public String bookingPage() {
        return "booking";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @PostMapping("/book")
    public String book(@RequestParam String userID,
                       @RequestParam String origin,
                       @RequestParam String destination,
                       @RequestParam int passengers,
                       @RequestParam String date,
                       Model model) {
        LocalDateTime departure = LocalDate.parse(date).atStartOfDay();
        boolean success = bookingComponent.createBooking(userID, origin, destination, departure, passengers);
        if (success) {
            Booking booking = bookingComponent.getLatestBooking();
            double estimatedCost = bookingComponent.calculateTotalCost(origin, destination, passengers);
            model.addAttribute("message", "Booking created! Please proceed to payment.");
            model.addAttribute("bookingID", booking.getBookingID());
            model.addAttribute("estimatedCost", estimatedCost);
            model.addAttribute("paymentDue", booking.getTotalCost());
        } else {
            model.addAttribute("message", "Booking failed. Check your inputs.");
        }
        model.addAttribute("userID", userID);
        return "booking";
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
        return "booking";
    }
}
