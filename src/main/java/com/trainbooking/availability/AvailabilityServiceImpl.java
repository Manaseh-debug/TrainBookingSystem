package com.trainbooking.availability;

import com.trainbooking.bookingcomponent.interfaces.IAvailabilityService;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityServiceImpl implements IAvailabilityService {

    private int availableSeats = 10;

    @Override
    public boolean isSeatAvailable(String origin, String destination, String departureTime) {
        return availableSeats > 0;
    }

    @Override
    public void reserveSeat() {
        if (availableSeats > 0) {
            availableSeats--;
        }
    }

    @Override
    public void releaseSeat() {
        availableSeats++;
    }
}

