package com.ercan.wordbuzzer.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by ercanozcan on 17/02/16.
 */
public class CommonUtils {

    /**
     * Reads File from assets and returns as string
     *
     * @param context
     * @param path
     * @return file as String or empty String
     */
    public static String readFileFromAssets(Context context, String path) {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = context.getAssets().open(path);
            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }

            in.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
