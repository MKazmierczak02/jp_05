import javax.swing.*;
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
    int max_players;
    ArrayList<PlayerHandler> clients = new ArrayList<>();
    PlayField play_field;
    JPanel game;
    public Host(ServerSocket listener, int max_players, PlayField playField, JPanel game) throws IOException {
        this.game = game;
        this.play_field = playField;
        this.listener=listener;
        this.max_players = max_players;
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
        play_field.start();
            while(running.get()){
                System.out.println("host thread");
                try {
                    if(clients.size()<max_players) {
                        System.out.println("[SERVER] Waiting for players");
                        Socket client = listener.accept();
                        System.out.println("[SERVER] Conneted to server");
                        PlayerHandler playerThread = new PlayerHandler(client);
                        clients.add(playerThread);
                        playerThread.start();
                        max_players++;
                    } else {
                        System.out.println("Server is Full");
                    }
                } catch (IOException e) {
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
