import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Host implements Runnable{
    ServerSocket listener;
    private Thread t;
    private final AtomicBoolean running = new AtomicBoolean(false);
    int max_players;
    ArrayList<PlayFieldHandler> clients = new ArrayList<>();
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
                try {
                    if(clients.size()<max_players) {
                        System.out.println("[SERVER] Waiting for players");
                        Socket client = listener.accept();
                        System.out.println("[SERVER] Conneted to server");
                        PlayFieldHandler playerThread = new PlayFieldHandler(client, play_field);
                        clients.add(playerThread);
                        playerThread.start();
                        max_players++;
                    } else {
                        System.out.println("Server is Full");
                    }
                } catch (IOException e) {
                }
            }
            for(PlayFieldHandler player : clients){
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
