package com.ercan.wordbuzzer.util;

import com.ercan.wordbuzzer.R;

import net.orange_box.storebox.annotations.method.DefaultValue;
import net.orange_box.storebox.annotations.method.KeyByString;

/**
 * Created by ercanozcan on 21/02/16.
 */
public interface SharePrefUtils {

    @KeyByString("sound")
    Boolean getSound();

    @KeyByString("sound")
    @DefaultValue(R.bool.sound_default_value)
    void setSound(Boolean sound);
}
