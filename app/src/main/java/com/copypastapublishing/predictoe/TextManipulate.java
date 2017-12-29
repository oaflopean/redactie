package com.copypastapublishing.predictoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by ghoulish on 12/28/2017.
 */

public class TextManipulate {


    public static String rndWord(String contents) {
        Random rnd = new Random();
        String[] token = contents.split(" ");
        String word;
        word = token[rnd.nextInt(token.length)];
        return word;
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
