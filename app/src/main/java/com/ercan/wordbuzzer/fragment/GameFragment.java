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
import android.widget.TextView;
import android.widget.Toast;

import com.ercan.wordbuzzer.R;
import com.ercan.wordbuzzer.data.model.Word;
import com.ercan.wordbuzzer.game.MediaManager;
import com.ercan.wordbuzzer.game.model.Game;
import com.ercan.wordbuzzer.game.model.GameState;
import com.ercan.wordbuzzer.game.model.Player;
import com.ercan.wordbuzzer.game.model.WordBuzzerAnimatorListener;
import com.ercan.wordbuzzer.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ercanozcan on 17/02/16.
 */
public class GameFragment extends Fragment implements Game.GameListener {

    public static final long DEFAULT_ANIM_DURATION = 300;
    @Bind(R.id.txtQuestion)
    TextView txtQuestion;
    @Bind(R.id.btnPlayer1)
    Button btnPlayer1;
    @Bind(R.id.btnPlayer2)
    Button btnPlayer2;
    @Bind(R.id.btnPlayer3)
    Button btnPlayer3;
    @Bind(R.id.btnPlayer4)
    Button btnPlayer4;
    @Bind(R.id.txtAnswer)
    TextView txtAnswer;
    @Bind(R.id.btnStart)
    Button btnStart;
    private Player player1 = new Player(1, "Leonardo", 0);
    private Player player2 = new Player(2, "Michelangelo", 0);
    private Player player3 = new Player(3, "Rafael", 0);
    private Player player4 = new Player(3, "Donatello", 0);

    private View rootView;
    private GameState state = GameState.PLAYER_LOBY;

