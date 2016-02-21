package com.ercan.wordbuzzer.game.model;

import com.ercan.wordbuzzer.GameApp;
import com.ercan.wordbuzzer.data.model.Word;

import java.util.List;
import java.util.Random;

/**
 * Created by ercanozcan on 17/02/16.
 */
public class Game {

    private int maxRounds = Integer.MAX_VALUE - 1;//if maximum round number is reached the player with most rounds win.
    private int bestOf;// if a player wins number of "bestOf" rounds, that player wins the game
    private int currentRound = -1;// the game is not started yet
    private List<Player> players;
    private Word currentQuestion;
    private Word currentAnswer; // the flying text that annoys the player
    private GameListener gameListener;

    private boolean started = false;


    public Game(int maxRounds, int bestOf, List<Player> players, GameListener gameListener) {
        this.maxRounds = maxRounds;
        this.bestOf = bestOf;
        this.players = players;
        this.gameListener = gameListener;
    }

    public void startGame() {
        started = true;
        nexRound();
        generateAnswer();

    }

    public void endGame(Player winner) {
        started = false;
        gameListener.onGameEnded(winner);
    }


    /**
     * This method should be called when all players gives a wrong answer.
     */
    public void nexRound() {
        if (currentRound + 1 == maxRounds) {//we end the game if max round cap is reached
            endGame(null);//TODO start sudden death mode !!!!
        } else {
            setQuestion();

            for (Player player : players) {
                player.setCanAnswer(true);
            }
            currentRound++;//we increase this number each round;
        }
    }

    /**
     * This method should be called when a right answer is given from a player
     *
     * @param winner
     */
    public void nexRound(Player winner) {

        winner.setScore(winner.getScore() + 1);// increase the winners score
        if (winner.getScore() == bestOf) {
            // and the winner is...
            endGame(winner);
        }
        nexRound();
    }

    /**
     * this method replaces the current question with a random one.
     */
    private void setQuestion() {
        int size = GameApp.getWords().size();
        int questionIndex = getRandomIndex(); // we get a random word from words.json
        currentQuestion = GameApp.getWords().get(questionIndex);
        gameListener.onQuestionChanged(currentQuestion);// notify the UI about new question
    }

    /**
     * must be called when a player answers the question
     *
     * @param answerer
     * @return correctness of answer
     */
    public boolean playerAnswer(Player answerer) {
        boolean correct = isAnswerCorrect();
        answerer.setCanAnswer(false);// he used his answer chance for this round

        if (correct) {
            for (Player player : players) {//the rounds winner is decided so no late answers
                player.setCanAnswer(false);
            }
            nexRound(answerer);//we move to next round with a winner
        } else {
            //if(didAllPlayersAnswer()) {// if everybody out of juice then we move to next round
            //    nexRound();
            //}
        }
        return correct;
    }

    /**
     * checks if the current answer equalst to current question
     *
     * @return
     */
    private boolean isAnswerCorrect() {
        return currentAnswer.equals(currentQuestion);
    }

    /**
     * all players used answer for this round or not
     *
     * @return
     */
    private boolean didAllPlayersAnswer() {
        boolean result = true;

        for (Player player : players) {
            result = !player.isCanAnswer() & result;
        }
        return result;
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

    public void generateAnswer() {
        if (!started)
            return;// we dont generate questions if not started
        boolean shouldShowCorrectAnswer = new Random().nextBoolean();// we will randomly show correct.mp3 answer.
        if (shouldShowCorrectAnswer) {
            currentAnswer = currentQuestion;// so we assign the question to answer.
        } else {
            currentAnswer = getRandomWrongAnswer();// or get a random wrong answer
        }

        gameListener.onAnswerChanged(currentAnswer);// we notify the ui here
    }

    /**
     * grabs a random wrong answer from words.json
     *
     * @return
     */
    public Word getRandomWrongAnswer() {
        int index = getRandomIndex();
        while (index == GameApp.getWords().indexOf(currentQuestion)) {// to being sure that we are getting a wrong answer
            index = getRandomIndex();
        }

        return GameApp.getWords().get(index);


    }

    /**
     * returns a random index in range of the words
     *
     * @return
     */
    public int getRandomIndex() {
        int size = GameApp.getWords().size();
        return new Random().nextInt(size);
    }

    /**
     * to listen events from Game.java
     */
    public interface GameListener {
        void onQuestionChanged(Word question);//notify the ui about new question

        void onAnswerChanged(Word question);// to notify ui for new answer

        void onGameEnded(Player winner);//the game ended and player decided here

        void onNewRound();// maybe I can make an animation for it later

    }

}
