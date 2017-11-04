package controller;

public class TransformUtils {

    public static String toPercentage(Double number, int nrOfDecimals, int multiply) {
        return String.format("%,3." + nrOfDecimals + "f", number * multiply) + " %";
    }

    public static String toPercentage(Double number, int nrOfDecimals) {
        return toPercentage(number, nrOfDecimals, 1);
    }

    public static String toMoney(double number, int nrOfDecimals) {
        return String.format("€ %,3." + nrOfDecimals + "f", number);
    }

}
