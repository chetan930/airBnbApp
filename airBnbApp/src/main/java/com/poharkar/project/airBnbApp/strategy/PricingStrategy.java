package com.poharkar.project.airBnbApp.strategy;

import com.poharkar.project.airBnbApp.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(Inventory inventory);
}
