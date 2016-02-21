package com.ercan.wordbuzzer;

import android.app.Application;

import com.ercan.wordbuzzer.data.model.Word;
import com.ercan.wordbuzzer.util.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ercanozcan on 17/02/16.
 */
public class GameApp extends Application {

    public static String WORDS_PATH = "words.json";
    private static List<Word> words;

    public static List<Word> getWords() {
        return words;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prepareWords();

    }

    private void prepareWords() {
        Type listType = new TypeToken<List<Word>>() {
        }.getType();
        words = new Gson().fromJson(CommonUtils.readFileFromAssets(getAssets(), WORDS_PATH), listType);
    }
}
