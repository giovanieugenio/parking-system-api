package com.park.api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {

    private static final double FIRST_15_MINUTES = 5.00;
    private static final double FIRST_60_MINUTES = 9.25;
    private static final double ADITIONAL_15_MINUTES = 1.75;

    public static String generateReceipt(){
        LocalDateTime date = LocalDateTime.now();
        String receipt = date.toString().substring(0, 19);
        return receipt.replaceAll("[\\-:T]", "");
    }

    public static BigDecimal calculatePrice(LocalDateTime entry, LocalDateTime exit) {
        long minutes = entry.until(exit, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {
            total = FIRST_15_MINUTES;
        } else if (minutes <= 60) {
            total = FIRST_60_MINUTES;
        } else {
            long addicionalMinutes = minutes - 60;
            Double totalParts = ((double) addicionalMinutes / 15);
            if (totalParts > totalParts.intValue()) {
                total += FIRST_60_MINUTES + (ADITIONAL_15_MINUTES * (totalParts.intValue() + 1));
            } else {
                total += FIRST_60_MINUTES + (ADITIONAL_15_MINUTES * totalParts.intValue());
            }
        }
        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }
}
