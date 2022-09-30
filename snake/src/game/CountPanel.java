package game;

import javax.swing.*;
import java.awt.*;

public class CountPanel extends JPanel {
    private JLabel countLabel ;

    public CountPanel(){
        //setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
        setBackground(Color.darkGray);
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        Font font = new Font("Courier", Font.BOLD, 24);
        countLabel = new JLabel("Score: 0");
        countLabel.setFont(font);
        countLabel.setForeground(Color.WHITE);
        add(countLabel);
    }

    public void setCountValue(int value){
        countLabel.setText("Score: "+String.valueOf(value));
        repaint();
    }
}
