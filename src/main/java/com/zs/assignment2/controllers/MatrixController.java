package com.zs.assignment2.controllers;

import com.zs.assignment2.Model.Matrix;
import com.zs.assignment2.services.MatrixService;
import java.util.Locale;
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
            System.out.println("1. A + B | 2. A - B | 3. A * B | 4. Scalar Value(k) * A | 5. Scalar Value(k) * B | 6. Transpose A | 7. Transpose B | 0. Exit");
            System.out.print("Choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> printMatrix("A + B", service.add(A, B));
                case 2 -> printMatrix("A - B", service.subtract(A, B));
                case 3 -> printMatrix("A * B", service.multiply(A, B));
                case 4 -> {
                    System.out.print("Enter scalar k for Matrix A: ");
                    double k = sc.nextDouble();
                    printMatrix(k + " * A", service.scalarMultiply(k, A));
                }
                case 5 -> {
                    System.out.print("Enter scalar k for Matrix B: ");
                    double k = sc.nextDouble();
                    printMatrix(k + " * B", service.scalarMultiply(k, B));
                }
                case 6 -> printMatrix("Transpose of A", service.transpose(A));
                case 7 -> printMatrix("Transpose of B", service.transpose(B));
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
        int rows = m.getRows();
        int cols = m.getCols();

        String[][] formatted = new String[rows][cols];
        int[] columnWidths = new int[cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String value = formatValue(m.getElement(i, j));
                formatted[i][j] = value;
                columnWidths[j] = Math.max(columnWidths[j], value.length());
            }
        }

        for (int i = 0; i < rows; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < cols; j++) {
                if (j < cols - 1) {
                    row.append(padRight(formatted[i][j], columnWidths[j])).append("  ");
                } else {
                    row.append(formatted[i][j]);
                }
            }
            System.out.println(row);
        }
    }

    private String formatValue(double value) {
        if (value == -0.0d) {
            value = 0.0d;
        }
        if (value == (long) value) {
            return String.format(Locale.US, "%.1f", value);
        }
        return String.valueOf(value);
    }

    private String padRight(String value, int width) {
        if (value.length() >= width) {
            return value;
        }
        return value + " ".repeat(width - value.length());
    }
}
