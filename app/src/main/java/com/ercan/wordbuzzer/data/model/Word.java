package com.ercan.wordbuzzer.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ercanozcan on 17/02/16.
 * <p/>
 * A POJO for holding word data
 */
public class Word {

    @SerializedName("text_eng")
    private String eng;
    @SerializedName("text_spa")
    private String spa;

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getSpa() {
        return spa;
    }

    public void setSpa(String spa) {
        this.spa = spa;
    }
}
