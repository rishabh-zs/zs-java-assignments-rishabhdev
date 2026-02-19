package com.zs.assignment2.controllers;

import com.zs.assignment2.Model.Matrix;
import com.zs.assignment2.services.MatrixService;
import java.util.Scanner;

public class MatrixController {
    private final MatrixService service = new MatrixService();
    private final Scanner sc = new Scanner(System.in);

    public void start() {
        System.out.println("--- Matrix Processor ---");
        Matrix A = inputMatrix("A");
        Matrix B = inputMatrix("B");

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Operations ---");
            System.out.println("1. A + B | 2. A - B | 3. A * B | 4. k * A | 5. Transpose A | 0. Exit");
            System.out.print("Choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> printMatrix("A + B", service.add(A, B));
                case 2 -> printMatrix("A - B", service.subtract(A, B));
                case 3 -> printMatrix("A * B", service.multiply(A, B));
                case 4 -> {
                    System.out.print("Enter scalar k: ");
                    double k = sc.nextDouble();
                    printMatrix(k + " * A", service.scalarMultiply(k, A));
                }
                case 5 -> printMatrix("Transpose of A", service.transpose(A));
                case 0 -> exit = true;
                default -> System.out.println("Invalid input!");
            }
        }
    }

    private Matrix inputMatrix(String name) {
        System.out.print("Enter rows and cols for Matrix " + name + " (e.g., 2 3): ");
        int r = sc.nextInt();
        int c = sc.nextInt();
        Matrix m = new Matrix(r, c);
        System.out.println("Enter the " + (r * c) + " elements:");
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                m.setElement(i, j, sc.nextDouble());
            }
        }
        return m;
    }

    private void printMatrix(String label, Matrix m) {
        if (m == null) {
            System.out.println("Error: Incompatible dimensions for " + label);
            return;
        }
        System.out.println("\nResult of " + label + ":");
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                System.out.print(m.getElement(i, j) + "\t");
            }
            System.out.println();
        }
    }
}