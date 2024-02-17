package ru.netology;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

       // List<Employee> employees = parseCSV(columnMapping, fileName);
       // if (employees != null) {
          //  String json = listToJson(employees);
            //if (json != null) {
             //   writeString(json, "data.json");
           // } else {
               // System.out.println("Ошибка преобразования списка в JSON");
           // }
       // } else {
           // System.out.println("Ошибка чтения CSV файла");
      //  }


        List<Employee> employees = parseXML("data2.xml");
        if (employees != null) {
            String json = listToJson(employees);
            if (json != null) {
                writeString(json, "data2.json");
            } else {
                System.out.println("Ошибка преобразования списка в JSON");
            }
        } else {
            System.out.println("Ошибка чтения XML файла");
        }


    }

   // private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
       // try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            //ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
           // strategy.setType(Employee.class);
           // strategy.setColumnMapping(columnMapping);

          //  CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader)
                   // .withMappingStrategy(strategy)
                    //.build();

          //  return csvToBean.parse();
       // } catch (IOException e) {
          //  e.printStackTrace();
        //}
      //  return null;
    //}

    private static List<Employee> parseXML(String fileName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(fileName);

            List<Employee> employees = new ArrayList<>();

            Node staffNode = document.getDocumentElement();
            NodeList employeeNodes = staffNode.getChildNodes();

            for (int i = 0; i < employeeNodes.getLength(); i++) {
                Node employeeNode = employeeNodes.item(i);
                if (employeeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element employeeElement = (Element) employeeNode;
                    int id = Integer.parseInt(employeeElement.getElementsByTagName("id").item(0).getTextContent());
                    String firstName = employeeElement.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = employeeElement.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = employeeElement.getElementsByTagName("country").item(0).getTextContent();
                    int age = Integer.parseInt(employeeElement.getElementsByTagName("age").item(0).getTextContent());

                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    employees.add(employee);
                }
            }

            return employees;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String listToJson(List<Employee> list) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        for (Employee employee : list) {
            jsonBuilder.append("  {\n");
            jsonBuilder.append("    \"id\": ").append(employee.getId()).append(",\n");
            jsonBuilder.append("    \"firstName\": \"").append(employee.getFirstName()).append("\",\n");
            jsonBuilder.append("    \"lastName\": \"").append(employee.getLastName()).append("\",\n");
            jsonBuilder.append("    \"country\": \"").append(employee.getCountry()).append("\",\n");
            jsonBuilder.append("    \"age\": ").append(employee.getAge()).append("\n");
            jsonBuilder.append("  },\n");
        }

        if (jsonBuilder.length() > 2) {
            jsonBuilder.delete(jsonBuilder.length() - 2, jsonBuilder.length() - 1);
        }
        jsonBuilder.append("\n]");
        return jsonBuilder.toString();
    }


    private static void writeString(String json, String jsonFileName) {
        try (FileWriter writer = new FileWriter(jsonFileName)) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}