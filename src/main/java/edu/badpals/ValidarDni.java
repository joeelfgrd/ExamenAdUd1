package edu.badpals;

import java.util.regex.Pattern;

public class ValidarDni {
    private static final Pattern DNI_PATTERN = Pattern.compile("^[0-9]{8}[A-Z]$");
    private static final String LETTERS = "TRWAGMYFPDXBNJZSQVHLCKET";
    public static boolean validarDNI(String dni) {
        if (!DNI_PATTERN.matcher(dni).matches()) {
            return false;
        }
        int numberPart = Integer.parseInt(dni.substring(0, 8));
        char letterPart = dni.charAt(8);
        char calculatedLetter = LETTERS.charAt(numberPart % 23);
        return letterPart == calculatedLetter;
    }
}