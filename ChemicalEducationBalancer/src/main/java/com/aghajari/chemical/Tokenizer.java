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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Tokenize a formula into a stream of token strings.
class Tokenizer {
    int i;
    String str;
    Pattern pattern;

    public Tokenizer(String str) {
        i = 0;
        this.str = str.trim();
        pattern = Pattern.compile("^([A-Za-z][a-z]*|[0-9]+|[+\\-^=()])");
    }

    // Returns the index of the next character to tokenize.
    public int position() {
        return i;
    }

    // Returns the next token as a string, or null if the end of the token stream is reached.
    public String peek() throws BalancerException {
        if (i == str.length())  // End of stream
            return null;
        try {
            Matcher match = pattern.matcher(str.substring(i));
            match.find();
            return match.group(0);
        }catch (Exception e) {
            e.printStackTrace();
            throw new BalancerException("Invalid symbol",i);
        }
    }

    // Returns the next token as a string and advances this tokenizer past the token.
    public String take() throws BalancerException {
        String result = this.peek();
        //if (result == null) throw "Advancing beyond last token"
        i += result.length();
        skipSpaces();
        return result;
    }

    // Takes the next token and checks that it matches the given string, or throws an exception.
    public void consume(String s) throws BalancerException {
        if (!this.take().equals(s)) throw new BalancerException("Token mismatch: " + s);
    }

    public void skipSpaces() {
        try {
            Pattern pattern = Pattern.compile("^[ \\t]*");
            Matcher matcher = pattern.matcher(str.substring(i));
            matcher.find();
            i += matcher.group().length();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}