package com.example.peter.bakingapp.utils;

import java.text.DecimalFormat;

public class IngredientFormat {

    /* Formatting for the quantity field */
    public static String formatRawQuantity(double rawQuantity) {
        DecimalFormat decimalFormat = new DecimalFormat("####.####");
        return decimalFormat.format(rawQuantity);
    }

    /* Formatting for the units field */
    public static String formatRawUnits(String rawUnits) {
        if (rawUnits.equals("UNIT")) {
            rawUnits = "";
        }
        return rawUnits.toLowerCase();
    }

    /* Formatting for the ingredients name */
    public static String formatRawIngredient(String rawIngredient) {
        return rawIngredient
                .substring(0,1)
                .toUpperCase()
                + rawIngredient.substring(1);
    }
}
