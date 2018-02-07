package com.copypastapublishing.predictoe;

import org.w3c.dom.Text;

import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

/**
 * Created by ghoulish on 12/28/2017.
 */

public class MarkovChain {

    public static String tipFinder(String lines) {
        try {
            String reduced = lines.replace(",", "").replace(". ", "").replace("? ", "").replace("! ", "").replace("\" ", "");
            String[] tipfinder = reduced.split(" ");
            if (tipfinder[tipfinder.length-1].contains(".")){
                return ". ";
            }
            String tip = tipfinder[tipfinder.length - 1];
            return tip;
        } catch (ArrayIndexOutOfBoundsException a) {
            return "\n\t \t \t";
        }
    }

    public static void addWords(String phrase, Hashtable<String, Vector<String>> markovChain) {
        //Here is where I must add multiple orders to my list

        String[] words;
        markovChain.put("_start", new Vector<String>());
        words = phrase.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[i].contains(".")) {
                Vector<String> startWords = markovChain.get("_start");
                try {
                    startWords.add(words[i + 1]);

                    Vector<String> suffix = markovChain.get(words[i]);
                    if (suffix == null) {
                        suffix = new Vector<String>();
                        suffix.add(words[i + 1]);
                        markovChain.put(words[i], suffix);
                    }
                } catch (ArrayIndexOutOfBoundsException a) {

                }
            } else {
                Vector<String> suffix = markovChain.get(words[i]);

                if (suffix == null) {
                    suffix = new Vector<String>();
                    suffix.add(words[i + 1]);
                    markovChain.put(words[i], suffix);
                } else {
                    suffix.add(words[i + 1]);
                    markovChain.put(words[i], suffix);
                }
            }
        }
    }


    public static String generateInk(String tip, Hashtable<String, Vector<String>> markovChain, int spinner, boolean toEnd) {
        Random rnd = new Random();
        String nextWord = tip;

        String newWord = new String("");
        Vector<String> newPhrase = new Vector<String>();

        Vector<String> nextWords = markovChain.get(tip);

        if (nextWords != null && nextWords.size() != 0) {
            try {
                if (toEnd==false){
                for (int i = 0; i < spinner; i++) {
                    Vector<String> wordSelection = markovChain.get(nextWord);
                    int wordSelectionLen = wordSelection.size();
                    nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen));

                    newPhrase.add(nextWord);
                }}
                else {
                    while (nextWord.charAt(nextWord.length()-1) != '.'){
                        Vector<String> wordSelection = markovChain.get(nextWord);
                        int wordSelectionLen = wordSelection.size();
                        nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen));

                        newPhrase.add(nextWord);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return generateInk(tip, markovChain, spinner, toEnd);
            } catch (NullPointerException n) {


            }
        } else {
        }
        return newPhrase.toString().replace("[", "").replace("]", "").replace(",", "").replace("\"", "");

    }

    public static String rndWord(Hashtable<String, Vector<String>> markovChain, Integer spinner) {
        Random rnd = new Random();
        Vector<String> newPhrase = new Vector<String>();
        String nextWord = "";
        Vector<String> startWords = markovChain.get("_start");
        int startWordsLen = startWords.size();
        nextWord = startWords.get(rnd.nextInt(startWordsLen));
        String tip = new String();
        tip = nextWord;
        String newWord = new String("");
        Vector<String> nextWords = markovChain.get(tip);
        if (nextWords != null && nextWords.size() != 0) {
            try {
                for (int i = 0; i < spinner; i++) {
                    Vector<String> wordSelection = markovChain.get(nextWord);
                    int wordSelectionLen = wordSelection.size();
                    nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen));

                    newPhrase.add(nextWord);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            } catch (NullPointerException n) {
            }
        } else {
        }
        return TextManipulate.toTitleCase(tip) + " " + newPhrase.toString().replace("[", "").replace("]", "").replace(",", "").replace("\"", "");
    }
}
