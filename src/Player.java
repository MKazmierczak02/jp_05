import models.Cell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player implements Runnable{

    Socket socket;
    String name;
    PrintWriter out;
    BufferedReader input;
    BufferedReader keyboard;
    String team;
    private Thread t;
    PlayField play_field;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public Player(Socket socket, String name, String team, PlayField play_field){
        this.name=name;
        this.team=team;
        this.play_field=play_field;
        try {
            this.socket = socket;
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            keyboard = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void start(){
        t=new Thread(this);
        t.start();
    }

    public void stop() throws IOException {
        socket.close();
        running.set(false);
    }

    @Override
    public void run() {
        running.set(true);
        play_field.board.get(1).set(2, new Cell("player_red"));
        try {
            while(running.get()) {
                Cell temp = play_field.board.get(1).get(3);
                play_field.board.get(1).set(3,new Cell("player_red"));
                play_field.board.get(1).set(3,temp);
                String command = "get_playfield";
                out.println(this.name+" "+this.team+" :" + command);
                String serverResponse = input.readLine();
                System.out.println("Server says: " + serverResponse);
            }
        } catch (IOException e) {
        }
    }
}
