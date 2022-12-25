package models;

public class Ball {
    int x;
    int y;
    boolean direction = true;   // true - prawo || false - lewo
    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(boolean change_direction, boolean goal){
        if (change_direction){
            direction = !direction;
        }
        if (direction){
            x++;
        } else {
            x--;
        }
    }
}
