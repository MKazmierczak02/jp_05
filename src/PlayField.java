import models.Cell;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayField extends JPanel implements Runnable, Serializable {
    ArrayList<ArrayList<Cell>> board;
    int width;
    int height;
    JPanel game;
    Thread t;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public PlayField(int width, int height){
        this.setLayout(new GridLayout(height,width,2,2));
        assignParameters(width, height);
        createBoard();
        setBoard();
        this.setVisible(true);
    }

    public void createBoard()
    {
        board = new ArrayList<>();
        for (int i=0;i<height; i++){
            ArrayList<Cell> row = new ArrayList<>();
            for (int j=0; j<width; j++){
                if (j==1 || j == width-2){
                    if(j==1){
                        row.add(new Cell("blank"));
                    }else{
                        row.add(new Cell("blank"));
                    }
                }else if (j==width/2){
                    row.add(new Cell("middle"));
                }else if (j==0 || j == width-1){
                    row.add(new Cell("counter"));
                }else{
                    row.add(new Cell("blank"));
                }
            }
            board.add(row);
        }
    }

    synchronized void setBoard(){
        for (int i=0;i<height; i++){
            for (int j=0; j<width; j++){
                if (board.get(i).get(j).type=="counter"){
                    System.out.println(board.get(i).get(j).type+"  "+(board.get(i).get(j).type=="counter"));
                    this.add(new JButton(String.valueOf(board.get(i).get(j).goals)));
                } else if (board.get(i).get(j).type=="player_left"){
                    this.add(new JLabel(new ImageIcon(new ImageIcon("player_blue.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT))));
                }else if (board.get(i).get(j).type=="player_right"){
                    this.add(new JLabel(new ImageIcon(new ImageIcon("player_red.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT))));
                }else if (board.get(i).get(j).type=="middle"){
                    this.add(new JLabel(new ImageIcon(new ImageIcon("ball.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT))));
                }else if (board.get(i).get(j).type=="blank"){
                    this.add(new JLabel(new ImageIcon(new ImageIcon("img.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT))));
                } else{
                    System.out.println(board.get(i).get(j).type+"!=counter"+(board.get(i).get(j).type=="counter"));
                    this.add(new JLabel(new ImageIcon(new ImageIcon("img.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT))));
                }
            }
        }
    }

    public String boardToString(){
        StringBuilder board = new StringBuilder();
        for (int i=0;i<height; i++){
            for (int j=0; j<width; j++){
                board.append(this.board.get(i).get(j).type);
                board.append("X");
            }
            board.append("/");
        }
        return board.toString();
    }


    public void start(){
        t=new Thread(this);
        t.start();
    }

    public void stop() throws IOException {
        running.set(false);
    }

    private void assignParameters(int width, int height){
        this.width=width;
        this.height=height;
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()){
            this.removeAll();
            this.setBoard();
            this.revalidate();
            this.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
