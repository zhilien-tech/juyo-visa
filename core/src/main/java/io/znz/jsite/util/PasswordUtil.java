package io.znz.jsite.util;

/**
 * Created by Chaly on 15/11/20.
 */
public class PasswordUtil {

    private static final String letters = "abcdefghijklmnopqrstuvwxyz";
    private static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String UpperLower = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String numLower = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String numUpper = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String symbol = "~!@#$%^&*()_+-=][';:.,\"\\/|<>?";
    private static final String symbolNum = "0123456789~!@#$%^&*()_+-=][';:.,\"\\/|<>?";
    private static final String symbolLower = "~!@#$%^&*()_+-=][';:.,\"\\/|<>?abcdefghijklmnopqrstuvwxyz";
    private static final String symbolUpper = "~!@#$%^&*()_+-=][';:.,\"\\/|<>?ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String symbolUpperAndNum = "~!@#$%^&*()_+-=][';:.,\"\\/|<>?0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String symbolLowerAndNum = "~!@#$%^&*()_+-=][';:.,\"\\/|<>?0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String symbolUpperAndLowerAndNum = "~!@#$%^&*()_+-=][';:.,\"\\/|<>?0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String numUpperAndLower = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String randomNum(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            //			System.out.print((int)((Math.random())*10));
            s = s + ((int) ((Math.random()) * 10));
        }
        return s;
    }

    public static String randomLetters(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + letters.charAt((int) (Math.random() * 100) % 26);
        }
        return s;
    }

    public static String randomUpper(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + upper.charAt((int) (Math.random() * 100) % 26);
        }
        return s;
    }

    public static String randomUpperAndLower(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + UpperLower.charAt((int) (Math.random() * 100) % 52);
        }
        return s;
    }

    public static String randomNumAndLower(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + numLower.charAt((int) (Math.random() * 100) % 36);
        }
        return s;
    }

    public static String randomNumAndUpper(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + numUpper.charAt((int) (Math.random() * 100) % 36);
        }
        return s;
    }

    public static String randomNumAndUpperAndLower(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + numUpperAndLower.charAt((int) (Math.random() * 100) % 62);
        }
        return s;
    }

    public static String randomSymbol(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + symbol.charAt((int) (Math.random() * 100) % 29);
        }
        return s;
    }

    public static String randomSymbolAndNum(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + symbolNum.charAt((int) (Math.random() * 100) % 39);
        }
        return s;
    }

    public static String randomSymbolAndLower(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + symbolLower.charAt((int) (Math.random() * 100) % 55);
        }
        return s;
    }

    public static String randomSymbolAndUpper(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + symbolUpper.charAt((int) (Math.random() * 100) % 55);
        }
        return s;
    }

    public static String randomSymbolAndUpperAndNum(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + symbolUpperAndNum.charAt((int) (Math.random() * 100) % 65);
        }
        return s;
    }

    public static String randomSymbolAndLowerAndNum(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + symbolLowerAndNum.charAt((int) (Math.random() * 100) % 65);
        }
        return s;
    }

    public static String randomSymbolAndLowerAndUpperAndNum(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s = s + symbolUpperAndLowerAndNum.charAt((int) (Math.random() * 100) % 91);
        }
        return s;
    }

    public static void main(String[] args) {
        System.err.println(PasswordUtil.randomSymbolAndLowerAndUpperAndNum(6));
    }

}
