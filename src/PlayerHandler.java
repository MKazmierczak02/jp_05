import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerHandler implements Runnable{
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Socket client;
    private PrintWriter out;
    private BufferedReader input;
    Thread t;

    public PlayerHandler(Socket clientSocket) throws IOException {
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
        try {
            while (running.get()) {
                String request = input.readLine();
                System.out.println(request);
                out.println(request);
            }
            System.out.println("host stopped");
        } catch (IOException e){

        }

    }
}
