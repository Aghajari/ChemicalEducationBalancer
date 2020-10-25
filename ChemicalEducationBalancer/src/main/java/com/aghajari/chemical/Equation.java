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

// A complete chemical equation. It has a left-hand side list of terms and a right-hand side list of terms.
// For example: H2 + O2 -> H2O.
class Equation {

    ArrayList<Term> lhs, rhs;

    public Equation(ArrayList<Term> lhs, ArrayList<Term> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    // Returns an array of the names all of the elements used in this equation.
    // The array represents a set, so the items are in an arbitrary order and no item is repeated.
    public ArrayList<String> getElements() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < lhs.size(); i++)
            lhs.get(i).getElements(result);
        for (int i = 0; i < rhs.size(); i++)
            rhs.get(i).getElements(result);
        return result;
    }

    // Returns result of this equation.
    // 'coefs' is an optional argument, which is an array of coefficients to match with the terms.
    public BalancerSpan getSpan(ArrayList<Integer> coefs) {
        if (coefs !=null && coefs.size() != lhs.size() + rhs.size())
            throw new RuntimeException("Mismatched number of coefficients");

        BalancerSpan span = new BalancerSpan(BalancerSpan.BalancerSpanType.EQUATION,"");
        int j = 0;
        j = termsToSpan(span,coefs,lhs,j);
        BalancerSpan right_arrow = new BalancerSpan(BalancerSpan.BalancerSpanType.RIGHT_ARROW," " + ChemicalEducationBalancer.RIGHT_ARROW + " ");
        span.addChild(right_arrow);
        termsToSpan(span,coefs,rhs,j);
        return span;
    }

    int termsToSpan(BalancerSpan span,ArrayList<Integer> coefs,ArrayList<Term> terms,int sj) {
        int j = sj;
        boolean head = true;
        for (int i = 0; i < terms.size(); i++, j++) {
            int coef = coefs != null ? coefs.get(j) : 1;
            if (coef != 0) {
                if (head) {
                    head = false;
                } else {
                    BalancerSpan plus = new BalancerSpan(BalancerSpan.BalancerSpanType.PLUS," + ");
                    span.addChild(plus);
                }
                if (coef != 1) {
                    String c = String.valueOf(coef).replaceFirst("-", ChemicalEducationBalancer.MINUS);
                    BalancerSpan coefficient = new BalancerSpan(BalancerSpan.BalancerSpanType.COEFFICIENT,c);
                    span.addChild(coefficient);
                }
                span.addChild(terms.get(i).getSpan());
            }
        }
        return j;
    }

}