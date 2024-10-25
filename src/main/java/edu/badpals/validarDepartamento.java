package edu.badpals;

import java.util.Arrays;

public class validarDepartamento {
    public static boolean validarDepartamento(String departamento) {
        return Arrays.stream(Departamento.values())
                .anyMatch(d -> d.name().equalsIgnoreCase(departamento));
    }
}