package edu.badpals;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class gestorProfesores {
    private List<Profesor> profesores;
    private Scanner scanner;

    public gestorProfesores() {
        profesores = new ArrayList<>();
        scanner = new Scanner(System.in);
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            File file = new File(getClass().getClassLoader().getResource("profesores.txt").toURI());
            List<String> lineas = Files.readAllLines(Paths.get(file.getPath()));
            for (String linea : lineas) {
                String[] datos = linea.split(";");
                if (datos.length == 4) {
                    profesores.add(new Profesor(datos[0], datos[1], Integer.parseInt(datos[2]), datos[3]));
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void menu() {
        int opcion;
        do {
            System.out.println("Menú de operaciones:");
            System.out.println("1. Listar profesores");
            System.out.println("2. Añadir un profesor");
            System.out.println("3. Borrar un profesor");
            System.out.println("4. Exportar a XML");
            System.out.println("5. Exportar a JSON");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    listarProfesores();
                    break;
                case 2:
                    añadirProfesor();
                    break;
                case 3:
                    borrarProfesor();
                    break;
                case 4:
                    exportarA_XML();
                    break;
                case 5:
                    exportarA_JSON();
                    break;
                case 6:
                    guardarDatos();
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 6);
    }

    private void listarProfesores() {
        profesores.sort(Comparator.comparing(Profesor::getNombre));
        for (int i = 0; i < profesores.size(); i++) {
            System.out.println(profesores.get(i));
        }
    }

    private void añadirProfesor() {
        System.out.print("Ingrese nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese DNI: ");
        String dni = scanner.nextLine();
        if (ValidarDni.validarDNI(dni)) {
            System.out.println("DNI válido");
        } else {
            System.out.println("DNI no válido");
            return;
        }

        System.out.print("Ingrese edad: ");
        String edad = scanner.nextLine();

        System.out.println("Seleccione el numero del departamento en el que se incluirá:");
        System.out.println("1. Informática");
        System.out.println("2. Ciencias");
        System.out.println("3. Matemáticas");
        System.out.println("4. Lengua y Literatura");
        System.out.print("Ingrese el número correspondiente: ");
        int opcionDepartamento = scanner.nextInt();
        scanner.nextLine();

        String departamento;
        switch (opcionDepartamento) {
            case 1:
                departamento = "INFORMATICA";
                break;
            case 2:
                departamento = "CIENCIAS";
                break;
            case 3:
                departamento = "MATEMATICAS";
                break;
            case 4:
                departamento = "LENGUA_Y_LITERATURA";
                break;
            default:
                System.out.println("Opción no válida. Departamento no válido.");
                return;
        }

        if (!validarDepartamento.validarDepartamento(departamento)) {
            System.out.println("Departamento no válido.");
            return;
        }

        StringBuilder profesorCreado = new StringBuilder();
        profesorCreado.append("Se acaba de crear al profesor:\n");
        profesorCreado.append("Nombre: ").append(nombre).append("\n");
        profesorCreado.append("DNI: ").append(dni).append("\n");
        profesorCreado.append("Edad: ").append(edad).append("\n");
        profesorCreado.append("Departamento: ").append(departamento).append("\n");
        System.out.println(profesorCreado);

        profesores.add(new Profesor(nombre, dni, Integer.parseInt(edad), departamento));
    }

    private void borrarProfesor() {
        System.out.print("Ingrese DNI del profesor a borrar: ");
        String dni = scanner.nextLine();
        boolean eliminado = profesores.removeIf(profesor -> profesor.getDni().equals(dni));
        if (eliminado) {
            System.out.println("Profesor eliminado con éxito.");
        } else {
            System.out.println("No se encontró ningún profesor con ese DNI.");
        }
    }

    public void exportarA_XML() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("profesores");
            doc.appendChild(rootElement);
            for (Profesor profesor : profesores) {
                Element profesorElement = doc.createElement("profesor");

                Element nombreElement = doc.createElement("nombre");
                nombreElement.appendChild(doc.createTextNode(profesor.getNombre()));
                profesorElement.appendChild(nombreElement);

                Element dniElement = doc.createElement("dni");
                dniElement.appendChild(doc.createTextNode(profesor.getDni()));
                profesorElement.appendChild(dniElement);

                Element edadElement = doc.createElement("edad");
                edadElement.appendChild(doc.createTextNode(String.valueOf(profesor.getEdad())));
                profesorElement.appendChild(edadElement);

                Element departamentoElement = doc.createElement("departamento");
                departamentoElement.appendChild(doc.createTextNode(profesor.getDepartamento()));
                profesorElement.appendChild(departamentoElement);

                rootElement.appendChild(profesorElement);
            }
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                if (!Files.exists(Paths.get("src/main/resources/profesores.xml"))) {
                    Files.createFile(Paths.get("src/main/resources/profesores.xml"));
                }
                StreamResult result = new StreamResult(new File("src/main/resources/profesores.xml"));
                transformer.transform(source, result);
                System.out.println("Profesores exportados a XML con éxito.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    public void exportarA_JSON() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (!Files.exists(Paths.get("src/main/resources/profesores.json"))) {
                Files.createFile(Paths.get("src/main/resources/profesores.json"));
            }
            File jsonFile = new File("src/main/resources/profesores.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, profesores);
            System.out.println("Profesores exportados a JSON con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void guardarDatos() {
        try (FileWriter writer = new FileWriter("src/main/resources/profesores.txt")) {
            for (Profesor profesor : profesores) {
                writer.write(profesor.getNombre() + ";" +
                        profesor.getDni() + ";" +
                        profesor.getEdad() + ";" +
                        profesor.getDepartamento() + "\n");
            }
            System.out.println("Datos guardados con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



