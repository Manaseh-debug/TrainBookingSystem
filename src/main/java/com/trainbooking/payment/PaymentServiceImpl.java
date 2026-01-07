package com.trainbooking.payment;

import com.trainbooking.bookingcomponent.interfaces.IPaymentService;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceImpl implements IPaymentService {

    @Override
    public boolean processPayment(String bookingID, double totalCost) {
        // Simulate payment success
        System.out.println("Processing payment for Booking ID: " + bookingID);
        System.out.println("Amount: " + totalCost);
        return true;
    }
}

