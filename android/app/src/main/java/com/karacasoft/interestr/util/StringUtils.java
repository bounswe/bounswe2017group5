package com.karacasoft.interestr.util;

/**
 * Created by karacasoft on 30.10.2017.
 */

public class StringUtils {

    public static String pluralize(int count, String word) {
        return ((count == 0) ? "No" : count) + " " + word + ((count == 1) ? "" : "s");
    }

    public static String pluralize(String word) {
        return word + "s";
    }

}
