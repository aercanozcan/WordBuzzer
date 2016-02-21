package com.ercan.wordbuzzer.game.model;

/**
 * Created by ercanozcan on 17/02/16.
 */
public class Player {


    private int num;
    private String name;
    private int score = 0;
    private boolean canAnswer = true;

    public Player() {
    }

    public Player(int num, String name, int score) {
        this.num = num;
        this.name = name;
        this.score = score;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isCanAnswer() {
        return canAnswer;
    }

    public void setCanAnswer(boolean canAnswer) {
        this.canAnswer = canAnswer;
    }
}
