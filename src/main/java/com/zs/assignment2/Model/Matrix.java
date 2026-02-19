package com.zs.assignment2.Model;

public class Matrix {
    private int rows;
    private int cols;
    private double[][] data;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    // Getters and Setters
    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public double[][] getData() { return data; }

    public void setData(double[][] data) {
        this.data = data;
    }

    // Helper to set a specific element
    public void setElement(int r, int c, double value) {
        data[r][c] = value;
    }
}