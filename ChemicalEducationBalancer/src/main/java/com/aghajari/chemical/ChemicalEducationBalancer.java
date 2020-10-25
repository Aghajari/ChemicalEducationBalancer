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

import java.util.ArrayList;

public class ChemicalEducationBalancer {

    // Unicode character constants (because this script file's character encoding is unspecified)
    public static final String MINUS = "\u2212";        // Minus sign
    public static final String RIGHT_ARROW = "\u2192";  // Right arrow

    public static BalancerSpan balance(String formula) throws BalancerException {
        try {
            Equation eqn = parse(formula);                          // Parse equation
            MatrixObject matrix = buildMatrix(eqn);                 // Set up matrix
            solve(matrix);                                          // Solve linear system
            ArrayList<Integer> coefs = extractCoefficients(matrix); // Get coefficients
            checkAnswer(eqn, coefs);                                // Self-test, should not fail
            return eqn.getSpan(coefs);
        }catch (Exception e){
            throw new BalancerException(e);
        }
    }

    public static BalancerSpan read(String formula) throws BalancerException {
        try {
            Equation eqn = parse(formula);
            return eqn.getSpan(null);
        }catch (Exception e){
            throw new BalancerException(e);
        }
    }

    public static int[][] readMatrix(String formula) throws BalancerException {
        try {
            Equation eqn = parse(formula);
            MatrixObject matrix = buildMatrix(eqn);
            return matrix.cells;
        }catch (Exception e){
            throw new BalancerException(e);
        }
    }

    /**
     * "H2 + O2 = H2O"
     * "Fe + O2 = Fe2O3"
     * "NH3 + O2 = N2 + H2O"
     * "C2H2 + O2 = CO2 + H2O"
     * "C3H8O + O2 = CO2 + H2O"
     * "Na + O2 = Na2O"
     * "P4 + O2 = P2O5"
     * "Na2O + H2O = NaOH"
     * "Mg + HCl = MgCl2 + H2"
     * "AgNO3 + LiOH = AgOH + LiNO3"
     * "Pb + PbO2 + H^+ + SO4^2- = PbSO4 + H2O"
     * "HNO3 + Cu = Cu(NO3)2 + H2O + NO"
     * "KNO2 + KNO3 + Cr2O3 = K2CrO4 + NO"
     * "AgNO3 + BaCl2 = Ba(NO3)2 + AgCl"
     * "Cu(NO3)2 = CuO + NO2 + O2"
     * "Al + CuSO4 = Al2(SO4)3 + Cu"
     * "Na3PO4 + Zn(NO3)2 = NaNO3 + Zn3(PO4)2"
     * "Cl2 + Ca(OH)2 = Ca(ClO)2 + CaCl2 + H2O"
     * "CHCl3 + O2 = CO2 + H2O + Cl2"
     * "H2C2O4 + MnO4^- = H2O + CO2 + MnO + OH^-"
     * "H2O2 + Cr2O7^2- = Cr^3+ + O2 + OH^-"
     * "KBr + KMnO4 + H2SO4 = Br2 + MnSO4 + K2SO4 + H2O"
     * "K2Cr2O7 + KI + H2SO4 = Cr2(SO4)3 + I2 + H2O + K2SO4"
     * "KClO3 + KBr + HCl = KCl + Br2 + H2O"
     * "Ag + HNO3 = AgNO3 + NO + H2O"
     * "P4 + OH^- + H2O = H2PO2^- + P2H4"
     * "Zn + NO3^- + H^+ = Zn^2+ + NH4^+ + H2O"
     * "ICl + H2O = Cl^- + IO3^- + I2 + H^+"
     * "AB2 + AC3 + AD5 + AE7 + AF11 + AG13 + AH17 + AI19 + AJ23 = A + ABCDEFGHIJ"
     */

