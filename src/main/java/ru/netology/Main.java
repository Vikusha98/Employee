package ru.netology;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.netology.Employee;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> employees = parseCSV(columnMapping, fileName);
        if (employees != null) {
            String json = listToJson(employees);
            if (json != null) {
                writeString(json, "data.json");
            } else {
                System.out.println("Ошибка преобразования списка в JSON");
            }
        } else {
            System.out.println("Ошибка чтения CSV файла");
        }
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
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
        // Удаление последней запятой и переноса строки
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