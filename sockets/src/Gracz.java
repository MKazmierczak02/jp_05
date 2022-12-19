import java.io.IOException;

public class Gracz {
    public static void main(String[] args) {
        Runnable client = new Runnable() {
            @Override
            public void run() {
                try {
                    new SocketClientExample().startClient();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(client, "gracz1").start();
    }
}