    // Returns a matrix based on the given equation object.
    static MatrixObject buildMatrix(Equation eqn) throws BalancerException {
        ArrayList<String> elems = eqn.getElements();
        int rows = elems.size() + 1;
        int cols = eqn.lhs.size() + eqn.rhs.size() + 1;

        MatrixObject matrix = new MatrixObject(rows, cols);
        for (int i = 0; i < elems.size(); i++) {
            int j = 0;
            for (int k = 0; k < eqn.lhs.size(); j++, k++)
                matrix.set(i, j, eqn.lhs.get(k).countElement(elems.get(i)));
            for (int k2 = 0; k2 < eqn.rhs.size(); j++, k2++)
                matrix.set(i, j, -eqn.rhs.get(k2).countElement(elems.get(i)));
        }
        return matrix;
    }

    static void solve(MatrixObject matrix) throws BalancerException {
        matrix.gaussJordanEliminate();

        // Find row with more than one non-zero coefficient
        int i;
        for (i = 0; i < matrix.rows - 1; i++) {
            if (countNonzeroCoeffs(matrix, i) > 1)
                break;
        }

        if (i == matrix.rows - 1)
            throw new BalancerException("Something wrong! can't solve the equation \n" + matrix.toString());  // Unique solution with all coefficients zero

        // Add an inhomogeneous equation
        matrix.set(matrix.rows - 1, i, 1);
        matrix.set(matrix.rows - 1, matrix.cols - 1, 1);

        matrix.gaussJordanEliminate();
    }

    static int countNonzeroCoeffs(MatrixObject matrix, int row) throws BalancerException {
        int count = 0;
        for (int i = 0; i < matrix.cols; i++) {
            if (matrix.get(row, i) != 0)
                count++;
        }
        return count;
    }

    static ArrayList<Integer> extractCoefficients(MatrixObject matrix) throws BalancerException {
        int rows = matrix.rows;
        int cols = matrix.cols;

        if (cols - 1 > rows || matrix.get(cols - 2, cols - 2) == 0) {
            throw new BalancerException("The given input is a combination of multiple independent chemical equations");
        }

        int lcm = 1;
        for (int i = 0; i < cols - 1; i++)
            lcm = (lcm / MatrixObject.gcd(lcm, matrix.get(i, i)) * matrix.get(i, i));

        ArrayList<Integer> coefs = new ArrayList<>();
        boolean allzero = true;
        for (int i = 0; i < cols - 1; i++) {
            int coef = (lcm / matrix.get(i, i) * matrix.get(i, cols - 1));
            coefs.add(coef);
            allzero &= coef == 0;
        }
        if (allzero)
            throw new BalancerException("Assertion error: All-zero solution");
        return coefs;
    }

    // Throws an exception if there's a problem, otherwise returns silently.
    static void checkAnswer(Equation eqn, ArrayList<Integer> coefs) throws BalancerException {
        if (coefs.size() != eqn.lhs.size() + eqn.rhs.size())
            throw new BalancerException("Assertion error: Mismatched length");

        boolean allzero = true;
        for (int i = 0; i < coefs.size(); i++) {
            int coef = coefs.get(i);
            allzero &= coef == 0;
        }
        if (allzero)
            throw new BalancerException("Assertion error: All-zero solution");

        ArrayList<String> elems = eqn.getElements();
        for (int i = 0; i < elems.size(); i++) {
            int sum = 0;
            int j = 0;
            for (int k = 0; k < eqn.lhs.size(); j++, k++)
                sum = sum + (eqn.lhs.get(k).countElement(elems.get(i)) * coefs.get(j));
            for (int k = 0; k < eqn.rhs.size(); j++, k++)
                sum = sum + (eqn.rhs.get(k).countElement(elems.get(i)) * -coefs.get(j));
            if (sum != 0)
                throw new BalancerException("Assertion error: Incorrect balance");
        }
    }

    // Parses the given formula string and returns an equation object, or throws an exception.
    static Equation parse(String formulaStr) throws BalancerException {
        Tokenizer tokenizer = new Tokenizer(formulaStr);
        return parseEquation(tokenizer);
    }

