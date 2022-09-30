package game;

import game.misc.ChangeCountListener;

import javax.swing.*;
import java.awt.*;

public class Snake extends JFrame {
    private CountPanel countPanel;
    private int w = 600;
    private int h = 440;

    public Snake(){
        super("Змейка");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(w,h);
        setResizable(false);
        setLayout(new BorderLayout());
        countPanel = new CountPanel();
        add(countPanel,BorderLayout.NORTH);
        add(new SnakePanel(listener),BorderLayout.CENTER);
        setVisible(true);
        this.setLocation((this.getToolkit().getScreenSize().width/2)-(w/2), (this.getToolkit().getScreenSize().height/2)-(h/2));
    }

    ChangeCountListener listener = new ChangeCountListener() {
        @Override
        public void onChangeCount(int value) {
            countPanel.setCountValue(value);
        }
    };

    public static void main(String args[]){
        Snake frm = new Snake();
    }
}
