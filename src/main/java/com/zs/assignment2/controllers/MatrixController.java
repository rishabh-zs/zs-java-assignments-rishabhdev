package com.zs.assignment2.controllers;

import com.zs.assignment2.Model.Matrix;
import com.zs.assignment2.services.MatrixService;
import java.util.Scanner;

public class MatrixController {
    private final MatrixService service = new MatrixService();
    private final Scanner sc = new Scanner(System.in);

    public void start() {
        System.out.println("--- Advanced Matrix Calculator ---");
        Matrix A = inputMatrix("A");
        Matrix B = inputMatrix("B");

        System.out.println("\nSelect: 1.Add | 2.Multiply | 3.Transpose A");
        int choice = sc.nextInt();

        switch (choice) {
            case 1 -> printMatrix(service.add(A, B));
            case 2 -> printMatrix(service.multiply(A, B));
            case 3 -> printMatrix(service.transpose(A));
            default -> System.out.println("Invalid option.");
        }
    }

    private Matrix inputMatrix(String name) {
        System.out.print("Enter rows and cols for Matrix " + name + ": ");
        int r = sc.nextInt();
        int c = sc.nextInt();
        Matrix m = new Matrix(r, c);

        System.out.println("Enter values:");
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                m.setElement(i, j, sc.nextDouble()); // Using Setter
            }
        }
        return m;
    }

    private void printMatrix(Matrix m) {
        if (m == null) {
            System.out.println("Error: Dimensions do not match!");
            return;
        }
        double[][] data = m.getData(); // Using Getter
        for (double[] row : data) {
            for (double val : row) System.out.print(val + "  ");
            System.out.println();
        }
    }
}