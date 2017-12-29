package com.copypastapublishing.predictoe;

import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

/**
 * Created by ghoulish on 12/28/2017.
 */

public class MarkovChain {
    public static void addWords(String phrase, Hashtable<String, Vector<String>> markovChain) {
        //Here is where I must add multiple orders to my list

        String[] words;
        markovChain.put("_start", new Vector<String>());
        words = phrase.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (i == 0) {
                Vector<String> startWords = markovChain.get("_start");
                startWords.add(words[i]);
                Vector<String> suffix = markovChain.get(words[i]);
                if (suffix == null) {
                    suffix = new Vector<String>();
                    suffix.add(words[i + 1]);
                    markovChain.put(words[i], suffix);
                }
            } else if (i == words.length - 1) {
                Vector<String> endWords = markovChain.get("_end");
                endWords.add(words[i]);
            } else {
                Vector<String> suffix = markovChain.get(words[i]);
                if (suffix == null) {
                    suffix = new Vector<String>();
                    suffix.add(words[i + 1] + " " + words[i + 2]);
                    markovChain.put(words[i], suffix);
                } else {
                    suffix.add(words[i + 1]);
                    markovChain.put(words[i], suffix);
                }
            }
        }
    }
    public static String generateInk(Hashtable<String, Vector<String>> markovChain, String tip) {
        Random rnd = new Random();
        String nextWord=tip;

        String newWord = new String("");
        Vector<String> newPhrase = new Vector<String>();

        Vector<String> nextWords = markovChain.get(tip);

        if (nextWords != null && nextWords.size() != 0) {
            try {
                for (int i = 0; i < 8; i++) {
                    Vector<String> wordSelection = markovChain.get(nextWord);
                    int wordSelectionLen = wordSelection.size();
                    nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen));
                    newPhrase.add(nextWord);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return generateInk(markovChain, tip);
            }catch (NullPointerException n){


            }
        } else {
        }
        return newPhrase.toString().replace("[", "").replace("]", "").replace(",", "");

    }
}
