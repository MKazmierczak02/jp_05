import models.Cell;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player implements Runnable{
    int x;
    int y;
    int max_y;
    boolean started = false;
    Socket socket;
    String name;
    PrintWriter out;
    BufferedReader input;
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
        if (y-1>=0) {
            out.println("location:" + x + "," + (y--) + "," + x + "," + y + "," + team);
        }
    }
    public void moveDown() throws IOException{
        if (y+1<=max_y) {
            out.println("location:" + x + "," + (y++) + "," + x + "," + y + "," + team);
        }
    }

    @Override
    public void run() {
        running.set(true);
        try {
            while(running.get()) {
                String serverResponse = input.readLine();
                makeBoard(serverResponse);
                Thread.sleep(20);
                out.println("location:" + x + "," + y + "," + x + "," + y + "," + team);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeBoard(String serverResponse) throws IOException {
        ArrayList<ArrayList<String>> board_in_str = new ArrayList<>();
        String [] strings =  serverResponse.split("/");
        ArrayList<String> stringList = new ArrayList<>(Arrays.asList(strings));

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
            Random rand = new Random();
            max_y=board.size()-1;
            y = rand.nextInt(board.size()-1);
            if(team.equals("LEFT")){
                this.x=1;
            } else{
                this.x=board.get(0).size()-2;
            }
            board.get(y).get(x).type="player_"+team.toLowerCase();
            System.out.println(x + " " + y);
            play_field = new PlayField(board.get(0).size(), board.size());
            play_field.board=board;
            play_field.start();
            game.add(play_field);
            started = true;
        }
        play_field.board=board;
    }
}
