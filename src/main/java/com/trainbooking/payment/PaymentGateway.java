package com.trainbooking.payment;

public class PaymentGateway {

    public boolean makePayment(double amount) {
        return amount > 0;
    }
}

