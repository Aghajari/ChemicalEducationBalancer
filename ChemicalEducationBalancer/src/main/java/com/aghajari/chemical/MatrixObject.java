/*
 * Copyright (C) 2020 - Amir Hossein Aghajari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.aghajari.chemical;

class MatrixObject {
    int rows, cols;
    int[][] cells;

    // A matrix of integers.
    public MatrixObject(int rows, int cols) throws BalancerException {
        if (rows < 0 || cols < 0) throw new BalancerException("Illegal argument");
        this.rows = rows;
        this.cols = cols;

        // Initialize with zeros
        cells = new int[rows][cols];  // Main data (the matrix)
        for (int i = 0; i < rows; i++) {
            cells[i] = new int[cols];
            for (int j = 0; j < cols; j++) cells[i][j] = 0;
        }
    }

    // Returns the value of the given cell in the matrix, where r is the row and c is the column.
    public int get(int r, int c) throws BalancerException {
        if (r < 0 || r >= rows || c < 0 || c >= cols)  throw new BalancerException("Index out of bounds");
        return cells[r][c];
    }

    // Sets the given cell in the matrix to the given value, where r is the row and c is the column.
    public void set(int r, int c, int val) throws BalancerException {
        if (r < 0 || r >= rows || c < 0 || c >= cols) throw new BalancerException("Index out of bounds");
        cells[r][c] = val;
    }

    // Swaps the two rows of the given indices in this matrix. The degenerate case of i == j is allowed.
    void swapRows(int i, int j) throws BalancerException {
        if (i < 0 || i >= rows || j < 0 || j >= rows) throw new BalancerException("Index out of bounds");
        int[] temp = cells[i];
        cells[i] = cells[j];
        cells[j] = temp;
    }

    // Returns a new row that is the sum of the two given rows. The rows are not indices. This object's data is unused.
    // For example, addRow([3, 1, 4], [1, 5, 6]) = [4, 6, 10].
    int[] addRows(int[] x, int[] y) {
        int[] z = x.clone();
        for (int i = 0; i < z.length; i++)
            z[i] = x[i] + y[i];
        return z;
    }

    // Returns a new row that is the product of the given row with the given scalar. The row is is not an index. This object's data is unused.
    // For example, multiplyRow([0, 1, 3], 4) = [0, 4, 12].
    int[] multiplyRow(int[] x, int c) {
        int[] y = x.clone();
        for (int i = 0; i < y.length; i++)
            y[i] = (x[i] * c);
        return y;
    }

    // Returns the GCD of all the numbers in the given row. The row is is not an index. This object's data is unused.
    // For example, gcdRow([3, 6, 9, 12]) = 3.
    int gcdRow(int[] x) {
        int result = 0;
        for (int value : x) result = gcd(value, result);
        return result;
    }

    // Returns the greatest common divisor of the given integers.
    static int gcd(int a, int b) {
        int x = Math.abs(a);
        int y = Math.abs(b);
        while (y != 0) {
            int z = x % y;
            x = y;
            y = z;
        }
        return x;
    }

    // Returns a new row where the leading non-zero number (if any) is positive, and the GCD of the row is 0 or 1. This object's data is unused.
    // For example, simplifyRow([0, -2, 2, 4]) = [0, 1, -1, -2].
    int[] simplifyRow(int[] x) {
        int sign = 0;
        for (int value : x) {
            if (value > 0) {
                sign = 1;
                break;
            } else if (value < 0) {
                sign = -1;
                break;
            }
        }
        int[] y = x.clone();
        if (sign == 0)
            return y;
        int g = gcdRow(x) * sign;
        for (int i = 0; i < y.length; i++)
            y[i] /= g;
        return y;
    }

    // Changes this matrix to reduced row echelon form (RREF), except that each leading coefficient is not necessarily 1. Each row is simplified.
    public void gaussJordanEliminate() throws BalancerException {
        // Simplify all rows
        for (int i = 0; i < rows; i++)
            cells[i] = simplifyRow(cells[i]);

        // Compute row echelon form (REF)
        int numPivots = 0;
        for (int i = 0; i < cols; i++) {
            // Find pivot
            int pivotRow = numPivots;
            while (pivotRow < rows && cells[pivotRow][i] == 0)
                pivotRow++;
            if (pivotRow == rows)
                continue;
            int pivot = cells[pivotRow][i];
            swapRows(numPivots, pivotRow);
            numPivots++;

            // Eliminate below
            for (int j = numPivots; j < rows; j++) {
                int g = gcd(pivot, cells[j][i]);
                cells[j] = simplifyRow(addRows(multiplyRow(cells[j], pivot / g), multiplyRow(cells[i], -cells[j][i] / g)));
            }
        }

        // Compute reduced row echelon form (RREF), but the leading coefficient need not be 1
        for (int i = rows - 1; i >= 0; i--) {
            // Find pivot
            int pivotCol = 0;
            while (pivotCol < cols && cells[i][pivotCol] == 0)
                pivotCol++;
            if (pivotCol == cols)
                continue;
            int pivot = cells[i][pivotCol];

            // Eliminate above
            for (int j = i - 1; j >= 0; j--) {
                int g = gcd(pivot, cells[j][pivotCol]);
                cells[j] = simplifyRow(addRows(multiplyRow(cells[j], pivot / g), multiplyRow(cells[i], -cells[j][pivotCol] / g)));
            }
        }
    }

    // Returns a string representation of this matrix, for debugging purposes.

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < rows; i++) {
            if (i != 0) result.append("],\n");
            result.append("[");
            for (int j = 0; j < cols; j++) {
                if (j != 0) result.append(", ");
                result.append(cells[i][j]);
            }
            result.append("]");
        }
        return result + "]";
    }
}
