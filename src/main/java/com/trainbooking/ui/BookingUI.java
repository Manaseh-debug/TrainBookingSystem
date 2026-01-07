package com.trainbooking.ui;

import com.trainbooking.bookingcomponent.BookingComponent;
import com.trainbooking.auth.AuthServiceImpl;
import com.trainbooking.payment.PaymentServiceImpl;
import com.trainbooking.availability.AvailabilityServiceImpl;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingUI extends JFrame {

    private JTextField userIdField;
    private JComboBox<String> originBox;
    private JComboBox<String> destinationBox;
    private JSpinner passengerSpinner;
    private JSpinner dateSpinner;
    private JLabel totalCostLabel;
    private JButton bookButton;

    private BookingComponent bookingComponent;

    public BookingUI() {
        setTitle("Train Booking System");
        setSize(450, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 2, 5, 5));

        // Services
        AuthServiceImpl authService = new AuthServiceImpl();
        PaymentServiceImpl paymentService = new PaymentServiceImpl();
        AvailabilityServiceImpl availabilityService = new AvailabilityServiceImpl();
        bookingComponent = new BookingComponent(paymentService, availabilityService, authService);

        // UI Fields
        userIdField = new JTextField();
        String[] locations = {"Nairobi", "Nakuru", "Kericho", "Kisumu", "Kakamega"};
        originBox = new JComboBox<>(locations);
        destinationBox = new JComboBox<>(locations);
        passengerSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        totalCostLabel = new JLabel("KES 0");
        bookButton = new JButton("Create Booking");

        // Add to layout
        // add(new JLabel("User ID:")); add(userIdField);
        add(new JLabel("Origin:")); add(originBox);
        add(new JLabel("Destination:")); add(destinationBox);
        add(new JLabel("Passengers:")); add(passengerSpinner);
        add(new JLabel("Departure Date:")); add(dateSpinner);
        add(new JLabel("Total Cost:")); add(totalCostLabel);
        add(bookButton);

        // ================== LISTENERS ==================
        originBox.addActionListener(e -> updateTotalCost());
        destinationBox.addActionListener(e -> updateTotalCost());
        passengerSpinner.addChangeListener(e -> updateTotalCost());

        bookButton.addActionListener(e -> createBooking());

        // Initial total cost display
        updateTotalCost();
    }

    private void updateTotalCost() {
        try {
            String origin = (String) originBox.getSelectedItem();
            String destination = (String) destinationBox.getSelectedItem();
            int passengers = (int) passengerSpinner.getValue();

            if (origin.equals(destination)) {
                totalCostLabel.setText("KES 0");
                return;
            }

            double totalCost = bookingComponent.calculateTotalCost(origin, destination, passengers);
            totalCostLabel.setText("KES " + totalCost);
        } catch (Exception e) {
            totalCostLabel.setText("KES 0");
        }
    }

    private void createBooking() {
        try {
            String userID = userIdField.getText();
            String origin = (String) originBox.getSelectedItem();
            String destination = (String) destinationBox.getSelectedItem();
            int passengers = (int) passengerSpinner.getValue();
            LocalDate date = new java.sql.Date(((java.util.Date) dateSpinner.getValue()).getTime()).toLocalDate();
            LocalDateTime departureTime = date.atStartOfDay();

            boolean success = bookingComponent.createBooking(userID, origin, destination, departureTime, passengers);

            if (success) {
                JOptionPane.showMessageDialog(this, "Booking created successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Booking failed. Check your inputs.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
