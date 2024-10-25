package edu.badpals;
public class Profesor {
    private String nombre;
    private String dni;
    private int edad;
    private String departamento;

    public Profesor(String nombre, String dni, int edad, String departamento) {
        this.nombre = nombre;
        this.dni = dni;
        this.edad = edad;
        this.departamento = departamento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public int getEdad() {
        return edad;
    }

    public String getDepartamento() {
        return departamento;
    }

    @Override
    public String toString() {
        return nombre + " | " + dni + " | " + edad + " | " + departamento;
    }
}