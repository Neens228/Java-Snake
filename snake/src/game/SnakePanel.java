package game;

import game.misc.ChangeCountListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.net.URL;
import java.util.Random;

public class SnakePanel extends JPanel implements ActionListener,KeyListener {
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private final int SIZE = 600;
    private final int SIZE_Y = 400;

    private Image rabbit;
    private Image snakeDot;

    private int rabbitX;
    private int rabbitY;
    private int dots;

    private int countRabbitEat;

    private ChangeCountListener changeCountListener;


    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private boolean inGame = true;
    private boolean leftSnake = false;
    private boolean rightSnake = true;
    private boolean upSnake = false;
    private boolean downSnake =false;

    private Timer clockTimer;

    public SnakePanel(ChangeCountListener listener){
        changeCountListener = listener;
        setBackground(Color.BLACK);
        loadImage();
        initGame();
        addKeyListener(this);
        setFocusable(true);
    }

    public void loadImage(){
       URL url = Snake.class.getResource("res/dot2.png");
       ImageIcon ll = new ImageIcon(url);
       snakeDot = ll.getImage();

        url = Snake.class.getResource("res/apple.png");
        ll = new ImageIcon(url);
        rabbit = ll.getImage();
    }

    private void initGame(){
        countRabbitEat = 0;
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i*DOT_SIZE;
            y[i] = 48;
        }

        clockTimer = new Timer(250,this);
        clockTimer.start();

        createRabbit();
    }

    public void createRabbit(){
        rabbitX = new Random().nextInt(20)*DOT_SIZE;
        rabbitY = new Random().nextInt(20)*DOT_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        FontRenderContext frc = g2d.getFontRenderContext();

        if(inGame){
            g.drawImage(rabbit,rabbitX,rabbitY,this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(snakeDot,x[i],y[i],this);
            }
            /*
            Font font = new Font("Arial",Font.BOLD,16);
            TextLayout tl = new TextLayout("Count rabbit: "+String.valueOf(countRabbitEat),font,frc);
            g2d.setColor(Color.GRAY);
            tl.draw(g2d,10,20);
            */
            if (changeCountListener != null){
                changeCountListener.onChangeCount(countRabbitEat);
            }
        } else {
            String str = "Game Over";
            Font font1 = new Font("Courier", Font.BOLD, 24);
            TextLayout tl = new TextLayout(str, font1, frc);
            g2d.setColor(Color.WHITE);
            tl.draw(g2d, 230, 200);

        }
    }

    private void move(){
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(leftSnake){
            x[0] -= DOT_SIZE;
        }
        if(rightSnake){
            x[0] += DOT_SIZE;
        } if(upSnake){
            y[0] -= DOT_SIZE;
        } if(downSnake){
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple(){
        if(x[0] == rabbitX && y[0] == rabbitY){
            dots++;
            createRabbit();
            countRabbitEat ++;
        }
    }

    public void checkCollisions(){
        for (int i = dots; i >0 ; i--) {
            if(i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }

        if(x[0]>SIZE){
            inGame = false;
        }
        if(x[0]<0){
            inGame = false;
        }
        if(y[0]>SIZE_Y){
            inGame = false;
        }
        if(y[0]<0){
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollisions();
            move();

        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_LEFT && !rightSnake){
            leftSnake = true;
            upSnake = false;
            downSnake = false;
        }
        if(key == KeyEvent.VK_RIGHT && !leftSnake){
            rightSnake = true;
            upSnake = false;
            downSnake = false;
        }

        if(key == KeyEvent.VK_UP && !downSnake){
            rightSnake = false;
            upSnake = true;
            leftSnake = false;
        }
        if(key == KeyEvent.VK_DOWN && !upSnake){
            rightSnake = false;
            downSnake = true;
            leftSnake = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
