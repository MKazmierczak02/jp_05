import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerHandler implements Runnable{
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Socket client;
    public PrintWriter out;
    public BufferedReader input;
    Thread t;
    PlayField play_field;

    public PlayerHandler(Socket clientSocket, PlayField play_field) throws IOException {
        this.play_field = play_field;
        this.client=clientSocket;
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    public void start(){
        t=new Thread(this);
        t.start();
    }

    public void stop() throws IOException {
        running.set(false);
    }

    @Override
    public void run() {
        running.set(true);
            while (running.get()) {
                out.println(this.play_field.boardToString());
                try {
                    String clientmess = input.readLine();
                    System.out.println(clientmess);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("host stopped");
    }
}
