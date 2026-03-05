package com.zs.assignment2.services;

import com.zs.assignment2.Model.Matrix;

public class MatrixService {

    public Matrix add(Matrix a, Matrix b) {
        if (a.getRows() != b.getRows() || a.getCols() != b.getCols()) return null;
        Matrix res = new Matrix(a.getRows(), a.getCols());
        for (int i = 0; i < a.getRows(); i++)
            for (int j = 0; j < a.getCols(); j++)
                res.setElement(i, j, a.getElement(i, j) + b.getElement(i, j));
        return res;
    }

    public Matrix subtract(Matrix a, Matrix b) {
        if (a.getRows() != b.getRows() || a.getCols() != b.getCols()) return null;
        Matrix res = new Matrix(a.getRows(), a.getCols());
        for (int i = 0; i < a.getRows(); i++)
            for (int j = 0; j < a.getCols(); j++)
                res.setElement(i, j, a.getElement(i, j) - b.getElement(i, j));
        return res;
    }

    // Method 1: Matrix * Matrix
    public Matrix multiply(Matrix a, Matrix b) {
        if (a.getCols() != b.getRows()) return null;
        Matrix res = new Matrix(a.getRows(), b.getCols());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < b.getCols(); j++) {
                double sum = 0;
                for (int k = 0; k < a.getCols(); k++) {
                    sum += a.getElement(i, k) * b.getElement(k, j);
                }
                res.setElement(i, j, sum);
            }
        }
        return res;
    }

    // Method 2: scalar * Matrix
    public Matrix multiply(double k, Matrix a) {
        Matrix res = new Matrix(a.getRows(), a.getCols());
        for (int i = 0; i < a.getRows(); i++)
            for (int j = 0; j < a.getCols(); j++)
                res.setElement(i, j, a.getElement(i, j) * k);
        return res;
    }

    public Matrix scalarMultiply(double k, Matrix a) {
        return multiply(k, a);
    }


    public Matrix transpose(Matrix a) {
        Matrix res = new Matrix(a.getCols(), a.getRows());
        for (int i = 0; i < a.getRows(); i++)
            for (int j = 0; j < a.getCols(); j++)
                res.setElement(j, i, a.getElement(i, j));
        return res;
    }
}
