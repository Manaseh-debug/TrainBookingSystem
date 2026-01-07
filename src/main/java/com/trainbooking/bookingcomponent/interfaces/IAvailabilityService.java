package com.trainbooking.bookingcomponent.interfaces;

public interface IAvailabilityService {
    boolean isSeatAvailable(String origin, String destination, String departureTime);
    void reserveSeat();
    void releaseSeat();
}