    private List<Player> players = new ArrayList<>(4);
    private List<View> playerButtons = new ArrayList<>(4);// for using a for loop for repetitive operations
    private Game game;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_game_layout, container, false);
        ButterKnife.bind(this, rootView);
        playerButtons.add(btnPlayer1);
        playerButtons.add(btnPlayer2);
        playerButtons.add(btnPlayer3);
        playerButtons.add(btnPlayer4);
        //all player buttons are added to a collection
        return rootView;
    }

    public void prepareGame() {
        game = new Game(Integer.MAX_VALUE, 3, players, this);
        game.startGame();
        state = GameState.NORMAL;

    }

    @OnClick({R.id.btnPlayer1, R.id.btnPlayer2, R.id.btnPlayer3, R.id.btnPlayer4})
    void playerButtonClick(View view) {

        int id = view.getId();
        if (state == GameState.PLAYER_LOBY) {// in lobby state, players decide to join the game or not
            switch (id) {
                case R.id.btnPlayer1:
                    togglePlayer(player1, view);
                    break;
                case R.id.btnPlayer2:
                    togglePlayer(player2, view);
                    break;
                case R.id.btnPlayer3:
                    togglePlayer(player3, view);
                    break;
                case R.id.btnPlayer4:
                    togglePlayer(player4, view);
                    break;
            }
        } else {
            switch (id) {//handle the answers
                case R.id.btnPlayer1:
                    if (game.playerAnswer(player1)) {
                        correctAnswerFeedback(view);
                        btnPlayer1.setCompoundDrawablesWithIntrinsicBounds(0, 0, CommonUtils.getDrawableIdforScore(player1.getScore()), 0);
                    } else {
                        wrongAnswerFeedback(view);
                    }
                    break;
                case R.id.btnPlayer2:
                    if (game.playerAnswer(player2)) {
                        correctAnswerFeedback(view);
                        btnPlayer2.setCompoundDrawablesWithIntrinsicBounds(0, 0, CommonUtils.getDrawableIdforScore(player2.getScore()), 0);
                    } else {
                        wrongAnswerFeedback(view);
                    }

                    break;
                case R.id.btnPlayer3:
                    if (game.playerAnswer(player3)) {
                        correctAnswerFeedback(view);
                        btnPlayer3.setCompoundDrawablesWithIntrinsicBounds(CommonUtils.getDrawableIdforScore(player3.getScore()), 0, 0, 0);
                    } else {
                        wrongAnswerFeedback(view);
                    }
                    break;
                case R.id.btnPlayer4:
                    if (game.playerAnswer(player4)) {
                        correctAnswerFeedback(view);
                        btnPlayer4.setCompoundDrawablesWithIntrinsicBounds(CommonUtils.getDrawableIdforScore(player4.getScore()), 0, 0, 0);
                    } else {
                        wrongAnswerFeedback(view);
                    }

                    break;
            }
        }
    }

    @OnClick(R.id.btnStart)
    public void btnStartClick(View v) {
        if (players.isEmpty()) {
            Toast.makeText(getActivity(), R.string.toast_player_needed, Toast.LENGTH_SHORT).show();
            return;
        }
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(btnStart, "alpha", 0);// this button will remove itself with fadeout animation
        fadeOut.setDuration(DEFAULT_ANIM_DURATION);
        fadeOut.addListener(new WordBuzzerAnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animation) {
                prepareGame();
                btnStart.setEnabled(false);
            }
        });
        for (View playerButton : playerButtons) {
            if (playerButton.getTag() == null) {// if tag = null means player is not joined to game so we remove the button
                getFadeOutAnimator(playerButton).start();
            }
        }
        fadeOut.start();
    }

    @Override
    public void onQuestionChanged(Word question) {
        txtQuestion.setText(question.getEng().toUpperCase());// set the question text as lang1
    }

    @Override
    public void onAnswerChanged(Word answer) {
        txtAnswer.setText(answer.getSpa().toUpperCase());// set the answer text as lang2
        if (new Random().nextBoolean()) {
            enterFromLeft();
        } else {
            enterFromRight();
        }
    }

    @Override
    public void onGameEnded(Player winner) {
        gameEndFeedBack(rootView.findViewWithTag(winner));
    }

    @Override
    public void onNewRound() {
        enablePlayers(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * returns if this player will be included in next game and adds the player in players list
     *
     * @param player
     * @return
     */
    private boolean togglePlayer(Player player, View view) {
        boolean included = players.contains(player);
        if (players.contains(player)) {
            players.remove(player);
            ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(view, "Alpha", 0.65f);
            alphaAnimation.setDuration(DEFAULT_ANIM_DURATION);
            alphaAnimation.start();
            view.setTag(null);
        } else {
            players.add(player);
            ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(view, "Alpha", 1f);
            alphaAnimation.setDuration(DEFAULT_ANIM_DURATION);
            alphaAnimation.start();
            view.setTag(player);
        }
        return !included;
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
                game.generateAnswer();
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

                game.generateAnswer();
            }
        });
        enterFromRight.start();
    }

    /**
     * enables or disables the player buzzer buttons
     */
    public void enablePlayers(boolean enable) {
        for (Player player : players) {
            View btnPlayer = rootView.findViewWithTag(player);
            btnPlayer.setEnabled(enable);
        }
    }

    /**
     * returns an Object animator with a default duration
     *
     * @param view
     * @return
     */
    public ObjectAnimator getFadeOutAnimator(final View view) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 0);
        fadeOut.setDuration(DEFAULT_ANIM_DURATION);
        fadeOut.addListener(new WordBuzzerAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setEnabled(false);
            }
        });
        return fadeOut;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        playerButtons.clear();// no memory leak :)
    }

    /**
     * scale up half and turn back to normal size again in 300ms. it looks nice with a mario coin sound.
     *
     * @param playerButton
     */
    public void correctAnswerFeedback(View playerButton) {

        playerButton.setPivotX(playerButton.getWidth() / 2);//horizontal center
        playerButton.setPivotY(playerButton.getHeight() / 2);//vertical center
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(playerButton, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(playerButton, "scaleY", 1f, 1.5f, 1f);
        scaleX.setDuration(DEFAULT_ANIM_DURATION);
        scaleY.setDuration(DEFAULT_ANIM_DURATION);
        scaleX.start();
        scaleY.start();
        MediaManager.getInstance(getActivity()).playCorrect();

    }

    /**
     * Vibrates the button with an annoying buzz sound. also the players button is disabled for 1500ms.rekt!
     *
     * @param playerButton
     */
    public void wrongAnswerFeedback(final View playerButton) {
        MediaManager.getInstance(getActivity()).playBuzzer();
        ObjectAnimator
                .ofFloat(playerButton, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(DEFAULT_ANIM_DURATION * 5)
                .start();
        ObjectAnimator vibrateVertical = ObjectAnimator
                .ofFloat(playerButton, "translationY", 0, 25, -25, 25, -25, 15, -15, 6, -6, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(DEFAULT_ANIM_DURATION * 5);
        vibrateVertical.addListener(new WordBuzzerAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                playerButton.setEnabled(false);//the penalty
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                playerButton.setEnabled(true);
            }
        });
        vibrateVertical.start();

    }


    public void gameEndFeedBack(final View playerButton) {
        MediaManager.getInstance(getActivity()).playWin();
        playerButton.bringToFront();//it will float above all other views
        int W = rootView.getWidth();//calculating the center of the container
        int w = playerButton.getWidth();
        int H = rootView.getHeight() - txtQuestion.getHeight();
        int h = playerButton.getHeight();
        int centerX = playerButton.getLeft() > W / 2 ? -(W - ((w / 2) + (W / 2))) : (W - ((w / 2) + (W / 2)));//deltaX
        int centerY = playerButton.getTop() > H / 2 ? -(H - ((h / 2) + (H / 2))) : (H - ((h / 2) + (H / 2)));//deltaY
        float scaleX = (float) W / (float) w;//ratio of W and w
        float scaleY = (float) H / (float) h;//ratio of H and h
        playerButton.setPivotY(h / 2);//setting pivots for scale animation
        playerButton.setPivotX(w / 2);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(playerButton, "translationX", centerX).setDuration(DEFAULT_ANIM_DURATION * 5);
        translateX.addListener(new WordBuzzerAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                playerButton.setEnabled(false);//to prevent early click

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                playerButton.setEnabled(true);
            }
        });
        translateX.start();
        ObjectAnimator.ofFloat(playerButton, "translationY", centerY).setDuration(DEFAULT_ANIM_DURATION * 5).start();
        ObjectAnimator.ofFloat(playerButton, "scaleX", scaleX).setDuration(DEFAULT_ANIM_DURATION * 5).start();
        ObjectAnimator.ofFloat(playerButton, "scaleY", scaleY).setDuration(DEFAULT_ANIM_DURATION * 5).start();
        playerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();//return to main menu
            }
        });
        enablePlayers(false);//disable all players
        playerButton.setEnabled(true);//except this one

    }
}
