package com.trainbooking.ui;

import com.trainbooking.auth.AuthServiceImpl;
import com.trainbooking.availability.AvailabilityServiceImpl;
import com.trainbooking.bookingcomponent.Booking;
import com.trainbooking.bookingcomponent.BookingComponent;
import com.trainbooking.payment.PaymentServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingUISwing extends JFrame {
    
    private BookingComponent bookingComponent;
    private JComboBox<String> originCombo;
    private JComboBox<String> destinationCombo;
    private JTextField userIDField;
    private JTextField dateField;
    private JSpinner passengersSpinner;
    private JLabel costLabel;
    private JTextArea resultArea;
    
    private final String[] locations = {"Nairobi", "Nakuru", "Kericho", "Kisumu", "Kakamega"};
    
    public BookingUISwing() {
        // Initialize booking component
        AuthServiceImpl authService = new AuthServiceImpl();
        AvailabilityServiceImpl availabilityService = new AvailabilityServiceImpl();
        PaymentServiceImpl paymentService = new PaymentServiceImpl();
        bookingComponent = new BookingComponent(paymentService, availabilityService, authService);
        
        setupUI();
    }
    
    private void setupUI() {
        setTitle("Train Booking - Desktop Entry");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(248, 249, 250));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 107, 53));
        JLabel titleLabel = new JLabel("Train Booking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        titlePanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // User ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userIDLabel = new JLabel("User ID:");
        userIDLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(userIDLabel, gbc);
        
        gbc.gridx = 1;
        userIDField = new JTextField(20);
        userIDField.setText("user1");
        formPanel.add(userIDField, gbc);
        
        // Origin
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel originLabel = new JLabel("Origin:");
        originLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(originLabel, gbc);
        
        gbc.gridx = 1;
        originCombo = new JComboBox<>(locations);
        originCombo.addActionListener(e -> {
            updateDestinationOptions();
            calculateCost();
        });
        formPanel.add(originCombo, gbc);
        
        // Destination
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(destinationLabel, gbc);
        
        gbc.gridx = 1;
        destinationCombo = new JComboBox<>(locations);
        destinationCombo.addActionListener(e -> calculateCost());
        formPanel.add(destinationCombo, gbc);
        
        // Passengers
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passengersLabel = new JLabel("Passengers:");
        passengersLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(passengersLabel, gbc);
        
        gbc.gridx = 1;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
        passengersSpinner = new JSpinner(spinnerModel);
        passengersSpinner.addChangeListener(e -> calculateCost());
        formPanel.add(passengersSpinner, gbc);
        
        // Date
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel dateLabel = new JLabel("Departure Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(dateLabel, gbc);
        
        gbc.gridx = 1;
        dateField = new JTextField(20);
        dateField.setText(LocalDate.now().plusDays(1).toString());
        formPanel.add(dateField, gbc);
        
        // Cost Display
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel costPanel = new JPanel();
        costPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        costPanel.setBackground(new Color(255, 243, 224));
        costPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 107, 53), 2));
        JLabel costTitleLabel = new JLabel("Estimated Total Cost: ");
        costTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        costLabel = new JLabel("KES 0");
        costLabel.setFont(new Font("Arial", Font.BOLD, 20));
        costLabel.setForeground(new Color(255, 107, 53));
        costPanel.add(costTitleLabel);
        costPanel.add(costLabel);
        formPanel.add(costPanel, gbc);
        
        // Buttons panel
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setBackground(Color.WHITE);
        
        JButton createBookingBtn = new JButton("Create Booking");
        createBookingBtn.setBackground(new Color(255, 107, 53));
        createBookingBtn.setForeground(Color.WHITE);
        createBookingBtn.setFont(new Font("Arial", Font.BOLD, 14));
        createBookingBtn.setFocusPainted(false);
        createBookingBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        createBookingBtn.addActionListener(e -> createBooking());
        
        JButton paymentBtn = new JButton("Process Payment");
        paymentBtn.setBackground(new Color(38, 166, 154));
        paymentBtn.setForeground(Color.WHITE);
        paymentBtn.setFont(new Font("Arial", Font.BOLD, 14));
        paymentBtn.setFocusPainted(false);
        paymentBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        paymentBtn.addActionListener(e -> processPayment());
        
        JButton clearBtn = new JButton("Clear");
        clearBtn.setBackground(new Color(108, 117, 125));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Arial", Font.BOLD, 14));
        clearBtn.setFocusPainted(false);
        clearBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        clearBtn.addActionListener(e -> clearFields());
        
        buttonsPanel.add(createBookingBtn);
        buttonsPanel.add(paymentBtn);
        buttonsPanel.add(clearBtn);
        formPanel.add(buttonsPanel, gbc);
        
        // Result area
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Result"));
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Initialize
        updateDestinationOptions();
        calculateCost();
    }
    
    private void updateDestinationOptions() {
        String selectedOrigin = (String) originCombo.getSelectedItem();
        String currentDestination = (String) destinationCombo.getSelectedItem();
        
        destinationCombo.removeAllItems();
        for (String location : locations) {
            if (!location.equals(selectedOrigin)) {
                destinationCombo.addItem(location);
            }
        }
        
        if (currentDestination != null && !currentDestination.equals(selectedOrigin)) {
            destinationCombo.setSelectedItem(currentDestination);
        }
    }
    
    private void calculateCost() {
        String origin = (String) originCombo.getSelectedItem();
        String destination = (String) destinationCombo.getSelectedItem();
        int passengers = (Integer) passengersSpinner.getValue();
        
        if (origin != null && destination != null && !origin.equals(destination)) {
            double cost = bookingComponent.calculateTotalCost(origin, destination, passengers);
            costLabel.setText("KES " + (int)cost);
        } else {
            costLabel.setText("KES 0");
        }
    }
    
    private void createBooking() {
        try {
            String userID = userIDField.getText().trim();
            String origin = (String) originCombo.getSelectedItem();
            String destination = (String) destinationCombo.getSelectedItem();
            int passengers = (Integer) passengersSpinner.getValue();
            String dateStr = dateField.getText().trim();
            
            // Validate
            if (userID.isEmpty()) {
                resultArea.setText("Error: User ID is required!");
                return;
            }
            
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now())) {
                resultArea.setText("Error: Departure date must be in the future!");
                return;
            }
            
            LocalDateTime departure = date.atStartOfDay();
            
            boolean success = bookingComponent.createBooking(userID, origin, destination, departure, passengers);
            
            if (success) {
                Booking booking = bookingComponent.getLatestBooking();
                StringBuilder result = new StringBuilder();
                result.append("✓ BOOKING CREATED SUCCESSFULLY!\n");
                result.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                result.append("Booking ID:    ").append(booking.getBookingID()).append("\n");
                result.append("User ID:       ").append(booking.getUserID()).append("\n");
                result.append("Route:         ").append(booking.getOrigin()).append(" → ").append(booking.getDestination()).append("\n");
                result.append("Departure:     ").append(booking.getDepartureTime().toLocalDate()).append("\n");
                result.append("Passengers:    ").append(booking.getNumberOfPassengers()).append("\n");
                result.append("Total Cost:    KES ").append((int)booking.getTotalCost()).append("\n");
                result.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                result.append("Status: Ready for payment\n");
                resultArea.setText(result.toString());
                resultArea.setForeground(new Color(0, 128, 0));
            } else {
                resultArea.setText("✗ BOOKING FAILED!\nPlease check your inputs and try again.");
                resultArea.setForeground(Color.RED);
            }
        } catch (Exception e) {
            resultArea.setText("✗ ERROR: " + e.getMessage());
            resultArea.setForeground(Color.RED);
        }
    }
    
    private void processPayment() {
        Booking booking = bookingComponent.getLatestBooking();
        
        if (booking == null) {
            resultArea.setText("✗ ERROR: No booking found!\nPlease create a booking first.");
            resultArea.setForeground(Color.RED);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to process payment of KES " + (int)booking.getTotalCost() + "?",
            "Confirm Payment",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = bookingComponent.confirmBooking(booking.getBookingID());
            
            if (success) {
                StringBuilder result = new StringBuilder();
                result.append("✓ PAYMENT SUCCESSFUL!\n");
                result.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                result.append("Booking ID:    ").append(booking.getBookingID()).append("\n");
                result.append("Amount Paid:   KES ").append((int)booking.getTotalCost()).append("\n");
                result.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                result.append("Status: CONFIRMED ✓\n");
                result.append("\nYour booking has been confirmed!\n");
                resultArea.setText(result.toString());
                resultArea.setForeground(new Color(0, 128, 0));
            } else {
                resultArea.setText("✗ PAYMENT FAILED!\nPlease try again.");
                resultArea.setForeground(Color.RED);
            }
        }
    }
    
    private void clearFields() {
        userIDField.setText("user1");
        originCombo.setSelectedIndex(0);
        destinationCombo.setSelectedIndex(0);
        passengersSpinner.setValue(1);
        dateField.setText(LocalDate.now().plusDays(1).toString());
        resultArea.setText("");
        resultArea.setForeground(Color.BLACK);
        calculateCost();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            BookingUISwing frame = new BookingUISwing();
            frame.setVisible(true);
        });
    }
}
