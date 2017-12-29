package com.copypastapublishing.predictoe;

import java.util.Random;

/**
 * Created by ghoulish on 12/28/2017.
 */

public class TextManipulate {
    public static String tipFinder(String lines) {
        try{String reduced = lines.replace(". ", "").replace("? ", "").replace("! ", "").replace("\" ", "");
        String[] tipfinder = reduced.split(" ");
        String tip = tipfinder[tipfinder.length - 1];
        return tip.toLowerCase();
    }catch(ArrayIndexOutOfBoundsException a){
            return "\n\t \t \t";
        }}
    public static String rndWord(String contents) {
        Random rnd = new Random();
        String[] token = contents.split(" ");
        String word;
        word = token[rnd.nextInt(token.length)];
        return word;
    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }
}
