package org.example.app.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserReadByIdView {

    // Отримуємо вхідні дані
    public Map<String, String> getData() {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> data = new HashMap<>();
        System.out.print("Enter user's ID: ");
        data.put("id", scanner.nextLine().trim());
        return data;
    }

    // Виведення результату роботи програми
    public void getOutput(String output) {
        System.out.println(output);
    }
}
