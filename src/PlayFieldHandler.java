import models.Cell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayFieldHandler implements Runnable{
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Socket client;
    public PrintWriter out;
    public BufferedReader input;
    Thread t;
    PlayField play_field;
    int max_players;
    ArrayList<PlayFieldHandler> clients;
    int max_balls;
    ArrayList<Ball> balls = new ArrayList<>();

    public PlayFieldHandler(Socket clientSocket, PlayField play_field, int max_players, int max_balls, ArrayList<PlayFieldHandler> clients) throws IOException {
        this.max_players = max_players;
        this.clients = clients;
        this.play_field = play_field;
        this.max_balls=max_balls;
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
        boolean started = true;
        Random rand = new Random();
            while (running.get()) {
                if (clients.size()>0 && started){
                    for (int i = 0; i < 1 ; i++){   //max_balls
                        Ball ball = new Ball(play_field.width/2, rand.nextInt(play_field.height),play_field);
                        balls.add(ball);
                        ball.start();
                    }
                    started=false;
                }
                for (Ball ball : balls){
                    if (ball.direction){
                        play_field.board.get(ball.y).get(ball.x-1).type="blank";
                        play_field.board.get(ball.y).get(ball.x).type="ball";
                        if (play_field.board.get(ball.y).get(ball.x+1).type.contains("player")){
                            ball.direction=false;
                        } else if (play_field.board.get(ball.y).get(ball.x+1).type.contains("counter")){
                            play_field.right_scores[ball.y]++;
                            ball.reset(play_field.width/2);
                            play_field.board.get(ball.y).get(play_field.width-2).type="blank";
                            out.println("right_scores:"+ball.y);
                        }
                    }
                    else {
                        play_field.board.get(ball.y).get(ball.x+1).type="blank";
                        play_field.board.get(ball.y).get(ball.x).type="ball";
                        if (play_field.board.get(ball.y).get(ball.x-1).type.contains("player")){
                            ball.direction=true;
                        } else if (play_field.board.get(ball.y).get(ball.x-1).type.contains("counter")){
                            play_field.left_scores[ball.y]++;
                            ball.reset(play_field.width/2);
                            play_field.board.get(ball.y).get(1).type="blank";
                            out.println("left_scores:"+ball.y);
                        }
                    }
                }

                out.println("map:"+this.play_field.boardToString());
                try {
                    String clientmess = input.readLine();
                    if(clientmess.contains("location")){
                        String[] str = clientmess.split(":");
                        str = str[1].split(",");
                        if (play_field.board.get(Integer.parseInt(str[3])).get(Integer.parseInt(str[2])).type!="player"+str[4].toLowerCase()) {
                            play_field.board.get(Integer.parseInt(str[1])).set(Integer.parseInt(str[0]), new Cell("blank"));
                            play_field.board.get(Integer.parseInt(str[3])).set(Integer.parseInt(str[2]), new Cell("player_" + str[4].toLowerCase()));
                        }
                    }else if (clientmess.contains("quit")){
                        String[] str = clientmess.split(":");
                        str = str[1].split(",");
                        play_field.board.get(Integer.parseInt(str[1])).set(Integer.parseInt(str[0]), new Cell("blank"));
                        balls.remove(0);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("host stopped");
    }
}
