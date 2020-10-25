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

// A group in a term. It has a list of groups or elements.
// For example: (OH)3
class Group implements ElementInterface {
    ArrayList<ElementInterface> items;
    int count;

    public Group(ArrayList items, int count) {
        if (count < 1)
            throw new RuntimeException("Assertion error: Count must be a positive integer");
        this.items = items;
        this.count = count;
    }

    @Override
    public void getElements(ArrayList<String> list) {
        for (int i = 0; i < items.size(); i++)
            items.get(i).getElements(list);
    }

    @Override
    public int countElement(String name) {
        int sum = 0;
        for (int i = 0; i < items.size(); i++)
            sum = sum + (items.get(i).countElement(name) * count);
        return sum;
    }

    @Override
    public BalancerSpan getSpan() {
        BalancerSpan span = new BalancerSpan(BalancerSpan.BalancerSpanType.GROUP,"");
        span.appendText("(");
        for (int i = 0; i < items.size(); i++) {
            span.addChild(items.get(i).getSpan());
        }
        span.appendText(")");
        if (count != 1) {
            BalancerSpan sub = new BalancerSpan(BalancerSpan.BalancerSpanType.SUB,String.valueOf(count));
            span.addChild(sub);
        }
        return span;
    }

}