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

public class BalancerException extends Exception {

    private int start = -100,end = -100;
    private String msg;

    public BalancerException (String msg){
        this.msg = msg;
    }

    public BalancerException (String msg,int start){
        this.msg = msg;
        this.start = start;
    }

    public BalancerException (String msg,int start,int end){
        this.msg = msg;
        this.start = start;
        this.end = end;
    }

    public BalancerException (Exception e){
        this.msg = e.getMessage();
        this.initCause(e);
    }

    public int getStart() {
        if (start==-100) return -1;
        return start;
    }

    public int getEnd() {
        if (end==-100) return -1;
        return end;
    }

    @Override
    public String getMessage() {
        StringBuilder result = new StringBuilder(msg);
        if (start!=-100) result.append(" start:"+start);
        if (end!=-100) result.append(" end:"+end);
        return result.toString();
    }

    @Override
    public String getLocalizedMessage() {
        return msg;
    }

}
