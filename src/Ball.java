import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
public class Ball implements Runnable{
    int x;
    int y;
    int starting_x;
    private Thread t;
    PlayField play_field;
    private final AtomicBoolean running = new AtomicBoolean(false);
    boolean direction = true;   // true - prawo || false - lewo
    public Ball(int x, int y, PlayField play_field) {
        this.x = x;
        this.y = y;
        this.play_field=play_field;
    }

    public void move(boolean change_direction){
        if (change_direction){
            direction = !direction;
        }
        if (direction){

            x++;
        } else {
            x--;
        }
    }
    public void moveRight(){
        x++;
    }
    public void moveLeft(){
        x--;
    }
    public void reset(){
        x=starting_x;
    }

    public void start(){
        t=new Thread(this);
        t.start();
    }
    @Override
    public void run() {
        running.set(true);
            while(running.get()) {

            }
    }
}
