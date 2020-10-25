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

public class BalancerSpan {

    private BalancerSpanType type;
    private String value;
    private ArrayList<BalancerSpan> children;

    public enum BalancerSpanType{
        EQUATION,
        TERM,
        GROUP,
        ELEMENT,
        SUB,
        TEXT,
        SUP,
        RIGHT_ARROW,
        PLUS,
        COEFFICIENT
    }

    public BalancerSpan(BalancerSpanType type,String value){
        this.type = type;
        this.value = value;
    }

    public BalancerSpanType getType(){
        return type;
    }

    public String getValue(){
        return value;
    }

    public String getResult(){
        StringBuilder v = new StringBuilder(value);
        if (children!=null && !children.isEmpty()){
            for (BalancerSpan span : children){
                v.append(span.getResult());
            }
        }
        return v.toString();
    }

    public ArrayList<BalancerSpan> getChildren(){
        return children;
    }

    void addChild(BalancerSpan childSpan){
        if (children==null) children = new ArrayList<>();
        children.add(childSpan);
    }

    void appendText(String value){
        addChild(new BalancerSpan(BalancerSpanType.TEXT,value));
    }

    @Override
    public String toString() {
        return getResult();
    }
}
