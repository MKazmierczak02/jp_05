package models;

import java.util.ArrayList;

public class BallGenerator implements Runnable {
    private int balls_val;
    ArrayList<Ball> balls = new ArrayList<>();
    public BallGenerator(ArrayList<Ball> balls, int balls_val){
        this.balls = balls;
        this.balls_val = balls_val;
    }

    @Override
    public void run(){

    }

}
