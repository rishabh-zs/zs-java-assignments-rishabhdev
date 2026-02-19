package com.zs.assignment2.services;

import com.zs.assignment2.Model.Matrix;

public class MatrixService {

    /**
     * A + B: Dimensions must be identical
     */
    public Matrix add(Matrix a, Matrix b) {
        if (a.getRows() != b.getRows() || a.getCols() != b.getCols()) {
            return null; // Dimension mismatch
        }
        Matrix result = new Matrix(a.getRows(), a.getCols());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getCols(); j++) {
                double val = a.getData()[i][j] + b.getData()[i][j];
                result.setElement(i, j, val);
            }
        }
        return result;
    }

    /**
     * A - B: Dimensions must be identical
     */
    public Matrix subtract(Matrix a, Matrix b) {
        if (a.getRows() != b.getRows() || a.getCols() != b.getCols()) {
            return null;
        }
        Matrix result = new Matrix(a.getRows(), a.getCols());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getCols(); j++) {
                double val = a.getData()[i][j] - b.getData()[i][j];
                result.setElement(i, j, val);
            }
        }
        return result;
    }

    /**
     * A * B: Columns of A must equal Rows of B.
     * Result size: (Rows of A) x (Cols of B)
     */
    public Matrix multiply(Matrix a, Matrix b) {
        if (a.getCols() != b.getRows()) {
            return null;
        }
        Matrix result = new Matrix(a.getRows(), b.getCols());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < b.getCols(); j++) {
                double sum = 0;
                for (int k = 0; k < a.getCols(); k++) {
                    sum += a.getData()[i][k] * b.getData()[k][j];
                }
                result.setElement(i, j, sum);
            }
        }
        return result;
    }

    /**
     * k * Matrix: Scalar multiplication.
     */
    public Matrix scalarMultiply(double k, Matrix a) {
        Matrix result = new Matrix(a.getRows(), a.getCols());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getCols(); j++) {
                double val = k * a.getData()[i][j];
                result.setElement(i, j, val);
            }
        }
        return result;
    }

    /**
     * A': Transpose.
     * Swaps rows and columns.
     */
    public Matrix transpose(Matrix a) {
        Matrix result = new Matrix(a.getCols(), a.getRows());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getCols(); j++) {
                // Notice the swap: result(j, i) gets value from a(i, j)
                result.setElement(j, i, a.getData()[i][j]);
            }
        }
        return result;
    }
}