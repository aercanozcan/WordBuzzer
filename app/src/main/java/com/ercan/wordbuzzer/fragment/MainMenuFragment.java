package com.ercan.wordbuzzer.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ercan.wordbuzzer.GameApp;
import com.ercan.wordbuzzer.R;
import com.ercan.wordbuzzer.data.model.Word;
import com.ercan.wordbuzzer.game.MediaManager;
import com.ercan.wordbuzzer.game.model.Game;
import com.ercan.wordbuzzer.game.model.WordBuzzerAnimatorListener;
import com.ercan.wordbuzzer.util.SharePrefUtils;

import net.orange_box.storebox.StoreBox;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ercanozcan on 17/02/16.
 */
public class MainMenuFragment extends Fragment {

    View rootView;
    @Bind(R.id.txtTitlePart1)
    TextView txtTitlePart1;
    @Bind(R.id.txtTitlePart2)
    TextView txtTitlePart2;
    @Bind(R.id.btnStart)
    Button btnStart;
    @Bind(R.id.swSound)
    Switch swSound;
    @Bind(R.id.txtAnswer)
    TextView txtAnswer;
    boolean animationFlag = false;
    int luckyNumber = 7;
    int animationCount = 0;
    private SharePrefUtils sharePrefUtils;// only for sound preference for now

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_menu_layout, container, false);
        ButterKnife.bind(this, rootView);
        sharePrefUtils = StoreBox.create(getActivity(), SharePrefUtils.class);
        swSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MediaManager.getInstance(getActivity()).playMainTheme();
                } else {
                    MediaManager.getInstance(getActivity()).stopMainTheme();
                }
                sharePrefUtils.setSound(isChecked);
            }
        });
        swSound.setChecked(sharePrefUtils.getSound());


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ObjectAnimator.ofFloat(txtTitlePart1, "translationX", +1200, 0).setDuration(1000).start();//from right
        ObjectAnimator.ofFloat(txtTitlePart2, "translationX", -1200, 0).setDuration(1000).start();//from left
        if (new Random().nextBoolean()) {//  randomly starts animation
            enterFromRight();
        } else {
            enterFromLeft();
        }

    }

    @OnClick(R.id.btnStart)
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragmentContainer, new GameFragment()).addToBackStack("GameFragment").commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * animates the {@link #txtAnswer}from left to right
     */
    public void enterFromLeft() {

        ObjectAnimator enterFromLeft = ObjectAnimator.
                ofFloat(txtAnswer, "translationX", -txtAnswer.getWidth(), rootView.getWidth() + txtAnswer.getWidth());
        enterFromLeft.setDuration(new Random().nextInt(2001) + 1000);//random duration between 1000-3000 ms
        enterFromLeft.setInterpolator(new LinearInterpolator());
        enterFromLeft.addListener(new WordBuzzerAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    prepareForSlide();
                    enterFromRight();
                } catch (Exception e) {//to prevent callback after fragment removed

                }
            }
        });

        enterFromLeft.start();
    }


    /**
     * animates the {@link #txtAnswer}from right to left
     */
    public void enterFromRight() {
        ObjectAnimator enterFromRight = ObjectAnimator.
                ofFloat(txtAnswer, "translationX", rootView.getWidth() + txtAnswer.getWidth(), -txtAnswer.getWidth());
        enterFromRight.setInterpolator(new LinearInterpolator());
        enterFromRight.setDuration(new Random().nextInt(2001) + 1000);//random duration between 1000-3000 ms
        enterFromRight.addListener(new WordBuzzerAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    prepareForSlide();
                    enterFromLeft();
                } catch (Exception e) {//to prevent callback after fragment removed

                }

            }
        });
        enterFromRight.start();
    }

    private void prepareForSlide() {
        Word word = GameApp.getWords().get(new Game(0, 0, null, null).getRandomIndex());
        animationCount++;

        if (animationCount % luckyNumber == 0) {
            txtAnswer.setText("HIRE ME!");
        } else {

            txtAnswer.setText(new Random().nextBoolean() ? word.getSpa() : word.getEng());
        }

        if (!animationFlag) {
            animationFlag = true;
        } else {
            txtAnswer.setTranslationY(-(rootView.getHeight() / 2) + new Random().nextInt(rootView.getHeight() / 2));//the frame of contaner layout
        }
    }



}
