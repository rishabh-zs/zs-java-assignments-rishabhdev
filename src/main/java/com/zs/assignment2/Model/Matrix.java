package com.zs.assignment2.Model;

public class Matrix {
    private final int rows;
    private final int cols;
    private double[][] data;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    // Getters
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double[][] getData() {
        return data;
    }

    // Setters
    public void setData(double[][] data) {
        this.data = data;
    }

    public void setElement(int r, int c, double value) {
        if (r >= 0 && r < rows && c >= 0 && c < cols) {
            data[r][c] = value;
        }
    }

    public double getElement(int r, int c) {
        return data[r][c];
    }
}