package com.ecommerce.util;

import java.util.Scanner;

public class InputUtil {
    public static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    public static String readString(Scanner sc,String fieldName) {
        String input = sc.next().trim();

        while (input.isEmpty()) {
            System.out.print(fieldName + "cannot be empty. Please enter again: ");
            input = sc.nextLine().trim();
        }
        return input;
    }

    public static double readDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        return sc.nextDouble();
    }
}
