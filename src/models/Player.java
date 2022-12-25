package models;

public class Player implements Runnable {
    int x;
    int y;
    String team;

    public Player(int x, int y, String team) {
        this.x = x;
        this.y = y;
        this.team = team;
    }

    @Override
    public void run(){}
}
