package dev.edu.ngochandev.paymentservice.commons.enums;

import lombok.Getter;

@Getter
public enum CurrencyType {
    VND("VND", 0),
    USD("USD", 2);
    private final String code;
    private final int decimalPlaces;
    CurrencyType(String code, int decimalPlaces) {
        this.code = code;
        this.decimalPlaces = decimalPlaces;
    }
    public static Long toStoredAmount(double price, CurrencyType currencyType) {
        return Math.round(price * Math.pow(10, currencyType.getDecimalPlaces()));
    }
    public static double fromStoredAmount(Long storedAmount, CurrencyType currencyType) {
        return storedAmount / Math.pow(10, currencyType.getDecimalPlaces());
    }
}
