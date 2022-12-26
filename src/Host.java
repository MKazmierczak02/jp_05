import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Host implements Runnable{
    ServerSocket listener;
    private Thread t;
    private final AtomicBoolean running = new AtomicBoolean(false);

    ArrayList<PlayerHandler> clients = new ArrayList<>();

    public Host(ServerSocket listener) throws IOException {
        this.listener=listener;
    }
    public void start(){
        t=new Thread(this);
        t.start();
    }

    public void stop() throws IOException {
        listener.close();
        running.set(false);
    }

    @Override
    public void run() {
        running.set(true);
            while(running.get()){
                System.out.println("[SERVER] Waiting for players");
                try {
                    Socket client = listener.accept();
                    System.out.println("[SERVER] Connected to client");
                    PlayerHandler playerThread = new PlayerHandler(client);
                    clients.add(playerThread);
                    playerThread.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            for(PlayerHandler player : clients){
                try {
                    player.stop();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            clients=new ArrayList<>();
            System.out.println("host stopped");
    }


}
