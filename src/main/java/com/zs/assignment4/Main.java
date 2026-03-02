package com.zs.assignment4;

import com.zs.assignment4.controllers.LruController;
import com.zs.assignment4.controllers.CategoryHierarchyController;
import java.util.Scanner;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("Assignment-4");
                System.out.println("Enter the operation you want to perform:");
                System.out.println("1. LRU Cache Operations");
                System.out.println("2. Category Hierarchy Operations");
                System.out.println("0. Exit");

                if (!sc.hasNextLine()) {
                    System.out.println("No input provided. Exiting...");
                    return;
                }

                int choice;
                try {
                    choice = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Try again.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        new LruController(sc).start();
                        break;
                    case 2:
                        new CategoryHierarchyController(sc).start();
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }
}
