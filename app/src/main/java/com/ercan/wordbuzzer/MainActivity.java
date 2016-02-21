package com.ercan.wordbuzzer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ercan.wordbuzzer.fragment.MainMenuFragment;
import com.ercan.wordbuzzer.game.MediaManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragmentContainer, new MainMenuFragment()).addToBackStack("MainMenuFragment").commit();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {// finish the activity when only MainMenuFragment is left in backstack
            finish();
        } else {
            super.onBackPressed();
        }


    }

    @Override
    protected void onDestroy() {
        try {
            MediaManager.getInstance(this).stopMainTheme();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }
}
