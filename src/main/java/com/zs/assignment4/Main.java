package com.zs.assignment4;

import com.zs.assignment4.controllers.LruController;
import com.zs.assignment4.controllers.CategoryHierarchyController;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Assignment-4");
        System.out.println("Enter the operation you want to perform:");
        System.out.println("1. LRU Cache Operations");
        System.out.println("2. Category Hierarchy Operations");
        System.out.println("0. Exit");
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) {
            System.out.println("No input provided. Exiting...");
            return;
        }

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice. Exiting...");
            return;
        }

        LruController controller1 = new LruController(sc);
        CategoryHierarchyController controller2 = new CategoryHierarchyController(sc);
        switch (choice) {
            case 1: controller1.start();
                break;
            case 2: controller2.start();
                break;
            case 0: System.out.println("Exiting...");
                return;
            default: System.out.println("Invalid choice. Exiting...");
        }
    }
}
