package org.example.pricecomparator.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class UnitNormalizer {

    public static double normalizeQuantity(double quantity, String unit) {
        double normalized = switch (unit.toLowerCase()) {
            case "g" -> quantity / 1000.0;
            case "ml" -> quantity / 1000.0;
            default -> quantity; // no change for kg, l, buc, etc.
        };

        return roundToTwoDecimals(normalized);
    }

    public static String normalizeUnit(String unit) {
        return switch (unit.toLowerCase()) {
            case "g" -> "kg";
            case "ml" -> "l";
            default -> unit;
        };
    }

    private static double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
