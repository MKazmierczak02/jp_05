import models.Cell;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player implements Runnable{
    int x;
    int y;
    boolean started = false;
    Socket socket;
    String name;
    PrintWriter out;
    BufferedReader input;
    BufferedReader keyboard;
    String team;
    private Thread t;
    PlayField play_field;
    private final AtomicBoolean running = new AtomicBoolean(false);
    JPanel game;

    public Player(Socket socket, String name, String team, PlayField play_field, JPanel game){
        this.name=name;
        this.team=team;
        this.game = game;
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
        started = true;
        socket.close();
        play_field.stop();
        running.set(false);
    }

    public void moveUp() throws IOException{
        out.println("moveUp");
    }
    public void moveDown() throws IOException{
        out.println("moveDown");
    }

    @Override
    public void run() {
        running.set(true);
        try {
            while(running.get()) {
                String serverResponse = input.readLine();
                makeBoard(serverResponse);
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeBoard(String serverResponse) {
        ArrayList<ArrayList<String>> board_in_str = new ArrayList<>();
        String [] strings =  serverResponse.split("/");
        ArrayList<String> stringList = new ArrayList<String>(Arrays.asList(strings));

        for (String row : stringList){
            String [] strings_2 =  row.split("X");
            ArrayList<String> stringList_2 = new ArrayList<>(Arrays.asList(strings_2));
            board_in_str.add(stringList_2);
        }
        ArrayList<ArrayList<Cell>> board = new ArrayList<>();

        for (ArrayList<String> row_in_str : board_in_str){
            ArrayList<Cell> row = new ArrayList<>();
            for (String type : row_in_str){
                row.add(new Cell(type));
            }
            board.add(row);
        }

        if (!started) {
            play_field = new PlayField(board.get(0).size(), board.size());
            play_field.board=board;
            play_field.start();
            game.add(play_field);
            started = true;
        }
        play_field.board=board;
    }
}
