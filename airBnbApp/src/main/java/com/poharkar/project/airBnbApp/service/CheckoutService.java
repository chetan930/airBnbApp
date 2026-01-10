package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.entity.Booking;

public interface CheckoutService {
    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);
}
