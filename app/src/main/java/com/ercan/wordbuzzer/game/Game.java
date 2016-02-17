package com.ercan.wordbuzzer.game;

import com.ercan.wordbuzzer.GameApp;
import com.ercan.wordbuzzer.data.model.Word;

import java.util.List;
import java.util.Random;

/**
 * Created by ercanozcan on 17/02/16.
 */
public class Game {

    private int maxRounds;//if maximum round number is reached the player with most rounds win.
    private int bestOf;// if a player wins number of "bestOf" rounds, that player wins the game
    private int currentRound = 0;// the game is not started yet
    private List<Player> players;
    private Word currentQuestion;
    private Word currentAnswer;

    private boolean started = false;


    public Game(int maxRounds, int bestOf, List<Player> players) {
        this.maxRounds = maxRounds;
        this.bestOf = bestOf;
        this.players = players;
    }

    public void startGame() {
        started = true;
        nexRound();

    }

    public void endGame() {
        started = false;
    }

    public void nexRound() {
        if (currentRound + 1 == maxRounds) {//we end the if max round cap is reached
            endGame();
        } else {
            setQuestion();
            currentRound++;//we increase this number each round;
        }
    }

    private void setQuestion() {
        int size = GameApp.getWords().size();
        int questionIndex = new Random().nextInt(size + 1); // we get a random word from words.json
        currentQuestion = GameApp.getWords().get(questionIndex);

    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public int getBestOf() {
        return bestOf;
    }

    public void setBestOf(int bestOf) {
        this.bestOf = bestOf;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }


}
