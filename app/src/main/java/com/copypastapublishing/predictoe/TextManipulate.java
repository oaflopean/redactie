package com.copypastapublishing.predictoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

/**
 * Created by ghoulish on 12/28/2017.
 */

public class TextManipulate {


    public static String toTitleCase(String input) {
        input = input.substring(0, 1).toUpperCase() + input.substring(1);
        return input;
    }

}
