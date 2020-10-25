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

// A chemical element.
// For example: Na, F2, Ace, Uuq6
class Element implements ElementInterface {
    String name;
    int count;

    public Element(String name, int count) {
        if (count < 1)
            throw new RuntimeException("Assertion error: Count must be a positive integer");
        this.name = name;
        this.count = count;
    }

    @Override
    public void getElements(ArrayList<String> list) {
        if (!list.contains(name)) list.add(name);
    }

    @Override
    public int countElement(String name) {
        return name.equals(this.name) ? count : 0;
    }

    @Override
    public BalancerSpan getSpan() {
        BalancerSpan span = new BalancerSpan(BalancerSpan.BalancerSpanType.ELEMENT,name);
        if (count != 1) {
            BalancerSpan sub = new BalancerSpan(BalancerSpan.BalancerSpanType.SUB,String.valueOf(count));
            span.addChild(sub);
        }
        return span;
    }
}