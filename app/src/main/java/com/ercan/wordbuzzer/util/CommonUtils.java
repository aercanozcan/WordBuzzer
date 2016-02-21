package com.ercan.wordbuzzer.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.ercan.wordbuzzer.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by ercanozcan on 17/02/16.
 */
public class CommonUtils {


    /**
     * Reads the text of an asset. Should not be run on the UI thread.
     *
     * @param mgr
     *            The {@link AssetManager} obtained via {@link Context#getAssets()}
     * @param path
     *            The path to the asset.
     * @return The plain text of the asset
     */
    public static String readFileFromAssets(AssetManager mgr, String path) {
        String contents = "";
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = mgr.open(path);
            reader = new BufferedReader(new InputStreamReader(is));
            contents = reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
                contents += '\n' + line;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return contents;
    }

    public static int getDrawableIdforScore(int score) {
        switch (score) {
            case 1:
                return R.drawable.one;
            case 2:
                return R.drawable.two;
            case 3:
                return R.drawable.win;
            default:
                return R.drawable.win;
        }
    }
}