    // Parses and returns an equation.
    static Equation parseEquation(Tokenizer tok) throws BalancerException {
        ArrayList<Term> lhs = new ArrayList<>();
        ArrayList<Term> rhs = new ArrayList<>();

        lhs.add(parseTERMO(tok));
        while (true) {
            String next = tok.peek();
            if (next.equals("=")) {
                tok.consume("=");
                break;
            } else if (next == null) {
                throw new BalancerException("Please complete the equation.", tok.position());
            } else if (next.equals("+")) {
                tok.consume("+");
                lhs.add(parseTERMO(tok));
            } else
                throw new BalancerException("Plus expected.", tok.position());
        }

        rhs.add(parseTERMO(tok));
        while (true) {
            String next = tok.peek();
            if (next == null)
                break;
            else if (next.equals("+")) {
                tok.consume("+");
                rhs.add(parseTERMO(tok));
            } else
                throw new BalancerException("Plus or end expected.", tok.position());
        }

        return new Equation(lhs, rhs);
    }

    // Parses and returns a term.
    static Term parseTERMO(Tokenizer tok) throws BalancerException {
        int startPosition = tok.position();

        // Parse groups and elements
        ArrayList<ElementInterface> items = new ArrayList<>();
        while (true) {
            String next = tok.peek();
            if (next == null)
                break;
            else if (next.equals("("))
                items.add(parseGroup(tok));
            else if (next.matches("^[A-Za-z][a-z]*$"))
                items.add(parseElement(tok));
            else
                break;
        }

        // Parse optional charge
        int charge = 0;
        String next = tok.peek();
        if (next != null && next.equals("^")) {
            tok.consume("^");
            next = tok.peek();
            if (next == null)
                throw new BalancerException("Number or sign expected.", tok.position());
            else
                charge = parseOptionalNumber(tok);

            next = tok.peek();
            if (next.equals("+"))
                charge = +charge;  // No-op
            else if (next.equals("-"))
                charge = -charge;
            else
                throw new BalancerException("Sign expected.", tok.position());
            tok.take();  // Consume the sign
        }

        // Check if term is valid
        ArrayList<String> elems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++)
            items.get(i).getElements(elems);

        if (items.isEmpty()) {
            throw new RuntimeException(" start:" + startPosition + " end:" + tok.position());
        } else if (elems.indexOf("e") != -1) {  // If it's the special electron element
            if (items.size() > 1)
                throw new BalancerException("Invalid term - electron needs to stand alone.", startPosition, tok.position());
            else if (charge != 0 && charge != -1)
                throw new BalancerException("Invalid term - invalid charge for electron.", startPosition, tok.position());
            // Tweak data
            items = new ArrayList<>();
            charge = -1;
        } else {  // Otherwise, a term must not contain an element that starts with lowercase
            for (int i = 0; i < elems.size(); i++) {
                if (elems.get(i).matches("^[a-z]+$"))
                    throw new BalancerException("something is wrong!", startPosition, tok.position());
            }
        }

        return new Term(items, charge);
    }

    // Parses and returns a group.
    static Group parseGroup(Tokenizer tok) throws BalancerException {
        int startPosition = tok.position();
        tok.consume("(");
        ArrayList<ElementInterface> items = new ArrayList<>();
        while (true) {
            String next = tok.peek();
            if (next == null)
                throw new BalancerException("Element, group, or closing parenthesis expected.", tok.position());
            else if (next.equals("("))
                items.add(parseGroup(tok));
            else if (next.matches("^[A-Za-z][a-z]*$"))
                items.add(parseElement(tok));
            else if (next.equals(")")) {
                tok.consume(")");
                if (items.size() == 0)
                    throw new BalancerException("Empty group.", startPosition, tok.position());
                break;
            } else
                throw new BalancerException("Element, group, or closing parenthesis expected.", tok.position());
        }

        return new Group(items, parseOptionalNumber(tok));
    }

    // Parses and returns an element.
    static Element parseElement(Tokenizer tok) throws BalancerException {
        String name = tok.take();
        if (!name.matches("^[A-Za-z][a-z]*$"))
            throw new BalancerException("Assertion error");
        return new Element(name, parseOptionalNumber(tok));
    }

    // Parses a number if it's the next token, returning a non-negative integer, with a default of 1.
    static int parseOptionalNumber(Tokenizer tok) throws BalancerException {
        String next = tok.peek();
        if (next != null && next.matches("^[0-9]+$"))
            return Integer.parseInt(tok.take());
        else return 1;
    }
}
