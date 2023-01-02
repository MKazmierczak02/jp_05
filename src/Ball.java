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

    public void move(){
        if (direction){
            x++;
        } else {
            x--;
        }
    }
    public void reset(int x ){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.x=x;
    }

    public void start(){
        t=new Thread(this);
        t.start();
    }
    @Override
    public void run() {
        running.set(true);
            while(running.get()) {
                this.move();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
    }
}
