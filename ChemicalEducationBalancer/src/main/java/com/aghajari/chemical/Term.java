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

// A term in a chemical equation. It has a list of groups or elements, and a charge.
// For example: H3O^+, or e^-.
class Term implements ElementInterface {
    ArrayList<ElementInterface> items;
    int charge;

    public Term(ArrayList<ElementInterface> items, int charge) {
        if (items.isEmpty() && charge != -1)
            throw new RuntimeException("Invalid term");  // Electron case
        this.charge = charge;
        this.items = items;
    }

    @Override
    public void getElements(ArrayList<String> list) {
        if (!list.contains("e")) list.add("e");
        for (int i = 0; i < items.size(); i++)
            items.get(i).getElements(list);
    }

    @Override
    public int countElement(String name) {
        if (name.equals("e")) {
            return -charge;
        } else {
            int sum = 0;
            for (int i = 0; i < items.size(); i++)
                sum = sum + items.get(i).countElement(name);
            return sum;
        }
    }

    @Override
    public BalancerSpan getSpan() {
        BalancerSpan span = new BalancerSpan(BalancerSpan.BalancerSpanType.TERM,"");
        if (items.isEmpty()&& charge == -1) {
            span.appendText("e");
            BalancerSpan sup = new BalancerSpan(BalancerSpan.BalancerSpanType.SUP, ChemicalEducationBalancer.MINUS);
            span.addChild(sup);
        } else {
            for (int i = 0; i < items.size(); i++)
                span.addChild(items.get(i).getSpan());
            if (charge != 0) {
                String s;
                if (Math.abs(charge) == 1) s = "";
                else s = String.valueOf(Math.abs(charge));
                if (charge > 0) s += "+";
                else s += ChemicalEducationBalancer.MINUS;
                BalancerSpan sup = new BalancerSpan(BalancerSpan.BalancerSpanType.SUP,s);
                span.addChild(sup);
            }
        }
        return span;
    }

}
