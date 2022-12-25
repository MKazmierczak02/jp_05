import models.Cell;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayField extends JPanel implements Runnable {

    ArrayList<ArrayList<Cell>> board;
    int width;
    int height;
    int players_a;
    int players_b;
    int balls;



    public PlayField(int width, int height, int players_a, int players_b, int balls){
        this.setLayout(new GridLayout(height,width,2,2));
        assignParameters(width, height,  players_a,  players_b,  balls);
        createBoard();
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
                        row.add(new Cell("player_left"));
                    }else{
                        row.add(new Cell("player_right"));
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

    public String getAndSetBoardString(){
        StringBuilder board_string= new StringBuilder();
        for (int i=0;i<height; i++){
            for (int j=0; j<width; j++){
                if (board.get(i).get(j).type=="counter"){
                    this.add(new JButton(String.valueOf(board.get(i).get(j).goals)));
                } else if (board.get(i).get(j).type=="player_left"){
                    this.add(new JLabel(new ImageIcon(new ImageIcon("player_blue.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT))));
                }else if (board.get(i).get(j).type=="player_right"){
                    this.add(new JLabel(new ImageIcon(new ImageIcon("player_red.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT))));
                }else if (board.get(i).get(j).type=="middle"){
                    this.add(new JLabel(new ImageIcon(new ImageIcon("ball.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT))));
                }else{
                    this.add(new JLabel(new ImageIcon(new ImageIcon("img.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT))));
                }
            }
            board_string.append("\n");
        }
        return String.valueOf(board_string);
    }

    private void assignParameters(int width, int height, int players_a, int players_b, int balls){
        this.width=width;
        this.height=height;
        this.players_a=players_a;
        this.players_b=players_b;
        this.balls=balls;
    }

    @Override
    public void run() {

    }
}
