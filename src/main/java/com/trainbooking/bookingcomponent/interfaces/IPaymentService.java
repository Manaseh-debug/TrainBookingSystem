package com.trainbooking.bookingcomponent.interfaces;

public interface IPaymentService {
    boolean processPayment(String bookingID, double totalCost);
}

